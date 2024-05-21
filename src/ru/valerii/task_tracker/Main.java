package ru.valerii.task_tracker;

import ru.valerii.task_tracker.model.Task;
import ru.valerii.task_tracker.service.FileBackedTaskManager;
import ru.valerii.task_tracker.service.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        fileBackedTaskManager.addTask(new Task("Обычная задача №1", "Описание", Status.NEW, LocalDateTime.of(2024,5,17,12,0), Duration.ofMinutes(30)));
        System.out.println(fileBackedTaskManager.getTasks());
    }
}