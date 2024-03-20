import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager>{
    @BeforeEach
    public void setUp() {
        manager = new InMemoryTaskManager();
    }
}
