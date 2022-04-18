package org.jmv.compress.lz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jmv.compress.io.BitWriter;
import org.jmv.compress.util.BinaryLogarithm;

/**
 * Luokka joka säilöö LZ-enkoodaukseen käytettävät funktiot.
 */
public class LZEncoder {
    private final int windowLength;
    private final int minMatchLength;
    private final int maxMatchLength;
    private final int offsetBitLength;
    private final int matchLengthBitLength;

    /**
     * Konstruktoi uusi LZ-enkoodaaja.
     *
     * @param windowLength Ikkunan pituus.
     * @param minMatchLength Osuman vähimmäispituus.
     * @param maxMatchLength Osuman enimmäispituus.
     */
    public LZEncoder(int windowLength, int minMatchLength, int maxMatchLength) {
        this.windowLength = windowLength;
        this.minMatchLength = minMatchLength;
        this.maxMatchLength = maxMatchLength;

        offsetBitLength = BinaryLogarithm.log2(windowLength + 1);
        matchLengthBitLength = BinaryLogarithm.log2(maxMatchLength - minMatchLength + 1);
    };

    /**
     * Siirrä ikkunaa, lue puskuri täyteen ja palauta luettujen tavujen lukumäärä.
     *
     * @param buffer Puskuri minne luetaan.
     * @param input Sisääntulo mistä luetaan.
     *
     * @return Luettujen tavujen lukumäärä.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    private int fillWindow(byte[] buffer, InputStream input) throws IOException {
        byte[] in = new byte[windowLength];
        int bytesRead = input.read(in, 0, windowLength);

        if (bytesRead == -1) {
            return bytesRead;
        }

        if (bytesRead == windowLength) {
            System.arraycopy(buffer, windowLength, buffer, 0, windowLength);
            System.arraycopy(in, 0, buffer, windowLength, windowLength);
        } else {
            System.arraycopy(buffer, bytesRead, buffer, 0, (2 * windowLength) - bytesRead);
            System.arraycopy(in, 0, buffer, (2 * windowLength) -  bytesRead, bytesRead);
        }

        return bytesRead;
    }

    /**
     * LZ-enkoodaa sisääntulon ulostuloon.
     *
     * @param input Enkoodattava sisääntulo.
     * @param output Ulostulo johon enkoodataan.
     *
     * @return Kirjoitettujen tavujen lukumäärä.
     */
    public int encode(InputStream input, OutputStream output) {
        try {
            //Laske ja kirjoita lähtötiedoston pituus
            int length = 0;
            while (input.read() != -1) {
                ++length;
            }
            input.reset();

            var bitWriter = new BitWriter(output);
            bitWriter.writeBits(length, 32);
            bitWriter.writeBits(windowLength, 32);
            bitWriter.writeBits(minMatchLength, 32);
            bitWriter.writeBits(maxMatchLength, 32);

            HashChain hc = new HashChain(windowLength, minMatchLength, 16);

            byte[] buffer = new byte[2 * windowLength];
            int lookahead = fillWindow(buffer, input);
            while (lookahead != -1) {
                int currentPosition = (2 * windowLength) - lookahead;
                while (currentPosition < (2 * windowLength)) {
                    // Etsi pisin osuma nykyiselle kohdalle.
                    hc.findLongestMatch(buffer, currentPosition);
                    int matchPosition = hc.getMatchPosition();
                    int matchLength = hc.getMatchLength();

                    // Jos osuma oli liian lyhyt, koodaa se literaalina.
                    // Muutoin koodaa se (offset, pituus)-parina.
                    if (matchLength < minMatchLength) {
                        bitWriter.writeBit(0);
                        bitWriter.writeBits((char)buffer[currentPosition], 8);

                        ++currentPosition;
                    } else {
                        if (matchLength > maxMatchLength) {
                            matchLength = maxMatchLength;
                        }
                        int offset = -(matchPosition - currentPosition);
                        bitWriter.writeBit(1);
                        bitWriter.writeBits(offset, offsetBitLength);
                        bitWriter.writeBits(matchLength - minMatchLength, matchLengthBitLength);

                        currentPosition += matchLength;
                    }
                }
                // Täytä puskuri ja siirrä ikkunaa.
                lookahead = fillWindow(buffer, input);
                hc.moveWindow(lookahead);
            }
            bitWriter.finish();
            return bitWriter.getBytesWritten();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
