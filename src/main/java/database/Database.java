package database;

import model.WordTfIdfEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static database.TfIdfCalculator.calculateWordIDFs;
import static database.TfIdfCalculator.generateTfMapForDocument;

public class Database {
    private static Database instance = null;

    // Using HashMap to ensure that finding words will happen fast (expected O(1) for contains() with worst case for search being O(log n))
    // Using TreeSet to maintain a sorted order - results in faster search but slower save
    private final HashMap<String, TreeSet<WordTfIdfEntry>> invertedIndex;

    private Database(String documentsFile) {
        this.invertedIndex = loadDocumentsFile(documentsFile);
    }

    public static Database getDataBase() {
        if(instance == null) {
            instance = new Database("documents.txt");
        }
        return instance;
    }

    public static HashMap<String, TreeSet<WordTfIdfEntry>> loadDocumentsFile(String documentsFile) {
        final HashMap<String, Long> numOfDocumentsWithWord = new HashMap<>();

        Stream<String> stream = FileLoader.getLinesFromFile(documentsFile);

        var termFrequenciesByDocument = stream
                .parallel()
                .map(generateTfMapForDocument(numOfDocumentsWithWord))
                .collect(Collectors.toList());

        Map<String, Double> idfMap = calculateWordIDFs(numOfDocumentsWithWord, numOfDocumentsWithWord.size());

        final HashMap<String, TreeSet<WordTfIdfEntry>> invertedIndex = new HashMap<>();

        IntStream.range(0, termFrequenciesByDocument.size())
                .forEach(documentOrdinal ->
                        termFrequenciesByDocument.get(documentOrdinal).forEach((word, wordTf) -> {
                            var wordTfIdf = idfMap.get(word) * wordTf;
                            Optional.ofNullable(invertedIndex.get(word))
                                    .ifPresentOrElse(
                                            wordIdfMap -> wordIdfMap.add(new WordTfIdfEntry(wordTfIdf, documentOrdinal)),
                                            () -> {
                                                TreeSet<WordTfIdfEntry> wordTfIdfSet = new TreeSet<>();
                                                wordTfIdfSet.add(new WordTfIdfEntry(wordTfIdf, documentOrdinal));
                                                invertedIndex.put(word, wordTfIdfSet);
                                            }
                                    );
                        })
                );

        return invertedIndex;
    }

    public TreeSet<WordTfIdfEntry> searchForOccurrences(String key) {
        return invertedIndex.get(key);
    }
}
