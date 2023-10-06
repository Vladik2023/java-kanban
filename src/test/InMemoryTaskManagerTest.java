package test;

import org.junit.jupiter.api.BeforeEach;
import service.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void serUpTaskManager(){
        taskManager = new InMemoryTaskManager();
    }

}