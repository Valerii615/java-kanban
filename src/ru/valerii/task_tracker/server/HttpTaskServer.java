package ru.valerii.task_tracker.server;

import com.sun.net.httpserver.HttpServer;
import ru.valerii.task_tracker.service.FileBackedTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServer {
    public static final int PORT = 8080;
    FileBackedTaskManager fileBackedTaskManager;
    HttpServer server;
    List<String> paths = new ArrayList<>(List.of("/tasks", "/subtasks", "/epics", "/history", "/prioritized"));

    public HttpTaskServer(FileBackedTaskManager fileBackedTaskManager) throws IOException {
        this.fileBackedTaskManager = fileBackedTaskManager;
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        createContext();
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        server.stop(0);
    }


    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(new FileBackedTaskManager());
    }

    public void createContext() {
        for (String path : paths) {
            server.createContext(path, new TaskHandler(fileBackedTaskManager));
        }
    }
}