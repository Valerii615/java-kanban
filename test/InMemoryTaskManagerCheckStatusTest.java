import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.valerii.task_tracker.model.Epic;
import ru.valerii.task_tracker.model.Subtask;
import ru.valerii.task_tracker.service.Managers;
import ru.valerii.task_tracker.service.Status;
import ru.valerii.task_tracker.service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerCheckStatusTest {
    static TaskManager taskManager;

    @BeforeAll
    static void addingAllTypesOfTasks() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void checkingTheBoundaryValuesOfTheCalculationOfTheEpicTaskStatus() {
        taskManager.addEpic(new Epic("Эпик задача", "Описание"));

        taskManager.addSubtask(new Subtask("Подзадача №1.Эпик1", "Описание", Status.NEW, 1, LocalDateTime.of(2024, 5, 17, 14, 0), Duration.ofMinutes(30)));
        taskManager.addSubtask(new Subtask("Подзадача №2.Эпик1", "Описание", Status.NEW, 1, LocalDateTime.of(2024, 5, 17, 15, 0), Duration.ofMinutes(30)));
        taskManager.addSubtask(new Subtask("Подзадача №3.Эпик1", "Описание", Status.NEW, 1, LocalDateTime.of(2024, 5, 17, 16, 0), Duration.ofMinutes(30)));
        assertEquals(Status.NEW, taskManager.getEpicOfId(1).getStatus(), "Epic имеет неверный статус");

        taskManager.updateSubtask(2, new Subtask("Подзадача №1.Эпик1", "Описание", Status.DONE, 1, LocalDateTime.of(2024, 5, 18, 14, 0), Duration.ofMinutes(30)));
        taskManager.updateSubtask(3, new Subtask("Подзадача №2.Эпик1", "Описание", Status.DONE, 1, LocalDateTime.of(2024, 5, 18, 15, 0), Duration.ofMinutes(30)));
        taskManager.updateSubtask(4, new Subtask("Подзадача №3.Эпик1", "Описание", Status.DONE, 1, LocalDateTime.of(2024, 5, 18, 16, 0), Duration.ofMinutes(30)));
        assertEquals(Status.DONE, taskManager.getEpicOfId(1).getStatus(), "Epic имеет неверный статус");

        taskManager.updateSubtask(2, new Subtask("Подзадача №1.Эпик1", "Описание", Status.NEW, 1, LocalDateTime.of(2024, 5, 18, 14, 0), Duration.ofMinutes(30)));
        taskManager.updateSubtask(3, new Subtask("Подзадача №2.Эпик1", "Описание", Status.DONE, 1, LocalDateTime.of(2024, 5, 18, 15, 0), Duration.ofMinutes(30)));
        taskManager.updateSubtask(4, new Subtask("Подзадача №3.Эпик1", "Описание", Status.DONE, 1, LocalDateTime.of(2024, 5, 18, 16, 0), Duration.ofMinutes(30)));
        assertEquals(Status.IN_PROGRESS, taskManager.getEpicOfId(1).getStatus(), "Epic имеет неверный статус");

        taskManager.updateSubtask(2, new Subtask("Подзадача №1.Эпик1", "Описание", Status.IN_PROGRESS, 1, LocalDateTime.of(2024, 5, 19, 14, 0), Duration.ofMinutes(30)));
        taskManager.updateSubtask(3, new Subtask("Подзадача №2.Эпик1", "Описание", Status.IN_PROGRESS, 1, LocalDateTime.of(2024, 5, 19, 15, 0), Duration.ofMinutes(30)));
        taskManager.updateSubtask(4, new Subtask("Подзадача №3.Эпик1", "Описание", Status.IN_PROGRESS, 1, LocalDateTime.of(2024, 5, 19, 16, 0), Duration.ofMinutes(30)));
        assertEquals(Status.IN_PROGRESS, taskManager.getEpicOfId(1).getStatus(), "Epic имеет неверный статус");
    }
}
