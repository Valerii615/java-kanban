package ru.valerii.task_tracker.service;

import ru.valerii.task_tracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    List<Task> browsingHistory = new ArrayList<>();

    public void addInHistory(Task task) { //  новый метод
        if (browsingHistory.size() < 10) {
            browsingHistory.add(task);
        } else {
            browsingHistory.remove(0);
            browsingHistory.add(task);
        }
    }

    public List<Task> getHistory() { // новый метод
        return browsingHistory;
    }
}
