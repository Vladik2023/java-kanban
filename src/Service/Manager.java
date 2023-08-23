package Service;

import java.io.File;

public final class Manager {
    private Manager() {
    }
    public static TaskManager getTaskDefault(File file){
        return new FileBackendTaskManager(file);
    }
    public static HistoryManager getHistoryDefault(){
        return new InMemoryHistoryManager();
    }
}
