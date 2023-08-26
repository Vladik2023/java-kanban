package Task;
import Service.TaskType;
import Task.SubTask;

import java.awt.*;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTaskId;

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.EPIC;
        this.subTaskId = new ArrayList<>();
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
