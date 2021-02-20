package com.sergsnmail.chat.server.services.history;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class FileHistory implements IHistory {

    private String HISTORY_DIR = "history/";
    private String PREF = "history_";
    private String EXT = ".txt";
    private File historyFile;

    public FileHistory(String fileName) {
        historyFile = new File(HISTORY_DIR + PREF + fileName + EXT);
        try {
            new File(HISTORY_DIR).mkdirs();
            historyFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(String message) {
        try (FileWriter writer = new FileWriter(historyFile, true)) {
            writer.write(message + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> get(int count) {
        List<String> result = null;
        int startPos = getStartPosition(count);
        int pos = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
            String line;
            result = new LinkedList<>();
            while ((line = reader.readLine()) != null) {
                if (pos >= startPos) {
                    result.add(line);
                }
                pos++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private int getStartPosition(int count) {
        int result = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
            while (reader.readLine() != null) {
                result++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return count > result ? 0 : result - count;
    }
}
