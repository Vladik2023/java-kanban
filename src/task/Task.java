package task;

import service.TaskType;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Task {

    protected Integer id;
    protected String name;
    protected String description;
    protected String status;
    protected TaskType type;
    protected Integer duration;
    protected Date startTime;
    private static int count = 0;


    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
        this.id = generateId();
        this.type = TaskType.TASK;
    }

    public Task(String name, String description, Integer duration, Date startTime) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
        this.id = generateId();
        this.type = TaskType.TASK;
        this.duration = duration;
        this.startTime = startTime;
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MINUTE, duration);
        return calendar.getTime();
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
