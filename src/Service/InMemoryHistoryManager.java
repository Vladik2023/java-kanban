package Service;
import Task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_SIZE = 10;

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
        CustomLinkedList.Node newNode = taskList.linkFirst(task);
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
        private int size;

        public Node linkFirst(Task task) {
            Node newNode = new Node(task);
            if (first == null) {
                last = newNode;
            } else {
                first.prev = newNode;
                newNode.next = first;
            }
            first = newNode;
            increaseSize(); // Увеличить значение size
            trimToSize(); // Обрезать список до HISTORY_SIZE
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
        private void increaseSize() {
            size++;
        }

        private void trimToSize() {
            if (size > HISTORY_SIZE) {
                Node current = last;
                for (int i = 0; i < size - HISTORY_SIZE; i++) {
                    Node prev = current.prev;
                    if (prev != null) {
                        prev.next = null;
                    }
                    current.prev = null;
                    current = prev;
                }
                last = current;
                size = HISTORY_SIZE;
            }
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
