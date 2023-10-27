import java.util.*;

public class TaskManager implements ITaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    private int generatorId = 0;

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        Iterator<Map.Entry<Integer, Task>> iter = tasks.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<Integer, Task> entry = iter.next();
            tasksList.add(entry.getValue());
        }
        return tasksList;
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        ArrayList<SubTask> subTaskList = new ArrayList<>();
        Iterator<Map.Entry<Integer, SubTask>> iter = subTasks.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<Integer, SubTask> entry = iter.next();
            subTaskList.add(entry.getValue());
        }
        return subTaskList;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicList = new ArrayList<>();
        Iterator<Map.Entry<Integer, Epic>> iter = epics.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<Integer, Epic> entry = iter.next();
            epicList.add(entry.getValue());
        }
        return epicList;
    }

    @Override
    public ArrayList<SubTask> getEpicSubTasks(Epic epic) {
        ArrayList<SubTask> subtasks = new ArrayList<>();
        for(Integer id : epic.getSubTaskIds()) {
            subtasks.add(subtasks.get(id));
        }
        return subtasks;
    }

    @Override
    public Task getTask(int id) {
        return tasks.get(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        return epics.get(id);
    }

    @Override
    public int addNewTask(Task task) {
        final int id = ++generatorId;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        final int id = ++generatorId;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    @Override
    public int addNewSubTask(SubTask subTask) {
        Epic epic = getEpic(subTask.getEpicId());
        if(epic == null) {
            System.out.println("no such epic: " + subTask.getEpicId());
            return -1;
        }
        final int id = ++generatorId;
        subTask.setId(id);
        epic.addSubTaskId(id);
        subTasks.put(id, subTask);
        updateEpicStatus(epic);
        return id;
    }

    @Override
    public void updateTask(Task task) {

    }

    @Override
    public void updateEpic(Epic epic) {

    }

    @Override
    public void updateSubTask(SubTask subTask) {

    }
    @Override
    public void updateEpicStatus(Epic epic) {
        Set<String> status = new HashSet<>();
        for(Integer id : epic.subTaskIds) {
            status.add(subTasks.get(id).getStatus());
        }
        if (status.size() == 1 && status.contains("NEW")) {
            epic.setStatus("NEW");
            return;
        }
        if (status.size() == 1 && status.contains("DONE")) {
            epic.setStatus("DONE");
            return;
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }

    @Override
    public boolean deleteTask(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            System.out.println("No such task.");
            return false;
        } else {
            tasks.remove(id);
            return true;
        }
    }

    @Override
    public boolean deleteSubTask(int id) {
        if (null == subTasks.get(id)) {
            System.out.println("No such subtask.");
            return false;
        } else {
            subTasks.remove(id);
            return true;
        }
    }

    @Override
    public boolean deleteEpic(int id) {
        if(null == epics.get(id)) {
            System.out.println("No such epic.");
            return false;
        } else {
            epics.remove(id);
            return true;
        }
    }
}
