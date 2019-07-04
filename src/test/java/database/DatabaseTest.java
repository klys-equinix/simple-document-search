package database;

import model.WordTfIdfEntry;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DatabaseTest {

    @Test
    public void thatCanSearchForToken() {
        var index = new Database("src/test/resources/documents.txt");

        var foxOccurrences = index.searchForOccurrences("fox");
        var brownOccurrences = index.searchForOccurrences("brown");

        assertTrue(foxOccurrences.isPresent());
        assertTrue(brownOccurrences.isPresent());
        assertEquals(foxOccurrences.get().size(), 2);
        assertEquals(brownOccurrences.get().size(), 2);
        assertEquals(2, (int) foxOccurrences.get().first().getDocumentOrdinal());
        assertEquals(0, (int) new ArrayList<>(foxOccurrences.get()).get(1).getDocumentOrdinal());
        assertEquals(0, (int) brownOccurrences.get().first().getDocumentOrdinal());
        assertEquals(1, (int) new ArrayList<>(brownOccurrences.get()).get(1).getDocumentOrdinal());
    }
}