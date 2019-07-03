package model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(of = "tdIdf")
@AllArgsConstructor
@Getter
public class WordTfIdfEntry implements Comparable<WordTfIdfEntry> {
    private Double tdIdf;
    private Integer documentOrdinal;

    @Override
    public int compareTo(WordTfIdfEntry e) {
        //because we want descending order
        return -1 * tdIdf.compareTo(e.tdIdf);
    }
}
