import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        task1 = new Task("TestTask1", "TestDesc1", NEW, LocalDateTime.of(2024, 03, 20, 12,30), Duration.ofMinutes(30));
        task1.setId(1);
        task2 = new Task("TestTask2", "TestDesc2", NEW, LocalDateTime.of(2024, 03, 21, 12,30), Duration.ofMinutes(15));
        task2.setId(2);
        epic = new Epic("TestEpic", "TestDescEpic",NEW, LocalDateTime.now(), Duration.ofMinutes(0));
        epic.setId(3);
        subTask1 = new SubTask("TestSub1", "TestSubDesc1", NEW, epic.getId(), LocalDateTime.of(2024, 03, 21, 12,30), Duration.ofMinutes(10));
        subTask1.setId(4);
        subTask2 = new SubTask("TestSub2", "TestSubDesc2", IN_PROGRESS, epic.getId(), LocalDateTime.of(2024, 03, 21, 12,30), Duration.ofMinutes(50));
        subTask2.setId(5);
    }

    @Test
    void addTaskStandard() {
        historyManager.add(epic);
        historyManager.add(subTask1);
        historyManager.add(subTask2);
        ArrayList<Task> expected = new ArrayList<>(List.of(epic, subTask1, subTask2));
        assertEquals(expected, historyManager.getHistory());
    }

    @Test
    void addTaskDoubled() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);

        ArrayList<Task> expected = new ArrayList<>(List.of(task2, task1));
        assertEquals(expected, historyManager.getHistory());
    }

    @Test
    void emptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void deleteHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic);
        historyManager.add(subTask1);
        historyManager.add(subTask2);

        historyManager.remove(task1.getId());
        historyManager.remove(epic.getId());
        historyManager.remove(subTask2.getId());
        ArrayList<Task> expected = new ArrayList<>(List.of(task2, subTask1));

        assertEquals(expected, historyManager.getHistory());
    }
}
