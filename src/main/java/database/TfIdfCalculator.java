package database;

import model.WordTfIdfEntry;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.log;

public class TfIdfCalculator {
    public static Function<String[], Map<String, Double>> generateTfMapForDocument() {
        return line -> {
            var size = line.length;
            return Arrays.stream(line)
                    .map(String::toLowerCase)
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

    public static Map<String, Double> calculateWordIDFs(Map<String, Long> numOfDocumentsWithWord) {
        var allWordsCount = numOfDocumentsWithWord.size();
        return numOfDocumentsWithWord.entrySet().parallelStream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> log(allWordsCount / e.getValue().doubleValue())
                ));
    }

    public static Stream<AbstractMap.SimpleEntry<String, WordTfIdfEntry>> calculateTdIdfForWordsInDoc(
            List<Map<String, Double>> termFrequenciesByDocument,
            Map<String, Double> idfMap,
            int documentOrdinal)
    {
        return termFrequenciesByDocument.get(documentOrdinal).entrySet().stream().map((entry) -> {
            var word = entry.getKey();
            var wordTf = entry.getValue();
            var wordTfIdf = idfMap.get(word) * wordTf;
            return new AbstractMap.SimpleEntry<>(word, new WordTfIdfEntry(wordTfIdf, documentOrdinal));
        });
    }
}
