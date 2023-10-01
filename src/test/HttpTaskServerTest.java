package test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import http.HttpTaskServer;
import http.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackendTaskManager;
import task.Epic;
import task.SubTask;
import task.Task;
import service.Manager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    public static final String URL = "http://localhost:8082/tasks";
    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;
    private FileBackendTaskManager manager;
    private Gson gson;
    private HttpClient httpClient;
    private final Type typeTask = new TypeToken<List<Task>>() {
    }.getType();
    private final Type typeEpic = new TypeToken<List<Epic>>() {
    }.getType();
    private final Type typeSubTask = new TypeToken<List<SubTask>>() {
    }.getType();


    @BeforeEach
    void setUp() throws IOException {
        httpClient = HttpClient.newHttpClient();
        File file = File.createTempFile("testTasks", ".csv");
        manager = new FileBackendTaskManager(file);
        gson = Manager.getGson();
        kvServer = new KVServer("localhost", 8081);
        kvServer.start();

        httpTaskServer = new HttpTaskServer("localhost", 8082, manager);
        httpTaskServer.start();

        createDefaultTasks();
    }

    @AfterEach
    void tearDown() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    private void createDefaultTasks() {
        Task task = new Task("Task - name", "Task - description", 10L,
                LocalDateTime.now().minusMinutes(30));
        manager.createTask(task);
        Epic epic = new Epic("Epic - name", "Epic - description");
        epic = manager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask - name", "SubTask - description", 15L,
                LocalDateTime.now().minusMinutes(10), epic.getId());
        subTask = manager.createSubTask(subTask);
        manager.getTaskById(task.getId());
        manager.getSubTaskById(subTask.getId());
    }

    private HttpRequest prepareHttpRequestGet(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(URL + url))
                .GET()
                .build();
    }

    private HttpRequest prepareHttpRequestDelete(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(URL + url))
                .DELETE()
                .build();
    }

    private HttpRequest prepareHttpRequestPost(String body, String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(URL + url))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }

    @Test
    void getAllTasks() throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(prepareHttpRequestGet("/task"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getAllTasks().toString(), gson.fromJson(response.body(), typeTask).toString());
    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(prepareHttpRequestGet("/task/1"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getTaskById(1).toString(), gson.fromJson(response.body(), Task.class).toString());
    }

    @Test
    void deleteAllTask() throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(prepareHttpRequestDelete("/task"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        assertEquals(0, manager.getAllTasks().size());
    }

    @Test
    void deleteTaskById() throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(prepareHttpRequestDelete("/task/1"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        assertEquals(0, manager.getAllTasks().size());
    }

    @Test
    void getAllEpics() throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(prepareHttpRequestGet("/epic"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getAllEpic().toString(), gson.fromJson(response.body(), typeEpic).toString());
    }

    @Test
    void getEpicById() throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(prepareHttpRequestGet("/epic/2"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getEpicById(2).toString(), gson.fromJson(response.body(), Epic.class).toString());
    }

    @Test
    void deleteAllEpic() throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(prepareHttpRequestDelete("/epic"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        assertEquals(0, manager.getAllEpic().size());
    }

    @Test
    void deleteEpicById() throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(prepareHttpRequestDelete("/epic/2"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        assertEquals(0, manager.getAllEpic().size());
    }

    @Test
    void getAllSubTasks() throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(prepareHttpRequestGet("/subtask"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getAllSubTask().toString(), gson.fromJson(response.body(), typeSubTask).toString());
    }

    @Test
    void getSubTaskById() throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(prepareHttpRequestGet("/subtask/3"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task subTask = manager.getSubTaskById(3);
        assertNotNull(subTask);
        assertEquals(subTask.toString(), gson.fromJson(response.body(), SubTask.class).toString());
    }

    @Test
    void deleteAllSubTasks() throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(prepareHttpRequestDelete("/subtask"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        assertEquals(0, manager.getAllSubTask().size());
    }

    @Test
    void deleteSubTaskById() throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(prepareHttpRequestDelete("/subtask/3"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        assertEquals(0, manager.getAllSubTask().size());
    }

    @Test
    void getSubTaskInEpicById() throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(prepareHttpRequestGet("/subtask/epic/2"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getSubTaskByEpic(2).toString(),
                Arrays.toString(gson.fromJson(response.body(), int[].class)));
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(prepareHttpRequestGet("/history"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getHistory().size(), gson.fromJson(response.body(), List.class).size());
    }

    @Test
    void getPriorityTask() throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(prepareHttpRequestGet(""),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getPrioritizedTasks().size(), gson.fromJson(response.body(), List.class).size());
    }

    @Test
    void createTask() throws IOException, InterruptedException {
        String body = "{\"name\": \"task - name\", \"description\": \"task - description\"}";
        HttpResponse<String> response = httpClient.send(prepareHttpRequestPost(body, "/task"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getAllTasks().get(0).toString(), gson.fromJson(response.body(), Task.class).toString());
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        String body = "{\"id\": \"1\", \"name\": \"task - update\", \"description\": \"task - update\"}";
        HttpResponse<String> response = httpClient.send(prepareHttpRequestPost(body, "/task"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        assertEquals(manager.getAllTasks().get(0).getName(), "task - update");
        assertEquals(manager.getAllTasks().get(0).getDescription(), "task - update");
    }

    @Test
    void createEpic() throws IOException, InterruptedException {
        String body = "{\"name\": \"epic - name\", \"description\": \"epic - description\"}";
        HttpResponse<String> response = httpClient.send(prepareHttpRequestPost(body, "/epic"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getAllEpic().get(0).toString(), gson.fromJson(response.body(), Epic.class).toString());
    }

    @Test
    void updateEpic() throws IOException, InterruptedException {
        createDefaultTasks();
        Task epic = manager.getEpicById(2);
        epic.setName("Epic - update");
        String body = gson.toJson(epic);
        HttpResponse<String> response = httpClient.send(prepareHttpRequestPost(body, "/epic"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        assertEquals(manager.getAllEpic().get(0).getName(), "Epic - update");
        assertEquals(manager.getAllEpic().get(0).getDescription(), "epic - update");
    }

    @Test
    void createSubTask() throws IOException, InterruptedException {
        String body = "{\"name\": \"subTask - name\", \"description\": \"subTask - description\", \"parentId\":\"2\"}";
        HttpResponse<String> response = httpClient.send(prepareHttpRequestPost(body, "/subtask"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getAllSubTask().get(1).toString(), gson.fromJson(response.body(), SubTask.class).toString());
    }

    @Test
    void updateSubtask() throws IOException, InterruptedException {
        String body = "{\"id\": \"3\", \"name\": \"subTask - update\", \"description\": \"subTask - update\""
                + ", \"parentId\":\"2\", \"status\":\"IN_PROGRESS\"}";
        HttpResponse<String> response = httpClient.send(prepareHttpRequestPost(body, "/subtask"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        assertEquals(manager.getAllSubTask().get(0).getName(), "subTask - update");
        assertEquals(manager.getAllSubTask().get(0).getDescription(), "subTask - update");
    }

    @Test
    void badURI() throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(prepareHttpRequestGet("/subtaaask"),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode());
    }
}