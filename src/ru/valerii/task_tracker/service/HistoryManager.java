package ru.valerii.task_tracker.service;

import ru.valerii.task_tracker.model.Task;

import java.util.List;

public interface HistoryManager {
    void addInHistory(Task task);
    List<Task> getHistory();
}
