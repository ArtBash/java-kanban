import java.util.ArrayList;

public interface ITaskManager {

    ArrayList<Task> getTasks();

    ArrayList<SubTask> getSubTasks();

    ArrayList<Epic> getEpics();

    ArrayList<SubTask> getEpicSubTasks(Epic epic);

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

    boolean removeAllTasks();
    boolean removeAllEpics();
    boolean removeAllSubTasks();

}
