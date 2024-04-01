package ru.valerii.task_tracker.service;

import ru.valerii.task_tracker.model.Epic;
import ru.valerii.task_tracker.model.Subtask;
import ru.valerii.task_tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    HistoryManager historyManager = Managers.getDefaultHistory();
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    @Override
    public void addNewTask(Task task) {
        task.setIdTask(task.hashCode());
        this.tasks.put(task.getIdTask(), task);
    }

    @Override
    public void addNewEpic(Epic epic) {
        epic.setIdTask(epic.hashCode());
        epic.setStatusTask(Status.NEW);
        epics.put(epic.getIdTask(), epic);
    }

    @Override
    public void addNewSub(Subtask subtask) {
        subtask.setIdTask(subtask.hashCode());
        subtasks.put(subtask.getIdTask(), subtask);

        Epic epic = epics.get(subtask.getIdEpic());
        epic.getSubtaskIds().add(subtask.getIdTask());

        chekStatus(epic);
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Task> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Task> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Task getNormalTaskOfId(int id) { // измененный метод
        historyManager.addInHistory(this.tasks.get(id));
        return this.tasks.get(id);
    }

    @Override
    public Epic getEpicTaskOfId(int id) { // измененный метод
        historyManager.addInHistory(this.epics.get(id));
        return this.epics.get(id);
    }

    @Override
    public Subtask getSubTaskOfId(int id) { // измененный метод
        historyManager.addInHistory(this.subtasks.get(id));
        return this.subtasks.get(id);
    }
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }



    @Override
    public void updateNormalTask(int id, Task task) {
        task.setIdTask(id);
        this.tasks.put(id, task);
    }

    @Override
    public void updateEpicTask(int id, Epic epic) {
        epics.put(id, epic);
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {

        subtask.setIdTask(id);
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getIdEpic());

        chekStatus(epic);
    }

    @Override
    public ArrayList<Subtask> getAllSubOfEpic(int idEpic) {
        Epic epic = epics.get(idEpic);
        ArrayList<Subtask> subTaskList = new ArrayList<>();
        for (Integer idSub : epic.getSubtaskIds()) {
            subTaskList.add(subtasks.get(idSub));
        }
        return subTaskList;
    }

    @Override
    public void deleteAllTask() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteNormalTaskOfId(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicTaskOfId(int id) {
        Epic epic = epics.get(id);
        for (Integer isSub : epic.getSubtaskIds()) {
            subtasks.remove(isSub);
        }
        epics.remove(id);
    }

    @Override
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

    @Override
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