import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import static tasks.TaskStatus.*;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        //Создание
        System.out.println("CREATING: 2 tasks, 2 epics and 3 subtasks");
        Task task1 = new Task("tasks.Task 1", "tasks.Task 1 description", NEW);
        final int taskId1 = manager.addNewTask(task1);
        Task task2 = manager.getTask(taskId1);
        assert(task2.getId() == task1.getId());


        Task task3 = new Task("tasks.Task 3", "tasks.Task 3 description", IN_PROGRESS);
        final int taskId3 = manager.addNewTask(task3);

        Epic epic1 = new Epic("tasks.Epic 1", "tasks.Epic 1 description", NEW);
        Epic epic2 = new Epic("tasks.Epic 2", "tasks.Epic 2 description", NEW);
        final int epicId1 = manager.addNewEpic(epic1);
        final int epicId2 = manager.addNewEpic(epic2);

        SubTask subTask1 = new SubTask("tasks.SubTask 1-1", "SubTask1 description", NEW, epicId1);
        SubTask subTask2 = new SubTask("tasks.SubTask 2-1", "SubTask2 description", NEW, epicId1);
        SubTask subTask3 = new SubTask("tasks.SubTask 1-2", "SubTask3 description", DONE, epicId2);
        final Integer subTaskId1 = manager.addNewSubTask(subTask1);
        final Integer subTaskId2 = manager.addNewSubTask(subTask2);
        final Integer subTaskId3 = manager.addNewSubTask(subTask3);
        new Main().printAllTasks(manager);
        System.out.println("__________________________________________________");

        //Обновление
        final Task task = manager.getTask(taskId1);
        task.setStatus(DONE);
        manager.updateTask(task);
        System.out.println("CHANGE STATUS: tasks.Task 1 NEW->DONE");
        new Main().printAllTasks(manager);


        System.out.println("Changing epic1 status NEW -> DONE");
        System.out.println("Epic1 status: " + manager.getEpic(epicId1).getStatus());
        final SubTask sub1 = manager.getSubTask(subTaskId1);
        final SubTask sub2 = manager.getSubTask(subTaskId2);
        sub1.setStatus(DONE);
        sub2.setStatus(DONE);
        manager.updateSubTask(sub1);
        manager.updateSubTask(sub2);
        System.out.println("Epic1 status: " + manager.getEpic(epicId1).getStatus());
        System.out.println("__________________________________________________");

        System.out.println("tasks.Task 3 and epic 1 removal.");
        manager.deleteTask(taskId3);
        manager.deleteEpic(epicId1);
        assert(null == manager.getSubTasks().get(taskId3));
        assert(null == manager.getEpics().get(epicId1));
        new Main().printAllTasks(manager);
        System.out.println("__________________________________________________");

        System.out.println("CHECKING: saving request history");
        System.out.println("Before test request");
        System.out.println("Items requested: " + manager.getHistory().size());
        for(Task item : manager.getHistory()) {
            System.out.println(item.getName());
        }
        System.out.println("--------------------------");

        manager.getTask(taskId1);
        manager.getEpic(epicId2);
        manager.getSubTask(subTaskId3);
        manager.getTask(taskId1);
        manager.getTask(taskId1);
        manager.getTask(taskId1);
        System.out.println("Items requested: " + manager.getHistory().size());
        for(Task item : manager.getHistory()) {
            System.out.println(item.getName());
        }



    }
    public void printAllTasks(InMemoryTaskManager manager) {
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        for(Epic epic : manager.getEpics()) {
            System.out.println(epic);
        }
        for(SubTask subTask : manager.getSubTasks() ) {
            System.out.println(subTask);
        }
    }
}
