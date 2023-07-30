package Service;

import Task.Task;
import Task.Epic;
import Task.SubTask;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    Task createTask(Task task);

    Epic createEpic(Epic epic);

    SubTask createSubTask(SubTask subTask);

    ArrayList<Integer> getSubTaskByEpic(int epicId);

    Task getTaskById(int id);
    Task getEpicById(int id);
    Task getSubTaskById(int id);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpic();

    ArrayList<SubTask> getAllSubTask();
    List<Task> getHistory();

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    String checkStatusEpic(int id);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubTaskById(int id);

    void deleteAllTask();

    void deleteAllEpic();

    void deleteAllSubTask();
}
