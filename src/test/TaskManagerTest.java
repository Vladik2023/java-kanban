package test;
import com.google.gson.Gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.Manager;
import service.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    public void setUp() {
        taskManager = createTaskManager();
    }

    @Test
    public void testCreateTask() {
        Task task = new Task("Задача 1", "Описание 1");
        Task createdTask = taskManager.createTask(task);

        assertNotNull(createdTask);
        assertNotNull(createdTask.getId());
        assertEquals(task.getName(), createdTask.getName());
        assertEquals(task.getDescription(), createdTask.getDescription());
        assertEquals("NEW", createdTask.getStatus());
    }

    @Test
    public void testCreateEpic() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        Epic createdEpic = taskManager.createEpic(epic);

        assertNotNull(createdEpic);
        assertNotNull(createdEpic.getId());
        assertEquals(epic.getName(), createdEpic.getName());
        assertEquals(epic.getDescription(), createdEpic.getDescription());
        assertEquals("NEW", createdEpic.getStatus());
    }

    @Test
    public void testCreateSubTask() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание 1", epic.getId());
        subTask.setStartTime(new Date());
        subTask.setDuration(0);
        SubTask createdSubTask = taskManager.createSubTask(subTask);

        assertNotNull(createdSubTask);
        assertNotNull(createdSubTask.getId());
        assertEquals(subTask.getName(), createdSubTask.getName());
        assertEquals(subTask.getDescription(), createdSubTask.getDescription());
        assertEquals("NEW", createdSubTask.getStatus());
        assertEquals(epic.getId(), createdSubTask.getEpicId());
    }

    @Test
    public void testGetSubTaskByEpic() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        taskManager.createEpic(epic);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", epic.getId());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание 2", epic.getId());
        subTask1.setStartTime(new Date());
        subTask1.setDuration(0);
        subTask2.setStartTime(new Date());
        subTask2.setDuration(0);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        List<Integer> subTaskIds = taskManager.getSubTaskByEpic(epic.getId());

        assertNotNull(subTaskIds);
        assertEquals(2, subTaskIds.size());
        assertTrue(subTaskIds.contains(subTask1.getId()));
        assertTrue(subTaskIds.contains(subTask2.getId()));
    }

    @Test
    public void testGetTaskById() {
        Task task = new Task("Задача 1", "Описание 1");
        Task createdTask = taskManager.createTask(task);

        Task retrievedTask = taskManager.getTaskById(createdTask.getId());

        assertNotNull(retrievedTask);
        assertEquals(createdTask.getId(), retrievedTask.getId());
        assertEquals(createdTask.getName(), retrievedTask.getName());
        assertEquals(createdTask.getDescription(), retrievedTask.getDescription());
        assertEquals(createdTask.getStatus(), retrievedTask.getStatus());
    }

    @Test
    public void testGetEpicById() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        Epic createdEpic = taskManager.createEpic(epic);

        Task retrievedEpic = taskManager.getEpicById(createdEpic.getId());

        assertNotNull(retrievedEpic);
        assertEquals(createdEpic.getId(), retrievedEpic.getId());
        assertEquals(createdEpic.getName(), retrievedEpic.getName());
        assertEquals(createdEpic.getDescription(), retrievedEpic.getDescription());
        assertEquals(createdEpic.getStatus(), retrievedEpic.getStatus());
    }

    @Test
    public void testGetSubTaskById() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание 1", epic.getId());
        subTask.setStartTime(new Date());
        subTask.setDuration(0);
        SubTask createdSubTask = taskManager.createSubTask(subTask);

        SubTask retrievedSubTask = (SubTask) taskManager.getSubTaskById(createdSubTask.getId());

        assertNotNull(retrievedSubTask);
        assertEquals(createdSubTask.getId(), retrievedSubTask.getId());
        assertEquals(createdSubTask.getName(), retrievedSubTask.getName());
        assertEquals(createdSubTask.getDescription(), retrievedSubTask.getDescription());
        assertEquals(createdSubTask.getStatus(), retrievedSubTask.getStatus());
        assertEquals(createdSubTask.getEpicId(), retrievedSubTask.getEpicId());
    }

    @Test
    public void testGetAllTasks() {
        Task task1 = new Task("Задача 1", "Описание 1");
        Task task2 = new Task("Задача 2", "Описание 2");
        task1.setStartTime(new Date());
        task1.setDuration(0);
        task2.setStartTime(new Date());
        task2.setDuration(0);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    public void testGetAllEpic() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        Epic epic2 = new Epic("Эпик 2", "Описание 2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        List<Epic> epics = taskManager.getAllEpic();

        assertNotNull(epics);
        assertEquals(2, epics.size());
        assertTrue(epics.contains(epic1));
        assertTrue(epics.contains(epic2));
    }

    @Test
    public void testGetAllSubTask() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        taskManager.createEpic(epic);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", epic.getId());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание 2", epic.getId());
        subTask1.setStartTime(new Date());
        subTask1.setDuration(0);
        subTask2.setStartTime(new Date());
        subTask2.setDuration(0);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        List<SubTask> subTasks = taskManager.getAllSubTask();

        assertNotNull(subTasks);
        assertEquals(2, subTasks.size());
        assertTrue(subTasks.contains(subTask1));
        assertTrue(subTasks.contains(subTask2));
    }

    @Test
    public void testGetHistory() {
        Task task1 = new Task("Задача 1", "Описание 1");
        Task task2 = new Task("Задача 2", "Описание 2");
        task1.setStartTime(new Date());
        task1.setDuration(0);
        task2.setStartTime(new Date());
        task2.setDuration(0);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());

        List<Task> history = taskManager.getHistory();

        assertNotNull(history);
        assertEquals(2, history.size());
        assertTrue(history.contains(task1));
        assertTrue(history.contains(task2));
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task("Задача 1", "Описание 1");
        Task createdTask = taskManager.createTask(task);

        createdTask.setName("Обновленная задача");
        createdTask.setDescription("Обновленное описание");
        createdTask.setStatus("IN_PROGRESS");

        taskManager.updateTask(createdTask);

        Task updatedTask = taskManager.getTaskById(createdTask.getId());

        assertNotNull(updatedTask);
        assertEquals(createdTask.getId(), updatedTask.getId());
        assertEquals(createdTask.getName(), updatedTask.getName());
        assertEquals(createdTask.getDescription(), updatedTask.getDescription());
        assertEquals(createdTask.getStatus(), updatedTask.getStatus());
    }

    @Test
    public void testUpdateEpic() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        Epic createdEpic = taskManager.createEpic(epic);

        createdEpic.setName("Обновленный эпик");
        createdEpic.setDescription("Обновленное описание");
        createdEpic.setStatus("IN_PROGRESS");

        taskManager.updateEpic(createdEpic);

        Task updatedEpic = taskManager.getEpicById(createdEpic.getId());

        assertNotNull(updatedEpic);
        assertEquals(createdEpic.getId(), updatedEpic.getId());
        assertEquals(createdEpic.getName(), updatedEpic.getName());
        assertEquals(createdEpic.getDescription(), updatedEpic.getDescription());
        assertEquals(createdEpic.getStatus(), updatedEpic.getStatus());
    }

    @Test
    public void testUpdateSubTask() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание 1", epic.getId());
        subTask.setStartTime(new Date());
        subTask.setDuration(0);
        SubTask createdSubTask = taskManager.createSubTask(subTask);

        createdSubTask.setName("Обновленная подзадача");
        createdSubTask.setDescription("Обновленное описание");
        createdSubTask.setStatus("IN_PROGRESS");

        taskManager.updateSubTask(createdSubTask);

        SubTask updatedSubTask = (SubTask) taskManager.getSubTaskById(createdSubTask.getId());

        assertNotNull(updatedSubTask);
        assertEquals(createdSubTask.getId(), updatedSubTask.getId());
        assertEquals(createdSubTask.getName(), updatedSubTask.getName());
        assertEquals(createdSubTask.getDescription(), updatedSubTask.getDescription());
        assertEquals(createdSubTask.getStatus(), updatedSubTask.getStatus());
        assertEquals(createdSubTask.getEpicId(), updatedSubTask.getEpicId());
    }

    @Test
    public void testDeleteTaskById() {
        Task task = new Task("Задача 1", "Описание 1");
        Task createdTask = taskManager.createTask(task);

        taskManager.deleteTaskById(createdTask.getId());

        Task deletedTask = taskManager.getTaskById(createdTask.getId());

        assertNull(deletedTask);
    }

    @Test
    public void testDeleteEpicById() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        Epic createdEpic = taskManager.createEpic(epic);

        taskManager.deleteEpicById(createdEpic.getId());

        Task deletedEpic = taskManager.getEpicById(createdEpic.getId());

        assertNull(deletedEpic);
    }

    @Test
    public void testDeleteSubTaskById() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание 1", epic.getId());
        subTask.setStartTime(new Date());
        subTask.setDuration(0);
        SubTask createdSubTask = taskManager.createSubTask(subTask);

        taskManager.deleteSubTaskById(createdSubTask.getId());

        Task deletedSubTask = taskManager.getSubTaskById(createdSubTask.getId());

        assertNull(deletedSubTask);
    }

    @Test
    public void testDeleteAllTask() {
        Task task1 = new Task("Задача 1", "Описание 1");
        Task task2 = new Task("Задача 2", "Описание 2");
        task1.setStartTime(new Date());
        task1.setDuration(0);
        task2.setStartTime(new Date());
        task2.setDuration(0);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.deleteAllTask();

        List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testDeleteAllEpic() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        Epic epic2 = new Epic("Эпик 2", "Описание 2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        taskManager.deleteAllEpic();

        List<Epic> epics = taskManager.getAllEpic();

        assertNotNull(epics);
        assertTrue(epics.isEmpty());
    }

    @Test
    public void testDeleteAllSubTask() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        taskManager.createEpic(epic);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", epic.getId());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание 2", epic.getId());
        subTask1.setStartTime(new Date());
        subTask1.setDuration(0);
        subTask2.setStartTime(new Date());
        subTask2.setDuration(0);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        taskManager.deleteAllSubTask();

        List<SubTask> subTasks = taskManager.getAllSubTask();

        assertNotNull(subTasks);
        assertTrue(subTasks.isEmpty());
    }

    @Test
    public void testGetSubTaskByEpicWithEmptyList() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        taskManager.createEpic(epic);

        List<Integer> subTaskIds = taskManager.getSubTaskByEpic(epic.getId());

        assertNotNull(subTaskIds);
        assertTrue(subTaskIds.isEmpty());
    }

    @Test
    public void testGetTaskByIdWithInvalidId() {
        Task task = taskManager.getTaskById(100);

        assertNull(task);
    }

    @Test
    public void testGetEpicByIdWithInvalidId() {
        Task epic = taskManager.getEpicById(100);

        assertNull(epic);
    }

    @Test
    public void testGetSubTaskByIdWithInvalidId() {
        Task subTask = taskManager.getSubTaskById(100);

        assertNull(subTask);
    }

    @Test
    public void testCreateTaskWithDurationAndStartTime() {
        TaskManager taskManager = Manager.getTaskDefault();
        Task task = new Task("Task with duration and start time", "Description", 60, new Date());
        Task createdTask = taskManager.createTask(task);
        assertNotNull(createdTask);
        assertNotNull(createdTask.getId());
        assertEquals("Task with duration and start time", createdTask.getName());
        assertEquals("Description", createdTask.getDescription());
        assertEquals(60, createdTask.getDuration().intValue());
        assertNotNull(createdTask.getStartTime());
        assertNotNull(createdTask.getEndTime());
    }

    @Test
    public void testCreateEpicWithDurationAndStartTime() {
        TaskManager taskManager = Manager.getTaskDefault();
        Epic epic = new Epic("Epic with duration and start time", "Description", 120, new Date());
        Epic createdEpic = taskManager.createEpic(epic);
        assertNotNull(createdEpic);
        assertNotNull(createdEpic.getId());
        assertEquals("Epic with duration and start time", createdEpic.getName());
        assertEquals("Description", createdEpic.getDescription());
        assertEquals(120, createdEpic.getDuration().intValue());
        assertNotNull(createdEpic.getStartTime());
        assertNotNull(createdEpic.getEndTime());
    }

    public static class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

        @Override
        protected InMemoryTaskManager createTaskManager() {
            return new InMemoryTaskManager();
        }
    }
}