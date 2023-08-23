package Service;
import Task.Task;
import Task.Epic;
import Task.SubTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CSVFormatHandler {

    private static  final String DELIMITER = ",";
    String toString(Task task){
        String result = task.getId() + DELIMITER +
                task.getType() + DELIMITER +
                task.getName() + DELIMITER +
                task.getStatus() + DELIMITER +
                task.getDescription();
        if(task.getType() == TaskType.SUBTASK){
            result = result + DELIMITER + ((SubTask) task).getEpicId();
        }
        return result;
    }

    static Task fromString(String value) {
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

    String historyToString(HistoryManager manager){
        List<String> result = new ArrayList<>();

        for (Task task : manager.getHistory()) {
            result.add(String.valueOf(task.getId()));
        }
        
        return String.join(DELIMITER,result);
    }

    static List<Integer> historyFromString(String value){
        List<Integer> result = new ArrayList<>();

        String[] parts = value.split(DELIMITER);
        for (String part : parts) {
            result.add(Integer.parseInt(part));
        }

        return result;
    }
    String getHeader(){
        return "id,type,name,status,description,epic";
    }
}
