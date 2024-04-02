package ru.valerii.task_tracker.model;


import ru.valerii.task_tracker.service.Status;

public class Subtask extends Task {
    private int idEpic;

    public Subtask(String nameTask, String descriptionTask, Status statusTask, int IdEpic) {
        super(nameTask, descriptionTask, statusTask);
        this.idEpic = IdEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        return "подзадача {" +
                "ID:" + getId() +
                ", Название='" + getName() + '\'' +
                ", Описание='" + getDescription() + '\'' +
                ", Статус='" + getStatus() + '\'' +
                "}" +
                "\n";
    }
}