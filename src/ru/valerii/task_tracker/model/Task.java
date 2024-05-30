package ru.valerii.task_tracker.model;

import ru.valerii.task_tracker.service.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;
    private LocalDateTime startTime;
    private Duration duration;

    public Task(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String nameTask, String descriptionTask, Status statusTask) {
        this.name = nameTask;
        this.description = descriptionTask;
        this.status = statusTask;
    }

    public Task(String nameTask, String descriptionTask) {
        this.name = nameTask;
        this.description = descriptionTask;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if ((startTime != null) | !duration.isZero()) {
            return startTime.plus(duration);
        }
        return null;
    }

    @Override
    public String toString() {
        return "задача {" +
                "ID:" + getId() +
                ", Название='" + getName() + '\'' +
                ", Описание='" + getDescription() + '\'' +
                ", Статус='" + getStatus() + '\'' +
                ", время начала='" + getStartTime() + '\'' +
                ", продолжительность='" + getDuration() + '\'' +
                "}" +
                "\n";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        if (this.getClass() != object.getClass()) return false;
        Task otherTask = (Task) object;
        return Objects.equals(name, otherTask.name) &&
                Objects.equals(description, otherTask.description);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (name != null) {
            hash = hash + name.hashCode();
        }
        hash = hash * 31;
        if (description != null) {
            hash = hash + description.hashCode();
        }
        return hash;
    }
}