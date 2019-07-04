package database;

import model.WordTfIdfEntry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static database.TfIdfCalculator.calculateTdIdfForWordsInDoc;
import static database.TfIdfCalculator.calculateWordIDFs;
import static database.TfIdfCalculator.generateTfMapForDocument;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toCollection;

public class Database {
    // Using HashMap to ensure that finding words will happen fast (expected O(1) for contains() with worst case for search being O(log n))
    // Using TreeSet to maintain a sorted order - results in faster search but slower save
    private final HashMap<String, TreeSet<WordTfIdfEntry>> invertedIndex;

    public Database(String documentsFile) {
        this.invertedIndex = loadDocumentsFile(documentsFile);
    }

    public static HashMap<String, TreeSet<WordTfIdfEntry>> loadDocumentsFile(String documentsFile) {
        final Map<String, Long> numOfDocumentsWithWord = new ConcurrentHashMap<>();

        final Stream<String> documents = FileLoader.getLinesFromFile(documentsFile);

        final var termFrequenciesByDocument = documents
                .parallel()
                .map(doc -> doc.split(" "))
                .peek(placeWordsInGlobalCount(numOfDocumentsWithWord))
                .map(generateTfMapForDocument())
                .collect(Collectors.toList());

        Map<String, Double> idfMap = calculateWordIDFs(numOfDocumentsWithWord);

        /*
        It is done in this a little overcomplicated way to ensure immutability
        and to enable efficient parallel processing
         */
        return IntStream.range(0, termFrequenciesByDocument.size())
                .parallel()
                .mapToObj(documentOrdinal ->
                        calculateTdIdfForWordsInDoc(termFrequenciesByDocument, idfMap, documentOrdinal)
                )
                .flatMap(Function.identity())
                .collect(
                        groupingBy(
                                Map.Entry::getKey,
                                HashMap::new,
                                mapping(Map.Entry::getValue, toCollection(TreeSet::new))
                        )
                );
    }

    public static Consumer<String[]> placeWordsInGlobalCount(Map<String, Long> numOfDocumentsWithWord) {
        return words ->
                Arrays.stream(words)
                        .forEach(word ->
                                Optional.ofNullable(numOfDocumentsWithWord.get(word))
                                        .ifPresentOrElse(
                                                count -> numOfDocumentsWithWord.put(word, ++count),
                                                () -> numOfDocumentsWithWord.put(word, 1L)
                                        )
                        );
    }

    public Optional<TreeSet<WordTfIdfEntry>> searchForOccurrences(String key) {
        return Optional.ofNullable(invertedIndex.get(key));
    }
}
