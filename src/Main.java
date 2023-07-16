import Task.Task;
import Task.Epic;
import Task.SubTask;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("задача 1", "Описание 1");
        taskManager.createTask(task1);
        Task task2 = new Task("задача 2", "Описание 2");
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Эпик 1");
        taskManager.createEpic(epic1);

        Epic epic2 = new Epic("Эпик 2", "Эпик 2");
        taskManager.createEpic(epic2);

        SubTask subTask1 = new SubTask("Саб таск 1", "Саб таск 1", epic1.getId());
        taskManager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask("Саб таск 2", "Саб таск 2", epic1.getId());
        taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Саб таск 3", "Саб таск 3", epic2.getId());
        taskManager.createSubTask(subTask3);

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());

        task1.setStatus("IN_PROGRESS");
        task2.setStatus("DONE");
        subTask1.setStatus("DONE");
        subTask3.setStatus("DONE");

        taskManager.deleteTaskById(task1.getId());
        taskManager.deleteEpicById(epic1.getId());

        System.out.println();
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());
    }
}
