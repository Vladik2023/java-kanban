package task;

import service.TaskType;

import java.time.LocalDateTime;

public class Task {
    private Integer id;
    private String name;
    private String status;
    private String description;


    public void setType(TaskType type) {
        this.type = type;
    }

    protected TaskType type;
    private Long duration;
    private LocalDateTime startTime;


    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.type = TaskType.TASK;
        this.status = "NEW";
    }

    public Task(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = "NEW";
        this.type = TaskType.TASK;
    }

    public Task(String name, String description, Long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
        this.type = TaskType.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, Long duration, LocalDateTime startTime, String status) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        this.status = status;
        this.type = TaskType.TASK;
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
        return startTime != null ? startTime.plusMinutes(duration) : null;
    }

    public TaskType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + this.id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", startTime=" + startTime +
                '}';
    }
}
