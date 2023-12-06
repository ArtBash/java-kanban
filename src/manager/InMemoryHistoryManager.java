package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager{

    private class Node {
        Task task;
        Node prev;
        Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

            @Override
            public String toString() {
                return "Node{" +
                        "task name=" + task.getName() +
                        ", task description=" + task.getDescription() +
                        ", task status=" + task.getStatus() +
                        '}';
            }
    }

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    private ArrayList<Task> getTasks() {
         ArrayList<Task> tasks = new ArrayList<>();
        for(Node x = first; x != null; x = x.next) {
            tasks.add(x.task);
        }
         return tasks;
    }

    private Node linkLast(Task task) {
        final Node l = last;
        final Node newNode = new Node(task, l, null);
        last = newNode;
        if(l == null) {
            first = newNode;
        } else {
            last.prev = l;
            l.next = newNode;
        }
        return newNode;
    }

    @Override
    public void add(Task task) {
        Integer taskId = (Integer) task.getId();
        Node node = nodeMap.get(taskId);
        removeNode(node);
        Node newNode = linkLast(task);
        nodeMap.put(taskId, newNode);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void removeNode(Node node) {
        if(node != null) {
            if(node.prev == null) {
                first  = node.next;
                node.next.prev = null;
            } else if (node.prev != null && node.next != null) {
                node.next.prev = node.prev;
                node.prev.next = node.next;
            } else if (node.next == null) {
                last = node.prev;
                node.prev.next = null;
            }
        }
    }

    @Override
    public void remove(int id) {
        Integer taskId = (Integer) id;
        Node node = nodeMap.remove(taskId);
        removeNode(node);
    }
}
