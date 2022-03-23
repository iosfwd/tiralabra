package org.jmv.compress.io;

import java.io.OutputStream;
import java.io.IOException;
import org.jmv.compress.huffman.HuffmanNode;

public class BitWriter {
    private OutputStream output;
    private int buffer;
    private int currentPosition;
    private final int bufferSize = 8;

    public BitWriter(OutputStream output) {
        this.output = output;
        buffer = 0;
        currentPosition = 0;
    }

    public void writeBit(int b) throws IOException {
        b &= 1;
        buffer <<= 1;
        buffer |= b;

        ++currentPosition;

        if (currentPosition == bufferSize) {
            flush();
        }
    }

    public void writeBits(int x, int length) throws IOException {
        for (int i = 0; i < length; ++i) {
            int b = (x >>> (length - 1 - i)) & 1;
            writeBit(b);
        }
    }

    public void flush() throws IOException {
        if (currentPosition == 0) {
            return;
        }

        buffer = (buffer << (8 - currentPosition));
        output.write(buffer);

        buffer = 0;
        currentPosition = 0;
    }

    public void finish() throws IOException {
        flush();
        output.flush();
    }

    public void close() throws IOException {
        finish();
        output.close();
    }

    public void writeTree(HuffmanNode n) throws IOException {
        if (n.isLeafNode()) {
            writeBit(1);
            writeBits(n.symbol, 8);
            return;
        }
        writeBit(0);
        writeTree(n.leftChild);
        writeTree(n.rightChild);
    }

}
