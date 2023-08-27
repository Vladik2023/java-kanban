package Service;

import Task.Task;
import Task.Epic;
import Task.SubTask;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FileBackendTaskManager extends InMemoryTaskManager{
    private File file;
    private CSVFormatHandler handler= new CSVFormatHandler();

    public FileBackendTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(handler.getHeader());
            writer.newLine();

            for (Task task : taskStorage.values()) {
                writer.write(handler.toString(task));
                writer.newLine();
            }
            for (Epic epic : epicStorage.values()) {
                writer.write(handler.toString(epic));
                writer.newLine();
            }
            for (SubTask subTask : subTaskStorage.values()) {
                writer.write(handler.toString(subTask));
                writer.newLine();
            }

            writer.newLine();
            List<Integer> history = new ArrayList<>();
            for (Task task : historyManager.getHistory()) {
                history.add(task.getId());
            }
            writer.write(CSVFormatHandler.historyToString(history));

        } catch (IOException exception) {
            throw new IllegalArgumentException("Файл не сохранен!");
        }
    }

    public static FileBackendTaskManager loadFromFile(File file) {
        FileBackendTaskManager taskManager = new FileBackendTaskManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeaderSkipped = false;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    if (!isHeaderSkipped) {
                        isHeaderSkipped = true;
                        continue;
                    }
                    Task task = CSVFormatHandler.fromString(line);
                    taskManager.addTaskToStorage(task);
                } else {
                    break;
                }
            }

            List<Integer> history = CSVFormatHandler.historyFromString(reader.readLine());
            Collections.reverse(history);
            taskManager.addHistoryToManager(history);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Ошибка при чтении файла: " + file.getName());
        }

        return taskManager;
    }

    private void addTaskToStorage(Task task) {
        TaskType type = task.getType();
        switch (type) {
            case TASK:
                taskStorage.put(task.getId(), task);
                break;
            case EPIC:
                epicStorage.put(task.getId(), (Epic) task);
                break;
            case SUBTASK:
                subTaskStorage.put(task.getId(), (SubTask) task);
                break;
            default:
                throw new IllegalArgumentException("Неверный тип задачи: " + type);
        }
    }

    private void addHistoryToManager(List<Integer> history) {
        for (Integer taskId : history) {
            Task task = getTaskById(taskId);
            if (task != null) {
                historyManager.addTask(task);
            }
            Task epic = getEpicById(taskId);
            if (epic != null) {
                historyManager.addTask(epic);
            }
            Task subTask = getSubTaskById(taskId);
            if (subTask != null) {
                historyManager.addTask(subTask);
            }
        }
    }

    @Override
    public Task createTask(Task task) {
        Task result = super.createTask(task);
        save();
        return result;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic result = super.createEpic(epic);
        save();
        return  result;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask result = super.createSubTask(subTask);
        save();
        return  result;
    }

    @Override
    public ArrayList<Integer> getSubTaskByEpic(int epicId) {
        ArrayList<Integer> result = super.getSubTaskByEpic(epicId);
        save();
        return result;
    }

    @Override
    public Task getTaskById(int id) {
        Task result = super.getTaskById(id);
        save();
        return result;
    }

    @Override
    public Task getEpicById(int id) {
        Task result = super.getEpicById(id);
        save();
        return result;
    }

    @Override
    public Task getSubTaskById(int id) {
        Task result = super.getSubTaskById(id);
        save();
        return result;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> result = super.getAllTasks();
        save();
        return result;
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        ArrayList<Epic> result = super.getAllEpic();
        save();
        return result;
    }

    @Override
    public ArrayList<SubTask> getAllSubTask() {
        ArrayList<SubTask> result = super.getAllSubTask();
        save();
        return result;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> result = super.getHistory();
        save();
        return result;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
        save();
    }


    public static void main(String[] args) {
        // Создание и добавление задач, эпиков и подзадач
//        Task task1 = new Task("Задача 1", "Описание задачи 1");
//        Task task2 = new Task("Задача 2", "Описание задачи 2");
//        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
//        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", epic1.getId());
//        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", epic1.getId());
//
//        File file = new File("tasks2.csv");
//
//        // Создание и сохранение менеджера в файл
//        FileBackendTaskManager taskManager1 = new FileBackendTaskManager(file);
//        taskManager1.createTask(task1);
//        taskManager1.createTask(task2);
//        taskManager1.createEpic(epic1);
//        taskManager1.createSubTask(subTask1);
//        taskManager1.createSubTask(subTask2);
//        taskManager1.getTaskById(task1.getId());
//        taskManager1.getEpicById(epic1.getId());
//        //taskManager1.getSubTaskById(subTask1.getId());
//        //taskManager1.getSubTaskById(subTask2.getId());
//
//        // Загрузка менеджера из файла
//        FileBackendTaskManager taskManager2 = FileBackendTaskManager.loadFromFile(file);
//
//        // Проверка восстановленных данных
//        List<Task> history1 = taskManager1.getHistory();
//        List<Task> history2 = taskManager2.getHistory();
//
//        System.out.println("История просмотра в менеджере 1:");
//
//            System.out.println(history1);
//
//
//        System.out.println("История просмотра в менеджере 2:");
//
//            System.out.println(history2);
//
//
//        // Проверка наличия задач, эпиков и подзадач в менеджере 2
//        System.out.println("Задачи в менеджере 2:");
//        for (Task task : taskManager2.getAllTasks()) {
//            System.out.println(task);
//        }
//
//        System.out.println("Эпики в менеджере 2:");
//        for (Epic epic : taskManager2.getAllEpic()) {
//            System.out.println(epic);
//        }
//
//        System.out.println("Подзадачи в менеджере 2:");
//        for (SubTask subTask : taskManager2.getAllSubTask()) {
//            System.out.println(subTask);
//        }
        FileBackendTaskManager fileManager = new FileBackendTaskManager(new File("saveTasks2.csv"));
        fileManager.createTask(new Task("task1", "Купить автомобиль"));
        fileManager.createEpic(new Epic("new Epic1", "Новый Эпик"));
        fileManager.createSubTask(new SubTask("New Subtask", "Подзадача", 2));
        fileManager.createSubTask(new SubTask("New Subtask2", "Подзадача2", 2));
        fileManager.getTaskById(1);
        fileManager.getEpicById(2);
        fileManager.getSubTaskById(3);
//        System.out.println(fileManager.getAllTasks());
//        System.out.println(fileManager.getAllEpic());
//        System.out.println(fileManager.getAllSubTask());
        System.out.println("История просмотров" + fileManager.getHistory());
        System.out.println("\n\n" + "new" + "\n\n");
        FileBackendTaskManager fileBackedTasksManager = loadFromFile(new File("saveTasks2.csv"));
//        System.out.println(fileManager.getAllTasks());
//        System.out.println(fileManager.getAllEpic());
//        System.out.println(fileManager.getAllSubTask());
        System.out.println("История просмотров" + fileBackedTasksManager.getHistory());
    }
}
