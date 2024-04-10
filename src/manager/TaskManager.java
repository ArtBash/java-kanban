package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;

public interface TaskManager {

    List<Task> getTasks();

    List<SubTask> getSubTasks();

    List<Epic> getEpics();

    List<SubTask> getEpicSubTasks(Epic epic);

    Task getTask(int id);

    SubTask getSubTask(int id);

    Epic getEpic(int id);

    int addNewTask(Task task);
    int addNewEpic(Epic epic);

    int addNewSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    boolean deleteTask(int id);

    boolean deleteSubTask(int id);

    boolean deleteEpic(int id);

    void removeAllTasks();
    void removeAllEpics();
    void removeAllSubTasks();

    List<Task> getHistory();

    void calculateEpicTimeAndDuration(Epic epic);
    TreeMap<LocalDateTime, Task> getPrioritizedTasks();
}
