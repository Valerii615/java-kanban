package ru.valerii.task_tracker.service;

import ru.valerii.task_tracker.model.Epic;
import ru.valerii.task_tracker.model.Subtask;
import ru.valerii.task_tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public void addNewTask(Task task) {
        task.setIdTask(task.hashCode());
        this.tasks.put(task.getIdTask(), task);
    }

    public void addNewEpic(Epic epic) {
        epic.setIdTask(epic.hashCode());
        epic.setStatusTask(Status.NEW);
        epics.put(epic.getIdTask(), epic);
    }

    public void addNewSub(Subtask subtask) {
        subtask.setIdTask(subtask.hashCode());
        subtasks.put(subtask.getIdTask(), subtask);

        Epic epic = epics.get(subtask.getIdEpic());
        epic.getSubtaskIds().add(subtask.getIdTask());

        chekStatus(epic);
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public Task getNormalTaskOfId(int id) {
        return this.tasks.get(id);
    }

    public Epic getEpicTaskOfId(int id) {
        return epics.get(id);
    }

    public Subtask getSubTaskOfId(int id) {
        return subtasks.get(id);
    }

    public void updateNormalTask(int id, Task task) {
        task.setIdTask(id);
        this.tasks.put(id, task);
    }

    public void updateEpicTask(int id, Epic epic) {
        epics.put(id, epic);
    }

    public void updateSubtask(int id, Subtask subtask) {

        subtask.setIdTask(id);
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getIdEpic());

        chekStatus(epic);
    }

    public ArrayList<Subtask> getAllSubOfEpic(int idEpic) {
        Epic epic = epics.get(idEpic);
        ArrayList<Subtask> subTaskList = new ArrayList<>();
        for (Integer idSub : epic.getSubtaskIds()) {
            subTaskList.add(subtasks.get(idSub));
        }
        return subTaskList;
    }

    public void deleteAllTask() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public void deleteNormalTaskOfId(int id) {
        tasks.remove(id);
    }

    public void deleteEpicTaskOfId(int id) {
        Epic epic = epics.get(id);
        for (Integer isSub : epic.getSubtaskIds()) {
            subtasks.remove(isSub);
        }
        epics.remove(id);
    }

    public void deleteSubTaskOfId(int id) {
        Subtask subtask = subtasks.get(id);
        int hashEpic = subtask.getIdEpic();

        subtasks.remove(id);
        Epic epic = epics.get(hashEpic);
        for (int i = 0; i < epic.getSubtaskIds().size(); i++) {
            if (epic.getSubtaskIds().get(i) == id) {
                epic.getSubtaskIds().remove(i);
            }
        }
        chekStatus(epic);
    }

    public void chekStatus(Epic epic) {
        int counterNEW = 0;
        int counterIN_PROGRESS = 0;
        int counterDONE = 0;

        for (Integer idSub : epic.getSubtaskIds()) {
            Subtask subtask1 = subtasks.get(idSub);
            if (subtask1.getStatusTask() == Status.NEW) counterNEW++;
            if (subtask1.getStatusTask() == Status.IN_PROGRESS) counterIN_PROGRESS++;
            if (subtask1.getStatusTask() == Status.DONE) counterDONE++;
        }
        if (counterNEW >= 0 && counterIN_PROGRESS == 0 && counterDONE == 0) {
            epic.setStatusTask(Status.NEW);
        } else if (counterDONE > 0 && counterIN_PROGRESS == 0 && counterNEW == 0) {
            epic.setStatusTask(Status.DONE);
        } else {
            epic.setStatusTask(Status.IN_PROGRESS);
        }
    }
}