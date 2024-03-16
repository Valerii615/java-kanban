public class Subtask extends Task {
    int hashEpic;
    public Subtask(String nameTask, String descriptionTask, Status statusTask, int hashEpic) {
        super(nameTask, descriptionTask, statusTask);
        this.hashEpic = hashEpic;
    }

    @Override
    public String toString () {
        return "подзадача {" +
                "№" + idTask +
                ", Название='" + nameTask + '\'' +
                ", Описание='" + descriptionTask + '\'' +
                ", Статус='" + statusTask + '\'' +
                "}" +
                "\n";
    }
}
