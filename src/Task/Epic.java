package Task;
import Task.SubTask;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTaskId;

    public Epic(String name, String description) {
        super(name, description);
        status = "NEW";
    }
    public ArrayList<Integer> getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(ArrayList<Integer> subTaskId) {
        this.subTaskId = subTaskId;
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
