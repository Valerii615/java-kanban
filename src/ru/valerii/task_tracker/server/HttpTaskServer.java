package ru.valerii.task_tracker.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.valerii.task_tracker.Exception.ValidationException;
import ru.valerii.task_tracker.model.Epic;
import ru.valerii.task_tracker.model.Subtask;
import ru.valerii.task_tracker.model.Task;
import ru.valerii.task_tracker.service.FileBackedTaskManager;
import ru.valerii.task_tracker.service.Managers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    public static final int PORT = 8080;
    FileBackedTaskManager fileBackedTaskManager;
    HttpServer server;

    public HttpTaskServer(FileBackedTaskManager fileBackedTaskManager) throws IOException {
        this.fileBackedTaskManager = fileBackedTaskManager;
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler(fileBackedTaskManager));
        server.createContext("/subtasks", new TaskHandler(fileBackedTaskManager));
        server.createContext("/epics", new TaskHandler(fileBackedTaskManager));
        server.createContext("/history", new TaskHandler(fileBackedTaskManager));
        server.createContext("/prioritized", new TaskHandler(fileBackedTaskManager));
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        server.stop(0);
    }


    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(new FileBackedTaskManager());

    }
}

class TaskHandler implements HttpHandler {
    FileBackedTaskManager fileBackedTaskManager;
    Gson gson = Managers.getGson();


    public TaskHandler(FileBackedTaskManager fileBackedTaskManager) {
        super();
        this.fileBackedTaskManager = fileBackedTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String endPoint = path.split("/")[1];
        String[] pathSplit = path.split("/");
        String method = exchange.getRequestMethod();

        System.out.println("Началась обработка /" + endPoint + " запроса от клиента.");

        switch (endPoint) {
            case "tasks":
                switch (method) {
                    case "GET":
                        if (pathSplit.length <= 2) {
                            sendText(exchange, gson.toJson(fileBackedTaskManager.getTasks()), 200);
                        } else {
                            Task task = fileBackedTaskManager.getTaskOfId(Integer.parseInt(pathSplit[2]));
                            if (task == null) {
                                sendText(exchange, "Задачи по указанному id не существует", 404);
                            } else {
                                sendText(exchange, gson.toJson(task), 200);
                            }
                        }
                        break;
                    case "POST":
                        if (pathSplit.length <= 2) {
                            String gsonText = readText(exchange);
                            Task taskFromJson = gson.fromJson(gsonText, Task.class);
                            taskFromJson.setId(0);
                            try {
                                fileBackedTaskManager.taskValidator(taskFromJson);
                                fileBackedTaskManager.addTask(taskFromJson);
                                sendText(exchange, "Задача добавлена", 201);
                            } catch (ValidationException e) {
                                sendText(exchange, e.getMessage(), 406);
                            }

                        } else {
                            Task task = fileBackedTaskManager.getTaskOfId(Integer.parseInt(pathSplit[2]));
                            if (task == null) {
                                sendText(exchange, "Задачи по указанному id не существует", 404);
                            } else {
                                String gsonText = readText(exchange);
                                Task taskFromGson = gson.fromJson(gsonText, Task.class);
                                try {
                                    fileBackedTaskManager.taskValidator(taskFromGson);
                                    fileBackedTaskManager.updateTask(Integer.parseInt(pathSplit[2]), taskFromGson);
                                    sendText(exchange, "Задача обнавлена", 201);
                                } catch (ValidationException e) {
                                    sendText(exchange, e.getMessage(), 406);
                                }

                            }

                        }
                        break;
                    case "DELETE":
                        if (pathSplit.length > 2) {
                            Task task = fileBackedTaskManager.getTaskOfId(Integer.parseInt(pathSplit[2]));
                            if (task == null) {
                                sendText(exchange, "Задачи по указанному id не существует", 404);
                            } else {
                                fileBackedTaskManager.removeTaskOfId(Integer.parseInt(pathSplit[2]));
                                sendText(exchange, "Задача удалена", 200);
                            }
                        }
                        break;
                    default:
                        sendText(exchange, "Передан неверный метод", 405);
                }
                break;
            case "subtasks":
                switch (method) {
                    case "GET":
                        if (pathSplit.length <= 2) {
                            sendText(exchange, gson.toJson(fileBackedTaskManager.getSubtasks()), 200);
                        } else {
                            Subtask subtask = fileBackedTaskManager.getSubtaskOfId(Integer.parseInt(pathSplit[2]));
                            if (subtask == null) {
                                sendText(exchange, "Задачи по указанному id не существует", 404);
                            } else {
                                sendText(exchange, gson.toJson(subtask), 200);
                            }
                        }
                        break;
                    case "POST":
                        if (pathSplit.length <= 2) {
                            String gsonText = readText(exchange);
                            Subtask subtaskFromGson = gson.fromJson(gsonText, Subtask.class);
                            subtaskFromGson.setId(0);
                            try {
                                fileBackedTaskManager.taskValidator(subtaskFromGson);
                                fileBackedTaskManager.addSubtask(subtaskFromGson);
                                sendText(exchange, "Задача добавлена", 201);
                            } catch (ValidationException e) {
                                sendText(exchange, e.getMessage(), 406);
                            }

                        } else {
                            Subtask subtask = fileBackedTaskManager.getSubtaskOfId(Integer.parseInt(pathSplit[2]));
                            if (subtask == null) {
                                sendText(exchange, "Задачи по указанному id не существует", 404);
                            } else {
                                String gsonText = readText(exchange);
                                Subtask subtaskFromGson = gson.fromJson(gsonText, Subtask.class);
                                try {
                                    fileBackedTaskManager.taskValidator(subtaskFromGson);
                                    fileBackedTaskManager.updateSubtask(Integer.parseInt(pathSplit[2]), subtaskFromGson);
                                    sendText(exchange, "Задача обнавлена", 201);
                                } catch (ValidationException e) {
                                    sendText(exchange, e.getMessage(), 406);
                                }
                            }
                        }
                        break;
                    case "DELETE":
                        if (pathSplit.length > 2) {
                            Subtask subtask = fileBackedTaskManager.getSubtaskOfId(Integer.parseInt(pathSplit[2]));
                            if (subtask == null) {
                                sendText(exchange, "Задачи по указанному id не существует", 404);
                            } else {
                                fileBackedTaskManager.removeSubtaskOfId(Integer.parseInt(pathSplit[2]));
                                sendText(exchange, "Задача удалена", 200);
                            }
                        }
                        break;
                    default:
                        sendText(exchange, "Передан неверный метод", 405);
                }
                break;
            case "epics":
                switch (method) {
                    case "GET":
                        if (pathSplit.length <= 2) {
                            sendText(exchange, gson.toJson(fileBackedTaskManager.getEpics()), 200);
                        } else if (pathSplit.length == 3) {
                            Epic epic = fileBackedTaskManager.getEpicOfId(Integer.parseInt(pathSplit[2]));
                            if (epic == null) {
                                sendText(exchange, "Задачи по указанному id не существует", 404);
                            } else {
                                sendText(exchange, gson.toJson(epic), 200);
                            }
                        } else {
                            Epic epic = fileBackedTaskManager.getEpicOfId(Integer.parseInt(pathSplit[2]));
                            if (epic == null) {
                                sendText(exchange, "Задачи по указанному id не существует", 404);
                            } else {
                                sendText(exchange, gson.toJson(fileBackedTaskManager.getAllSubtaskOfEpic(Integer.parseInt(pathSplit[2]))), 200);
                            }
                        }
                        break;
                    case "POST":
                        String gsonText = readText(exchange);
                        Epic epicFromGson = gson.fromJson(gsonText, Epic.class);
                        fileBackedTaskManager.addEpic(epicFromGson);
                        sendText(exchange, "Задача добавлена", 201);
                        break;
                    case "DELETE":
                        if (pathSplit.length > 2) {
                            Epic epic1 = fileBackedTaskManager.getEpicOfId(Integer.parseInt(pathSplit[2]));
                            if (epic1 == null) {
                                sendText(exchange, "Задачи по указанному id не существует", 404);
                            } else {
                                fileBackedTaskManager.removeEpicOfId(Integer.parseInt(pathSplit[2]));
                                sendText(exchange, "Задача удалена", 200);
                            }
                        }
                        break;
                    default:
                        sendText(exchange, "Передан неверный метод", 405);
                }
                break;
            case "history":
                switch (method) {
                    case "GET":
                        sendText(exchange, gson.toJson(fileBackedTaskManager.getHistory()), 200);
                        break;
                    default:
                        sendText(exchange, "Передан неверный метод", 405);
                }
                break;
            case "prioritized":
                switch (method) {
                    case "GET":
                        sendText(exchange, gson.toJson(fileBackedTaskManager.getPrioritizedTasksList()), 200);
                        break;
                    default:
                        sendText(exchange, "Передан неверный метод", 405);
                }
                break;
            default:
                sendText(exchange, "Неверный путь", 404);

        }
    }

    protected void sendText(HttpExchange exchange, String text, int rCode) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(rCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(text.getBytes(StandardCharsets.UTF_8));
        }
    }

    protected String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }
}