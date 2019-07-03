package database;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.log;

public class TfIdfCalculator {
    public static Map<String, Double> calculateWordIDFs(HashMap<String, Long> numOfDocumentsWithWord, int allWordsCount) {
        return numOfDocumentsWithWord.entrySet().parallelStream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> log(allWordsCount / e.getValue().doubleValue())
                ));
    }

    public static Function<String, Map<String, Double>> generateTfMapForDocument(HashMap<String, Long> numOfDocumentsWithWord) {
        return line -> {
            String[] wordsInLine = line.split(" ");
            var size = wordsInLine.length;
            return Arrays.stream(wordsInLine)
                    .map(String::toLowerCase)
                    .peek(placeWordInGlobalCount(numOfDocumentsWithWord))
                    .collect(
                            Collectors.groupingBy(s -> s,
                                    Collectors.collectingAndThen(
                                            Collectors.counting(),
                                            count -> ((double) count) / size
                                    )
                            )
                    );
        };
    }

    public static Consumer<String> placeWordInGlobalCount(HashMap<String, Long> numOfDocumentsWithWord) {
        return word ->
                Optional.ofNullable(numOfDocumentsWithWord.get(word))
                        .ifPresentOrElse(
                                count -> numOfDocumentsWithWord.put(word, ++count),
                                () -> numOfDocumentsWithWord.put(word, 1L)
                        );
    }
}
