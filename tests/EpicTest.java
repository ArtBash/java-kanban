import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.TaskStatus.*;

class EpicTest {

    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    Epic epic1;
    SubTask subTask1;
    SubTask subTask2;

    @BeforeEach
    public void create() {
        epic1 = new Epic("TestEpic1", "TestEpic1 description", NEW);
        inMemoryTaskManager.addNewEpic(epic1);
        subTask1 = new SubTask("Subtask1", "Subtask1 description", NEW, epic1.getId());
        inMemoryTaskManager.addNewSubTask(subTask1);
        subTask2 = new SubTask("Subtask2", "Subtask2 description", NEW, epic1.getId());
        inMemoryTaskManager.addNewSubTask(subTask2);
    }

    @Test
    void getEpicsStandart() {
        assertEquals(1, inMemoryTaskManager.getEpics().size());
    }

    @Test
    void getEpicsEmpty() {
        inMemoryTaskManager.deleteEpic(epic1.getId());
        assertTrue(inMemoryTaskManager.getEpics().isEmpty());
    }

    @Test
    void getEpicStandard() {
        Epic epic = inMemoryTaskManager.getEpic(epic1.getId());
        assertEquals(epic1, epic);
    }

    @Test
    void getEpicEmpty() {
        inMemoryTaskManager.deleteEpic(epic1.getId());
        assertThrows(NullPointerException.class, () -> inMemoryTaskManager.getEpic(epic1.getId()));
    }

    @Test
    void getEpicWrong() {
        assertThrows(NullPointerException.class, () -> inMemoryTaskManager.getEpic(28));
    }

    @Test
    void getAllSubTasksStandart() {
        assertEquals(2, inMemoryTaskManager.getSubTasks().size());
    }

    @Test
    void getAllSubTasksEmpty() {
        inMemoryTaskManager.deleteEpic(epic1.getId());
        assertTrue(inMemoryTaskManager.getSubTasks().isEmpty());
    }

    @Test
    void gteAllSubTaskWrongId() {
        assertThrows(NullPointerException.class, () -> inMemoryTaskManager.getSubTask(28));
    }

    @Test
    public void whenAllSubAreNewThanEpicNew() {
        assertEquals(epic1.getStatus(), NEW);
    }

    @Test
    public void whenNoSubTasksThanStatusNew() {
        inMemoryTaskManager.deleteSubTask(subTask1.getId());
        inMemoryTaskManager.deleteSubTask(subTask2.getId());
        assertEquals(epic1.getStatus(), NEW, "Статусы не совпадают");
    }

    @Test
    public void whenAllSubDoneThanStatusDone() {
        SubTask newSubTask1 = new SubTask("Subtask1 upd", "Subtask1 description upd", DONE, epic1.getId());
        newSubTask1.setId(subTask1.getId());
        SubTask newSubTask2 = new SubTask("Subtask2 upd", "Subtask2 description upd", DONE, epic1.getId());
        newSubTask2.setId(subTask2.getId());
        inMemoryTaskManager.updateSubTask(newSubTask1);
        inMemoryTaskManager.updateSubTask(newSubTask2);
        assertEquals(epic1.getStatus(), DONE, "Статус не равен DONE");
    }

    @Test
    public void whenSubDoneAndNewThanEpicNew() {
        SubTask newSubTask1 = new SubTask("Subtask1 upd", "Subtask1 description upd", DONE, epic1.getId());
        newSubTask1.setId(subTask1.getId());
        inMemoryTaskManager.updateSubTask(newSubTask1);
        assertEquals(epic1.getStatus(), IN_PROGRESS, "Статус не равен IN_PROGRESS");
    }

    @Test
    public void whenSubsInProgressThanEpicInProgress() {
        SubTask newSubTask1 = new SubTask("Subtask1 upd", "Subtask1 description upd", IN_PROGRESS, epic1.getId());
        newSubTask1.setId(subTask1.getId());
        SubTask newSubTask2 = new SubTask("Subtask2 upd", "Subtask2 description upd", IN_PROGRESS, epic1.getId());
        newSubTask2.setId(subTask2.getId());
        inMemoryTaskManager.updateSubTask(newSubTask1);
        inMemoryTaskManager.updateSubTask(newSubTask2);
        assertEquals(epic1.getStatus(), IN_PROGRESS, "Статус по прежнему NEW");
    }

    @Test
    void updateEpicStandard() {
        Epic epic = new Epic("TestEpic1 updated", "TestEpic1 updatedDescription", epic1.getId(), NEW);
        inMemoryTaskManager.updateEpic(epic);
        assertEquals("TestEpic1 updated", inMemoryTaskManager.getEpic(epic.getId()).getName());
    }

    @Test
    void updateEpicEmpty() {
        inMemoryTaskManager.deleteEpic(epic1.getId());
        Epic epic = new Epic("TestEpic1 updated", "TestEpic1 updatedDescription", epic1.getId(), NEW);
        inMemoryTaskManager.updateEpic(epic);
        assertThrows(NullPointerException.class, () -> inMemoryTaskManager.getEpic(epic.getId()).getName());
    }

    @Test
    void updateEpicWrong() {
        int wrongId = 28;
        Epic epic = new Epic("TestEpic1 updated", "TestEpic1 updatedDescription", wrongId, NEW);
        inMemoryTaskManager.updateEpic(epic);
        assertThrows(NullPointerException.class, () -> inMemoryTaskManager.getEpic(epic.getId()).getName());
    }

    @Test
    void whenEpicDeletedSubtasksAlsoRemoved() {
        boolean isDeleted = inMemoryTaskManager.deleteEpic(epic1.getId());
        assertEquals(true, isDeleted, "Epic is still present");
        assertThrows(NullPointerException.class, () -> inMemoryTaskManager.getSubTask(subTask1.getId()), "Subtask 1 is still present");
        assertThrows(NullPointerException.class, () -> inMemoryTaskManager.getSubTask(subTask2.getId()), "Subtask 2 is still present");
    }

    @Test
    void removeAllEpics() {
        inMemoryTaskManager.removeAllEpics();
        assertEquals(true, inMemoryTaskManager.getEpics().isEmpty());
    }
}