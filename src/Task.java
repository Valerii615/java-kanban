public class Task {
    String nameTask;
    String descriptionTask;
    int idTask;
    Status statusTask;

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
}