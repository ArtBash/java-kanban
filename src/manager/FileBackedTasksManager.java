package manager;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static tasks.TaskStatus.NEW;
import static tasks.TaskType.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;
    private final static CSVTaskFormat taskFormat = new CSVTaskFormat();

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) throws IOException, ManagerSaveException {
        Path writeFilePath = Paths.get("C:\\Users\\art\\dev\\first-project\\java-kanban\\src\\resources\\file.txt");
            Path createdFile = Files.createFile(writeFilePath);
            File file = new File(String.valueOf(createdFile));

        FileBackedTasksManager fb = new FileBackedTasksManager(file);

        Task task1 = new Task("Task 1", "task 1 description", NEW);
        Task task2 = new Task("Task 2", "task 2 description", NEW);
        final int taskId1 = fb.addNewTask(task1);
        final int taskId2 = fb.addNewTask(task2);

        Epic epic1 = new Epic("Epic 1", "epic 1 description", NEW);
        Epic epic2 = new Epic("Epic 2", "epic 2 description", NEW);
        final int epicId1 = fb.addNewEpic(epic1);
        final int epicId2 = fb.addNewEpic(epic2);

        SubTask subtask1 = new SubTask("sub 1", "sub 1 description", NEW, epic1.getId());
        SubTask subtask2 = new SubTask("sub 2", "sub 2 description", NEW, epic1.getId());
        SubTask subtask3 = new SubTask("sub 3", "sub 3 description", NEW, epic1.getId());
        final int subTaskId1 = fb.addNewSubTask(subtask1);
        final int subTaskId2 = fb.addNewSubTask(subtask2);
        final int subTaskId3 = fb.addNewSubTask(subtask3);

        fb.getTask(taskId1);
        fb.getEpic(epicId1);
        fb.getSubTask(subTaskId1);
        fb.getSubTask(subTaskId2);
        fb.getSubTask(subTaskId3);

        printAllTasks(fb);
        System.out.println("first print is over");

        FileBackedTasksManager restoredManager = new FileBackedTasksManager(file);
        restoredManager = loadFromFile(restoredManager.file);


        printAllTasks(restoredManager);
        System.out.println("restored tasks printed");

        Task task3 = new Task("Task 3", "task 3 description", NEW);
        final int taskId3 = restoredManager.addNewTask(task3);

        printAllTasks(restoredManager);
        restoredManager.getTask(taskId3);
        for(Task item : restoredManager.getHistory()) {
            System.out.print(item.getId() + ",");
        }
    }

    public static void printAllTasks(FileBackedTasksManager manager) {
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);
        }
        for (SubTask sub : manager.getSubTasks()) {
            System.out.println(sub);
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        int latestId = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(manager.file))) {
            br.readLine();
            while(br.ready()) {
                String line = br.readLine();
                if(line.isBlank()) {
                    String historyLine = br.readLine();
                    addHistory(taskFormat.historyFromString(historyLine));
                    break;
                }
                Task task = taskFormat.taskFromString(line);
                addTask(task, task.getId());

                if(task.getId() > latestId) {
                    latestId = task.getId();
                }
            }

            manager.restoreId(latestId);

        } catch (IOException e) {
            throw new ManagerSaveException("Cannot read the file.");
        }
        return manager;
    }

    private static void addTask(Task task, int id) {
        Integer taskId = id;
        TaskType type = task.getType();

        if(type.equals(TASK)) {
            tasks.put(taskId, task);
        } else if (type.equals(SUBTASK)) {
            subTasks.put(taskId, (SubTask) task);
        } else if (type.equals(EPIC)) {
            epics.put(taskId, (Epic) task);
        }
    }

    private static void addHistory(List<Integer> historyIds) {
        for(Integer id : historyIds) {
             historyManager.add(getTaskById(id));
        }
    }

    private void save() {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epicId\n");
            for(Task task : getTasks()) {
                writer.write(taskFormat.toString(task));
            }
            for(SubTask task : getSubTasks()) {
                writer.write(taskFormat.toString(task));
            }
            for(Epic task : getEpics()) {
                writer.write(taskFormat.toString(task));
            }
            writer.write("\n");
            writer.write(taskFormat.historyToString(historyManager));
        } catch(IOException e) {
            try {
                throw new ManagerSaveException("Impossile to write to the file");
            } catch (ManagerSaveException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public Task getTask(int id) {
        final Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public SubTask getSubTask(int id) {
        final SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }

    @Override
    public Epic getEpic(int id) {
        final Epic epic = super.getEpic(id);
        save();
        return epic;
    }



    @Override
    public int addNewTask(Task task) {
        int id = super.addNewTask(task);
        save();
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        int id = super.addNewEpic(epic);
        save();
        return id;
    }

    @Override
    public int addNewSubTask(SubTask subtask) {
        int id = super.addNewSubTask(subtask);
        save();
        return id;
    }
    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public boolean deleteTask(int id) {
        boolean removed = super.deleteTask(id);
        save();
        return removed;
        }

    @Override
    public boolean deleteSubTask(int id) {
        boolean isDeleted = super.deleteSubTask(id);
        save();
        return isDeleted;
    }

    @Override
    public boolean deleteEpic(int id) {
        boolean isDeleted = super.deleteEpic(id);
        save();
        return isDeleted;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }
}
