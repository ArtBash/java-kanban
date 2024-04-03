import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

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

    @BeforeEach
    void create() {
        task1 = new Task("TestTask1", "TestDesc1", NEW, LocalDateTime.of(2024, 03, 20, 13,30), Duration.ofMinutes(30));
        task2 = new Task("TestTask2", "TestDesc2", NEW, LocalDateTime.of(2024, 03, 20, 14,30), Duration.ofMinutes(30));
        epic = new Epic("TestEpic", "TestDescEpic", NEW, LocalDateTime.now(), Duration.ofMinutes(0));
        subTask1 = new SubTask("TestSub1", "TestSubDesc1", NEW, epic.getId(), LocalDateTime.of(2024, 03, 29, 12,30), Duration.ofMinutes(30));
        subTask2 = new SubTask("TestSub2", "TestSubDesc2", NEW, epic.getId(), LocalDateTime.of(2024, 03, 30, 12,30), Duration.ofMinutes(30));
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
        subTask1 = new SubTask("TestSub", "TestSubDesc", NEW, epicId, LocalDateTime.of(2024, 03, 20, 20,30), Duration.ofMinutes(30));
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
        subTask1 = new SubTask("TestSub", "TestSubDesc", NEW, epicId, LocalDateTime.of(2024, 03, 20, 21,30), Duration.ofMinutes(30));
        manager.addNewSubTask(subTask1);
        assertThrows(NullPointerException.class, () -> manager.getSubTask(28));
    }

    @Test
     void updateTaskStandard() {
       int taskId = manager.addNewTask(task1);
       Task replaceTask = new Task("ReplaceTaskName", "ReplaceTaskDescription", IN_PROGRESS, LocalDateTime.of(2024, 03, 20, 22,30), Duration.ofMinutes(30));
       replaceTask.setId(taskId);
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
        Task replaceTask = new Task("ReplaceTaskName", "ReplaceTaskDescription", IN_PROGRESS, LocalDateTime.of(2024, 04, 23, 12,30), Duration.ofMinutes(30));
        replaceTask.setId(wrongId);
        manager.updateTask(replaceTask);
        assertEquals(task1, manager.getTask(task1.getId()));
    }

    @Test
    void updateSubTaskStandard() {
       int epicId = manager.addNewEpic(epic);
       SubTask sub1 = new SubTask("TestSub1", "TestSubDesc1", NEW, epicId, LocalDateTime.of(2024, 04, 20, 23,30), Duration.ofMinutes(30));
       manager.addNewSubTask(sub1);
       SubTask sub2 = new SubTask("TestSub2", "TestSubDesc2", NEW, epicId, LocalDateTime.of(2024, 04, 21, 12,30), Duration.ofMinutes(30));
       manager.addNewSubTask(sub2);
       SubTask sub1UPD = new SubTask("TestSub1 UPD", "TestSubDesc1 UPD", NEW, epicId, LocalDateTime.of(2024, 04, 22, 12,30), Duration.ofMinutes(30));
        sub1UPD.setId(sub1.getId());
       manager.updateSubTask(sub1UPD);
       assertEquals(sub1UPD, manager.getSubTask(sub1.getId()));
    }

    @Test
    void updateSubTaskEmpty() {
        int epicId = manager.addNewEpic(epic);
        SubTask sub1 = new SubTask("TestSub1", "TestSubDesc1", NEW, epicId, LocalDateTime.of(2024, 04, 24, 12,30), Duration.ofMinutes(30));

        SubTask sub2 = new SubTask("TestSub2", "TestSubDesc2", NEW, epicId, LocalDateTime.of(2024, 04, 25, 12,30), Duration.ofMinutes(30));
        manager.addNewSubTask(sub2);
        SubTask sub1UPD = new SubTask("TestSub1 UPD", "TestSubDesc1 UPD", NEW, epicId, LocalDateTime.of(2024, 04, 26, 12,30), Duration.ofMinutes(30));
        sub1.setId(sub1UPD.getId());

        manager.updateSubTask(sub1UPD);
        assertThrows(NullPointerException.class, () -> manager.getSubTask(sub1.getId()));
    }

    @Test
    void updateSubTaskWrong() {
        int epicId = manager.addNewEpic(epic);
        SubTask sub1 = new SubTask("TestSub1", "TestSubDesc1", NEW, epicId, LocalDateTime.of(2024, 04, 26, 12,30), Duration.ofMinutes(30));
        manager.addNewSubTask(sub1);
        SubTask sub2 = new SubTask("TestSub2", "TestSubDesc2", NEW, epicId, LocalDateTime.of(2024, 04, 27, 12,30), Duration.ofMinutes(30));
        manager.addNewSubTask(sub2);
        int wrongId = 28;
        SubTask sub1UPD = new SubTask("TestSub1 UPD", "TestSubDesc1 UPD", NEW, epicId, LocalDateTime.of(2024, 04, 28, 12,30), Duration.ofMinutes(30));
        sub1UPD.setId(wrongId);
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
        int subId1 = manager.addNewSubTask(new SubTask("TestSub1", "TestSubDesc1", NEW, epicId, LocalDateTime.of(2024, 04, 29, 12,30), Duration.ofMinutes(30)));
        int subId2 = manager.addNewSubTask(new SubTask("TestSub2", "TestSubDesc2", IN_PROGRESS, epicId, LocalDateTime.of(2024, 04, 30, 12,30), Duration.ofMinutes(30)));
        assertEquals(true, manager.deleteSubTask(subId1));
        assertEquals(true, manager.deleteSubTask(subId2));
    }

    @Test
    void deleteSubTaskEmpty() {
        int epicId = manager.addNewEpic(epic);
        int subId1 = manager.addNewSubTask(new SubTask("TestSub1", "TestSubDesc1", NEW, epicId, LocalDateTime.of(2024, 04, 25, 13,30), Duration.ofMinutes(30)));
        int subId2 = manager.addNewSubTask(new SubTask("TestSub2", "TestSubDesc2", IN_PROGRESS, epicId, LocalDateTime.of(2024, 04, 25, 14,30), Duration.ofMinutes(30)));
        manager.deleteSubTask(subId1);
        manager.deleteSubTask(subId2);
        assertThrows(NullPointerException.class, () -> manager.getSubTask(subId1));
        assertThrows(NullPointerException.class, () -> manager.getSubTask(subId2));
    }

    @Test
    void deleteSubTaskWrong() {
        int epicId = manager.addNewEpic(epic);
        int subId1 = manager.addNewSubTask(new SubTask("TestSub1", "TestSubDesc1", NEW, epicId, LocalDateTime.of(2024, 04, 25, 15,30), Duration.ofMinutes(30)));
        int subId2 = manager.addNewSubTask(new SubTask("TestSub2", "TestSubDesc2", IN_PROGRESS, epicId, LocalDateTime.of(2024, 04, 25, 16,30), Duration.ofMinutes(30)));
        int wrongId1 = 28;
        int wrongId2 = 29;
        manager.deleteSubTask(subId1);
        manager.deleteSubTask(subId2);
        assertThrows(NullPointerException.class, () -> manager.getSubTask(wrongId1));
        assertThrows(NullPointerException.class, () -> manager.getSubTask(wrongId2));
    }

    @Test
    void getTasks() {
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        assertEquals(2, manager.getTasks().size());
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
        manager.addNewSubTask(new SubTask("TestSub1", "TestSubDesc1", NEW, epicId, LocalDateTime.of(2024, 04, 25, 17,30), Duration.ofMinutes(30)));
        manager.addNewSubTask(new SubTask("TestSub2", "TestSubDesc2", NEW, epicId, LocalDateTime.of(2024, 04, 25, 18,30), Duration.ofMinutes(30)));
        manager.removeAllSubTasks();
        assertEquals(true, manager.getSubTasks().isEmpty());
        assertEquals(epic, manager.getEpic(epicId));
    }

    @Test
    void getHistory() {
        int taskId1 = manager.addNewTask(task1);
        int taskId2 = manager.addNewTask(task2);
        int epicId = manager.addNewEpic(epic);
        int subId1 = manager.addNewSubTask(new SubTask("TestSub1", "TestSubDesc1", NEW, epicId, LocalDateTime.of(2024, 04, 25, 20,30), Duration.ofMinutes(30)));
        int subId2 = manager.addNewSubTask(new SubTask("TestSub2", "TestSubDesc2", NEW, epicId, LocalDateTime.of(2024, 04, 25, 21,30), Duration.ofMinutes(30)));
        manager.getTask(taskId1);
        manager.getTask(taskId2);
        manager.getSubTask(subId1);
        manager.getEpic(epicId);
        manager.getSubTask(subId2);
        manager.getSubTask(subId1);
        manager.getEpic(epicId);
        assertEquals(5, manager.getHistory().size());
    }

    @Test
    void whenStartTimeEqualsEndTime() {
       Task task01 = new Task("TestTask1", "TestDesc1", NEW, LocalDateTime.of(2024, 03, 20, 13,30), Duration.ofMinutes(30));
       Task task02 = new Task("TestTask2", "TestDesc2", NEW, LocalDateTime.of(2024, 03, 20, 14,00), Duration.ofMinutes(30));
       manager.addNewTask(task01);
       assertTrue(manager.isValid(task02));
    }
    @Test
    void whenEndTimeIsBeforeStartTime() {
        Task task01 = new Task("TestTask1", "TestDesc1", NEW, LocalDateTime.of(2024, 03, 20, 13,30), Duration.ofMinutes(30));
        Task task02 = new Task("TestTask2", "TestDesc2", NEW, LocalDateTime.of(2024, 03, 20, 14,30), Duration.ofMinutes(30));
        manager.addNewTask(task01);
        assertTrue(manager.isValid(task02));
    }
    @Test
    void whenStartTimeIsBeforeEndTime() {
        Task task01 = new Task("TestTask1", "TestDesc1", NEW, LocalDateTime.of(2024, 03, 20, 13,30), Duration.ofMinutes(40));
        Task task02 = new Task("TestTask2", "TestDesc2", NEW, LocalDateTime.of(2024, 03, 20, 14,00), Duration.ofMinutes(30));
        manager.addNewTask(task01);
        assertFalse(manager.isValid(task02));
    }

    @Test
    void addSubtaskWithoutEpic() {
        SubTask sub = new SubTask("TestSub1", "TestSubDesc1", NEW, 99, LocalDateTime.of(2024, 03, 29, 12,30), Duration.ofMinutes(30));
        int subId = manager.addNewSubTask(sub);
        assertThrows(NullPointerException.class, () -> manager.getSubTask(subId));
    }

}
