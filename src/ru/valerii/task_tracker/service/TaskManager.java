package ru.valerii.task_tracker.service;

import ru.valerii.task_tracker.model.Epic;
import ru.valerii.task_tracker.model.Subtask;
import ru.valerii.task_tracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int generateId();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    ArrayList<Task> getTasks();

    ArrayList<Task> getEpics();

    ArrayList<Task> getSubtasks();

    Task getTaskOfId(int id);

    Epic getEpicOfId(int id);

    Subtask getSubtaskOfId(int id);

    List<Task> getHistory();

    void updateTask(int id, Task task);

    void updateEpic(int id, Epic epic);

    void updateSubtask(int id, Subtask subtask);

    ArrayList<Subtask> getAllSubtaskOfEpic(int idEpic);

    void removeAllTask();

    void renoveTaskOfId(int id);

    void removeEpicOfId(int id);

    void renoveSubtaskOfId(int id);

    void checkStatus(Epic epic);

}
