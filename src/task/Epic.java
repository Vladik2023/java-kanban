package task;
import service.InMemoryTaskManager;
import service.Manager;
import service.TaskManager;
import service.TaskType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Epic extends Task {

    private ArrayList<Integer> subTaskId;
    private Integer duration;
    private Date startTime;
    private Date endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.EPIC;
        this.subTaskId = new ArrayList<>();
    }

    public Epic(String name, String description, Integer duration, Date startTime) {
        super(name, description);
        this.type = TaskType.EPIC;
        this.subTaskId = new ArrayList<>();
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = calculateEndTime();
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public ArrayList<Integer> getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(ArrayList<Integer> subTaskId) {
        this.subTaskId = subTaskId;
    }

    public void addSubtaskId(int id){
        subTaskId.add(id);
    }

    private Date calculateEndTime() {
        Date latestEndTime = null;
        for (Integer subTaskId : subTaskId) {
            TaskManager taskManager = Manager.getTaskDefault();
            Task subTask = taskManager.getSubTaskById(subTaskId);
            Date subTaskEndTime = subTask.getEndTime();
            if (latestEndTime == null || subTaskEndTime.after(latestEndTime)) {
                latestEndTime = subTaskEndTime;
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MINUTE, duration);
        Date epicEndTime = calendar.getTime();
        if (latestEndTime != null && latestEndTime.after(epicEndTime)) {
            return latestEndTime;
        } else {
            return epicEndTime;
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTaskId=" + subTaskId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
