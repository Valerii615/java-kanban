import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.valerii.task_tracker.model.Epic;
import ru.valerii.task_tracker.model.Subtask;
import ru.valerii.task_tracker.model.Task;
import ru.valerii.task_tracker.server.HttpTaskServer;
import ru.valerii.task_tracker.service.FileBackedTaskManager;
import ru.valerii.task_tracker.service.Status;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    static HttpTaskServer httpTaskServer;
    static HttpClient httpClient;

    /**
     * создание, старт сервера, добавление задач для тестирования
     */
    @BeforeAll
    static void startServerAndClient() throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        httpTaskServer = new HttpTaskServer(fileBackedTaskManager);
        httpClient = HttpClient.newHttpClient();

        fileBackedTaskManager.addTask(new Task("Обычная задача №1", "добавлена принудительно", Status.NEW, LocalDateTime.of(2024, 5, 17, 12, 0), Duration.ofMinutes(30)));
        fileBackedTaskManager.addTask(new Task("Обычная задача №2", "добавлена принудительно", Status.NEW, LocalDateTime.of(2024, 5, 17, 13, 0), Duration.ofMinutes(30)));

        fileBackedTaskManager.addEpic(new Epic("Эпик задача №1 с 2-мя подзадачами", "добавлена принудительно"));
        fileBackedTaskManager.addEpic(new Epic("Эпик задача №2 с одной подзадачей", "добавлена принудительно"));

        fileBackedTaskManager.addSubtask(new Subtask("Подзадача №1.Эпик1", "добавлена принудительно", Status.NEW, 3, LocalDateTime.of(2024, 5, 17, 14, 0), Duration.ofMinutes(30)));
        fileBackedTaskManager.addSubtask(new Subtask("Подзадача №2.Эпик1", "добавлена принудительно", Status.NEW, 3, LocalDateTime.of(2024, 5, 17, 15, 0), Duration.ofMinutes(30)));
        fileBackedTaskManager.addSubtask(new Subtask("Подзадача №1.Эпик2", "добавлена принудительно", Status.NEW, 4, LocalDateTime.of(2024, 5, 17, 16, 0), Duration.ofMinutes(30)));
    }

    @AfterAll
    static void stopServer() {
        httpTaskServer.stop();
        System.out.println("Сервер остановлен");
    }

    /**
     * получение всех задач, эпиков и подзадач
     */
    @Test
    void gettingAllTheTasksEpicsAndSubtasks() throws IOException, InterruptedException {
        HttpRequest requestTasks = getRequest("http://localhost:8080/tasks");
        HttpResponse<String> responseTasks = httpClient.send(requestTasks, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseTasks.statusCode(), "Неверный статус код при получении задач");

        HttpRequest requestSubtasks = getRequest("http://localhost:8080/subtasks");
        HttpResponse<String> responseSubtasks = httpClient.send(requestSubtasks, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseSubtasks.statusCode(), "Неверный статус код при получении подзадач");

        HttpRequest requestEpics = getRequest("http://localhost:8080/epics");
        HttpResponse<String> responseEpics = httpClient.send(requestEpics, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseEpics.statusCode(), "Неверный статус код при получении эпиков");
    }

    /**
     * получение задач, эпиков и подзадач по id
     * удачные сценарии
     */
    @Test
    void gettingTasksEpicsAndSubtasksByIdSuccessfulScenarios() throws IOException, InterruptedException {
        HttpRequest requestTaskId1 = getRequest("http://localhost:8080/tasks/1");
        HttpResponse<String> responseTaskId1 = httpClient.send(requestTaskId1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseTaskId1.statusCode(), "Неверный статус код при получении задачи TaskId1");
        HttpRequest requestTaskId2 = getRequest("http://localhost:8080/tasks/2");
        HttpResponse<String> responseTaskId2 = httpClient.send(requestTaskId2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseTaskId2.statusCode(), "Неверный статус код при получении задачи TaskId2");

        HttpRequest requestEpicId3 = getRequest("http://localhost:8080/epics/3");
        HttpResponse<String> responseEpicId3 = httpClient.send(requestEpicId3, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseEpicId3.statusCode(), "Неверный статус код при получении эпика EpicId3");
        HttpRequest requestEpicId4 = getRequest("http://localhost:8080/epics/4");
        HttpResponse<String> responseEpicId4 = httpClient.send(requestEpicId4, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseEpicId4.statusCode(), "Неверный статус код при получении эпика EpicId4");

        HttpRequest requestSubaskId5 = getRequest("http://localhost:8080/subtasks/5");
        HttpResponse<String> responseSubaskId5 = httpClient.send(requestSubaskId5, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseSubaskId5.statusCode(), "Неверный статус код при получении подзадачи SubaskId5");
        HttpRequest requestSubaskId6 = getRequest("http://localhost:8080/subtasks/6");
        HttpResponse<String> responseSubaskId6 = httpClient.send(requestSubaskId6, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseSubaskId6.statusCode(), "Неверный статус код при получении подзадачи SubaskId6");
        HttpRequest requestSubaskId7 = getRequest("http://localhost:8080/subtasks/7");
        HttpResponse<String> responseSubaskId7 = httpClient.send(requestSubaskId7, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseSubaskId7.statusCode(), "Неверный статус код при получении подзадачи SubaskId7");
    }

    /**
     * получение задач, эпиков и подзадач по id
     * неудачные сценарии
     */
    @Test
    void gettingTasksEpicsAndSubtasksByIdUnsuccessfulScenarios() throws IOException, InterruptedException {
        HttpRequest requestTaskId999 = getRequest("http://localhost:8080/tasks/999");
        HttpResponse<String> responseTaskId999 = httpClient.send(requestTaskId999, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, responseTaskId999.statusCode(), "Неверный статус код при получении задачи TaskId999");
        HttpRequest requestEpicId999 = getRequest("http://localhost:8080/epics/999");
        HttpResponse<String> responseEpicId999 = httpClient.send(requestEpicId999, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, responseEpicId999.statusCode(), "Неверный статус код при получении эпика EpicId999");
        HttpRequest requestSubaskId999 = getRequest("http://localhost:8080/subtasks/999");
        HttpResponse<String> responseSubaskId999 = httpClient.send(requestSubaskId999, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, responseSubaskId999.statusCode(), "Неверный статус код при получении подзадачи SubaskId999");
    }

    /**
     * добавление задач, эпиков и подзадач
     */
    @Test
    void addingTasksEpicsAndSubtasks() throws IOException, InterruptedException {
        HttpRequest requestAddTask = getRequestPost("http://localhost:8080/tasks", "{\"name\":\"Обычная задача №1\",\"description\":\"добавлена клиентом\",\"status\":\"NEW\",\"startTime\":\"2024-05-18|12:00\",\"duration\":30}");
        HttpResponse<String> responseAddTask = httpClient.send(requestAddTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseAddTask.statusCode(), "Неверный статус код при добавлении задачи");

        HttpRequest requestAddTask406 = getRequestPost("http://localhost:8080/tasks", "{\"name\":\"Обычная задача №1\",\"description\":\"добавлена клиентом\",\"status\":\"NEW\",\"startTime\":\"2024-05-18|12:00\",\"duration\":30}");
        HttpResponse<String> responseAddTask406 = httpClient.send(requestAddTask406, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, responseAddTask406.statusCode(), "Неверный статус код при добавлении задачи");

        HttpRequest requestAddEpic = getRequestPost("http://localhost:8080/epics", "{\"subtaskId\": [],\"name\":\"Эпик задача\",\"description\":\"добавлена клиентом\"}");
        HttpResponse<String> responseAddEpic = httpClient.send(requestAddEpic, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseAddEpic.statusCode(), "Неверный статус код при добавлении задачи");

        HttpRequest requestAddSubtask = getRequestPost("http://localhost:8080/subtasks", "{\"idEpic\":9,\"name\":\"Подзадача №1.Эпик1\",\"description\":\"добавлена принудительно\",\"status\":\"NEW\",\"startTime\":\"2024-06-17|14:00\",\"duration\":30}");
        HttpResponse<String> responseAddSubtask = httpClient.send(requestAddSubtask, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseAddSubtask.statusCode(), "Неверный статус код при добавлении задачи");

        HttpRequest requestAddSubtask406 = getRequestPost("http://localhost:8080/subtasks", "{\"idEpic\":9,\"name\":\"Подзадача №1.Эпик1\",\"description\":\"добавлена принудительно\",\"status\":\"NEW\",\"startTime\":\"2024-06-17|14:00\",\"duration\":30}");
        HttpResponse<String> responseAddSubtask406 = httpClient.send(requestAddSubtask406, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, responseAddSubtask406.statusCode(), "Неверный статус код при добавлении задачи");
    }

    /**
     * обновление задач и подзадач
     */
    @Test
    void updatingTasksAndSubtasks() throws IOException, InterruptedException {
        HttpRequest requestUpdateTask = getRequestPost("http://localhost:8080/tasks/1", "{\"name\":\"Обнавленная задача \",\"description\":\"добавлена клиентом\",\"status\":\"NEW\",\"startTime\":\"2024-05-19|12:00\",\"duration\":30}");
        HttpResponse<String> responseUpdateTask = httpClient.send(requestUpdateTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseUpdateTask.statusCode(), "Неверный статус код при добавлении задачи");

        HttpRequest requestUpdateSubtask = getRequestPost("http://localhost:8080/subtasks/5", "{\"idEpic\":3,\"name\":\"Обнавленная Подзадача №1.Эпик1\",\"description\":\"добавлена принудительно\",\"status\":\"NEW\",\"startTime\":\"2024-06-17|15:00\",\"duration\":30}");
        HttpResponse<String> responseUpdateSubtask = httpClient.send(requestUpdateSubtask, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseUpdateSubtask.statusCode(), "Неверный статус код при добавлении задачи");
    }

    /**
     * удаление задачи эпика и подзадачи
     */
    @Test
    void deletingAnEpicTaskAndSubtask() throws IOException, InterruptedException {
        HttpRequest requestDeleteTask = getRequestDelete("http://localhost:8080/tasks/2");
        HttpResponse<String> responseDeleteTask = httpClient.send(requestDeleteTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDeleteTask.statusCode(), "Неверный статус код при удавении задачи");

        HttpRequest requestDeleteSubtask = getRequestDelete("http://localhost:8080/subtasks/5");
        HttpResponse<String> responseDeleteSubtask = httpClient.send(requestDeleteSubtask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDeleteSubtask.statusCode(), "Неверный статус код при удавении подзадачи");

        HttpRequest requestDeleteEpic = getRequestDelete("http://localhost:8080/epics/4");
        HttpResponse<String> responseDeleteEpic = httpClient.send(requestDeleteEpic, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseDeleteEpic.statusCode(), "Неверный статус код при удавении эпика");
    }

    /**
     * получение истории задач
     */
    @Test
    void gettingTheTaskHistory() throws IOException, InterruptedException {
        HttpRequest requestHistory = getRequest("http://localhost:8080/history");
        HttpResponse<String> responseHistory = httpClient.send(requestHistory, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseHistory.statusCode(), "Неверный статус код при получении итории");
    }

    /**
     * получение списка приоритетов
     */
    @Test
    void gettingListOfPriorities() throws IOException, InterruptedException {
        HttpRequest requestPriorities = getRequest("http://localhost:8080/prioritized");
        HttpResponse<String> responsePriorities = httpClient.send(requestPriorities, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responsePriorities.statusCode(), "Неверный статус код при получении списка приоритетов");
    }

    protected HttpRequest getRequest(String url) {
        URI uriSubtasksEpics = URI.create(url);
        return HttpRequest.newBuilder()
                .uri(uriSubtasksEpics)
                .header("Accept", "application/json")
                .GET()
                .build();
    }

    protected HttpRequest getRequestPost(String url, String gsonText) {
        URI uriSubtasksEpics = URI.create(url);
        return HttpRequest.newBuilder()
                .uri(uriSubtasksEpics)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gsonText))
                .build();
    }

    protected HttpRequest getRequestDelete(String url) {
        URI uriSubtasksEpics = URI.create(url);
        return HttpRequest.newBuilder()
                .uri(uriSubtasksEpics)
                .header("Accept", "application/json")
                .DELETE()
                .build();
    }
}