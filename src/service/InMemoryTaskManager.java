package service;

import task.Task;
import task.Epic;
import task.SubTask;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int newId = 0;
    protected HashMap<Integer, Task> taskStorage = new HashMap<>();
    protected HashMap<Integer, Epic> epicStorage = new HashMap<>();
    protected HashMap<Integer, SubTask> subTaskStorage = new HashMap<>();
    protected InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())));

    @Override
    public Task createTask(Task task) {
        for (Task existingTask : prioritizedTasks) {
            if (isTaskIntersecting(existingTask, task)) {
                throw new IllegalArgumentException("Задача пересекается с другой задачей: " + existingTask.getName());
            }
        }
        int id = ++newId;
        task.setId(id);
        taskStorage.put(id, task);
        prioritizedTasks.add(task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic){
        int id = epic.getId();
        epic.setId(id);
        epicStorage.put(id, epic);
        prioritizedTasks.add(epic);
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask){
        for (Task existingTask : prioritizedTasks) {
            if (isTaskIntersecting(existingTask, subTask)) {
                throw new IllegalArgumentException("Подзадача пересекается с другой задачей: " + existingTask.getName());
            }
        }
        subTaskStorage.put(subTask.getId(), subTask);
        Epic epic = epicStorage.get(subTask.getEpicId());
        epic.addSubtaskId(subTask.getId());
        prioritizedTasks.add(subTask);
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
    public Task getTaskById(int id) {
        Task task = taskStorage.get(id);
        if (task != null) {
            historyManager.addTask(task);
        }
        return task;
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
        return new ArrayList(taskStorage.values());
    }

    @Override
    public ArrayList<Epic> getAllEpic(){
        return new ArrayList(epicStorage.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTask(){
        return new ArrayList(subTaskStorage.values());
    }

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }

    @Override
    public void updateTask(Task task){
        for (Task existingTask : prioritizedTasks) {
            if (!existingTask.equals(task) && isTaskIntersecting(existingTask, task)) {
                throw new IllegalArgumentException("Задача пересекается с другой задачей: " + existingTask.getName());
            }
        }
        Task saved = taskStorage.get(task.getId());
        if (saved == null){
            return;
        }
        taskStorage.put(task.getId(), task);
        prioritizedTasks.remove(saved);
        prioritizedTasks.add(task);
    }

    @Override
    public void updateEpic(Epic epic){
        Epic saved = epicStorage.get(epic.getId());
        if (saved == null){
            return;
        }
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
        prioritizedTasks.remove(saved);
        prioritizedTasks.add(epic);
    }

    @Override
    public void updateSubTask(SubTask subTask){
        for (Task existingTask : prioritizedTasks) {
            if (!existingTask.equals(subTask) && isTaskIntersecting(existingTask, subTask)) {
                throw new IllegalArgumentException("Подзадача пересекается с другой задачей: " + existingTask.getName());
            }
        }
        Task saved = subTaskStorage.get(subTask.getId());
        if (saved == null){
            return;
        }
        subTaskStorage.put(subTask.getId(), subTask);
        epicStorage.get(subTask.getEpicId()).setStatus(checkStatusEpic(subTask.getEpicId()));
        prioritizedTasks.remove(saved);
        prioritizedTasks.add(subTask);
    }

    private boolean isTaskIntersecting(Task task1, Task task2) {
        LocalDateTime startTime1 = task1.getStartTime();
        LocalDateTime endTime1 = task1.getEndTime();
        LocalDateTime startTime2 = task2.getStartTime();
        LocalDateTime endTime2 = task2.getEndTime();

        return startTime1 != null && startTime2 != null && endTime1 != null && endTime2 != null &&
                startTime1.isBefore(endTime2) && endTime1.isAfter(startTime2);
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

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
}
