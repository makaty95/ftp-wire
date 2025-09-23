package com.makaty.code.Common.Models;

import java.util.ArrayList;
import java.util.List;

public class UtilityFunctions {

    // wrap text with a specified max line length.
    public static List<String> wrap(String line, int lineWidth) {
        ArrayList<String> lines = new ArrayList<>();
        String[] words = line.split(" ");

        int curLen = 0;
        for(String word : words) {
            int toAdd = (word.length() + 1);
            if(lines.isEmpty()) {
                lines.add(word);
                curLen += word.length();
            } else if((toAdd + curLen) <= lineWidth) {
                String newLine = lines.get(lines.size() - 1).concat(" ").concat(word);
                lines.set(lines.size() - 1, newLine);
                curLen += toAdd;
            } else {
                lines.add(word);
                curLen = 0;
            }
        }

        return lines;

    }
}
