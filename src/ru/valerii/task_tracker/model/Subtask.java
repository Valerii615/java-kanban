package ru.valerii.task_tracker.model;


import ru.valerii.task_tracker.service.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int idEpic;

    public Subtask(String nameTask, String descriptionTask, Status statusTask, int idEpic) {
        super(nameTask, descriptionTask, statusTask);
        this.idEpic = idEpic;
    }

    public Subtask(String nameTask, String descriptionTask, Status statusTask, int idEpic, LocalDateTime localDateTime, Duration duration) {
        super(nameTask, descriptionTask, statusTask, localDateTime, duration);
        this.idEpic = idEpic;
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
                ", время начала='" + getStartTime() + '\'' +
                ", продолжительность='" + getDuration() + '\'' +
                "}" +
                "\n";
    }
}