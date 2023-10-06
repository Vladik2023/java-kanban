package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.LocalDateTimeAdapter;

import java.io.File;
import java.time.LocalDateTime;

public final class Manager {
    private Manager() {
    }

    public static FileBackendTaskManager getFileBackendTaskManagerDefault() {
        return new FileBackendTaskManager();
    }

    public static FileBackendTaskManager getFileBackendTaskManagerDefault(File file) {
        return new FileBackendTaskManager(file);
    }

    public static TaskManager getTaskDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getHistoryDefault() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}
