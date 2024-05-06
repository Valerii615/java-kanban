package ru.valerii.task_tracker.Exception;

public class ManagerSaveStatusException  extends RuntimeException {
    public ManagerSaveStatusException() {

    }
    public ManagerSaveStatusException (String message) {
        super(message);
    }
}
