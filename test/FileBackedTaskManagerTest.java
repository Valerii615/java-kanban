import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.valerii.task_tracker.model.Epic;
import ru.valerii.task_tracker.model.Subtask;
import ru.valerii.task_tracker.model.Task;
import ru.valerii.task_tracker.service.FileBackedTaskManager;
import ru.valerii.task_tracker.service.Status;


import java.time.Duration;
import java.time.LocalDateTime;

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

        fileBackedTaskManager.addTask(new Task("Обычная задача №1", "Описание", Status.NEW, LocalDateTime.of(2024,5,17,12,0), Duration.ofMinutes(30)));
        fileBackedTaskManager.addTask(new Task("Обычная задача №2", "Описание", Status.NEW, LocalDateTime.of(2024,5,17,13,0), Duration.ofMinutes(30)));

        fileBackedTaskManager.addEpic(new Epic("Эпик задача №1 с 2-мя подзадачами", "Описание"));
        fileBackedTaskManager.addEpic(new Epic("Эпик задача №2 с одной подзадачей", "Описание"));

        fileBackedTaskManager.addSubtask(new Subtask("Подзадача №1.Эпик1", "Описание", Status.NEW, 3, LocalDateTime.of(2024,5,17,14,0), Duration.ofMinutes(30)));
        fileBackedTaskManager.addSubtask(new Subtask("Подзадача №2.Эпик1", "Описание", Status.NEW, 3, LocalDateTime.of(2024,5,17,15,0), Duration.ofMinutes(30)));
        fileBackedTaskManager.addSubtask(new Subtask("Подзадача №1.Эпик2", "Описание", Status.NEW, 4, LocalDateTime.of(2024,5,17,16,0), Duration.ofMinutes(30)));
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
     * проверка на наличие сохраненного id в подзадаче
     */
    @Test
    public void checkingForSavedIdInSubtask() {
        assertEquals(3, fileBackedTaskManager.getSubtaskOfId(5).getIdEpic(), "неверно сохранненый id эпика");
    }

    /**
     * проверка на восстановление задачь из файла
     */
    @Test
    void restoringTasksFromFile() {
        fileBackedTaskManager.removeAllTaskNotSave();
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
        System.out.println();
        assertEquals(fileBackedTaskManager.getIdCount(), fileBackedTaskManager.getTaskOfId(fileBackedTaskManager.getIdCount()).getId(), "Неверно присвоенный id");
    }


    /**
     * обработка неккоректных форматов данных
      */
    @Test
    public void processingIncorrectDataFormats() {
        Wrapper wrapper = new Wrapper("src/test_file/task_storage_id.csv");
        Exception exception = assertThrows(RuntimeException.class, wrapper);
        assertEquals("Неверный формат id", exception.getMessage(), "получено неверное исключение");
        Wrapper wrapper1 = new Wrapper("src/test_file/task_storage_type_task.csv");
        Exception exception1 = assertThrows(RuntimeException.class, wrapper1);
        assertEquals("Неверный тип задачи", exception1.getMessage(), "получено неверное исключение");
        Wrapper wrapper2 = new Wrapper("src/test_file/task_storage_status.csv");
        Exception exception2 = assertThrows(RuntimeException.class, wrapper2);
        assertEquals("Неверный формат статуса", exception2.getMessage(), "получено неверное исключение");
        Wrapper wrapper3 = new Wrapper("src/test_file/task_storage_epic_is_null.csv");
        Exception exception3 = assertThrows(RuntimeException.class, wrapper3);
        assertEquals("Epic с заданным id не существует", exception3.getMessage(), "получено неверное исключение");
    }

    /**
     * тест на чтение несуществующего файла
     */
    @Test
    void readingNonExistentFile() {
        Wrapper wrapper = new Wrapper("storage.csv");
        Exception exception = assertThrows(RuntimeException.class, wrapper);
        assertEquals("Ошибка Чтения файла", exception.getMessage(), "получено неверное исключение");
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
class Wrapper implements Executable {
    String fileName;

    public Wrapper(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        FileBackedTaskManagerTest.fileBackedTaskManager.loadFromFile(fileName);
    }
}