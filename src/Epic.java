import java.util.ArrayList;

public class Epic extends Task {

    protected ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String name, String description, int id, String status) {
        super(name, description, status, id);
    }

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }

    @Override
    public boolean isEpic() {
        return true;
    }

    public void addSubTaskId(int id) {
        subTaskIds.add(id);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void clearSubTaskIds() {
        subTaskIds.clear();
    }

    public void removeSubTask(int id) {
        subTaskIds.remove(Integer.valueOf(id));
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
