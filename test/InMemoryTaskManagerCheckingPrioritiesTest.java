import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.valerii.task_tracker.model.Epic;
import ru.valerii.task_tracker.model.Subtask;
import ru.valerii.task_tracker.model.Task;
import ru.valerii.task_tracker.service.FileBackedTaskManager;
import ru.valerii.task_tracker.service.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerCheckingPrioritiesTest {
    static FileBackedTaskManager taskManager = new FileBackedTaskManager();

    /**
     * проверка на добавление задачи в приоритетный спиок
     */
    @BeforeAll
    static void addingTaskInPrioritizedTasksList() {
        taskManager.addTask(new Task("Обычная задача №1", "Описание", Status.NEW, LocalDateTime.of(2024, 5, 17, 12, 0), Duration.ofMinutes(30)));
        taskManager.addTask(new Task("Обычная задача №2", "Описание", Status.NEW, LocalDateTime.of(2024, 5, 17, 13, 0), Duration.ofMinutes(40)));

        taskManager.addEpic(new Epic("Эпик задача №1 с 2-мя подзадачами", "Описание"));
        taskManager.addEpic(new Epic("Эпик задача №2 с одной подзадачей", "Описание"));

        taskManager.addSubtask(new Subtask("Подзадача №1.Эпик1", "Описание", Status.NEW, 3, LocalDateTime.of(2024, 5, 17, 14, 0), Duration.ofMinutes(30)));
        taskManager.addSubtask(new Subtask("Подзадача №2.Эпик1", "Описание", Status.NEW, 3, LocalDateTime.of(2024, 5, 17, 15, 0), Duration.ofMinutes(30)));
        taskManager.addSubtask(new Subtask("Подзадача №1.Эпик2", "Описание", Status.NEW, 4, LocalDateTime.of(2024, 5, 17, 16, 0), Duration.ofMinutes(30)));
    }

    /**
     * проверка корректного расчета времени эпик задачи c одной, двумя и тремя подзадачами
     */
    @Test
    void correctnessOfTheCalculationOfTheEpicTime() {
        assertEquals("2024-05-17T16:00", taskManager.getEpicOfId(4).getStartTime().toString(), "Время начала эпика расчитано не верно");
        assertEquals("2024-05-17T16:30", taskManager.getEpicOfId(4).getEndTime().toString(), "Время завершения эпика расчитано неверно");
        taskManager.addSubtask(new Subtask("Подзадача №*.Эпик2", "Описание", Status.NEW, 4, LocalDateTime.of(2024, 5, 17, 17, 0), Duration.ofMinutes(30)));
        assertEquals("2024-05-17T16:00", taskManager.getEpicOfId(4).getStartTime().toString(), "Время начала эпика расчитано не верно");
        assertEquals("2024-05-17T17:30", taskManager.getEpicOfId(4).getEndTime().toString(), "Время завершения эпика расчитано неверно");
        taskManager.addSubtask(new Subtask("Подзадача №*2.Эпик2", "Описание", Status.NEW, 4, LocalDateTime.of(2024, 5, 17, 18, 0), Duration.ofMinutes(30)));
        assertEquals("2024-05-17T16:00", taskManager.getEpicOfId(4).getStartTime().toString(), "Время начала эпика расчитано не верно");
        assertEquals("2024-05-17T18:30", taskManager.getEpicOfId(4).getEndTime().toString(), "Время завершения эпика расчитано неверно");
        taskManager.removeSubtaskOfId(8);
        taskManager.removeSubtaskOfId(9);
    }

    /**
     * проверка на добавление задач в список приоритетов
     */
    @Test
    void addingTasksToThePriorityList() {
        assertEquals(5, taskManager.getPrioritizedTasksList().size(), "неверная длинна списка приоритетов");
    }

    /**
     * проверка корректного порядка задач
     */
    @Test
    void correctTaskOrder() {
        assertEquals(1, taskManager.getPrioritizedTasksList().get(0).getId(), "неверный порядок задач");
        assertEquals(5, taskManager.getPrioritizedTasksList().get(2).getId(), "неверный порядок задач");
        assertEquals(7, taskManager.getPrioritizedTasksList().get(4).getId(), "неверный порядок задач");
    }

    /**
     * проверка обновления списка приоритетов с начала середины и конца списка
     */
    @Test
    void updatingThePriorityList() {
        taskManager.updateTask(1, new Task("Обычная задача №1", "Описание", Status.NEW, LocalDateTime.of(2024, 5, 17, 12, 10), Duration.ofMinutes(30)));
        taskManager.updateSubtask(5, new Subtask("Подзадача №1.Эпик1", "Описание", Status.NEW, 3, LocalDateTime.of(2024, 5, 17, 14, 10), Duration.ofMinutes(30)));
        taskManager.updateSubtask(7, new Subtask("Подзадача №1.Эпик2", "Описание", Status.NEW, 4, LocalDateTime.of(2024, 5, 17, 16, 10), Duration.ofMinutes(30)));
        assertEquals(5, taskManager.getPrioritizedTasksList().size(), "неверная длинна списка приоритетов");
        assertEquals("2024-05-17T12:10", taskManager.getTaskOfId(1).getStartTime().toString(), "время начала задачи не обновилось");
        assertEquals("2024-05-17T14:10", taskManager.getSubtaskOfId(5).getStartTime().toString(), "время начала задачи не обновилось");
        assertEquals("2024-05-17T16:10", taskManager.getSubtaskOfId(7).getStartTime().toString(), "время начала задачи не обновилось");
        taskManager.updateSubtask(7, new Subtask("Подзадача №1.Эпик2", "Описание", Status.NEW, 4, LocalDateTime.of(2024, 5, 17, 16, 0), Duration.ofMinutes(30)));
    }

    /**
     * проверка удаления задачи из начала середины конца и полное удаление задачь из списка приоритетов
     */
    @AfterAll
    static void removingFromThePriorityList() {
        taskManager.removeTaskOfId(1);
        assertEquals(4, taskManager.getPrioritizedTasksList().size(), "задача id-1 не удалилась из списка приоритетов");
        taskManager.removeSubtaskOfId(5);
        assertEquals(3, taskManager.getPrioritizedTasksList().size(), "задача id-5 не удалилась из списка приоритетов");
        taskManager.removeSubtaskOfId(7);
        assertEquals(2, taskManager.getPrioritizedTasksList().size(), "задача id-7 не удалилась из списка приоритетов");
        taskManager.removeAllTask();
        assertEquals(0, taskManager.getPrioritizedTasksList().size(), "задачи не удалились из списка приоритетов");
    }
}