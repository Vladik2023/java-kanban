import service.Manager;
import service.TaskManager;
import task.Epic;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Manager.getTaskDefault();
        Epic epic = new Epic("Epic 1", "d1");
        taskManager.createEpic(epic);
        taskManager.getEpicById(epic.getId());

        System.out.println(taskManager.getHistory());
    }
}
