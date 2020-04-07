package ru.itmo.java;

import java.util.Arrays;


public class HashTable {
    private static final double BASE_LOAD_FACTOR = 0.5;

    private int capacity;
    private double loadFactor;
    private int threshold;
    private int size = 0;

    private HashNode[] arr;
    private boolean[] deleted;

    HashTable(int itemsNumber) {
        this(itemsNumber, BASE_LOAD_FACTOR);
    }

    HashTable(int itemsNumber, double loadFactor) {

        this.capacity = Math.max(100, itemsNumber);
        this.loadFactor = Math.max(0, Math.min(1, loadFactor));
        this.threshold = (int) (this.loadFactor * capacity);

        this.arr = new HashNode[capacity];
        this.deleted = new boolean[capacity];
    }

    public Object put(Object key, Object value) {
        HashNode node = new HashNode(key, value);
        int i = indexToTake(key);

        if (arr[i] == null) {
            i = indexToPut(key);
            deleted[i] = false;
            arr[i] = node;
            size++;

            if (size >= threshold) {
                this.resize();
            }

            return null;
        }

        HashNode oldElement = arr[i];
        arr[i] = node;
        return oldElement.value;
    }

    public Object get(Object key) {
        int i = indexToTake(key);

        if (arr[i] == null) {
            return null;
        }

        return arr[i].value;
    }

    public Object remove(Object key) {
        int index = indexToTake(key);

        if (arr[index] == null) {
            return null;
        }

        HashNode deletedElement = arr[index];
        deleted[index] = true;
        arr[index] = null;
        size--;

        return deletedElement.value;
    }

    public int size() {
        return this.size;
    }

    private int hashCode(Object key) {
        return (key.hashCode() % capacity + capacity) % capacity;
    }

    private int indexToTake(Object key) {
        int hash = hashCode(key);

        while (deleted[hash] || arr[hash] != null && !key.equals(arr[hash].key)) {
            hash++;
            hash = hash % arr.length;
        }

        return hash;
    }

    private int indexToPut(Object key) {
        int hash = hashCode(key);

        while (arr[hash] != null) {
            hash++;
            hash = hash % arr.length;
        }

        return hash;
    }

    private void resize() {
        size = 0;
        capacity *= 2;
        threshold = (int) (loadFactor * capacity);

        var oldArray = this.arr;
        arr = new HashNode[capacity];
        deleted = new boolean[capacity];

        for (HashNode element : oldArray) {
            if (element != null) {
                this.put(element.key, element.value);
            }
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(arr);
    }

    protected class HashNode {
        Object key;
        Object value;

        HashNode(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("key = %s; value = %s", key, value);
        }
    }
}
