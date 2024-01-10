package tasks;

import static tasks.TaskType.SUBTASK;

public class SubTask extends Task {

    private int epicId;

    TaskType type = SUBTASK;

    public SubTask(String name, String description, int id, TaskStatus status, int epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
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
                '}';
    }
}
