package org.jmv.compress.huffman;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jmv.compress.io.BitReader;

/**
 * Luokka joka säilöö Huffman-dekoodaukseen käytettävät funktiot.
 */
public final class HuffmanDecoder {
    /**
     * Huffman-dekoodaa sisääntulon ulostuloon.
     *
     * @param input Dekoodattava sisääntulo.
     * @param output Ulostulo johon dekoodataan.
     */
    public final static int decode(InputStream input, OutputStream output) {
        try {
            final var bitReader = new BitReader(input);

            final var ht = new HuffmanTable(bitReader.readTree());
            final var codebook = new HuffmanCodebook(ht.getTable());
            final int depth = codebook.getDepth();
            final int size = bitReader.readBits(32);

            for (int i = 0; i < size; ++i) {
                bitReader.mark();
                final int n = bitReader.readBits(depth);
                final int symbol = codebook.decode(n);
                final int len = codebook.getLength();

                bitReader.reset();
                bitReader.readBits(len);

                output.write(symbol);
            }

            return size;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
