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

        taskManager.addNewTask(new Task("Обычная задача №1", "Описание", Status.NEW));
        taskManager.addNewTask(new Task("Обычная задача №2", "Описание", Status.NEW));

        taskManager.addNewEpic(new Epic("Эпик задача №1 с 2-мя подзадачами", "Описание"));
        taskManager.addNewEpic(new Epic("Эпик задача №2 с одной подзадачей", "Описание"));

        taskManager.addNewSub(new Subtask("Подзадача №1.Эпик1", "Описание", Status.NEW, -1257237928));
        taskManager.addNewSub(new Subtask("Подзадача №2.Эпик1", "Описание", Status.NEW, -1257237928));
        taskManager.addNewSub(new Subtask("Подзадача №1.Эпик2", "Описание", Status.NEW, 194570775));
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
        taskManager.updateNormalTask(-633266119, new Task("Обычная обновленная задача №1", "Описание", Status.IN_PROGRESS));
        taskManager.updateNormalTask(-633266088, new Task("Обычная обновленная задача №2", "Описание", Status.DONE));

        taskManager.updateSubtask(-1604581076, new Subtask("Подзадача обновленная №1.Эпик1", "Описание", Status.IN_PROGRESS, -1257237928));
        taskManager.updateSubtask(138229259, new Subtask("Подзадача обновленная №2.Эпик1", "Описание", Status.DONE, -1257237928));
        taskManager.updateSubtask(-1604581045, new Subtask("Подзадача обновленная №1.Эпик2", "Описание", Status.DONE, 194570775));

        ArrayList<Task> tasks = taskManager.getTasks();
        for (int i = 0; i < tasks.size(); i++) {
            switch (i) {
                case 0:
                    assertEquals(Status.IN_PROGRESS, tasks.get(i).getStatusTask(), "Не удалось изменить статус задачи " + tasks.get(i).getNameTask());
                    break;
                case 1:
                    assertEquals(Status.DONE, tasks.get(i).getStatusTask(), "Не удалось изменить статус задачи " + tasks.get(i).getNameTask());
                    break;
            }
        }
        ArrayList<Task> epics = taskManager.getEpics();
        for (int i = 0; i < epics.size(); i++) {
            switch (i) {
                case 0:
                    assertEquals(Status.IN_PROGRESS, epics.get(i).getStatusTask(), "Не удалось изменить статус эпика " + epics.get(i).getNameTask());
                    break;
                case 1:
                    assertEquals(Status.DONE, epics.get(i).getStatusTask(), "Не удалось изменить статус эпика " + epics.get(i).getNameTask());
            }
        }
        ArrayList<Task> subtasks = taskManager.getSubtasks();
        for (int i = 0; i < subtasks.size(); i++) {
            switch (i) {
                case 0:
                    assertEquals(Status.IN_PROGRESS, subtasks.get(i).getStatusTask(), "Не удалось изменить статус подзадачи " + subtasks.get(i).getNameTask());
                    break;
                case 1, 2:
                    assertEquals(Status.DONE, subtasks.get(i).getStatusTask(), "Не удалось изменить статус подзадачи " + subtasks.get(i).getNameTask());
                    break;
            }
        }
    }

    /**
     * провера на добавление задачи в историю
     */
    @Test
    void checkTheAdditionOfTaskToTheHierarchy() {
        taskManager.getNormalTaskOfId(-633266119);
        taskManager.getNormalTaskOfId(-633266088);

        taskManager.getEpicTaskOfId(-1257237928);
        taskManager.getEpicTaskOfId(194570775);

        taskManager.getSubTaskOfId(-1604581076);
        taskManager.getSubTaskOfId(138229259);
        taskManager.getSubTaskOfId(-1604581045);

        List<Task> history = taskManager.getHistory();
        assertEquals(7, history.size(), "история имеет неверное количество сохраненных значений");

        taskManager.getNormalTaskOfId(-633266119);
        taskManager.getNormalTaskOfId(-633266088);

        taskManager.getEpicTaskOfId(-1257237928);
        taskManager.getEpicTaskOfId(194570775);

        assertTrue(history.size() <= 10, "история имеет больше 10 сохраненных значений" );
    }


    /**
     * проверка на удаление задач
     */
    @AfterAll
    static void checkingForDeletionOfTasks() {
        taskManager.deleteNormalTaskOfId(-633266119);
        taskManager.deleteEpicTaskOfId(194570775);
        assertNull(taskManager.getNormalTaskOfId(-633266119), "задача не удалена");
        assertNull(taskManager.getEpicTaskOfId(194570775), "задача не удалена");
        assertNull(taskManager.getSubTaskOfId(-1604581045), "задача не удалена");

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