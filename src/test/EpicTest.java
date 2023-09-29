package test;

import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.Manager;
import service.TaskManager;
import task.Epic;
import task.SubTask;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {

    @Test
    public void testCalculateStatus_emptySubTaskList() {
        TaskManager taskManager = Manager.getTaskDefault();
        Epic epic = new Epic("Epic 1", "Описание 1");


        taskManager.createEpic(epic);

        epic.setSubTaskId(new ArrayList<>());

        String status = epic.getStatus();

        assertEquals("NEW", status);
    }

    @Test
    public void testCalculateStatus_allSubTasksDone() {
        TaskManager taskManager = Manager.getTaskDefault();
        Epic epic = new Epic("Epic 3", "Описание 3");

        taskManager.createEpic(epic);

        ArrayList<Integer> subTaskIds = new ArrayList<>();

        SubTask subTask3 = new SubTask("SubTask 3", "Описание 3", epic.getId());
        SubTask subTask4 = new SubTask("SubTask 4", "Описание 4", epic.getId());



        taskManager.createSubTask(subTask3);
        taskManager.createSubTask(subTask4);

        subTaskIds.add(subTask3.getId());
        subTaskIds.add(subTask4.getId());
        epic.setSubTaskId(subTaskIds);
        subTask3.setStatus("DONE");
        subTask4.setStatus("DONE");
        taskManager.updateSubTask(subTask3);
        taskManager.updateSubTask(subTask4);

        assertEquals("DONE", epic.getStatus());
    }

    @Test
    public void testCalculateStatus_subTasksNewAndDone() {
        TaskManager taskManager = Manager.getTaskDefault();
        Epic epic = new Epic("Epic 4", "Описание 4");

        taskManager.createEpic(epic);

        ArrayList<Integer> subTaskIds = new ArrayList<>();

        SubTask subTask5 = new SubTask("SubTask 5", "Описание 5", epic.getId());
        SubTask subTask6 = new SubTask("SubTask 6", "Описание 6", epic.getId());


        taskManager.createSubTask(subTask5);
        taskManager.createSubTask(subTask6);

        subTaskIds.add(subTask5.getId());
        subTaskIds.add(subTask6.getId());
        epic.setSubTaskId(subTaskIds);
        subTask5.setStatus("NEW");
        subTask6.setStatus("DONE");
        taskManager.updateSubTask(subTask5);
        taskManager.updateSubTask(subTask6);

        assertEquals("IN_PROGRESS", epic.getStatus());
    }

    @Test
    public void testCalculateStatus_subTasksInProgress() {
        TaskManager taskManager = Manager.getTaskDefault();
        Epic epic = new Epic("Epic 5", "Описание 5");

        taskManager.createEpic(epic);

        ArrayList<Integer> subTaskIds = new ArrayList<>();

        SubTask subTask7 = new SubTask("SubTask 7", "Описание 7", epic.getId());
        SubTask subTask8 = new SubTask("SubTask 8", "Описание 8", epic.getId());


        taskManager.createSubTask(subTask7);
        taskManager.createSubTask(subTask8);

        subTaskIds.add(subTask7.getId());
        subTaskIds.add(subTask8.getId());
        epic.setSubTaskId(subTaskIds);
        subTask7.setStatus("IN_PROGRESS");
        subTask8.setStatus("IN_PROGRESS");
        taskManager.updateSubTask(subTask7);
        taskManager.updateSubTask(subTask8);

        assertEquals("IN_PROGRESS", epic.getStatus());
    }
}