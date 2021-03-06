package com.sergsnmail.chat.server.services.history;

import java.util.List;

public class HistoryService {
    IHistory history;

    public HistoryService(IHistory history){
        this.history = history;
    }

    public void add(String message){
        history.add(message);
    }

    public List<String> get(int count){
        return history.get(count);
    }
}
