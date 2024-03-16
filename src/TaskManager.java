import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public void addNewTask (Task task) {
        task.setIdTask(task.hashCode());
        this.tasks.put(task.getIdTask(), task);
    }

    public void addNewEpic (Epic epic) {
        epic.setIdTask(epic.hashCode());
        epic.setStatusTask(Status.NEW);
        epics.put(epic.getIdTask(), epic);
    }

    public void addNewSub(Subtask subtask) {
        int counterNEW = 0;
        int counterIN_PROGRESS = 0;
        int counterDONE = 0;

        subtask.setIdTask(subtask.hashCode());
        subtasks.put(subtask.getIdTask(), subtask);

        Epic epic = epics.get(subtask.hashEpic);
        epic.subtaskIds.add(subtask.getIdTask());
        for (Integer idSub: epic.subtaskIds) {
            Subtask subtask1 = subtasks.get(idSub);
            if (subtask1.getStatusTask() == Status.NEW) counterNEW++;
            if (subtask1.getStatusTask() == Status.IN_PROGRESS) counterIN_PROGRESS++;
            if (subtask1.getStatusTask() == Status.DONE) counterDONE++;
        }
        if (counterNEW >= 0 && counterIN_PROGRESS == 0 && counterDONE == 0) {
            epic.setStatusTask(Status.NEW);
        } else if (counterDONE > 0 && counterIN_PROGRESS == 0 && counterNEW ==0) {
            epic.setStatusTask(Status.DONE);
        } else {
            epic.setStatusTask(Status.IN_PROGRESS);
        }
    }

    public String getTasks() {
        ArrayList<String> normalTaskList = new ArrayList<>();
        for (Task task: tasks.values()) {
            normalTaskList.add(task.toString());
        }
        return normalTaskList.toString();
    }

    public String getEpics() {
        ArrayList<String> epicTaskList = new ArrayList<>();
        for (Epic epic: epics.values()) {
            epicTaskList.add(epic.toString());
        }
        return epicTaskList.toString();
    }

    public String getSubtasks() {
        ArrayList<String> subTaskList = new ArrayList<>();
        for (Subtask subtask: subtasks.values()) {
            subTaskList.add(subtask.toString());
        }
        return subTaskList.toString();
    }

    public String getNormalTaskOfId(int id) {
        Task task = this.tasks.get(id);
        return task.toString();
    }

    public String getEpicTaskOfId(int id) {
        Task task = epics.get(id);
        return task.toString();
    }

    public String getSubTaskOfId(int id) {
        Task task = subtasks.get(id);
        return task.toString();
    }

    public void updateNormalTask(int id, Task task) {
        task.setIdTask(id);
        this.tasks.put(id,task);
    }

    public void updateEpicTask (int id, Epic epic) {
        epics.put(id, epic);
    }

    public void  updateSubtask (int id, Subtask subtask) {
        int counterNEW = 0;
        int counterIN_PROGRESS = 0;
        int counterDONE = 0;

        subtask.setIdTask(id);
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.hashEpic);

        for (Integer idSub: epic.subtaskIds) {
            Subtask subtask1 = subtasks.get(idSub);
            if (subtask1.getStatusTask() == Status.NEW) counterNEW++;
            if (subtask1.getStatusTask() == Status.IN_PROGRESS) counterIN_PROGRESS++;
            if (subtask1.getStatusTask() == Status.DONE) counterDONE++;
        }
        if (counterNEW >= 0 && counterIN_PROGRESS == 0 && counterDONE == 0) {
            epic.setStatusTask(Status.NEW);
        } else if (counterDONE > 0 && counterIN_PROGRESS == 0 && counterNEW ==0) {
            epic.setStatusTask(Status.DONE);
        } else {
            epic.setStatusTask(Status.IN_PROGRESS);
        }
    }

    public String getAllSubOfEpic (int idEpic) {
        Epic epic = epics.get(idEpic);
        ArrayList<String> subTaskList = new ArrayList<>();
        for (Integer idSub : epic.subtaskIds) {
            subTaskList.add(subtasks.get(idSub).toString());
        }
        return subTaskList.toString();
    }

    public  void deleteAllTask() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public void deleteNormalTaskOfId (int id) {
        tasks.remove(id);
    }

    public void deleteEpicTaskOfId (int id) {
        Epic epic = epics.get(id);
        for (Integer isSub: epic.subtaskIds){
            subtasks.remove(isSub);
        }
        epics.remove(id);
    }

    public void deleteSubTaskOfId (int id) {
        Subtask subtask = subtasks.get(id);
        int hashEpic = subtask.hashEpic;
        int counterNEW = 0;
        int counterIN_PROGRESS = 0;
        int counterDONE = 0;

        subtasks.remove(id);
        Epic epic = epics.get(hashEpic);
        for (int i = 0; i < epic.subtaskIds.size(); i++) {
            if (epic.subtaskIds.get(i) == id) {
                epic.subtaskIds.remove(i);
            }
        }

        for (Integer idSub: epic.subtaskIds) {
            Subtask subtask1 = subtasks.get(idSub);
            if (subtask1.getStatusTask() == Status.NEW) counterNEW++;
            if (subtask1.getStatusTask() == Status.IN_PROGRESS) counterIN_PROGRESS++;
            if (subtask1.getStatusTask() == Status.DONE) counterDONE++;
        }
        if (counterNEW >= 0 && counterIN_PROGRESS == 0 && counterDONE == 0) {
            epic.setStatusTask(Status.NEW);
        } else if (counterDONE > 0 && counterIN_PROGRESS == 0 && counterNEW ==0) {
            epic.setStatusTask(Status.DONE);
        } else {
            epic.setStatusTask(Status.IN_PROGRESS);
        }
    }

}