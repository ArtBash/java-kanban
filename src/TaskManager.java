import java.util.*;

public class TaskManager implements ITaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    private int generatorId = 0;

    Epic epic;

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>(tasks.values());
        return tasksList;
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        ArrayList<SubTask> subTaskList = new ArrayList<>(subTasks.values());
        return subTaskList;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicList = new ArrayList<>(epics.values());
        return epicList;
    }

    @Override
    public ArrayList<SubTask> getEpicSubTasks(Epic epic) {
        ArrayList<SubTask> subtaskList = new ArrayList<>();
        for(Integer id : epic.getSubTaskIds()) {
            subtaskList.add(subTasks.get(id));
        }
        return subtaskList;
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
        updateEpicStatus(epic);
        return id;
    }

    @Override
    public int addNewSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
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
        tasks.replace(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.replace(epic.getId(), epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.replace(subTask.getId(), subTask);
    }

    private void updateEpicStatus(Epic epicItem) {
        Set<String> status = new HashSet<>();
        if(epicItem.getSubTaskIds().isEmpty()) {
            epicItem.setStatus("NEW");
            return;
        }
        for(Integer id : epicItem.getSubTaskIds()) {
            status.add(subTasks.get(id).getStatus());
        }
        if (status.size() == 1 && status.contains("NEW")) {
            epicItem.setStatus("NEW");
            return;
        }
        if (status.size() == 1 && status.contains("DONE")) {
            epicItem.setStatus("DONE");
            return;
        } else {
            epicItem.setStatus("IN_PROGRESS");
        }
    }

    @Override
    public boolean deleteTask(int id) {
        if(tasks.remove(id).equals(null)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean deleteSubTask(int id) {
        if ((subTasks.remove(id)).equals(null)) {
            System.out.println("No subtask with id: " + id);
            return false;
        } else {
            epic.removeSubTaskId(id);
            updateEpicStatus(epics.get(id));
            return true;
        }
    }

    @Override
    public boolean deleteEpic(int id) {
        if(epics.remove(id).equals(null)) {
            System.out.println("No epic with id: " + id);
            return false;
        } else {
            ArrayList<SubTask> subList = new ArrayList<>(subTasks.values());
            for(SubTask targetTask : subList) {
                if(targetTask.getEpicId() == id) {
                    subTasks.remove(targetTask.getId());
                }
            }
            return true;
        }
    }
    @Override
    public boolean removeAllTasks() {
        tasks.clear();
        if(tasks.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public boolean removeAllEpics() {
        epics.clear();
        if(epics.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public boolean removeAllSubTasks() {
        subTasks.clear();
        if(subTasks.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
