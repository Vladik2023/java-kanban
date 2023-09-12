package service;

import java.io.File;

public final class Manager {
    private Manager() {
    }
    public static TaskManager getTaskDefault(File file){
        return new FileBackendTaskManager(file);
    }
    public static TaskManager getTaskDefault(){
        return new InMemoryTaskManager();
    }
    public static HistoryManager getHistoryDefault(){
        return new InMemoryHistoryManager();
    }
}
