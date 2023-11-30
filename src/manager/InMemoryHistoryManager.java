package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private List<Task> tasks = new ArrayList<>();
    @Override
    public void addTask(Task task) {
        tasks.add(task);
        if(tasks.size() > 10) {
            tasks.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(tasks);
    }

    @Override
    public void remove(int id) {
        for(Task item : tasks) {
            if(item.getId() == id) {
                tasks.remove(item);
            }
        }
    }

}
