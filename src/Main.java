import java.sql.SQLOutput;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        //Создание
        Task task1 = new Task("Task 1", "Task 1 description", "NEW");
        final int taskId1 = manager.addNewTask(task1);
        Task task3 = manager.getTask(taskId1);
        assert(task3.getId() == task1.getId());
        System.out.println("all good");
        //System.exit(1);

        Task task2 = new Task("Task 2", "Task 2 description", "IN_PROGRESS");

        final int taskId2 = manager.addNewTask(task2);

        Epic epic1 = new Epic("Epic 1", "Epic 1 description", "NEW");
        Epic epic2 = new Epic("Epic 2", "Epic 2 description", "NEW");
        final int epicId1 = manager.addNewEpic(epic1);
        final int epicId2 = manager.addNewEpic(epic2);

        SubTask subTask1 = new SubTask("SubTask 1-1", "SubTask1 description", "NEW", epicId1);
        SubTask subTask2 = new SubTask("SubTask 2-1", "SubTask1 description", "NEW", epicId1);
        SubTask subTask3 = new SubTask("SubTask 1-2", "SubTask1 description", "DONE", epicId2);
        final Integer subTaskId1 = manager.addNewSubTask(subTask1);
        final Integer subTaskId2 = manager.addNewSubTask(subTask2);
        final Integer subTaskId3 = manager.addNewSubTask(subTask3);
        new Main().printAllTasks(manager);

        //Обновление
        final Task task = manager.getTask(taskId2);
        task.setStatus("DONE");
        manager.updateTask(task);
        System.out.println("CHANGE STATUS: Task 2 IN_PROGRESS->DONE");
        System.out.println("Задачи: ");
        for (Task t : manager.getTasks()) {
            System.out.println(t);
        }

        final SubTask sub1 = manager.getSubTask(subTaskId1);
        final SubTask sub2 = manager.getSubTask(subTaskId2);
        sub1.setStatus("DONE");
        sub2.setStatus("DONE");
        manager.updateSubTask(sub1);
        manager.updateSubTask(sub2);
        new Main().printAllTasks(manager);

        System.out.println("Task and epic removal.");
        manager.subTasks.remove(task2.getId());
        manager.epics.remove(epicId1);
        assert(null == manager.epics.get(epicId1));
        assert(null == manager.subTasks.get(taskId2));
        new Main().printAllTasks(manager);
    }
    public void printAllTasks(TaskManager manager) {
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
