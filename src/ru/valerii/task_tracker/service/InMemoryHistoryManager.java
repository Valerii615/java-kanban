package ru.valerii.task_tracker.service;

import ru.valerii.task_tracker.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> browsingHistory = new CustomLinkedList<>();
    private final Map<Integer, Node<Task>> listNodes = new HashMap<>();

    @Override
    public void add(Task task) {
        Node<Task> node = new Node<>(null, task, null);
        if(listNodes.containsKey(task.getId())) {
            browsingHistory.removeNode(listNodes.get(task.getId()));
        }
        browsingHistory.linkLast(node);
        listNodes.put(task.getId(), node);

    }

    @Override
    public List<Task> getHistory() {
        return browsingHistory.getTasks();
    }

    @Override
    public void remove(int id) {
        if(listNodes.containsKey(id)) browsingHistory.removeNode(listNodes.get(id));
    }

    private static class CustomLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;

        /**
         * добавление узла в конец списка
         */
        void linkLast(Node<T> node) {
            final Node<T> oldTail = tail;
            node.prev = oldTail;
            tail = node;
            if (oldTail == null) {
                head = node;
            } else {
                oldTail.next = node;
            }
        }

        /**
         * получение списка задачь из свсясзанного списка
         *
         */

        List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node<T> node = head;
            while (node != null) {
                tasks.add((Task) node.task);
                node = node.next;
            }
            return tasks;
        }

        /**
         * удаление из связанного списка
         *
         */
        void removeNode(Node<T> node) {
            Node<T> prevNode = node.prev;
            Node<T> nextNode = node.next;
            if (prevNode == null && nextNode == null) {
                head = null;
                tail = null;
            } else if (prevNode == null) {
                head = nextNode;
                nextNode.prev = null;
            } else if (nextNode == null) {
                tail = prevNode;
                prevNode.next = null;
            } else {
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
            }
        }
    }



    /**
     * класс описывающий узел кастомного списка
     *
     */
    private static class Node<T> {
        public Node<T> prev;// предыдущий узел
        public T task;// задача хранящаяся в узле
        public Node<T> next;// следующий узел

        public Node(Node<T> prev, T task, Node<T> next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }
    }
}
