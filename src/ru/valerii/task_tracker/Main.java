package ru.valerii.task_tracker;

import ru.valerii.task_tracker.model.Epic;
import ru.valerii.task_tracker.model.Subtask;
import ru.valerii.task_tracker.model.Task;
import ru.valerii.task_tracker.service.FileBackedTaskManager;
import ru.valerii.task_tracker.service.Managers;
import ru.valerii.task_tracker.service.Status;
import ru.valerii.task_tracker.service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager;
        taskManager = Managers.getDefault();
//        taskManager.addTask(new Task("task1", "oppp", Status.NEW));
//        taskManager.addEpic(new Epic("epic1", "oppp"));
//        taskManager.addEpic(new Epic("epic2", "oppp"));
//        taskManager.addSubtask(new Subtask("sub1", "oppp", Status.NEW, 2));
//        taskManager.addSubtask(new Subtask("sub2", "oppp", Status.NEW, 2));
//        taskManager.addSubtask(new Subtask("sub3", "oppp", Status.NEW, 2));
//        System.out.println(taskManager.getEpicOfId(1));
//        System.out.println(taskManager.getEpicOfId(2));
//        System.out.println(taskManager.getEpicOfId(1));
//        System.out.println(taskManager.getSubtaskOfId(3));
//        System.out.println(taskManager.getSubtaskOfId(4));
//        System.out.println(taskManager.getSubtaskOfId(5));
//        System.out.println(taskManager.getSubtaskOfId(3));
//        System.out.println(taskManager.getHistory());
//
//        System.out.println("del sub");
//        taskManager.removeSubtaskOfId(4);
//        System.out.println(taskManager.getHistory());
//
//        System.out.println("del epi");
//
//        taskManager.removeEpicOfId(2);
//        System.out.println(taskManager.getHistory());

        FileBackedTaskManager tm = new FileBackedTaskManager();
        tm.addTask(new Task("task1", "oppp1", Status.NEW));
        tm.addEpic(new Epic("task2", "oppp2"));
        tm.addSubtask(new Subtask("task3", "oppp3", Status.IN_PROGRESS, 2));
        tm.loadFromFile("src/ru/valerii/task_tracker/task_storage.csv");

        System.out.println(tm.getTasks());
        System.out.println(tm.getEpics());
        System.out.println(tm.getSubtasks());

        System.out.println(tm.getTaskOfId(1));
        System.out.println(tm.getTaskOfId(1));
        System.out.println(tm.getEpicOfId(2));
        System.out.println(tm.getSubtaskOfId(3));
        System.out.println(tm.getTaskOfId(1));

        System.out.println("история");

        System.out.println(tm.getHistory());

        tm.addTask(new Task("task111", "oop", Status.NEW));

        System.out.println(tm.getTasks());
    }
}