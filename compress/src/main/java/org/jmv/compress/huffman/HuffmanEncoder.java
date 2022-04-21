package org.jmv.compress.huffman;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import org.jmv.compress.io.BitWriter;

/**
 * Luokka joka säilöö Huffman-enkoodaukseen käytettävät funktiot.
 */
public class HuffmanEncoder {
    /**
     * Huffman-enkoodaa sisääntulon ulostuloon.
     *
     * @param input Enkoodattava sisääntulo.
     * @param output Ulostulo johon enkoodataan.
     */
    public static int encode(InputStream input, OutputStream output) {
        try {
            var counts = scanCounts(input);
            input.reset();

            int size = Arrays.stream(counts).sum();

            var ht = new HuffmanTable(counts);

            var bitWriter = new BitWriter(output);
            bitWriter.writeTree(ht.getRoot());
            bitWriter.writeBits(size, 32);

            int symbol = 0;
            while ((symbol = input.read()) != -1) {
                bitWriter.writeBits(ht.lookupCode(symbol), ht.lookupLength(symbol));
            }
            bitWriter.finish();
            return bitWriter.getBytesWritten();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Laskee symbolien esiintymät sisääntulosta.
     *
     * @param input Sisääntulo josta symbolien esiintymät lasketaan.
     *
     * @return Symbolien esiintymien lukumäärät taulukoituna.
     */
    private static int[] scanCounts(InputStream input) throws IOException {
        int[] counts = new int[256];

        int token = 0;
        while ((token = input.read()) != -1) {
            ++counts[token];
        }

        return counts;
    }
}
