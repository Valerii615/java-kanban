import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.valerii.task_tracker.model.Epic;
import ru.valerii.task_tracker.model.Subtask;
import ru.valerii.task_tracker.model.Task;
import ru.valerii.task_tracker.service.FileBackedTaskManager;
import ru.valerii.task_tracker.service.Status;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    static FileBackedTaskManager fileBackedTaskManager;

    /**
     * создаем все экземпляры задач c сохранением в файл:
     * <p>
     * 2 обычные задачи, эпик с 2-мя подзадачами, эпик с одной подзадачей
     */
    @BeforeAll
    static void creatingAndSavingTasks() {
        fileBackedTaskManager = new FileBackedTaskManager();

        fileBackedTaskManager.addTask(new Task("Обычная задача №1", "Описание", Status.NEW));
        fileBackedTaskManager.addTask(new Task("Обычная задача №2", "Описание", Status.NEW));

        fileBackedTaskManager.addEpic(new Epic("Эпик задача №1 с 2-мя подзадачами", "Описание"));
        fileBackedTaskManager.addEpic(new Epic("Эпик задача №2 с одной подзадачей", "Описание"));

        fileBackedTaskManager.addSubtask(new Subtask("Подзадача №1.Эпик1", "Описание", Status.NEW, 3));
        fileBackedTaskManager.addSubtask(new Subtask("Подзадача №2.Эпик1", "Описание", Status.NEW, 3));
        fileBackedTaskManager.addSubtask(new Subtask("Подзадача №1.Эпик2", "Описание", Status.NEW, 4));
    }

    /**
     * проверка на сохранение задачь в оперативной памяти
     */
    @Test
    void savingTasksInRam() {
        assertFalse(fileBackedTaskManager.getTasks().isEmpty(), "Задачи типа Task не сохранились в оперативную память");
        assertFalse(fileBackedTaskManager.getEpics().isEmpty(), "Задачи типа Epic не сохранились в оперативной памяти");
        assertFalse(fileBackedTaskManager.getSubtasks().isEmpty(), "Задачи типа Subtask не сохранились в оперативной памяти");
    }

    /**
     * проверка на восстановление задачь из файла
     */
    @Test
    void restoringTasksFromFile() {
        fileBackedTaskManager.loadFromFile("src/ru/valerii/task_tracker/task_storage.csv");
        assertFalse(fileBackedTaskManager.getTasks().isEmpty(), "Задачи типа Task не восстановились из файла");
        assertFalse(fileBackedTaskManager.getEpics().isEmpty(), "Задачи типа Epic не восстановились из файла");
        assertFalse(fileBackedTaskManager.getSubtasks().isEmpty(), "Задачи типа Subtask не восстановились из файла");
    }

    /**
     * проверка корректного присвоения id после восстановления из файла
     */
    @Test
    void correctAssignmentOfAnIdAfterRecoveryFromFile() {
        fileBackedTaskManager.loadFromFile("src/ru/valerii/task_tracker/task_storage.csv");
        fileBackedTaskManager.addTask(new Task("Обычная задача №1", "Описание", Status.NEW));
        System.out.println(fileBackedTaskManager.getIdCount());
    }

    /**
     * проверка на удаление задачь из оперативной и постоянной памяти
     */
    @AfterAll
    static void deletingTasksFromRamAndPermanentMemory() {
        fileBackedTaskManager.removeAllTask();
        assertTrue(fileBackedTaskManager.getTasks().isEmpty(), "Задачи типа Task не удалились из оперативной памяти");
        assertTrue(fileBackedTaskManager.getEpics().isEmpty(), "Задачи типа Epic из оперативной памяти");
        assertTrue(fileBackedTaskManager.getSubtasks().isEmpty(), "Задачи типа Subtask из оперативной памяти");

        fileBackedTaskManager.loadFromFile("src/ru/valerii/task_tracker/task_storage.csv");
        assertTrue(fileBackedTaskManager.getTasks().isEmpty(), "Задачи типа Task не удалились из постоянной памяти");
        assertTrue(fileBackedTaskManager.getEpics().isEmpty(), "Задачи типа Epic из постоянной памяти");
        assertTrue(fileBackedTaskManager.getSubtasks().isEmpty(), "Задачи типа Subtask из постоянной памяти");

    }

}
