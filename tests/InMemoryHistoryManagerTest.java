import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tasks.TaskStatus.IN_PROGRESS;
import static tasks.TaskStatus.NEW;



public class InMemoryHistoryManagerTest {
    protected Task task1;
    protected Task task2;
    protected Epic epic;
    protected SubTask subTask1;
    protected SubTask subTask2;
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @BeforeEach
    void create() {
        task1 = new Task("TestTask1", "TestDesc1", NEW);
        task2 = new Task("TestTask2", "TestDesc2", NEW);
        epic = new Epic("TestEpic", "TestDescEpic",1,  NEW);
        subTask1 = new SubTask("TestSub1", "TestSubDesc1", 2, NEW, epic.getId());
        subTask2 = new SubTask("TestSub2", "TestSubDesc2", 3, IN_PROGRESS, epic.getId());
    }

    @Test
    void addTaskStandard() {
        historyManager.add(epic);
        historyManager.add(subTask1);
        historyManager.add(subTask2);
        ArrayList<Task> expected = new ArrayList<>(List.of(epic, subTask1, subTask2));
        assertEquals(expected, historyManager.getHistory());
    }



}
