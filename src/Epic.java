import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subtaskIds = new ArrayList<>(); //имя переменной поменяно, необходимо поставить модификатор доступа.
    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask);
    }

    @Override
    public String toString () {
        return "эпик задача {" +
                "№" + getIdTask() +
                ", Название='" + getNameTask() + '\'' +
                ", Описание='" + getDescriptionTask() + '\'' +
                ", Статус='" + getStatusTask() + '\'' +
                "}" +
                "\n";
    }
}
