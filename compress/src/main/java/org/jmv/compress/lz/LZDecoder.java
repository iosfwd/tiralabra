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
    public static void decode(InputStream input, OutputStream output) {

        String decoded = "";

        try {
            var bitReader = new BitReader(input);

            while (bitReader.available()) {

                int offset = bitReader.readBits(12);
                int length = bitReader.readBits(12);
                int symbol = bitReader.readBits(8);

                if (offset == 0 && length == 0) {
                    decoded += (char)symbol;
                } else {
                    int index = decoded.length() - offset;
                    for (int j = 0; j < length; ++j) {
                        decoded += decoded.charAt(index + j);

                    }
                    decoded += (char)symbol;
                }
            }

            output.write(decoded.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
