package org.jmv.compress.huffman;

public class HuffmanNode implements Comparable<HuffmanNode> {
    public int symbol;
    public int count;
    public HuffmanNode next;
    public HuffmanNode leftChild;
    public HuffmanNode rightChild;

    public HuffmanNode() {
        this.symbol = 0;
        this.count = 0;
        this.next = null;
        this.leftChild = null;
        this.rightChild = null;
    }

    public HuffmanNode(int symbol, int count, HuffmanNode next, HuffmanNode leftChild, HuffmanNode rightChild) {
        this.symbol = symbol;
        this.count = count;
        this.next = next;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public boolean isLeafNode() {
        return (leftChild == null && rightChild == null);
    }

    @Override
    public int compareTo(HuffmanNode hn) {
        return Integer.compare(count, hn.count);
    }
}
