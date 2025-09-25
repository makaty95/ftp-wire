package com.makaty.code.Common.Models;

import com.makaty.code.Server.Models.ClientProfile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilityFunctions {

    // Wrap text with a specified max line length.
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

    // Open a directory inside some directory and return the final file.
    public static File openDirectory(File current, String directoryName) throws IOException {
        return new File(current, directoryName).getCanonicalFile();
    }

    // Get all files inside some directory.
    public static List<File> getFilesInside(File newFile) {
        File[] files = newFile.listFiles();
        if(files == null) return List.of();
        return new ArrayList<>(Arrays.asList(files));
    }

    // Check whether the current File is authorized or not.
    public static Status checkFileAuthorization(File file, ClientProfile clientProfile) {
        File root = clientProfile.getRootDir();

        if(!file.getPath().startsWith(root.getPath())) return Status.UNAUTHORIZED_ACCESS;
        return Status.OK;
    }
}
