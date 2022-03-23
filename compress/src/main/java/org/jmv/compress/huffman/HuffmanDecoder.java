package org.jmv.compress.huffman;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import org.jmv.compress.io.BitReader;

public class HuffmanDecoder {
    public static void decode(InputStream input, OutputStream output) {
        try {
            var bitReader = new BitReader(input);
            var root = bitReader.readTree();
            var size = bitReader.readBits(32);

            for (int i = 0; i < size; i++) {
                var n = root;

                while (!n.isLeafNode()) {
                    var b = bitReader.readBit();
                    if (b == 1) {
                        n = n.rightChild;
                    }
                    else {
                        n = n.leftChild;
                    }
                }
                output.write(n.symbol);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
