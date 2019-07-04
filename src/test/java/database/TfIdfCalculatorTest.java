package database;

import model.WordTfIdfEntry;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Math.log;
import static org.junit.Assert.*;

public class TfIdfCalculatorTest {

    @Test
    public void generateTfMapForDocument() {
        var document = "the Brown fox jumped the brown".split(" ");

        var documentTfMap = TfIdfCalculator.generateTfMapForDocument().apply(document);

        assertEquals(documentTfMap.get("the"), ((double) 2) / document.length, 0.0);
        assertEquals(documentTfMap.get("brown"), ((double) 2) / document.length, 0.0);
        assertNull(documentTfMap.get("Brown"));
        assertEquals(documentTfMap.get("fox"), ((double) 1) / document.length, 0.0);
        assertEquals(documentTfMap.get("jumped"), ((double) 1) / document.length, 0.0);
    }

    @Test
    public void calculateWordIDFs() {
        var globalWordCount = Map.of(
                "a", 1L,
                "b", 2L
        );

        var wordIdfs = TfIdfCalculator.calculateWordIDFs(globalWordCount);

        assertEquals(wordIdfs.get("b"), 0, 0.0);
        assertEquals(wordIdfs.get("a"), log(2), 0.0);
    }

    @Test
    public void calculateTdIdfForWordsInDoc() {
        var termFrequenciesByDocument = List.of(
                Map.of(
                        "a", 0.125,
                        "b", 0.5
                )
        );

        var idfMap = Map.of(
                "a", 0.3,
                "b", 0.1
        );

        var tdIdfsForDoc = TfIdfCalculator.calculateTdIdfForWordsInDoc(
                termFrequenciesByDocument,
                idfMap,
                0
        ).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
        ));

        assertEquals(tdIdfsForDoc.get("a"), new WordTfIdfEntry(0.125 * 0.3, 0));
        assertEquals(tdIdfsForDoc.get("b"), new WordTfIdfEntry(0.5 * 0.1, 0));

    }
}