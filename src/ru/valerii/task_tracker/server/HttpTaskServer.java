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