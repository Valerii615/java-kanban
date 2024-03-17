import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask);
    }

    @Override
    public String toString() {
        return "эпик задача {" +
                "№" + getIdTask() +
                ", Название='" + getNameTask() + '\'' +
                ", Описание='" + getDescriptionTask() + '\'' +
                ", Статус='" + getStatusTask() + '\'' +
                "}" +
                "\n";
    }
}