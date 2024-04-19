package ru.valerii.task_tracker;

import ru.valerii.task_tracker.model.Epic;
import ru.valerii.task_tracker.model.Subtask;
import ru.valerii.task_tracker.service.Managers;
import ru.valerii.task_tracker.service.Status;
import ru.valerii.task_tracker.service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager;
        taskManager = Managers.getDefault();
        taskManager.addEpic(new Epic("epic1", "oppp"));
        taskManager.addEpic(new Epic("epic2", "oppp"));
        taskManager.addSubtask(new Subtask("sub1", "oppp", Status.NEW, 2));
        taskManager.addSubtask(new Subtask("sub2", "oppp", Status.NEW, 2));
        taskManager.addSubtask(new Subtask("sub3", "oppp", Status.NEW, 2));
        System.out.println(taskManager.getEpicOfId(1));
        System.out.println(taskManager.getEpicOfId(2));
        System.out.println(taskManager.getEpicOfId(1));
        System.out.println(taskManager.getSubtaskOfId(3));
        System.out.println(taskManager.getSubtaskOfId(4));
        System.out.println(taskManager.getSubtaskOfId(5));
        System.out.println(taskManager.getSubtaskOfId(3));
        System.out.println(taskManager.getHistory());

        System.out.println("del sub");
        taskManager.deleteSubtaskOfId(4);
        System.out.println(taskManager.getHistory());

        System.out.println("del epi");


        taskManager.deleteEpicOfId(2);
        System.out.println(taskManager.getHistory());

    }
}