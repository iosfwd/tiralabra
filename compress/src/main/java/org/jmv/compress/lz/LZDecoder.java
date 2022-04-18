package org.jmv.compress.lz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jmv.compress.io.BitReader;
import org.jmv.compress.util.BinaryLogarithm;

/**
 * Luokka joka säilöö LZ-dekoodaukseen käytettävät funktiot.
 */
public class LZDecoder {
    /**
     * LZ-dekoodaa sisääntulon ulostuloon.
     *
     * @param input Dekoodattava sisääntulo.
     * @param output Ulostulo johon dekoodataan.
     */
    public static int decode(InputStream input, OutputStream output) {
        try {
            var bitReader = new BitReader(input);
            int size = bitReader.readBits(32);
            int windowLength = bitReader.readBits(32);
            int minMatchLength = bitReader.readBits(32);
            int maxMatchLength = bitReader.readBits(32);
            int offsetBitLength = BinaryLogarithm.log2(windowLength + 1);
            int matchLengthBitLength = BinaryLogarithm.log2(maxMatchLength - minMatchLength + 1);


            int windowMask = windowLength - 1;

            byte[] decoded = new byte[windowLength];

            int i = 0;
            int r = 0;
            while (i < size) {

                if (bitReader.readBit() == 0) {
                    int symbol = bitReader.readBits(8);
                    output.write(symbol);
                    ++i;

                    decoded[r] = (byte)symbol;
                    ++r;
                    r &= windowMask;
                } else {
                    int offset = bitReader.readBits(offsetBitLength);
                    int length = bitReader.readBits(matchLengthBitLength) + minMatchLength;
                    int index = i - offset;

                    for (int j = 0; j < length; ++j) {
                        decoded[(r + j) & windowMask] = decoded[(index + j) & windowMask];
                        output.write((int)decoded[(r + j) & windowMask]);
                    }
                    r += length;
                    r &= windowMask;

                    i += length;
                }
            }

            return size;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
