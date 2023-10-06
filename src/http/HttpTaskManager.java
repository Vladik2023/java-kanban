package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import service.FileBackendTaskManager;
import service.Manager;
import task.Epic;
import task.SubTask;
import task.Task;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackendTaskManager {

    public static final String KEY_TASK = "ru/yandex/finalTest8/task";
    public static final String KEY_HISTORY = "history";
    public static final String KEY_EPIC = "epic";
    public static final String KEY_SUBTASK = "subtask";
    private final KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(String url) {
        client = new KVTaskClient(url);
        gson = Manager.getGson();
    }

    public HttpTaskManager(KVTaskClient client) {
        this.client = client;
        gson = Manager.getGson();
    }

    @Override
    public void save() {
        String jsonTask = gson.toJson(getAllTasks());
        client.save(KEY_TASK, jsonTask);

        String jsonEpic = gson.toJson(getAllEpic());
        client.save(KEY_EPIC, jsonEpic);

        String jsonSubTask = gson.toJson(getAllSubTask());
        client.save(KEY_SUBTASK, jsonSubTask);

        String jsonHistory = gson.toJson(getHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList()));
        client.save(KEY_HISTORY, jsonHistory);
    }

    public void load() {

        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(client.load(KEY_TASK), taskType);
        for (Task task : tasks) {
            taskStorage.put(task.getId(), task);
        }

        Type epicType = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> epics = gson.fromJson(client.load(KEY_EPIC), epicType);
        for (Epic epic : epics) {
            epicStorage.put(epic.getId(), epic);
        }

        Type subTaskType = new TypeToken<List<SubTask>>() {
        }.getType();
        List<SubTask> subTasks = gson.fromJson(client.load(KEY_SUBTASK), subTaskType);
        for (SubTask subTask : subTasks) {
            subTaskStorage.put(subTask.getId(), subTask);
        }

        newId += tasks.size() + epics.size() + subTasks.size();

        int[] historyIds = gson.fromJson(client.load(KEY_HISTORY), int[].class);
        for (int id : historyIds) {
            historyManager.addTask(findTask(id));
        }
    }

    private Task findTask(int id) {
        if (taskStorage.containsKey(id)) {
            return taskStorage.get(id);
        } else if (epicStorage.containsKey(id)) {
            return epicStorage.get(id);
        } else if (subTaskStorage.containsKey(id)) {
            return subTaskStorage.get(id);
        }
        return null;
    }

}
