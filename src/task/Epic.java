package task;
import service.InMemoryTaskManager;
import service.Manager;
import service.TaskManager;
import service.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Epic extends Task {

    private ArrayList<Integer> subTaskId;
    private Long duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.EPIC;
        this.subTaskId = new ArrayList<>();
    }

    public Epic(String name, String description, Long duration, LocalDateTime startTime) {
        super(name, description);
        this.type = TaskType.EPIC;
        this.subTaskId = new ArrayList<>();
        this.duration = duration;
        this.startTime = startTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
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
