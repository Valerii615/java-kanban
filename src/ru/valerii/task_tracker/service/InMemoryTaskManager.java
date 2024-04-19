package ru.valerii.task_tracker.service;

import ru.valerii.task_tracker.model.Epic;
import ru.valerii.task_tracker.model.Subtask;
import ru.valerii.task_tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    HistoryManager historyManager = Managers.getDefaultHistory();
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    @Override
    public int generateId() {
        id++;
        return id;
    }

    @Override
    public void addTask(Task task) {
        task.setId(generateId());
        this.tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epic.setStatus(Status.NEW);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getIdEpic());
        epic.getSubtaskId().add(subtask.getId());

        checkStatus(epic);
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
    public Task getTaskOfId(int id) {
        if (this.tasks.get(id) != null) {
            historyManager.add(this.tasks.get(id));
        }
        return this.tasks.get(id);
    }

    @Override
    public Epic getEpicOfId(int id) {
        if (this.epics.get(id) != null) {
            historyManager.add(this.epics.get(id));
        }
        return this.epics.get(id);
    }

    @Override
    public Subtask getSubtaskOfId(int id) {
        if (this.subtasks.get(id) != null) {
            historyManager.add(this.subtasks.get(id));
        }
        return this.subtasks.get(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


    @Override
    public void updateTask(int id, Task task) {
        task.setId(id);
        this.tasks.put(id, task);
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        epics.put(id, epic);
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {

        subtask.setId(id);
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getIdEpic());

        checkStatus(epic);
    }

    @Override
    public ArrayList<Subtask> getAllSubtaskOfEpic(int idEpic) {
        Epic epic = epics.get(idEpic);
        ArrayList<Subtask> subTaskList = new ArrayList<>();
        for (Integer idSub : epic.getSubtaskId()) {
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
    public void deleteTaskOfId(int id) {
        historyManager.remove(tasks.get(id).getId());// исправление
        tasks.remove(id);
    }

    @Override
    public void deleteEpicOfId(int id) {
        Epic epic = epics.get(id);
        for (Integer isSub : epic.getSubtaskId()) {
            historyManager.remove(subtasks.get(isSub).getId());// исправление
            subtasks.remove(isSub);
        }
        historyManager.remove(epic.getId());// исправление
        epics.remove(id);
    }

    @Override
    public void deleteSubtaskOfId(int id) {
        Subtask subtask = subtasks.get(id);
        int hashEpic = subtask.getIdEpic();

        historyManager.remove(subtask.getId());// исправление
        subtasks.remove(id);
        Epic epic = epics.get(hashEpic);
        for (int i = 0; i < epic.getSubtaskId().size(); i++) {
            if (epic.getSubtaskId().get(i) == id) {
                epic.getSubtaskId().remove(i);
            }
        }
        checkStatus(epic);
    }

    @Override
    public void checkStatus(Epic epic) {
        int counterNew = 0;
        int counterInProgress = 0;
        int counterDone = 0;

        for (Integer idSub : epic.getSubtaskId()) {
            Subtask subtask1 = subtasks.get(idSub);
            if (subtask1.getStatus() == Status.NEW) counterNew++;
            if (subtask1.getStatus() == Status.IN_PROGRESS) counterInProgress++;
            if (subtask1.getStatus() == Status.DONE) counterDone++;
        }
        if (counterNew >= 0 && counterInProgress == 0 && counterDone == 0) {
            epic.setStatus(Status.NEW);
        } else if (counterDone > 0 && counterInProgress == 0 && counterNew == 0) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}