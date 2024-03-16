import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> listHashSubtask = new ArrayList<>();
    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask);
    }

    @Override
    public String toString () {
        return "эпик задача {" +
                "№" + idTask +
                ", Название='" + nameTask + '\'' +
                ", Описание='" + descriptionTask + '\'' +
                ", Статус='" + statusTask + '\'' +
                "}" +
                "\n";
    }
}
