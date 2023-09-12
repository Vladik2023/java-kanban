package test;

import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import task.Epic;
import task.SubTask;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {

    @Test
    public void testCalculateStatus_emptySubTaskList() {
        Epic epic = new Epic("Epic 1", "Описание 1");
        epic.setSubTaskId(new ArrayList<>());

        String status = epic.getStatus();

        assertEquals("NEW", status);
    }

    @Test
    public void testCalculateStatus_allSubTasksNew() {
        Epic epic = new Epic("Epic 2", "Описание 2");
        ArrayList<Integer> subTaskIds = new ArrayList<>();
        subTaskIds.add(1);
        subTaskIds.add(2);
        epic.setSubTaskId(subTaskIds);

        SubTask subTask1 = new SubTask("SubTask 1", "Описание 1", 2);
        SubTask subTask2 = new SubTask("SubTask 2", "Описание 2", 2);
        subTask1.setStatus("NEW");
        subTask2.setStatus("NEW");

        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        String status = epic.getStatus();

        assertEquals("NEW", status);
    }

    @Test
    public void testCalculateStatus_allSubTasksDone() {
        Epic epic = new Epic("Epic 3", "Описание 3");
        ArrayList<Integer> subTaskIds = new ArrayList<>();
        subTaskIds.add(3);
        subTaskIds.add(4);
        epic.setSubTaskId(subTaskIds);

        SubTask subTask3 = new SubTask("SubTask 3", "Описание 3", 3);
        SubTask subTask4 = new SubTask("SubTask 4", "Описание 4", 3);
        subTask3.setStatus("DONE");
        subTask4.setStatus("DONE");

        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.createSubTask(subTask3);
        taskManager.createSubTask(subTask4);

        String status = epic.getStatus();

        assertEquals("DONE", status);
    }

    @Test
    public void testCalculateStatus_subTasksNewAndDone() {
        Epic epic = new Epic("Epic 4", "Описание 4");
        ArrayList<Integer> subTaskIds = new ArrayList<>();
        subTaskIds.add(5);
        subTaskIds.add(6);
        epic.setSubTaskId(subTaskIds);

        SubTask subTask5 = new SubTask("SubTask 5", "Описание 5", 4);
        SubTask subTask6 = new SubTask("SubTask 6", "Описание 6", 4);
        subTask5.setStatus("NEW");
        subTask6.setStatus("DONE");

        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.createSubTask(subTask5);
        taskManager.createSubTask(subTask6);

        String status = epic.getStatus();

        assertEquals("IN_PROGRESS", status);
    }

    @Test
    public void testCalculateStatus_subTasksInProgress() {
        Epic epic = new Epic("Epic 5", "Описание 5");
        ArrayList<Integer> subTaskIds = new ArrayList<>();
        subTaskIds.add(7);
        subTaskIds.add(8);
        epic.setSubTaskId(subTaskIds);

        SubTask subTask7 = new SubTask("SubTask 7", "Описание 7", 5);
        SubTask subTask8 = new SubTask("SubTask 8", "Описание 8", 5);
        subTask7.setStatus("IN_PROGRESS");
        subTask8.setStatus("IN_PROGRESS");

        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.createSubTask(subTask7);
        taskManager.createSubTask(subTask8);

        String status = epic.getStatus();

        assertEquals("IN_PROGRESS", status);
    }
}