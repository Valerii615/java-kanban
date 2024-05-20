import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.valerii.task_tracker.model.Epic;
import ru.valerii.task_tracker.model.Subtask;
import ru.valerii.task_tracker.model.Task;
import ru.valerii.task_tracker.service.Managers;
import ru.valerii.task_tracker.service.Status;
import ru.valerii.task_tracker.service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerCheckingPrioritiesTest {
    static TaskManager taskManager = Managers.getDefault();

    /**
     * проверка на добавление задачи в приоритетный спиок
     */
    @BeforeAll
    static void addingTaskInPrioritizedTasksList() {
        taskManager.addTask(new Task("Обычная задача №1", "Описание", Status.NEW, LocalDateTime.of(2024,5,17,12,0), Duration.ofMinutes(30)));
        taskManager.addTask(new Task("Обычная задача №2", "Описание", Status.NEW, LocalDateTime.of(2024,5,17,13,0), Duration.ofMinutes(40)));

        taskManager.addEpic(new Epic("Эпик задача №1 с 2-мя подзадачами", "Описание"));
        taskManager.addEpic(new Epic("Эпик задача №2 с одной подзадачей", "Описание"));

        taskManager.addSubtask(new Subtask("Подзадача №1.Эпик1", "Описание", Status.NEW, 3, LocalDateTime.of(2024,5,17,14,0), Duration.ofMinutes(30)));
        taskManager.addSubtask(new Subtask("Подзадача №2.Эпик1", "Описание", Status.NEW, 3, LocalDateTime.of(2024,5,17,15,0), Duration.ofMinutes(30)));
        taskManager.addSubtask(new Subtask("Подзадача №1.Эпик2", "Описание", Status.NEW, 4, LocalDateTime.of(2024,5,17,16,0), Duration.ofMinutes(30)));
    }

    /**
     * проверка на добавление задач в список приоритетов
     */
    @Test
    void addingTasksToThePriorityList() {
        assertEquals(7, taskManager.getPrioritizedTasksList().size(), "неверная длинна списка приоритетов");
    }

    /**
     * проверка корректного порядка задач
     */
    @Test
    void correctTaskOrder() {
        assertEquals(1, taskManager.getPrioritizedTasksList().get(0).getId(), "неверный порядок задач");
        assertEquals(5, taskManager.getPrioritizedTasksList().get(3).getId(), "неверный порядок задач");
        assertEquals(7, taskManager.getPrioritizedTasksList().get(6).getId(), "неверный порядок задач");
    }

    @AfterAll
    static void removingFromThePriorityList() {
        taskManager.removeAllTask();
        assertEquals(0, taskManager.getPrioritizedTasksList().size(), "задачи не удалились из списка приоритетов");
    }
}