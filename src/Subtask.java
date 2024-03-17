public class Subtask extends Task {
    private int idEpic;

    public Subtask(String nameTask, String descriptionTask, Status statusTask, int hashEpic) {
        super(nameTask, descriptionTask, statusTask);
        this.idEpic = hashEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        return "подзадача {" +
                "№" + getIdTask() +
                ", Название='" + getNameTask() + '\'' +
                ", Описание='" + getDescriptionTask() + '\'' +
                ", Статус='" + getStatusTask() + '\'' +
                "}" +
                "\n";
    }
}