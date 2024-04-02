package ru.valerii.task_tracker.test;

import org.junit.jupiter.api.Test;
import ru.valerii.task_tracker.model.Task;
import ru.valerii.task_tracker.service.HistoryManager;
import ru.valerii.task_tracker.service.Managers;
import ru.valerii.task_tracker.service.Status;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    static HistoryManager historyManager = Managers.getDefaultHistory();
    static Task task = new Task("Обычная задача №1", "Описание", Status.NEW);

    /**
     * проверка на добавление задачи в историю и получение истории задач
     */
    @Test
    void addTaskInHistory() {
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "неверная длинна истории");
        historyManager.add(task);
        assertEquals(2, historyManager.getHistory().size(), "неверная длинна истории");
    }

    @Test
    void removeAllTaskFromHistory() {
        historyManager.removeAll();
        assertEquals(0, historyManager.getHistory().size(), "история не очищена");
    }
}