package org.jmv.compress.huffman;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.util.Arrays;
import java.util.Comparator;

import org.jmv.compress.io.BitWriter;

/**
 * Luokka joka säilöö Huffman-enkoodaukseen käytettävät funktiot.
 */
public final class HuffmanEncoder {
    /**
     * Huffman-enkoodaa sisääntulon ulostuloon.
     *
     * @param input Enkoodattava sisääntulo.
     * @param output Ulostulo johon enkoodataan.
     */
    public final static int encode(InputStream input, OutputStream output) {
        try {
            final var counts = scanCounts(input);
            input.reset();

            final int size = Arrays.stream(counts).sum();

            final var ht = new HuffmanTable(counts);

            final var bitWriter = new BitWriter(output);
            bitWriter.writeTree(ht.getRoot());
            bitWriter.writeBits(size, 32);

            final var bis = new BufferedInputStream(input);

            int symbol = 0;
            while ((symbol = bis.read()) != -1) {
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
    private final static int[] scanCounts(InputStream input) throws IOException {
        final int[] counts = new int[256];

        final var bis = new BufferedInputStream(input);

        int token = 0;
        while ((token = bis.read()) != -1) {
            ++counts[token];
        }

        return counts;
    }
}
