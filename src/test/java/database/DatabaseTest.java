package database;

import model.WordTfIdfEntry;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DatabaseTest {

    @Test
    public void createIndexFromFile() {
        var index = Database.createIndexFromFile("src/test/resources/documents.txt");

        assertEquals(index.size(), 12);
        assertEquals(index.get("brown").size(), 2);
        assertTrue(index.get("brown").first().getDocumentOrdinal().equals(0));
        assertTrue(new ArrayList<>(index.get("brown")).get(1).getDocumentOrdinal().equals(1));
        assertEquals(index.get("fox").size(), 2);
        assertTrue(index.get("fox").first().getDocumentOrdinal().equals(2));
        assertTrue(new ArrayList<>(index.get("fox")).get(1).getDocumentOrdinal().equals(0));
    }
}