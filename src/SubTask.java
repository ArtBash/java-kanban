public class SubTask extends Task {

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    protected int epicId;

    public SubTask(String name, String description, int id, String status, int epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, String status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
