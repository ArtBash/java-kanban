package tasks;

import java.util.ArrayList;
import java.util.Iterator;

public class Epic extends Task {

    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String name, String description, int id, TaskStatus status) {
        super(name, description, status, id);
    }

    public Epic(String name, String description, TaskStatus status) {
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

    public void updateSubTaskIds(ArrayList<Integer> newSubTaskIds) {
        subTaskIds.clear();
        subTaskIds = newSubTaskIds;
    }

    public void removeSubTaskId(int id) {
        Integer itemId = Integer.valueOf(id);
        Iterator<Integer> it = subTaskIds.iterator();
        while(it.hasNext()) {
            Integer subtaskId = it.next();
            if(subtaskId.equals(itemId)) {
                it.remove();
            }
        }
    }

    public void removeSubTasksIds() {
        subTaskIds.clear();
    }

    @Override
    public String toString() {
        return "tasks.Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
