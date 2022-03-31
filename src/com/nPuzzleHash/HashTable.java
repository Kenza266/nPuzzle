package com.nPuzzleHash;

public class HashTable {

    private static final int DEFAULT_MAX_SIZE = 362897;
    private final HashNode[] table;
    private final int maxSize;
    private int size;

    public static class HashNode {
        final private int value;
        private final HashNode nextNode;

        public HashNode(int value, HashNode nextNode) {
            this.value = value;
            this.nextNode = nextNode;
        }
        public int getValue() {
            return this.value;
        }
        public HashNode getNextNode() {
            return this.nextNode;
        }
    }

    public HashTable(int maxSize) {
        this.table = new HashNode[maxSize];
        this.maxSize = maxSize;
        this.size = 0;
    }

    public HashTable() {
        this(DEFAULT_MAX_SIZE);
    }

    public int hash(int value) {
        return value % this.maxSize;
    }

    public boolean notIn(int value) {
        int key = hash(value);
        HashNode head = table[key];
        while (head != null) {
            if (head.getValue() == value)
                return false;
            head = head.getNextNode();
        }
        return true;
    }

    public void add(int value) {
        int key = hash(value);
        HashNode head = table[key];
        HashNode newNode = new HashNode(value, head);
        table[key] = newNode;
        this.size = this.size + 1;
    }

    public void display() {
        HashNode head;
        System.out.println("Key\t\tValues");
        for (int i = 0; i<this.maxSize; i++) {
            head = table[i];
            System.out.print(i + "\t\t");
            while(head != null) {
                System.out.print(head.getValue() + " --> ");
                head = head.getNextNode();
            }
            System.out.print("\n");
        }
    }
    public int getSize() { return this.size; }
    public boolean isEmpty() { return this.size == 0; }

}
