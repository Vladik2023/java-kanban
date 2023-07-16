import Task.Task;
import Task.Epic;
import Task.SubTask;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private  int generateId = 0;

    private HashMap<Integer, Task> taskStorage = new HashMap<>();
    private HashMap<Integer, Epic> epicStorage = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskStorage = new HashMap<>();

    public Task createTask(Task task){
        int id = generateId();
        task.setId(id);
        taskStorage.put(id, task);
        return task;
    }
    public Epic createEpic(Epic epic){
        int id = generateId();
        epic.setId(id);
        epicStorage.put(id, epic);
        return epic;
    }
    public SubTask createSubTask(SubTask subTask){
        int id = generateId();
        subTask.setId(id);
        subTaskStorage.put(id, subTask);
        ArrayList<Integer> subTaskId = new ArrayList<>(getSubTaskByEpic(subTask.getEpicId()));

        epicStorage.get(subTask.getEpicId()).setSubTaskId(subTaskId);
        return subTask;
    }

    public ArrayList<Integer> getSubTaskByEpic(int epicId){
        ArrayList<Integer>  result = new ArrayList<>();
        for (SubTask value : subTaskStorage.values()) {
                if(value.getEpicId() == epicId){
                    result.add(value.getId());
                }
        }
        return result;
    }

    public Task getTaskById(int id){
        return taskStorage.get(id);
    }

    public ArrayList<Task> getAllTasks(){
        return new ArrayList(taskStorage.values());
    }
    public ArrayList<Epic> getAllEpic(){
        return new ArrayList(epicStorage.values());
    }
    public ArrayList<SubTask> getAllSubTask(){
        return new ArrayList(subTaskStorage.values());
    }

    public void updateTask(Task task){
        Task saved = taskStorage.get(task.getId());
        if (saved == null){
            return;
        }
        taskStorage.put(task.getId(), task);
    }
    public void updateEpic(Epic epic){
        Epic saved = epicStorage.get(epic.getId());
        if (saved == null){
            return;
        }
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }
    public void updateSubTask(SubTask subTask){
        Task saved = subTaskStorage.get(subTask.getId());
        if (saved == null){
            return;
        }
        subTaskStorage.put(subTask.getId(), subTask);
        epicStorage.get(subTask.getEpicId()).setStatus(checkStatusEpic(subTask.getEpicId()));

    }

    public String checkStatusEpic(int id){
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

    public void deleteTaskById(int id){
        taskStorage.remove(id);
    }

    public void deleteEpicById(int id){

        ArrayList<Integer> removeSubTask = new ArrayList<>(epicStorage.get(id).getSubTaskId());
        for (Integer subTaskId : removeSubTask) {
            subTaskStorage.remove(subTaskId);
        }
        epicStorage.remove(id);
    }

    public void deleteSubTaskById(int id){
        SubTask subTask = subTaskStorage.get(id);
        subTaskStorage.remove(id);
        ArrayList<Integer> subTaskId = new ArrayList<>(getSubTaskByEpic(subTask.getEpicId()));
        epicStorage.get(subTask.getEpicId()).setSubTaskId(subTaskId);
        epicStorage.get(subTask.getEpicId()).setStatus(checkStatusEpic(subTask.getEpicId()));
    }
    public void deleteAllTask(){
        taskStorage.clear();
    }
    public void deleteAllEpic(){
        epicStorage.clear();
        subTaskStorage.clear();
    }
    public void deleteAllSubTask(){
        subTaskStorage.clear();
        for (Epic value : epicStorage.values()) {
            value.setSubTaskId(null);
            value.setStatus("NEW");
        }
    }



    private  int generateId(){
        return ++generateId;
    }
}
