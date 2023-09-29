package task;

import service.TaskType;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Task {

    protected Integer id;
    protected String name;
    protected String description;
    protected String status;
    protected TaskType type;
    protected Long duration;
    protected LocalDateTime startTime;
    private static int count = 0;


    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
        this.id = generateId();
        this.type = TaskType.TASK;
    }

    public Task(String name, String description, Long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
        this.id = generateId();
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

    private Integer generateId() {
        return ++count;
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Task other = (Task) obj;
        return Objects.equals(id, other.id) &&
                Objects.equals(name, other.name) &&
                Objects.equals(description, other.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }
}
