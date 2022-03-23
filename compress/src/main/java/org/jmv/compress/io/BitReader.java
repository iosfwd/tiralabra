package org.jmv.compress.io;

import java.io.InputStream;
import java.io.IOException;

import org.jmv.compress.huffman.HuffmanNode;

public class BitReader {
    private InputStream input;
    private int buffer;
    private int currentPosition;
    private final int bufferSize = 8;
    private boolean eof = false;

    public BitReader(InputStream input) throws IOException {
        this.input = input;

        fillBuffer();
    }

    public int readBit() throws IOException {
        --currentPosition;
        int b = ((buffer >>> currentPosition) & 1);

        if (currentPosition == 0) {
            fillBuffer();
        }

        return b;
    }

    public int readBits(int length) throws IOException {
        int x = 0;

        for (int i = 0; i < length; ++i) {
            x |= (readBit() << (length - 1 - i));
        }

        return x;
    }

    private void fillBuffer() throws IOException {
        var b = input.read();

        if (b == -1) {
            eof = true;
            return;
        }

        buffer = b;
        currentPosition = 8;
    }

    public void close() throws IOException {
        input.close();
    }

    public boolean available() {
        return !eof;
    }

    public HuffmanNode readTree() throws IOException {
        var leaf = readBit();
        if (leaf == 1) {
            return new HuffmanNode(readBits(8), -1, null, null, null);
        } else {
            return new HuffmanNode(-1, -1, null, readTree(), readTree());
        }
    }
}
