package ru.valerii.task_tracker.service;

import ru.valerii.task_tracker.model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    void clear();

    List<Task> getHistory();
}