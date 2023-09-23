package http;

import com.google.gson.Gson;
import service.Manager;
import service.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final String TASKS_PATH = "/tasks";
    private static final String TASK_PATH = "/tasks/task";
    private static final String EPIC_PATH = "/tasks/epic";
    private static final String SUBTASK_PATH = "/tasks/subtask";
    private static final String HISTORY_PATH = "/tasks/history";

    private TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext(TASKS_PATH, new TasksHandler());
        server.createContext(TASK_PATH, new TaskHandler());
        server.createContext(EPIC_PATH, new EpicHandler());
        server.createContext(SUBTASK_PATH, new SubTaskHandler());
        server.createContext(HISTORY_PATH, new HistoryHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Сервер запущен на порту " + PORT);
    }

    private class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Task> tasks = taskManager.getAllTasks();
                sendResponse(exchange, 200, new Gson().toJson(tasks));
            } else {
                sendResponse(exchange, 405, "Метод не разрешен");
            }
        }
    }

    private class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                int taskId = getTaskIdFromQuery(exchange.getRequestURI().getQuery());
                Task task = taskManager.getTaskById(taskId);
                if (task != null) {
                    sendResponse(exchange, 200, new Gson().toJson(task));
                } else {
                    sendResponse(exchange, 404, "Задача не найдена");
                }
            } else if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = getRequestString(exchange);
                Task newTask = new Gson().fromJson(requestBody, Task.class);
                Task createdTask = taskManager.createTask(newTask);
                sendResponse(exchange, 201, new Gson().toJson(createdTask));
            } else if ("DELETE".equals(exchange.getRequestMethod())) {
                int taskId = getTaskIdFromQuery(exchange.getRequestURI().getQuery());
                taskManager.deleteTaskById(taskId);
                sendResponse(exchange, 200, "Задача успешно удалена");
            } else {
                sendResponse(exchange, 405, "Метод не разрешен");
            }
        }
    }

    private class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                int epicId = getTaskIdFromQuery(exchange.getRequestURI().getQuery());
                Task epic = taskManager.getEpicById(epicId);
                if (epic != null) {
                    sendResponse(exchange, 200, new Gson().toJson(epic));
                } else {
                    sendResponse(exchange, 404, "Эпик не найден");
                }
            } else if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = getRequestString(exchange);
                Epic newEpic = new Gson().fromJson(requestBody, Epic.class);
                Epic createdEpic = taskManager.createEpic(newEpic);
                sendResponse(exchange, 201, new Gson().toJson(createdEpic));
            } else if ("DELETE".equals(exchange.getRequestMethod())) {
                int epicId = getTaskIdFromQuery(exchange.getRequestURI().getQuery());
                taskManager.deleteEpicById(epicId);
                sendResponse(exchange, 200, "Эпик успешно удален");
            } else {
                sendResponse(exchange, 405, "Метод не разрешен");
            }
        }
    }

    private class SubTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                int subtaskId = getTaskIdFromQuery(exchange.getRequestURI().getQuery());
                Task subtask = taskManager.getSubTaskById(subtaskId);
                if (subtask != null) {
                    sendResponse(exchange, 200, new Gson().toJson(subtask));
                } else {
                    sendResponse(exchange, 404, "Подзадача не найдена");
                }
            } else if ("DELETE".equals(exchange.getRequestMethod())) {
                int subtaskId = getTaskIdFromQuery(exchange.getRequestURI().getQuery());
                taskManager.deleteSubTaskById(subtaskId);
                sendResponse(exchange, 200, "Подзадача успешно удалена");
            } else {
                sendResponse(exchange, 405, "Метод не разрешен");
            }
        }
    }

    private class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Task> history = taskManager.getHistory();
                sendResponse(exchange, 200, new Gson().toJson(history));
            } else {
                sendResponse(exchange, 405, "Метод не разрешен");
            }
        }
    }

    private int getTaskIdFromQuery(String query) {
        String[] params = query.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2 && "id".equals(keyValue[0])) {
                return Integer.parseInt(keyValue[1]);
            }
        }
        return -1;
    }

    private String getRequestString(HttpExchange exchange) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = exchange.getRequestBody().read(buffer)) != -1) {
            requestBody.append(new String(buffer, 0, bytesRead));
        }
        return requestBody.toString();
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Manager.getTaskDefault();
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
    }
}