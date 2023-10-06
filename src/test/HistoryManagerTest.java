package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Manager;
import task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = Manager.getHistoryDefault();
    }

    @Test
    public void testGetHistoryWithEmptyList() {
        List<Task> history = historyManager.getHistory();

        assertNotNull(history);
        assertTrue(history.isEmpty());
    }

    @Test
    public void testAddTaskToEmptyHistory() {
        Task task = new Task(1, "Задача 1", "Описание 1");

        historyManager.addTask(task);

        List<Task> history = historyManager.getHistory();

        assertNotNull(history);
        assertEquals(1, history.size());
        assertTrue(history.contains(task));
    }

    @Test
    public void testRemoveTaskFromEmptyHistory() {
        Task task = new Task(1,"Задача 1", "Описание 1");

        historyManager.remove(task.getId());

        List<Task> history = historyManager.getHistory();

        assertNotNull(history);
        assertTrue(history.isEmpty());
    }

    @Test
    public void testAddDuplicateTaskToHistory() {
        Task task = new Task(1,"Задача 1", "Описание 1");

        historyManager.addTask(task);
        historyManager.addTask(task);

        List<Task> history = historyManager.getHistory();

        assertNotNull(history);
        assertEquals(1, history.size());
        assertTrue(history.contains(task));
    }

    @Test
    public void testRemoveTaskFromBeginningOfHistory() {
        Task task1 = new Task(1,"Задача 1", "Описание 1");
        Task task2 = new Task(2,"Задача 2", "Описание 2");
        historyManager.addTask(task1);
        historyManager.addTask(task2);

        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();

        assertNotNull(history);
        assertEquals(1, history.size());
        assertTrue(history.contains(task2));
    }

    @Test
    public void testRemoveTaskFromMiddleOfHistory() {
        Task task1 = new Task(1,"Задача 1", "Описание 1");
        Task task2 = new Task(2,"Задача 2", "Описание 2");
        Task task3 = new Task(3,"Задача 3", "Описание 3");
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);

        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();

        assertNotNull(history);
        assertEquals(2, history.size());
        assertTrue(history.contains(task1));
        assertTrue(history.contains(task3));
    }

    @Test
    public void testRemoveTaskFromEndOfHistory() {
        Task task1 = new Task(1,"Задача 1", "Описание 1");
        Task task2 = new Task(2,"Задача 2", "Описание 2");
        historyManager.addTask(task1);
        historyManager.addTask(task2);

        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();

        assertNotNull(history);
        assertEquals(1, history.size());
        assertTrue(history.contains(task1));
    }
}