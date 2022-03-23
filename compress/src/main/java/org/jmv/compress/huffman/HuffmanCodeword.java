package org.jmv.compress.huffman;

public class HuffmanCodeword {
    public int code;
    public int length;

    public HuffmanCodeword() {
        code = 0;
        length = 0;
    }

    public HuffmanCodeword(int code, int length) {
        this.code = code;
        this.length = length;
    }
}
