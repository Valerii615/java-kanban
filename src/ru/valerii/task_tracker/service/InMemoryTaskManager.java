package ru.valerii.task_tracker.service;

import ru.valerii.task_tracker.Exception.ValidationException;
import ru.valerii.task_tracker.model.Epic;
import ru.valerii.task_tracker.model.Subtask;
import ru.valerii.task_tracker.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    private int idCount;
    HistoryManager historyManager = Managers.getDefaultHistory();
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));

    public int getIdCount() {
        return idCount;
    }

    public void setIdCount(int idCount) {
        this.idCount = idCount;
    }

    @Override
    public int generateId() {
        idCount++;
        return idCount;
    }

    @Override
    public void addTask(Task task) {
        taskValidator(task);
        task.setId(generateId());
        this.tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        taskValidator(epic);
        epic.setId(generateId());
        epic.setStatus(Status.NEW);
        epic.setStartTime(null);
        epic.setDuration(null);
        epic.setEndTime(null);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        taskValidator(subtask);
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getIdEpic());
        epic.getSubtaskId().add(subtask.getId());

        checkStatus(epic);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        epicTimeCalculation(epic);
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Task> getPrioritizedTasksList() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public Task getTaskOfId(int id) {
        if (this.tasks.get(id) != null) {
            historyManager.add(this.tasks.get(id));
        }
        return this.tasks.get(id);
    }

    public Task getTaskOfIdNotHistory(int id) {
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

    public Subtask getSubtaskOfIdNotHistory(int id) {
        return this.subtasks.get(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


    @Override
    public void updateTask(int id, Task task) {
        task.setId(id);
        taskValidator(task);
        prioritizedTasks.remove(getTaskOfId(task.getId()));
        this.tasks.put(id, task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        epic.setId(id);
        taskValidator(epic);
        epics.put(id, epic);
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        subtask.setId(id);
        taskValidator(subtask);
        prioritizedTasks.remove(getSubtaskOfId(subtask.getId()));
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getIdEpic());
        checkStatus(epic);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        epicTimeCalculation(epic);
    }

    @Override
    public List<Subtask> getAllSubtaskOfEpic(int idEpic) {
        return epics.get(idEpic).getSubtaskId().stream()
                .map(this::getSubtaskOfId)
                .collect(Collectors.toList());
    }

    @Override
    public void removeAllTask() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        historyManager.clear();
        prioritizedTasks.clear();
    }

    @Override
    public void removeTaskOfId(int id) {
        historyManager.remove(id);
        prioritizedTasks.remove(getTaskOfIdNotHistory(id));
        tasks.remove(id);
    }

    @Override
    public void removeEpicOfId(int id) {
        Epic epic = epics.get(id);
        for (Integer isSub : epic.getSubtaskId()) {
            historyManager.remove(subtasks.get(isSub).getId());
            prioritizedTasks.remove(subtasks.get(isSub));
            subtasks.remove(isSub);
        }
        historyManager.remove(epic.getId());
        prioritizedTasks.remove(epic);
        epics.remove(id);
    }

    @Override
    public void removeSubtaskOfId(int id) {
        Subtask subtask = subtasks.get(id);
        int hashEpic = subtask.getIdEpic();

        historyManager.remove(subtask.getId());
        prioritizedTasks.remove(subtask);
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

    public void epicTimeCalculation(Epic epic) {
        List<Subtask> subtaskList = new ArrayList<>();
        LocalDateTime startTime;
        Duration duration = Duration.ofMinutes(0);
        LocalDateTime endTime = LocalDateTime.of(1, 1, 1, 0, 0);
        for (Integer subtaskId : epic.getSubtaskId()) {
            subtaskList.add(getSubtaskOfIdNotHistory(subtaskId));
        }
        startTime = subtaskList.getFirst().getStartTime();
        for (Subtask subtask : subtaskList) {
            if (startTime.isAfter(subtask.getStartTime())) {
                startTime = subtask.getStartTime();
            }
            if (endTime.isBefore(subtask.getStartTime().plus(subtask.getDuration()))) {
                endTime = subtask.getStartTime().plus(subtask.getDuration());
            }
            duration = duration.plus(subtask.getDuration());
        }
        epic.setStartTime(startTime);
        epic.setDuration(duration);
        epic.setEndTime(endTime);
    }

    public void taskValidator(Task task) {
        if (prioritizedTasks.isEmpty() || task.getStartTime() == null) {
            return;
        }
        for (Task prioritiTask : prioritizedTasks) {
            if (prioritiTask == null || prioritiTask.getStartTime() == null) {
                continue;
            }
            if (prioritiTask.getId() == task.getId()) {
                continue;
            }
            LocalDateTime prioritiTaskStartTime = prioritiTask.getStartTime();
            LocalDateTime taskStartTime = task.getStartTime();
            LocalDateTime prioritiTaskEndTime = prioritiTaskStartTime.plus(prioritiTask.getDuration());
            LocalDateTime taskEndTime = taskStartTime.plus(task.getDuration());
            if (taskEndTime.isBefore(prioritiTaskStartTime)) {
                continue;
            }
            if (taskStartTime.isAfter(prioritiTaskEndTime)) {
                continue;
            }
            throw new ValidationException(
                    "Задача не может быть добавлена так как пересекается с задачей \'id = " + prioritiTask.getId() +
                            "\'время начала задачи = " + prioritiTaskStartTime +
                            "\'время завершения задачи = " + prioritiTaskEndTime
            );
        }
    }
}