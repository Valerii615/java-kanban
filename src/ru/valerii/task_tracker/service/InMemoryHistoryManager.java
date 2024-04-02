package ru.valerii.task_tracker.service;

import ru.valerii.task_tracker.model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> browsingHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (browsingHistory.size() < 10) {
            browsingHistory.addLast(task);
        } else {
            browsingHistory.remove(0);
            browsingHistory.addLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return browsingHistory;
    }

    @Override
    public void removeAll() {
        browsingHistory.clear();
    }

    @Override
    public void remove(Task task) {
        for (int i = 0; i < browsingHistory.size(); ) {
            if (browsingHistory.get(i).getId() == task.getId()) {
                browsingHistory.remove(i);
                i = 0;
            } else {
                i++;
            }
        }
    }
}
