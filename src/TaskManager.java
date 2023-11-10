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
        if(epics.containsKey(epic.getId())) {
            Integer id = epic.getId();
            epics.get(id).setName(epic.getName());
            epics.get(id).setDescription(epic.getDescription());
        } else {
            System.out.println("No epic with such id");
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        Integer id = subTask.getId();
        if(subTasks.containsKey(id)) {
            if(subTask.getEpicId() == subTasks.get(id).getEpicId()) {
                subTasks.replace(id, subTask);
                updateEpicStatus(epics.get(subTask.getEpicId()));
            }
        } else {
            System.out.println("No subtask with such id");
        }
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
        Integer itemId = id;
        if(tasks.remove(itemId).equals(null)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean deleteSubTask(int id) {
        Integer itemId = id;
        if ((subTasks.remove(itemId)).equals(null)) {
            System.out.println("No subtask with id: " + itemId);
            return false;
        } else {
            epic.removeSubTaskId(itemId);
            updateEpicStatus(epics.get(itemId));
            return true;
        }
    }

    @Override
    public boolean deleteEpic(int id) {
        Integer itemId = id;
        if(epics.remove(itemId).equals(null)) {
            System.out.println("No epic with id: " + itemId);
            return false;
        } else {
            final Epic epic = epics.get(Integer.valueOf(id));
            for(Integer epicsSubTask : epic.getSubTaskIds()) {
                subTasks.remove(epicsSubTask);
            }
            epics.remove(itemId);
            return true;
        }
    }
    @Override
    public void removeAllTasks() {
        tasks.clear();
    }
    @Override
    public void removeAllEpics() {
        removeAllSubTasks();
        epics.clear();

    }
    @Override
    public void removeAllSubTasks() {
        subTasks.clear();
        ArrayList<Epic> listOfEpics = new ArrayList<>(epics.values());
        for(Epic item : listOfEpics){
            item.removeSubTasksIds();
            updateEpicStatus(item);
        }
    }
}
