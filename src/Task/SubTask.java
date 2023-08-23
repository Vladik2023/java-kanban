package Task;

import Service.TaskType;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description,Integer epicId) {
        super(name, description);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
