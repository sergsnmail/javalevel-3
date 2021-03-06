package com.sergsnmail.chat.server.services.history;

import java.util.List;

public interface IHistory {
    void add(String message);
    List<String> get(int count);
}
