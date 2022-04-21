package org.jmv.compress.huffman;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jmv.compress.io.BitReader;

/**
 * Luokka joka säilöö Huffman-dekoodaukseen käytettävät funktiot.
 */
public class HuffmanDecoder {
    /**
     * Huffman-dekoodaa sisääntulon ulostuloon.
     *
     * @param input Dekoodattava sisääntulo.
     * @param output Ulostulo johon dekoodataan.
     */
    public static int decode(InputStream input, OutputStream output) {
        try {
            var bitReader = new BitReader(input);

            var ht = new HuffmanTable(bitReader.readTree());
            var codebook = new HuffmanCodebook(ht.getTable());

            var size = bitReader.readBits(32);
            for (int i = 0; i < size; ++i) {
                bitReader.mark();
                int n = bitReader.readBits(codebook.getDepth());
                int symbol = codebook.decode(n);
                int len = codebook.getLength();

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
