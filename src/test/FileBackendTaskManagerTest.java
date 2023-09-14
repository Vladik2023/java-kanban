package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackendTaskManager;
import task.Epic;
import task.Task;

import java.io.File;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class FileBackendTaskManagerTest extends TaskManagerTest<FileBackendTaskManager> {

    private static final String TEST_FILE_PATH = "testTasks.csv";

    @Override
    protected FileBackendTaskManager createTaskManager() {
        File file = new File(TEST_FILE_PATH);
        return new FileBackendTaskManager(file);
    }

    @BeforeEach
    public void setUp() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
        taskManager = createTaskManager();
    }

    @Test
    public void testSaveAndLoadFromFile() {
        Task task1 = new Task("Задача 1", "Описание 1");
        Task task2 = new Task("Задача 2", "Описание 2");
        task1.setStartTime(new Date());
        task1.setDuration(0);
        task2.setStartTime(new Date());
        task2.setDuration(0);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.save();

        FileBackendTaskManager loadedTaskManager = FileBackendTaskManager.loadFromFile(new File(TEST_FILE_PATH));

        List<Task> tasks = loadedTaskManager.getAllTasks();

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    public void testSaveAndLoadFromFileWithEmptyFile() {
        taskManager.save();

        FileBackendTaskManager loadedTaskManager = FileBackendTaskManager.loadFromFile(new File(TEST_FILE_PATH));

        List<Task> tasks = loadedTaskManager.getAllTasks();

        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testSaveAndLoadWithEmptyTaskList() {
        File file = new File("testTasks.csv");
        FileBackendTaskManager fileManager = new FileBackendTaskManager(file);

        fileManager.save();

        FileBackendTaskManager loadedManager = FileBackendTaskManager.loadFromFile(file);

        List<Task> loadedTasks = loadedManager.getAllTasks();
        assertNotNull(loadedTasks);
        assertTrue(loadedTasks.isEmpty());
    }

    @Test
    public void testSaveAndLoadWithEpicWithoutSubtasks() {
        File file = new File("testTasks.csv");
        FileBackendTaskManager fileManager = new FileBackendTaskManager(file);

        Epic epic = new Epic("Эпик 1", "Описание 1");
        fileManager.createEpic(epic);

        fileManager.save();

        FileBackendTaskManager loadedManager = FileBackendTaskManager.loadFromFile(file);

        List<Epic> loadedTasks = loadedManager.getAllEpic();
        assertNotNull(loadedTasks);
        assertEquals(1, loadedTasks.size());
        assertTrue(loadedTasks.contains(epic));
    }

    @Test
    public void testSaveAndLoadWithEmptyHistory() {
        File file = new File("testTasks.csv");
        FileBackendTaskManager fileManager = new FileBackendTaskManager(file);

        fileManager.save();

        FileBackendTaskManager loadedManager = FileBackendTaskManager.loadFromFile(file);

        List<Task> loadedHistory = loadedManager.getHistory();
        assertNotNull(loadedHistory);
        assertTrue(loadedHistory.isEmpty());
    }
}