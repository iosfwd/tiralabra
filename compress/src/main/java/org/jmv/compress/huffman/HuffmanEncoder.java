package org.jmv.compress.huffman;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Arrays;

import org.jmv.compress.io.BitWriter;

public class HuffmanEncoder {
    public static void encode(InputStream input, OutputStream output) {
        try {
            var counts = scanCounts(input);

            int size = 0;
            for (int i = 0; i < 256; i++) {
                size += counts[i];
            }

            var root = buildTree(counts);
            var codes = buildCodesFromTree(root);

            var bitWriter = new BitWriter(output);
            bitWriter.writeTree(root);
            bitWriter.writeBits(size, 32);

            input.reset();
            int token = 0;
            while ((token = input.read()) != -1) {
                bitWriter.writeBits(codes[token].code, codes[token].length);
            }
            bitWriter.finish();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] scanCounts(InputStream input) throws IOException {
        int[] counts = new int[256];

        int token = 0;

        while ((token = input.read()) != -1) {
            counts[token] += 1;
        }

        return counts;
    }

    public static HuffmanNode buildTree(int[] counts) {
        int elems = 0;
        for (int i = 0; i < 256; ++i) {
            if (counts[i] > 0) {
                ++elems;
            }
        }

        HuffmanNode[] alphabet = new HuffmanNode[(2 * elems) - 1];

        int j = 0;
        for (int i = 0; i < 256; ++i) {
            if (counts[i] > 0) {
                alphabet[j++] = new HuffmanNode(i, counts[i], null, null, null);
            }
        }

        Arrays.sort(alphabet, 0, elems, new Comparator<HuffmanNode>() {
                public int compare(HuffmanNode n1, HuffmanNode n2) {
                    return n1.compareTo(n2);
                }
            });

        for (int i = 0; i < elems - 1; ++i) {
            alphabet[i].next = alphabet[i+1];
        }


        alphabet[elems - 1].next = null;

        var I = alphabet[0];

        while (alphabet[0].next != null) {
            var N = new HuffmanNode(-1, alphabet[0].count + alphabet[0].next.count,
                                    null, alphabet[0], alphabet[0].next);

            while (I.next != null && I.next.count <= N.count) {
                I = I.next;
            }

            N.next = I.next;
            I.next = N;
            alphabet[0] = alphabet[0].next.next;
        }

        return alphabet[0];
    }

    public static HuffmanCodeword[] buildCodesFromTree(HuffmanNode root) {
        HuffmanCodeword[] codes = new HuffmanCodeword[256];

        buildCodes(root, codes, 0, 0);

        return codes;
    }

    public static void buildCodes(HuffmanNode n, HuffmanCodeword[] codes, int code, int depth) {
        if (!n.isLeafNode()) {
            buildCodes(n.leftChild, codes, (code << 1), depth + 1);
            buildCodes(n.rightChild, codes, (code << 1) | 1 , depth + 1);
        } else {
            codes[n.symbol] = new HuffmanCodeword(code, depth);
        }
    }
}
