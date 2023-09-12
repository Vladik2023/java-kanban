package service;
import task.Task;
import task.Epic;
import task.SubTask;

import java.util.ArrayList;
import java.util.List;

public class CSVFormatHandler {

    private static  final String DELIMITER = ",";
    public static String toString(Task task) {
        String type = task.getType().toString();
        String result = String.format("%d,%s,%s,%s,%s", task.getId(), type, task.getName(), task.getStatus(), task.getDescription());

        if (task.getType() == TaskType.SUBTASK) {
            SubTask subTask = (SubTask) task;
            result += "," + subTask.getEpicId();
        } else if (task.getType() == TaskType.EPIC) {
            Epic epic = (Epic) task;
            result += "," + String.join("," + epic.getSubTaskId());
        }

        return result;
    }

    public static Task fromString(String value) {
        String[] parts = value.split(DELIMITER);
        if (parts.length < 5) {
            throw new IllegalArgumentException("Неверная входная строка: " + value);
        }

        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        String status = parts[3];
        String description = parts[4];

        Task task;
        if (type == TaskType.TASK) {
            task = new Task(name, description);
        } else if (type == TaskType.EPIC) {
            task = new Epic(name, description);
        } else if (type == TaskType.SUBTASK) {
            if (parts.length < 6) {
                throw new IllegalArgumentException("Неверная входная строка: " + value);
            }
            int epicId = Integer.parseInt(parts[5]);
            task = new SubTask(name, description, epicId);
        } else {
            throw new IllegalArgumentException("Неверная входная строка: " + value);
        }

        task.setId(id);
        task.setStatus(status);

        return task;
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> result = new ArrayList<>();

        String[] parts = value.split(DELIMITER);
        for (String part : parts) {
            try {
                int taskId = Integer.parseInt(part);
                result.add(taskId);
            } catch (NumberFormatException e) {
            }
        }

        return result;
    }

    public static String historyToString(List<Integer> history) {
        List<String> result = new ArrayList<>();

        for (Integer taskId : history) {
            result.add(String.valueOf(taskId));
        }

        return String.join(DELIMITER, result);
    }
    public static String getHeader(){
        return "id,type,name,status,description,epic";
    }
}
