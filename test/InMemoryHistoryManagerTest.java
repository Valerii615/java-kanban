import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.valerii.task_tracker.model.Task;
import ru.valerii.task_tracker.service.Managers;
import ru.valerii.task_tracker.service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    static TaskManager taskManager;

    @BeforeEach
    void addTask() {
        taskManager = Managers.getDefault();
        taskManager.addTask(new Task("1", "1"));
        taskManager.addTask(new Task("2", "2"));
        taskManager.addTask(new Task("3", "3"));
        taskManager.addTask(new Task("4", "4"));
        taskManager.addTask(new Task("5", "5"));
    }

    /**
     * проверка на добавление задачи в историю и дублирование
     */
    @Test
    void addTaskInHistory() {
        taskManager.getTaskOfId(1);
        assertEquals(1, taskManager.getHistory().size(), "неверная длинна истории");
        taskManager.getTaskOfId(2);
        assertEquals(2, taskManager.getHistory().size(), "неверная длинна истории");
        taskManager.getEpicOfId(1);
        assertEquals(2, taskManager.getHistory().size(), "неверная длинна истории");
        taskManager.removeAllTask();
    }

    /**
     * Удаление из истории: начало, середина, конец.
     */
    @Test
    void deletionFromHistoryBeginningMiddleEnd() {
        taskManager.getTaskOfId(1);
        taskManager.getTaskOfId(2);
        taskManager.getTaskOfId(3);
        taskManager.getTaskOfId(4);
        taskManager.getTaskOfId(5);
        taskManager.removeTaskOfId(1);
        assertEquals(4, taskManager.getHistory().size(), "неверная длинна истории (удаление задачи id-1)");
        taskManager.removeTaskOfId(3);
        assertEquals(3, taskManager.getHistory().size(), "неверная длинна истории (удаление задачи id-3)");
        taskManager.removeTaskOfId(5);
        assertEquals(2, taskManager.getHistory().size(), "неверная длинна истории (удаление задачи id-5");
        taskManager.removeAllTask();
    }

    /**
     * получение пустой истории задач
     */
    @AfterAll
    static void gettingAnEmptyTaskHistory() {
        assertEquals(0, taskManager.getHistory().size(), "неверная длинна истории");
    }


}