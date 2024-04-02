package ru.valerii.task_tracker.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.valerii.task_tracker.model.Epic;
import ru.valerii.task_tracker.model.Subtask;
import ru.valerii.task_tracker.model.Task;
import ru.valerii.task_tracker.service.Managers;
import ru.valerii.task_tracker.service.Status;
import ru.valerii.task_tracker.service.TaskManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    static TaskManager taskManager;

    /**
     * создаем все экземпляры задач:
     * <p>
     * 2 обычные задачи, эпик с 2-мя подзадачами, эпик с одной подзадачей
     */
    @BeforeAll
    static void addingAllTypesOfTasks() {
        taskManager = Managers.getDefault();

        taskManager.addTask(new Task("Обычная задача №1", "Описание", Status.NEW));
        taskManager.addTask(new Task("Обычная задача №2", "Описание", Status.NEW));

        taskManager.addEpic(new Epic("Эпик задача №1 с 2-мя подзадачами", "Описание"));
        taskManager.addEpic(new Epic("Эпик задача №2 с одной подзадачей", "Описание"));

        taskManager.addSubtask(new Subtask("Подзадача №1.Эпик1", "Описание", Status.NEW, 3));
        taskManager.addSubtask(new Subtask("Подзадача №2.Эпик1", "Описание", Status.NEW, 3));
        taskManager.addSubtask(new Subtask("Подзадача №1.Эпик2", "Описание", Status.NEW, 4));
    }

    /**
     * проверка на добавление задач
     */
    @Test
    void checkingForAddingTasks() {
        assertFalse(taskManager.getTasks().isEmpty(), "Задача типа Task не добавилась");
        assertFalse(taskManager.getEpics().isEmpty(), "Задача типа Epic не добавилась");
        assertFalse(taskManager.getSubtasks().isEmpty(), "Задача типа Subask не добавилась");
    }

    /**
     * проверка на сохранение статусов задач, подзадач и расчет статуса эпика
     */
    @Test
    void checkingForSavingAndUpdatingStatuses() {
        taskManager.updateTask(1, new Task("Обычная обновленная задача №1", "Описание", Status.IN_PROGRESS));
        taskManager.updateTask(2, new Task("Обычная обновленная задача №2", "Описание", Status.DONE));

        taskManager.updateSubtask(5, new Subtask("Подзадача обновленная №1.Эпик1", "Описание", Status.IN_PROGRESS, 3));
        taskManager.updateSubtask(6, new Subtask("Подзадача обновленная №2.Эпик1", "Описание", Status.DONE, 3));
        taskManager.updateSubtask(7, new Subtask("Подзадача обновленная №1.Эпик2", "Описание", Status.DONE, 4));

        ArrayList<Task> tasks = taskManager.getTasks();
        for (int i = 0; i < tasks.size(); i++) {
            switch (i) {
                case 0:
                    assertEquals(Status.IN_PROGRESS, tasks.get(i).getStatus(), "Не удалось изменить статус задачи " + tasks.get(i).getName());
                    break;
                case 1:
                    assertEquals(Status.DONE, tasks.get(i).getStatus(), "Не удалось изменить статус задачи " + tasks.get(i).getName());
                    break;
            }
        }
        ArrayList<Task> epics = taskManager.getEpics();
        for (int i = 0; i < epics.size(); i++) {
            switch (i) {
                case 0:
                    assertEquals(Status.IN_PROGRESS, epics.get(i).getStatus(), "Не удалось изменить статус эпика " + epics.get(i).getName());
                    break;
                case 1:
                    assertEquals(Status.DONE, epics.get(i).getStatus(), "Не удалось изменить статус эпика " + epics.get(i).getName());
            }
        }
        ArrayList<Task> subtasks = taskManager.getSubtasks();
        for (int i = 0; i < subtasks.size(); i++) {
            switch (i) {
                case 0:
                    assertEquals(Status.IN_PROGRESS, subtasks.get(i).getStatus(), "Не удалось изменить статус подзадачи " + subtasks.get(i).getName());
                    break;
                case 1, 2:
                    assertEquals(Status.DONE, subtasks.get(i).getStatus(), "Не удалось изменить статус подзадачи " + subtasks.get(i).getName());
                    break;
            }
        }
    }

    /**
     * провера на добавление задачи в историю
     */
    @Test
    void checkTheAdditionOfTaskToTheHierarchy() {
        Task taskPozi1 = taskManager.getTaskOfId(1);
        Task taskPozi2 = taskManager.getEpicOfId(3);
        Task taskPozi3 = taskManager.getSubtaskOfId(5);

        List<Task> history = taskManager.getHistory();
        assertEquals(3, history.size(), "история имеет неверное количество сохраненных значений");
        for (int i = 0; i < history.size(); i++) {
            switch (i){
                case 0:
                    assertEquals(taskPozi1, history.get(0), "сохранена неверная задача");
                    break;
                case 1:
                    assertEquals(taskPozi2, history.get(1), "сохранена неверная задача");
                    break;
                case 2:
                    assertEquals(taskPozi3, history.get(2), "сохранена неверная задача");
            }
        }
        taskManager.getTaskOfId(1);
        taskManager.getTaskOfId(2);
        taskManager.getEpicOfId(3);
        taskManager.getEpicOfId(3);
        taskManager.getEpicOfId(4);
        taskManager.getEpicOfId(4);
        taskManager.getSubtaskOfId(7);
        taskManager.getSubtaskOfId(5);
        taskManager.getSubtaskOfId(6);

        assertTrue(history.size() <= 10, "история имеет больше 10 сохраненных значений" );
    }

    /**
     * проверка на удаление задач
     */
    @AfterAll
    static void checkingForDeletionOfTasks() {
        taskManager.deleteTaskOfId(1);
        taskManager.deleteEpicOfId(4);
        assertNull(taskManager.getTaskOfId(1), "задача не удалена");
        assertNull(taskManager.getEpicOfId(4), "задача не удалена");
        assertNull(taskManager.getSubtaskOfId(7), "задача не удалена");

        taskManager.deleteAllTask();
        assertEquals(0, taskManager.getTasks().size(), "остались неудаленные задачи");
        assertEquals(0, taskManager.getEpics().size(), "остались неудаленные задачи");
        assertEquals(0, taskManager.getSubtasks().size(), "остались неудаленные задачи");
    }

    void printAllTask() {
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
    }
}