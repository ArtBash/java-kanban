package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

import static tasks.TaskType.SUBTASK;

public class SubTask extends Task {

    private int epicId;
    TaskType type = SUBTASK;

    public SubTask(String name, String description, TaskStatus status, int epicId, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType(){
        return type;
    }

    @Override
    public String toString() {
        return "tasks.SubTask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", start time='" + super.getStartTime() + '\'' +
                ", duration='" + super.getDuration() + '\'' +
                '}';
    }
}
