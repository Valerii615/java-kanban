package ru.valerii.task_tracker.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.valerii.task_tracker.model.Task;
import ru.valerii.task_tracker.service.HistoryManager;
import ru.valerii.task_tracker.service.Managers;
import ru.valerii.task_tracker.service.Status;
import ru.valerii.task_tracker.service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    TaskManager taskManager = Managers.getDefault();

    /**
     * проверка на добавление задачи в историю и получение истории задач
     */
    @Test
    void addTaskInHistory() {
        taskManager.addTask(new Task("1", "1"));
        taskManager.addTask(new Task("2", "2"));
        taskManager.getTaskOfId(1);
        assertEquals(1, taskManager.getHistory().size(), "неверная длинна истории");
        taskManager.getTaskOfId(2);
        assertEquals(2, taskManager.getHistory().size(), "неверная длинна истории");
    }
}