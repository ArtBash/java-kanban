package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    List<Task> tasks = new ArrayList<>();
    @Override
    public void addTask(Task task) {
        tasks.add(task);
        if(tasks.size() > 10) {
            tasks.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return tasks;
    }
}
