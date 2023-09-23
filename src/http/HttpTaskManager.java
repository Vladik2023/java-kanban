package http;

import service.FileBackendTaskManager;
import service.TaskManager;

import java.io.File;
import java.io.IOException;

public class HttpTaskManager extends FileBackendTaskManager {
    private final KVTaskClient taskClient;

    public HttpTaskManager(String serverUrl) throws IOException, InterruptedException {
        super(File.createTempFile("tasks", ".csv"));
        this.taskClient = new KVTaskClient(serverUrl);
        loadFromServer();
    }

    private void loadFromServer() throws IOException, InterruptedException {
        String tasksJson = taskClient.load("tasks");
        super.loadFromFile(new File(tasksJson));
    }

    @Override
    public void save() {
        String tasksJson = super.toString();
        try {
            taskClient.put("tasks", tasksJson);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static TaskManager getDefault(String serverUrl) throws IOException, InterruptedException {
        return new HttpTaskManager(serverUrl);
    }
}