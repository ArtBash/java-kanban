import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTaskIds = new ArrayList<>();

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
        return new ArrayList<>(subTaskIds);
    }

    public void clearSubTaskIds() {
        subTaskIds.clear();
    }

    public void removeSubTaskId(int id) {
        for(Integer item : subTaskIds) {
            if(item.equals(id)) {
                subTaskIds.remove(item);
            }
        }
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
