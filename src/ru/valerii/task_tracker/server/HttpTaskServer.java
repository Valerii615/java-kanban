package ru.valerii.task_tracker.server;

import com.sun.net.httpserver.HttpServer;
import ru.valerii.task_tracker.service.FileBackedTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

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