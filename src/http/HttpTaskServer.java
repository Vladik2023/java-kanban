package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import service.FileBackendTaskManager;
import service.Manager;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final int RESPONSE_CODE_SUCCESS_NO_CONTENT = 204;
    private final HttpServer server;
    private final FileBackendTaskManager taskManager;
    private final Gson gson;

    public HttpTaskServer() throws IOException {
        this.taskManager = Manager.getFileBackendTaskManagerDefault();
        this.gson = Manager.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handleTask);
    }

    public HttpTaskServer(String host, int port, FileBackendTaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.gson = Manager.getGson();
        server = HttpServer.create(new InetSocketAddress(host, port), 0);
        server.createContext("/tasks", this::handleTask);
    }

    private void handleTask(HttpExchange httpExchange) {
        try {
            EndPoints endPoint = getEndPoint(httpExchange);
            String[] partPath = httpExchange.getRequestURI().getPath().split("/");
            String response;
            Integer id;
            String body;
            switch (endPoint) {
                case GET_PRIORITIZED_TASKS:
                    response = gson.toJson(taskManager.getPrioritizedTasks());
                    sendText(httpExchange, response);
                    return;
                case GET_HISTORY:
                    response = gson.toJson(taskManager.getHistory());
                    sendText(httpExchange, response);
                    return;
                case GET_SUBTASK_IN_EPIC:
                    id = parsePathId(partPath[partPath.length - 1]);
                    response = gson.toJson(taskManager.getSubTaskByEpic(id));
                    sendText(httpExchange, response);
                    return;
                case GET_TASKS:
                    response = gson.toJson(taskManager.getAllTasks());
                    sendText(httpExchange, response);
                    return;
                case GET_TASK_BY_ID:
                    id = parsePathId(partPath[partPath.length - 1]);
                    response = gson.toJson(taskManager.getTaskById(id));
                    sendText(httpExchange, response);
                    return;
                case GET_SUBTASKS:
                    response = gson.toJson(taskManager.getAllSubTask());
                    sendText(httpExchange, response);
                    return;
                case GET_SUBTASK_BY_ID:
                    id = parsePathId(partPath[partPath.length - 1]);
                    response = gson.toJson(taskManager.getSubTaskById(id));
                    sendText(httpExchange, response);
                    return;
                case GET_EPICS:
                    response = gson.toJson(taskManager.getAllEpic());
                    sendText(httpExchange, response);
                    return;
                case GET_EPIC_BY_ID:
                    id = parsePathId(partPath[partPath.length - 1]);
                    response = gson.toJson(taskManager.getEpicById(id));
                    sendText(httpExchange, response);
                    return;
                case DELETE_TASKS:
                    taskManager.deleteAllTask();
                    httpExchange.sendResponseHeaders(RESPONSE_CODE_SUCCESS_NO_CONTENT, -1);
                    return;
                case DELETE_TASK_BY_ID:
                    id = parsePathId(partPath[partPath.length - 1]);
                    taskManager.deleteTaskById(id);
                    httpExchange.sendResponseHeaders(RESPONSE_CODE_SUCCESS_NO_CONTENT, -1);
                    return;
                case DELETE_EPICS:
                    taskManager.deleteAllEpic();
                    httpExchange.sendResponseHeaders(RESPONSE_CODE_SUCCESS_NO_CONTENT, -1);
                    return;
                case DELETE_EPIC_BY_ID:
                    id = parsePathId(partPath[partPath.length - 1]);
                    taskManager.deleteEpicById(id);
                    httpExchange.sendResponseHeaders(RESPONSE_CODE_SUCCESS_NO_CONTENT, -1);
                    return;
                case DELETE_SUBTASKS:
                    taskManager.deleteAllSubTask();
                    httpExchange.sendResponseHeaders(RESPONSE_CODE_SUCCESS_NO_CONTENT, -1);
                    return;
                case DELETE_SUBTASK_BY_ID:
                    id = parsePathId(partPath[partPath.length - 1]);
                    taskManager.deleteSubTaskById(id);
                    httpExchange.sendResponseHeaders(RESPONSE_CODE_SUCCESS_NO_CONTENT, -1);
                    return;
                case POST_TASK:
                    body = new String(httpExchange.getRequestBody().readAllBytes());
                    Task task = gson.fromJson(body, Task.class);
                    if (task.getId() != null) {
                        taskManager.updateTask(task);
                        httpExchange.sendResponseHeaders(RESPONSE_CODE_SUCCESS_NO_CONTENT, -1);
                    } else {
                        response = gson.toJson(taskManager.createTask(task));
                        sendText(httpExchange, response);
                    }
                    return;
                case POST_EPIC:
                    body = new String(httpExchange.getRequestBody().readAllBytes());
                    Epic epic = gson.fromJson(body, Epic.class);
                    if (epic.getId() != null) {
                        taskManager.updateEpic(epic);
                        httpExchange.sendResponseHeaders(RESPONSE_CODE_SUCCESS_NO_CONTENT, -1);
                    } else {
                        response = gson.toJson(taskManager.createEpic(epic));
                        sendText(httpExchange, response);
                    }
                    return;
                case POST_SUBTASK:
                    body = new String(httpExchange.getRequestBody().readAllBytes());
                    SubTask subTask = gson.fromJson(body, SubTask.class);
                    if (subTask.getId() != null) {
                        taskManager.updateSubTask(subTask);
                        httpExchange.sendResponseHeaders(RESPONSE_CODE_SUCCESS_NO_CONTENT, -1);
                    } else {
                        response = gson.toJson(taskManager.createSubTask(subTask));
                        sendText(httpExchange, response);
                    }
                    return;
                default:
                    httpExchange.sendResponseHeaders(405, 0);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error is happened");
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private Integer parsePathId(String pathId) {
        try {
            return Integer.parseInt(pathId);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private EndPoints getEndPoint(HttpExchange httpExchange) {
        System.out.println("Processing end-point - " + httpExchange.getRequestURI() + ", method -" + httpExchange.getRequestMethod());
        String path = httpExchange.getRequestURI().getPath().replaceFirst("/tasks", "");
        String method = httpExchange.getRequestMethod();
        switch (method) {
            case "GET": {
                if (path.isEmpty()) { //done
                    return EndPoints.GET_PRIORITIZED_TASKS;
                } else if (Pattern.matches("/task$", path)) {
                    return EndPoints.GET_TASKS;
                } else if (Pattern.matches("/task/\\d+$", path)) {
                    return EndPoints.GET_TASK_BY_ID;
                } else if (Pattern.matches("/subtask$", path)) {
                    return EndPoints.GET_SUBTASKS;
                } else if (Pattern.matches("/subtask/\\d+$", path)) {
                    return EndPoints.GET_SUBTASK_BY_ID;
                } else if (Pattern.matches("/epic$", path)) {
                    return EndPoints.GET_EPICS;
                } else if (Pattern.matches("/epic/\\d+$", path)) {
                    return EndPoints.GET_EPIC_BY_ID;
                } else if (Pattern.matches("/subtask/epic/\\d+$", path)) {
                    return EndPoints.GET_SUBTASK_IN_EPIC;
                } else if (Pattern.matches("/history$", path)) {
                    return EndPoints.GET_HISTORY;
                }
            }
            case "POST": {
                if (Pattern.matches("/task$", path)) {
                    return EndPoints.POST_TASK;
                } else if (Pattern.matches("/epic$", path)) {
                    return EndPoints.POST_EPIC;
                } else if (Pattern.matches("/subtask$", path)) {
                    return EndPoints.POST_SUBTASK;
                }
            }
            case "DELETE": {
                if (Pattern.matches("/task$", path)) {
                    return EndPoints.DELETE_TASKS;
                } else if (Pattern.matches("/task/\\d+$", path)) {
                    return EndPoints.DELETE_TASK_BY_ID;
                } else if (Pattern.matches("/subtask$", path)) {
                    return EndPoints.DELETE_SUBTASKS;
                } else if (Pattern.matches("/subtask/\\d+$", path)) {
                    return EndPoints.DELETE_SUBTASK_BY_ID;
                } else if (Pattern.matches("/epic$", path)) {
                    return EndPoints.DELETE_EPICS;
                } else if (Pattern.matches("/epic/\\d+$", path)) {
                    return EndPoints.DELETE_EPIC_BY_ID;
                }
            }
        }
        return EndPoints.UNKNOWN;
    }

    private void sendText(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, response.length());

        byte[] responseBytes = response.getBytes(Charset.defaultCharset());
        httpExchange.getResponseBody().write(responseBytes);
    }

    public void start() {
        server.start();
        System.out.println("Server HttpTaskServer started - " + PORT);
    }

    public void stop() {
        System.out.println("Server HttpTaskServer stopped - " + PORT);
        server.stop(0);
    }

}
