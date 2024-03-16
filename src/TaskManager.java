import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> normalTask = new HashMap<>();
    HashMap<Integer, Epic> epicTask = new HashMap<>();
    HashMap<Integer, Subtask> subTask = new HashMap<>();

    public void addNewTask (Task task) {
        task.idTask = task.hashCode();
        normalTask.put(task.idTask, task);
    }

    public void addNewEpic (Epic epic) {
        epic.idTask = epic.hashCode();
        epic.statusTask = Status.NEW;
        epicTask.put(epic.idTask, epic);
    }

    public void addNewSub(Subtask subtask) {
        subtask.idTask = subtask.hashCode();
        subTask.put(subtask.idTask, subtask);
        Epic epic = epicTask.get(subtask.hashEpic);
        epic.listHashSubtask.add(subtask.idTask);
        int counterNEW = 0;
        int counterIN_PROGRESS = 0;
        int counterDONE = 0;
        for (Integer idSub: epic.listHashSubtask) {
            Subtask subtask1 = subTask.get(idSub);
            if (subtask1.statusTask == Status.NEW) counterNEW++;
            if (subtask1.statusTask == Status.IN_PROGRESS) counterIN_PROGRESS++;
            if (subtask1.statusTask == Status.DONE) counterDONE++;
        }
        if (counterNEW >= 0 && counterIN_PROGRESS == 0 && counterDONE == 0) {
            epic.statusTask = Status.NEW;
        } else if (counterDONE > 0 && counterIN_PROGRESS == 0 && counterNEW ==0) {
            epic.statusTask = Status.DONE;
        } else {
            epic.statusTask = Status.IN_PROGRESS;
        }
    }

    public String getNormalTask () {
        ArrayList<String> normalTaskList = new ArrayList<>();
        for (Task task: normalTask.values()) {
            normalTaskList.add(task.toString());
        }
        return normalTaskList.toString();
    }

    public String getEpicTask () {
        ArrayList<String> epicTaskList = new ArrayList<>();
        for (Epic epic: epicTask.values()) {
            epicTaskList.add(epic.toString());
        }
        return epicTaskList.toString();
    }

    public String getSubTask () {
        ArrayList<String> subTaskList = new ArrayList<>();
        for (Subtask subtask: subTask.values()) {
            subTaskList.add(subtask.toString());
        }
        return subTaskList.toString();
    }

    public String getNormalTaskOfId(int id) {
        Task task = normalTask.get(id);
        return task.toString();
    }
    public String getEpicTaskOfId(int id) {
        Task task = epicTask.get(id);
        return task.toString();
    }

    public String getSubTaskOfId(int id) {
        Task task = subTask.get(id);
        return task.toString();
    }

    public void updateNormalTask(int id, Task task) {
        task.idTask = id;
        normalTask.put(id,task);
    }

    public void updateEpicTask (int id, Epic epic) {
        epicTask.put(id, epic);
    }

    public void  updateSubtask (int id, Subtask subtask) {
        subtask.idTask = id;
        subTask.put(id, subtask);
        Epic epic = epicTask.get(subtask.hashEpic);
        int counterNEW = 0;
        int counterIN_PROGRESS = 0;
        int counterDONE = 0;
        for (Integer idSub: epic.listHashSubtask) {
            Subtask subtask1 = subTask.get(idSub);
            if (subtask1.statusTask == Status.NEW) counterNEW++;
            if (subtask1.statusTask == Status.IN_PROGRESS) counterIN_PROGRESS++;
            if (subtask1.statusTask == Status.DONE) counterDONE++;
        }
        if (counterNEW >= 0 && counterIN_PROGRESS == 0 && counterDONE == 0) {
            epic.statusTask = Status.NEW;
        } else if (counterDONE > 0 && counterIN_PROGRESS == 0 && counterNEW ==0) {
            epic.statusTask = Status.DONE;
        } else {
            epic.statusTask = Status.IN_PROGRESS;
        }
    }

    public String getAllSubOfEpic (int idEpic) {
        Epic epic = epicTask.get(idEpic);
        ArrayList<String> subTaskList = new ArrayList<>();
        for (Integer idSub : epic.listHashSubtask) {
            subTaskList.add(subTask.get(idSub).toString());
        }
        return subTaskList.toString();
    }

    public  void deleteAllTask() {
        normalTask.clear();
        epicTask.clear();
        subTask.clear();
    }

    public void deleteNormalTaskOfId (int id) {
        normalTask.remove(id);
    }

    public void deleteEpicTaskOfId (int id) {
        Epic epic = epicTask.get(id);
        for (Integer isSub: epic.listHashSubtask){
            subTask.remove(isSub);
        }
        epicTask.remove(id);
    }

    public void deleteSubTaskOfId (int id) {
        Subtask subtask = subTask.get(id);
        int hashEpic = subtask.hashEpic;
        subTask.remove(id);
        Epic epic = epicTask.get(hashEpic);
        for (int i = 0; i < epic.listHashSubtask.size(); i++) {
            if (epic.listHashSubtask.get(i) == id) {
                epic.listHashSubtask.remove(i);
            }
        }
        int counterNEW = 0;
        int counterIN_PROGRESS = 0;
        int counterDONE = 0;
        for (Integer idSub: epic.listHashSubtask) {
            Subtask subtask1 = subTask.get(idSub);
            if (subtask1.statusTask == Status.NEW) counterNEW++;
            if (subtask1.statusTask == Status.IN_PROGRESS) counterIN_PROGRESS++;
            if (subtask1.statusTask == Status.DONE) counterDONE++;
        }
        if (counterNEW >= 0 && counterIN_PROGRESS == 0 && counterDONE == 0) {
            epic.statusTask = Status.NEW;
        } else if (counterDONE > 0 && counterIN_PROGRESS == 0 && counterNEW ==0) {
            epic.statusTask = Status.DONE;
        } else {
            epic.statusTask = Status.IN_PROGRESS;
        }
    }

}