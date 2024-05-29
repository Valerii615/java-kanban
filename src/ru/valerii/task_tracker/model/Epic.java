package ru.valerii.task_tracker.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtaskId = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask);
    }

    public List<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(List<Integer> subtaskId) {
        this.subtaskId = subtaskId;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "эпик задача {" +
                "ID:" + getId() +
                ", Название='" + getName() + '\'' +
                ", Описание='" + getDescription() + '\'' +
                ", Статус='" + getStatus() + '\'' +
                ", время начала='" + getStartTime() + '\'' +
                ", продолжительность='" + getDuration() + '\'' +
                ", время завершения='" + getEndTime() + '\'' +
                "}" +
                "\n";
    }
}