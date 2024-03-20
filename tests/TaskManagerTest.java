import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tasks.TaskStatus.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected Task task1;
    protected Task task2;
    protected Epic epic;
    protected SubTask subTask1;
    protected SubTask subTask2;
    protected T manager;
//    protected int taskId;
//    protected int epicId;
//    protected int subId;

    @BeforeEach
    void create() {
        task1 = new Task("TestTask1", "TestDesc1", NEW);
        task2 = new Task("TestTask2", "TestDesc2", NEW);
        epic = new Epic("TestEpic", "TestDescEpic", NEW);
        subTask1 = new SubTask("TestSub1", "TestSubDesc1", NEW, epic.getId());
        subTask2 = new SubTask("TestSub2", "TestSubDesc2", NEW, epic.getId());
    }

    @Test
    void getTasksStandard() {
        manager.addNewTask(task1);
        assertTrue(manager.getTasks().size() > 0);
    }
    @Test
    void getTasksEmpty() {
        assertTrue(manager.getTasks().isEmpty());
    }


    @Test
    void getTaskStandard() {
       int taskId = manager.addNewTask(task1);
       Task savedTask = manager.getTask(taskId);
       assertNotNull(savedTask);
    }

    @Test
    void getTaskEmpty() {
        assertThrows(NullPointerException.class, () -> manager.getTask(task1.getId()));
    }

    @Test
    void getTaskWrong() {
        manager.addNewTask(task1);
        assertThrows(NullPointerException.class, () -> manager.getTask(28));
    }

    @Test
     void getSubTaskStandard() {
        int epicId = manager.addNewEpic(epic);
        subTask1 = new SubTask("TestSub", "TestSubDesc", NEW, epicId);
        int subId = manager.addNewSubTask(subTask1);
        SubTask saved = manager.getSubTask(subId);
        assertNotNull(saved);
        assertEquals(manager.getSubTasks().size(), 1);
        assertEquals(manager.getEpics().size(), 1);
        assertEquals(manager.getHistory().size(), 1);
    }

    @Test
    void getSubTaskEmpty() {
        assertThrows(NullPointerException.class, () -> manager.getSubTask(subTask1.getId()));
    }

    @Test
    void getSubTaskWrong() {
        int epicId = manager.addNewEpic(epic);
        subTask1 = new SubTask("TestSub", "TestSubDesc", NEW, epicId);
        int subId = manager.addNewSubTask(subTask1);
        assertThrows(NullPointerException.class, () -> manager.getSubTask(28));
    }

    @Test
     void updateTaskStandard() {
       int taskId = manager.addNewTask(task1);
       Task replaceTask = new Task("ReplaceTaskName", "ReplaceTaskDescription", IN_PROGRESS, taskId);
       manager.updateTask(replaceTask);
       assertEquals(replaceTask, manager.getTask(taskId));
    }

    @Test
    void updateTaskEmpty(){
        manager.updateTask(task1);
        assertThrows(NullPointerException.class, () -> manager.getTask(task1.getId()));
    }

    @Test
    void updateTaskWrong() {
        manager.addNewTask(task1);
        int wrongId = 28;
        Task replaceTask = new Task("ReplaceTaskName", "ReplaceTaskDescription", IN_PROGRESS, wrongId);
        manager.updateTask(replaceTask);
        assertEquals(task1, manager.getTask(task1.getId()));
    }

    @Test
    void updateSubTaskStandard() {
       int epicId = manager.addNewEpic(epic);
       SubTask sub1 = new SubTask("TestSub1", "TestSubDesc1", NEW, epicId);
       manager.addNewSubTask(sub1);
       SubTask sub2 = new SubTask("TestSub2", "TestSubDesc2", NEW, epicId);
       manager.addNewSubTask(sub2);
       SubTask sub1UPD = new SubTask("TestSub1 UPD", "TestSubDesc1 UPD", sub1.getId(), NEW, epicId);

       manager.updateSubTask(sub1UPD);
       assertEquals(sub1UPD, manager.getSubTask(sub1.getId()));
    }

    @Test
    void updateSubTaskEmpty() {
        int epicId = manager.addNewEpic(epic);
        SubTask sub1 = new SubTask("TestSub1", "TestSubDesc1", NEW, epicId);

        SubTask sub2 = new SubTask("TestSub2", "TestSubDesc2", NEW, epicId);
        manager.addNewSubTask(sub2);
        SubTask sub1UPD = new SubTask("TestSub1 UPD", "TestSubDesc1 UPD", sub1.getId(), NEW, epicId);

        manager.updateSubTask(sub1UPD);
        assertThrows(NullPointerException.class, () -> manager.getSubTask(sub1.getId()));
    }

    @Test
    void updateSubTaskWrong() {
        int epicId = manager.addNewEpic(epic);
        SubTask sub1 = new SubTask("TestSub1", "TestSubDesc1", NEW, epicId);
        manager.addNewSubTask(sub1);
        SubTask sub2 = new SubTask("TestSub2", "TestSubDesc2", NEW, epicId);
        manager.addNewSubTask(sub2);
        int wrongId = 28;
        SubTask sub1UPD = new SubTask("TestSub1 UPD", "TestSubDesc1 UPD", wrongId, NEW, epicId);

        manager.updateSubTask(sub1UPD);
        assertNotEquals(sub1UPD, manager.getSubTask(sub1.getId()));
    }

    @Test
    void deleteTaskStandard() {
        int taskId = manager.addNewTask(task1);
        boolean isRemoved = manager.deleteTask(taskId);
        assertEquals(true, isRemoved);
    }

    @Test
    void deleteTaskEmpty() {
        int taskId = manager.addNewTask(task1);
        manager.deleteTask(taskId);
        assertThrows(NullPointerException.class, () ->  manager.deleteTask(taskId));
    }

    @Test
    void deleteTaskWrong() {
        manager.addNewTask(task1);
        assertThrows(NullPointerException.class, () -> manager.deleteTask(28));
    }

    @Test
    void deleteSubTaskStandard() {
        int epicId = manager.addNewEpic(epic);
        int subId1 = manager.addNewSubTask(new SubTask("TestSub1", "TestSubDesc1", NEW, epicId));
        int subId2 = manager.addNewSubTask(new SubTask("TestSub2", "TestSubDesc2", IN_PROGRESS, epicId));
        assertEquals(true, manager.deleteSubTask(subId1));
        assertEquals(true, manager.deleteSubTask(subId2));
    }

    @Test
    void deleteSubTaskEmpty() {
        int epicId = manager.addNewEpic(epic);
        int subId1 = manager.addNewSubTask(new SubTask("TestSub1", "TestSubDesc1", NEW, epicId));
        int subId2 = manager.addNewSubTask(new SubTask("TestSub2", "TestSubDesc2", IN_PROGRESS, epicId));
        manager.deleteSubTask(subId1);
        manager.deleteSubTask(subId2);
        assertThrows(NullPointerException.class, () -> manager.getSubTask(subId1));
        assertThrows(NullPointerException.class, () -> manager.getSubTask(subId2));
    }

    @Test
    void deleteSubTaskWrong() {
        int epicId = manager.addNewEpic(epic);
        int subId1 = manager.addNewSubTask(new SubTask("TestSub1", "TestSubDesc1", NEW, epicId));
        int subId2 = manager.addNewSubTask(new SubTask("TestSub2", "TestSubDesc2", IN_PROGRESS, epicId));
        int wrongId1 = 28;
        int wrongId2 = 29;
        manager.deleteSubTask(subId1);
        manager.deleteSubTask(subId2);
        assertThrows(NullPointerException.class, () -> manager.getSubTask(wrongId1));
        assertThrows(NullPointerException.class, () -> manager.getSubTask(wrongId2));
    }

    @Test
    void removeAllTasks() {
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.removeAllTasks();
        assertEquals(true, manager.getTasks().isEmpty());
    }

    @Test
    void removeAllSubTasks() {
        int epicId = manager.addNewEpic(epic);
        manager.addNewSubTask(new SubTask("TestSub1", "TestSubDesc1", NEW, epicId));
        manager.addNewSubTask(new SubTask("TestSub2", "TestSubDesc2", NEW, epicId));
        manager.removeAllSubTasks();
        assertEquals(true, manager.getSubTasks().isEmpty());
        assertEquals(epic, manager.getEpic(epicId));
    }

    @Test
    void getHistory() {
        int taskId1 = manager.addNewTask(task1);
        int taskId2 = manager.addNewTask(task2);
        int epicId = manager.addNewEpic(epic);
        int subId1 = manager.addNewSubTask(new SubTask("TestSub1", "TestSubDesc1", NEW, epicId));
        int subId2 = manager.addNewSubTask(new SubTask("TestSub2", "TestSubDesc2", NEW, epicId));
        manager.getTask(taskId1);
        manager.getTask(taskId2);
        manager.getSubTask(subId1);
        manager.getEpic(epicId);
        manager.getSubTask(subId2);
        manager.getSubTask(subId1);
        manager.getEpic(epicId);
        assertEquals(5, manager.getHistory().size());
    }
}
