package ru.valerii.task_tracker.service;

import ru.valerii.task_tracker.model.Epic;
import ru.valerii.task_tracker.model.Subtask;
import ru.valerii.task_tracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void addNewTask(Task task);

    void addNewEpic(Epic epic);

    void addNewSub(Subtask subtask);

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

    Task getNormalTaskOfId(int id);

    Epic getEpicTaskOfId(int id);

    Subtask getSubTaskOfId(int id);

    List<Task> getHistory();

    void updateNormalTask(int id, Task task);

    void updateEpicTask(int id, Epic epic);

    void updateSubtask(int id, Subtask subtask);

    ArrayList<Subtask> getAllSubOfEpic(int idEpic);

    void deleteAllTask();

    void deleteNormalTaskOfId(int id);

    void deleteEpicTaskOfId(int id);

    void deleteSubTaskOfId(int id);

    void chekStatus(Epic epic);

}
