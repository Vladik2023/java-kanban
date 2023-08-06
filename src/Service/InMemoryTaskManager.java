package Service;

import Task.Task;
import Task.Epic;
import Task.SubTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private HashMap<Integer, Task> taskStorage = new HashMap<>();
    private HashMap<Integer, Epic> epicStorage = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskStorage = new HashMap<>();
    private final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    @Override
    public Task createTask(Task task){
        int id = task.getId();
        task.setId(id);
        taskStorage.put(id, task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic){
        int id = epic.getId();
        epic.setId(id);
        epicStorage.put(id, epic);
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask){
        int id = subTask.getId();
        subTask.setId(id);
        subTaskStorage.put(id, subTask);
        ArrayList<Integer> subTaskId = new ArrayList<>(getSubTaskByEpic(subTask.getEpicId()));

        epicStorage.get(subTask.getEpicId()).setSubTaskId(subTaskId);
        return subTask;
    }

    @Override
    public ArrayList<Integer> getSubTaskByEpic(int epicId){
        ArrayList<Integer>  result = new ArrayList<>();
        for (SubTask value : subTaskStorage.values()) {
                if(value.getEpicId() == epicId){
                    result.add(value.getId());
                    getSubTaskById(value.getId());
                }
        }
        return result;
    }

    @Override
    public Task getTaskById(int id){
        historyManager.addTask(taskStorage.get(id));
        return taskStorage.get(id);
    }

    @Override
    public Task getEpicById(int id){
        historyManager.addTask(epicStorage.get(id));
        return epicStorage.get(id);
    }

    @Override
    public Task getSubTaskById(int id){
        historyManager.addTask(subTaskStorage.get(id));
        return subTaskStorage.get(id);
    }

    @Override
    public ArrayList<Task> getAllTasks(){
        for (Integer id : taskStorage.keySet()) {
            getTaskById(id);
        }
        return new ArrayList(taskStorage.values());
    }

    @Override
    public ArrayList<Epic> getAllEpic(){
        for (Integer id : epicStorage.keySet()) {
            getEpicById(id);
        }
        return new ArrayList(epicStorage.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTask(){
        for (Integer id : subTaskStorage.keySet()) {
            getSubTaskById(id);
        }
        return new ArrayList(subTaskStorage.values());
    }

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }

    @Override
    public void updateTask(Task task){
        Task saved = taskStorage.get(task.getId());
        if (saved == null){
            return;
        }
        taskStorage.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic){
        Epic saved = epicStorage.get(epic.getId());
        if (saved == null){
            return;
        }
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubTask(SubTask subTask){
        Task saved = subTaskStorage.get(subTask.getId());
        if (saved == null){
            return;
        }
        subTaskStorage.put(subTask.getId(), subTask);
        epicStorage.get(subTask.getEpicId()).setStatus(checkStatusEpic(subTask.getEpicId()));

    }


    private String checkStatusEpic(int id){
        boolean isNew = false;
        boolean isDone = false;
        for (Integer subTaskId : getSubTaskByEpic(id)) {
            if(subTaskStorage.get(subTaskId).getStatus().equals("IN_PROGRESS")){
                return "IN_PROGRESS";
            }else if(subTaskStorage.get(subTaskId).getStatus().equals("NEW")){
                isNew = true;
            }   else if(subTaskStorage.get(subTaskId).getStatus().equals("DONE")){
                isDone = true;
            }
        }
        if(isDone && isNew){
            return "IN_PROGRESS";
        }else if(!isNew && isDone){
            return "DONE";
        }else{
            return "NEW";
        }
    }

    @Override
    public void deleteTaskById(int id){
        taskStorage.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id){

        ArrayList<Integer> removeSubTask = new ArrayList<>(epicStorage.get(id).getSubTaskId());
        for (Integer subTaskId : removeSubTask) {
            subTaskStorage.remove(subTaskId);
            historyManager.remove(subTaskId);
        }
        epicStorage.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id){
        SubTask subTask = subTaskStorage.get(id);
        subTaskStorage.remove(id);
        historyManager.remove(id);
        ArrayList<Integer> subTaskId = new ArrayList<>(getSubTaskByEpic(subTask.getEpicId()));
        epicStorage.get(subTask.getEpicId()).setSubTaskId(subTaskId);
        epicStorage.get(subTask.getEpicId()).setStatus(checkStatusEpic(subTask.getEpicId()));
    }

    @Override
    public void deleteAllTask(){
        for (Integer id : taskStorage.keySet()) {
            historyManager.remove(id);
        }
        taskStorage.clear();
    }

    @Override
    public void deleteAllEpic(){
        for (Integer id : epicStorage.keySet()) {
            historyManager.remove(id);
        }
        for (Integer id : subTaskStorage.keySet()) {
            historyManager.remove(id);
        }
        epicStorage.clear();
        subTaskStorage.clear();
    }

    @Override
    public void deleteAllSubTask(){
        for (Integer id : subTaskStorage.keySet()) {
            historyManager.remove(id);
        }
        subTaskStorage.clear();
        for (Epic value : epicStorage.values()) {
            value.setSubTaskId(null);
            value.setStatus("NEW");
        }
    }


}
