import database.Database;

import java.util.Scanner;

public class SearchEngine {
    public static void main(String[] args) {
        Database.getDataBase();
        System.out.println("Enter your search: ");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            var searchString = scanner.nextLine();
            Database.getDataBase().searchForOccurrences(searchString).stream().forEach(e -> {
                System.out.println("Dokument " + e.getDocumentOrdinal());
            });
        }
    }
}
