package Task;

import Service.TaskType;

public class Task {

    protected Integer id;
    protected String name;
    protected String description;
    protected String status;
    protected TaskType type;
    private static int count = 0;


    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
        this.id = generateId();
        this.type = TaskType.TASK;
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
}
