package org.jmv.compress.lz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jmv.compress.io.BitWriter;

/**
 * Luokka joka säilöö LZ-enkoodaukseen käytettävät funktiot.
 */
public class LZEncoder {

    private static final int maxWindowLength = 4096;

    /**
     * LZ-enkoodaa sisääntulon ulostuloon.
     *
     * @param input Enkoodattava sisääntulo.
     * @param output Ulostulo johon enkoodataan.
     */
    public static void encode(InputStream input, OutputStream output) {
        try {
            var bitWriter = new BitWriter(output);

            int windowBegin = 0;
            int matchBegin = 0;
            int matchEnd = 0;

            String buf = new String(input.readAllBytes());
            String window = "";

            for (int i = 0; i < buf.length(); ++i) {
                // Kokeile löytyykö ikkunasta osumaa seuraava merkki mukaan luettuna.
                String tmp = new String(buf.substring(matchBegin, matchEnd + 1));
                int tmpIndex = window.lastIndexOf(tmp);

                // Jos ei löydy, enkoodaa mahdollinen aiempi osuma ja sitä seuraava merkki.
                if (tmpIndex == -1 || i == buf.length() - 1) {
                    // Hae mahdollisen osuman offset suhteessa nykyiseen kohtaan.
                    int offset = 0;
                    if (tmp.length() > 1) {
                        int matchIndex = window.lastIndexOf(buf.substring(matchBegin, matchEnd));
                        offset = matchBegin - (windowBegin + matchIndex);
                    }

                    bitWriter.writeBits(offset, 12);
                    bitWriter.writeBits(tmp.length() - 1, 12);
                    bitWriter.writeBits((int)tmp.charAt(tmp.length() - 1), 8);

                    ++matchEnd;
                    matchBegin = matchEnd;

                    // Konkatenoi osuma ja seuraava merkki ikkunaan.
                    window += tmp;
                    if (window.length() > maxWindowLength) {
                        window = window.substring(window.length() - maxWindowLength);
                    }
                    windowBegin = i - window.length() + 1;
                } else {
                    ++matchEnd;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
