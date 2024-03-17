import java.util.Objects;

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

    public Task(String nameTask, String descriptionTask) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
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

    @Override
    public String toString() {
        return "задача {" +
                "№" + getIdTask() +
                ", Название='" + getNameTask() + '\'' +
                ", Описание='" + getDescriptionTask() + '\'' +
                ", Статус='" + getStatusTask() + '\'' +
                "}" +
                "\n";
    }

    @Override
    public boolean equals (Object object) {
        if (this == object) return true;
        if (object == null) return false;
        if (this.getClass() != object.getClass()) return false;
        Task otherTask = (Task) object;
        return Objects.equals(nameTask, otherTask.nameTask) &&
                Objects.equals(descriptionTask, otherTask.descriptionTask);
    }

    @Override
    public int hashCode () {
        int hash = 17;
        if (nameTask != null) {
            hash = hash + nameTask.hashCode();
        }
        hash = hash * 31;
        if (descriptionTask != null) {
            hash = hash + descriptionTask.hashCode();
        }
        return hash;
    }
}