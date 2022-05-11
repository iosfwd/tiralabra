package org.jmv.compress.lz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;

import org.jmv.compress.huffman.HuffmanCodebook;
import org.jmv.compress.huffman.HuffmanTable;
import org.jmv.compress.io.BitReader;
import org.jmv.compress.util.BinaryLogarithm;

/**
 * Luokka joka säilöö LZHuffman-dekoodaukseen käytettävät funktiot.
 */
public final class LZHuffmanDecoder {
    /**
     * LZHuffman-dekoodaa sisääntulon ulostuloon.
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
            final int windowLength = bitReader.readBits(32);
            final int minMatchLength = bitReader.readBits(32);
            final int maxMatchLength = bitReader.readBits(32);
            final int offsetBitLength = BinaryLogarithm.log2(windowLength + 1);
            final int matchLengthBitLength = BinaryLogarithm.log2(maxMatchLength - minMatchLength + 1);

            final int windowMask = windowLength - 1;

            final byte[] decoded = new byte[windowLength];

            int i = 0;
            int windowPosition = 0;
            final var bos = new BufferedOutputStream(output);

            while (i < size) {

                if (bitReader.readBit() == 0) {
                    bitReader.mark();
                    final int n = bitReader.readBits(depth);
                    final int symbol = codebook.decode(n);
                    final int len = codebook.getLength();

                    bitReader.reset();
                    bitReader.readBits(len);

                    bos.write(symbol);
                    ++i;

                    decoded[windowPosition] = (byte)symbol;
                    ++windowPosition;
                    windowPosition &= windowMask;
                } else {
                    final int offset = bitReader.readBits(offsetBitLength);
                    final int length = bitReader.readBits(matchLengthBitLength) + minMatchLength;
                    final int index = i - offset;

                    for (int j = 0; j < length; ++j) {
                        decoded[(windowPosition + j) & windowMask] = decoded[(index + j) & windowMask];
                        bos.write((int)decoded[(windowPosition + j) & windowMask]);
                    }
                    windowPosition += length;
                    windowPosition &= windowMask;

                    i += length;
                }
            }

            bos.flush();
            return size;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
