package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.HttpTaskManager;
import http.LocalDateTimeAdapter;

import java.time.LocalDateTime;

public final class Manager {
    private Manager() {
    }
    public static FileBackendTaskManager getFileBackendTaskManagerDefault(){
        return new FileBackendTaskManager();
    }
    public static TaskManager getTaskDefault(){
        return new InMemoryTaskManager();
    }
    public static HistoryManager getHistoryDefault(){
        return new InMemoryHistoryManager();
    }
    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}
