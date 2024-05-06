package ru.valerii.task_tracker.service;

import ru.valerii.task_tracker.Exception.ManagerSaveException;
import ru.valerii.task_tracker.Exception.ManagerSaveStatusException;
import ru.valerii.task_tracker.model.Epic;
import ru.valerii.task_tracker.model.Subtask;
import ru.valerii.task_tracker.model.Task;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public Task getTaskOfId(int id) {
        Task task = super.getTaskOfId(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicOfId(int id) {
        try {
            Epic epic = super.getEpicOfId(id);
            save();
            return epic;
        } catch (RuntimeException e) {
            throw new ManagerSaveException("Epic с заданным id не существует");
        }

    }

    @Override
    public Subtask getSubtaskOfId(int id) {
        Subtask subtask = super.getSubtaskOfId(id);
        save();
        return subtask;
    }

    @Override
    public void updateTask(int id, Task task) {
        super.updateTask(id, task);
        save();
    }

    public void updateEpic(int id, Epic epic) {
        super.updateEpic(id, epic);
        save();
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        super.updateSubtask(id, subtask);
        save();
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    public void removeAllTaskNotSave() {
        super.removeAllTask();
    }

    @Override
    public void removeTaskOfId(int id) {
        super.removeTaskOfId(id);
        save();
    }

    @Override
    public void removeEpicOfId(int id) {
        super.removeEpicOfId(id);
        save();
    }

    @Override
    public void removeSubtaskOfId(int id) {
        super.removeSubtaskOfId(id);
        save();
    }


    public void save() {
        List<Task> tasksList = getTasks();
        List<Epic> epicsList = getEpics();
        List<Subtask> subtasksList = getSubtasks();

        try (Writer fileWriter = new FileWriter("src/ru/valerii/task_tracker/task_storage.csv")) {
            for (Task task : tasksList) {
                fileWriter.write(toStringTask(task) + "\n");
            }
            for (Epic epic : epicsList) {
                fileWriter.write(toStringEpic(epic) + "\n");
            }
            for (Subtask subtask : subtasksList) {
                fileWriter.write(toStringSubtask(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Данные не сохранены");
        }
    }

    public String toStringTask(Task task) {
        return String.format("%d,TASK,%s,%s,%s", task.getId(), task.getName(), task.getStatus(), task.getDescription());
    }

    public String toStringEpic(Epic epic) {
        return String.format("%d,EPIC,%s,%s,%s", epic.getId(), epic.getName(), epic.getStatus(), epic.getDescription());
    }

    public String toStringSubtask(Subtask subtask) {
        return String.format("%d,SUBTASK,%s,%s,%s,%d",
                subtask.getId(), subtask.getName(), subtask.getStatus(), subtask.getDescription(), subtask.getIdEpic());
    }

    public FileBackedTaskManager loadFromFile(String fileName) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        List<String> tasksListBacked = new ArrayList<>();
        try (FileReader reader = new FileReader(fileName); BufferedReader bufferedReader = new BufferedReader(reader)) {
            while (bufferedReader.ready()) {
                tasksListBacked.add(bufferedReader.readLine());
            }
            if (!tasksListBacked.isEmpty()) {
                for (String taskLine : tasksListBacked) {
                    String[] taskContent = fromeString(taskLine);
                    switch (taskContent[1]) {
                        case "TASK":
                            Task task = new Task(taskContent[2], taskContent[4], checkingStatusFromString(taskContent[3]));
                            task.setId(Integer.parseInt(taskContent[0]));
                            tasks.put(task.getId(), task);
                            fileBackedTaskManager.setIdCount(checkCounterId(Integer.parseInt(taskContent[0]), fileBackedTaskManager.getIdCount()));
                            break;
                        case "EPIC":
                            Epic epic = new Epic(taskContent[2], taskContent[4]);
                            epic.setStatus(checkingStatusFromString(taskContent[3]));
                            epic.setId(Integer.parseInt(taskContent[0]));
                            epics.put(epic.getId(), epic);
                            fileBackedTaskManager.setIdCount(checkCounterId(Integer.parseInt(taskContent[0]), fileBackedTaskManager.getIdCount()));
                            break;
                        case "SUBTASK":
                            Subtask subtask = new Subtask(taskContent[2], taskContent[4], checkingStatusFromString(taskContent[3]), Integer.parseInt(taskContent[5]));
                            subtask.setId(Integer.parseInt(taskContent[0]));
                            Epic epicFromSubtask = epics.get(Integer.parseInt(taskContent[5]));
                            epicFromSubtask.getSubtaskId().add(subtask.getId());
                            subtasks.put(subtask.getId(), subtask);
                            fileBackedTaskManager.setIdCount(checkCounterId(Integer.parseInt(taskContent[0]), fileBackedTaskManager.getIdCount()));
                            break;
                        default:
                            throw new ManagerSaveException();
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка Чтения файла");
        } catch (NumberFormatException e) {
            throw new ManagerSaveException("Неверный формат id");
        } catch (ManagerSaveException e) {
            throw new ManagerSaveException("Неверный тип задачи");
        } catch (ManagerSaveStatusException e) {
            throw new ManagerSaveStatusException("Неверный формат статуса");
        } catch (RuntimeException e) {
            throw new ManagerSaveException("Epic с заданным id не существует");
        }
        return fileBackedTaskManager;
    }

    public int checkCounterId(int idTask, int idCount) {
        if (idCount <= idCount) {
            idCount = idTask + 1;
            return idCount;
        } else {
            return idCount;
        }
    }

    public String[] fromeString(String value) {
        return value.split(",");
    }

    public Status checkingStatusFromString(String status) {
        switch (status) {
            case "NEW":
                return Status.NEW;
            case "IN_PROGRESS":
                return Status.IN_PROGRESS;
            case "DONE":
                return Status.DONE;
            default:
                throw new ManagerSaveStatusException();
        }
    }
}
