package org.jmv.compress.lz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jmv.compress.io.BitReader;

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

            byte[] decoded = new byte[size];

            int i = 0;
            while (i < size) {

                if (bitReader.readBit() == 0) {
                    int symbol = bitReader.readBits(8);
                    decoded[i] = (byte)symbol;
                    ++i;
                } else {
                    int offset = bitReader.readBits(12);
                    int length = bitReader.readBits(9);
                    int index = i - offset;

                    for (int j = 0; j < length; ++j) {
                        decoded[i + j] = decoded[index + j];
                    }
                    i += length;
                }
            }
            output.write(decoded);

            return size;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
