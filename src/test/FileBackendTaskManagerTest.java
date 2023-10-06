package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackendTaskManager;
import service.Manager;
import task.Epic;
import task.Task;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackendTaskManagerTest extends TaskManagerTest<FileBackendTaskManager> {

    private static final File TEST_FILE_PATH = new File("testTasks.csv");

    @BeforeEach
    public void setUpFileManager() {
        TEST_FILE_PATH.deleteOnExit();
        taskManager = Manager.getFileBackendTaskManagerDefault(TEST_FILE_PATH);
    }

    @Test
    public void testSaveAndLoadFromFile() {
        Task task1 = new Task("Задача 1", "Описание 1");
        Task task2 = new Task("Задача 2", "Описание 2");

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.save();

        FileBackendTaskManager loadedTaskManager = FileBackendTaskManager.loadFromFile(TEST_FILE_PATH);

        List<Task> tasks = loadedTaskManager.getAllTasks();

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertEquals(taskManager.getAllTasks().toString(), tasks.toString());
    }

    @Test
    public void testSaveAndLoadFromFileWithEmptyFile() {
        taskManager.save();

        FileBackendTaskManager loadedTaskManager = FileBackendTaskManager.loadFromFile(TEST_FILE_PATH);

        List<Task> tasks = loadedTaskManager.getAllTasks();

        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testSaveAndLoadWithEmptyTaskList() {

        FileBackendTaskManager fileManager = new FileBackendTaskManager(TEST_FILE_PATH);

        fileManager.save();

        FileBackendTaskManager loadedManager = FileBackendTaskManager.loadFromFile(TEST_FILE_PATH);

        List<Task> loadedTasks = loadedManager.getAllTasks();
        assertNotNull(loadedTasks);
        assertTrue(loadedTasks.isEmpty());
    }

    @Test
    public void testSaveAndLoadWithEpicWithoutSubtasks() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        taskManager.createEpic(epic);

        FileBackendTaskManager loadedManager = FileBackendTaskManager.loadFromFile(TEST_FILE_PATH);

        List<Epic> loadedTasks = loadedManager.getAllEpic();

        assertNotNull(loadedTasks);
        assertEquals(1, loadedTasks.size());
        assertEquals(loadedManager.getAllEpic().toString(), List.of(epic).toString());
    }

    @Test
    void loadFromFileBadFilePatch() {
        assertThrows(IllegalArgumentException.class, () -> FileBackendTaskManager.loadFromFile(new File("123")));
    }

    @Test
    void saveBadFilePatch() {
        FileBackendTaskManager fileBackedTaskManager = new FileBackendTaskManager(new File("src/test"));
        assertThrows(IllegalArgumentException.class, fileBackedTaskManager::save);
    }
}