public class Task {
    private String nameTask;
    private String descriptionTask;
    private int idTask;
    private Status statusTask;

    public Task(String nameTask, String descriptionTask, Status statusTask) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.statusTask = statusTask;
    }

    public Task (String nameTask, String descriptionTask) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
    }

    @Override
    public String toString () {
        return "задача {" +
                "№" + idTask +
                ", Название='" + nameTask + '\'' +
                ", Описание='" + descriptionTask + '\'' +
                ", Статус='" + statusTask + '\'' +
                "}" +
                "\n";
    }

    public String getNameTask() {
        return nameTask;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public int getIdTask() {
        return idTask;
    }

    public Status getStatusTask() {
        return statusTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public void setStatusTask(Status statusTask) {
        this.statusTask = statusTask;
    }
}