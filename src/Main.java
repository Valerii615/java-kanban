public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        taskManager.addNewTask(new Task("task1", "des", Status.NEW));
        taskManager.addNewTask(new Task("task2", "des", Status.NEW));

        taskManager.addNewEpic(new Epic("epic1(2)", "des"));
        taskManager.addNewEpic(new Epic("epic2(1)", "des"));

        taskManager.addNewSub(new Subtask("sub11", "des", Status.NEW, 793589513));
        taskManager.addNewSub(new Subtask("sub12", "des", Status.NEW, 793589513));
        taskManager.addNewSub(new Subtask("sub21", "des", Status.NEW, 1313922862));

        System.out.println(taskManager.getNormalTask());
        System.out.println(taskManager.getEpicTask());
        System.out.println(taskManager.getSubTask());

        taskManager.updateNormalTask(1915910607, new Task("task1(!)", "des", Status.IN_PROGRESS));
        taskManager.updateNormalTask(284720968, new Task("task2(!)", "des", Status.DONE));

        taskManager.updateSubtask(1922154895, new Subtask("sub11", "des", Status.IN_PROGRESS, 793589513));
        taskManager.updateSubtask(883049899, new Subtask("sub12", "des", Status.DONE, 793589513));
        taskManager.updateSubtask(2093176254, new Subtask("sub21", "des", Status.DONE, 1313922862));

        System.out.println(taskManager.getNormalTask());
        System.out.println(taskManager.getEpicTask());
        System.out.println(taskManager.getSubTask());

        taskManager.deleteNormalTaskOfId(1915910607);
        taskManager.deleteSubTaskOfId(1922154895);
        taskManager.deleteEpicTaskOfId(1313922862);

        System.out.println(taskManager.getNormalTask());
        System.out.println(taskManager.getEpicTask());
        System.out.println(taskManager.getSubTask());
    }
}