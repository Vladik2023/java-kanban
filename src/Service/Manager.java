package Service;

public final class Manager {
    private Manager() {
    }
    public static TaskManager getTaskDefault(){
        return new InMemoryTaskManager();
    }
    public static HistoryManager getHistoryDefault(){
        return new InMemoryHistoryManager();
    }
}
