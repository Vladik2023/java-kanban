package Service;
import Task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    //private static final int HISTORY_SIZE = 10;
    //private final LinkedList<Task> history = new LinkedList<>();
    private CustomLinkedList taskList = new CustomLinkedList();
    private HashMap<Integer, CustomLinkedList.Node> taskMap = new HashMap<>();


    @Override
    public List<Task> getHistory() {
        return taskList.getTasks();
    }

    @Override
    public void addTask(Task task) {
        int taskId = task.getId();
        if (taskMap.containsKey(taskId)) {
            CustomLinkedList.Node node = taskMap.get(taskId);
            removeNode(node);
        }
        CustomLinkedList.Node newNode = taskList.linkLast(task);
        taskMap.put(taskId, newNode);
    }

    private void removeNode(CustomLinkedList.Node node) {
        CustomLinkedList.Node prev = node.prev;
        CustomLinkedList.Node next = node.next;

        if (prev != null) {
            prev.next = next;
        } else {
            taskList.setFirst(next);
        }

        if (next != null) {
            next.prev = prev;
        } else {
            taskList.setLast(prev);
        }
    }

    @Override
    public void remove(int id) {
        CustomLinkedList.Node node = taskMap.get(id);
        if (node != null) {
            removeNode(node);
            taskMap.remove(id);
        }
    }

    private class CustomLinkedList {
        private Node first;
        private Node last;

        public Node linkLast(Task task) {
            Node newNode = new Node(task);
            if (last == null) {
                first = newNode;
            } else {
                last.next = newNode;
                newNode.prev = last;
            }
            last = newNode;
            return newNode;
        }

        public List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node current = first;
            while (current != null) {
                tasks.add(current.task);
                current = current.next;
            }
            return tasks;
        }

        private class Node {
            private Task task;
            private Node prev;
            private Node next;

            public Node(Task task) {
                this.task = task;
            }
        }

        public void setFirst(Node first) {
            this.first = first;
        }

        public void setLast(Node last) {
            this.last = last;
        }
    }
}
