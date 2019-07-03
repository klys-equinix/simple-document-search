import database.Database;

import java.util.Scanner;

public class SearchEngine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter documents file path: ");
        var docsPath = scanner.nextLine();
        var database = new Database(docsPath);

        System.out.println("Enter your search: ");
        while (scanner.hasNext()) {
            var searchString = scanner.nextLine();
            database.searchForOccurrences(searchString).forEach(e -> {
                System.out.println("Dokument " + e.getDocumentOrdinal());
            });
        }
    }
}
