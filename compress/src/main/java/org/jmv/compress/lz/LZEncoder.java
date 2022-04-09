package org.jmv.compress.lz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.RandomAccessFile;

import org.jmv.compress.io.BitWriter;

/**
 * Luokka joka säilöö LZ-enkoodaukseen käytettävät funktiot.
 */
public class LZEncoder {
    private static final int maxWindowLength = 4096;
    private static final int minMatchLength = 3;
    private static final int maxMatchLength = 258;

    /**
     * Siirrä ikkunaa, lue puskuri täyteen ja palauta luettujen tavuen lukumäärä.
     *
     * @param buffer Puskuri minne luetaan.
     * @param input Sisääntulo mistä luetaan.
     *
     * @return Luettujen tavujen lukumäärä.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    private static int fillWindow(byte[] buffer, RandomAccessFile input) throws IOException {
        byte[] in = new byte[maxWindowLength];
        int bytesRead = input.read(in, 0, maxWindowLength);

        if (bytesRead == -1) {
            return bytesRead;
        }

        if (bytesRead == maxWindowLength) {
            System.arraycopy(buffer, maxWindowLength, buffer, 0, maxWindowLength);
            System.arraycopy(in, 0, buffer, maxWindowLength, maxWindowLength);
        } else {
            System.arraycopy(buffer, bytesRead, buffer, 0, (2 * maxWindowLength) - bytesRead);
            System.arraycopy(in, 0, buffer, (2 * maxWindowLength) -  bytesRead, bytesRead);
        }

        return bytesRead;
    }

    /**
     * Siirrä ikkunaa, lue puskuri täyteen ja palauta luettujen tavuen lukumäärä.
     *
     * @param buffer Puskuri minne luetaan.
     * @param input Sisääntulo mistä luetaan.
     *
     * @return Luettujen tavujen lukumäärä.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    private static int fillWindow(byte[] buffer, InputStream input) throws IOException {
        byte[] in = new byte[maxWindowLength];
        int bytesRead = input.read(in, 0, maxWindowLength);

        if (bytesRead == -1) {
            return bytesRead;
        }

        if (bytesRead == maxWindowLength) {
            System.arraycopy(buffer, maxWindowLength, buffer, 0, maxWindowLength);
            System.arraycopy(in, 0, buffer, maxWindowLength, maxWindowLength);
        } else {
            System.arraycopy(buffer, bytesRead, buffer, 0, (2 * maxWindowLength) - bytesRead);
            System.arraycopy(in, 0, buffer, (2 * maxWindowLength) -  bytesRead, bytesRead);
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
    public static int encode(InputStream input, OutputStream output) {
        try {
            //Laske ja kirjoita lähtötiedoston pituus
            int length = 0;
            while (input.read() != -1) {
                ++length;
            }
            input.reset();

            var bitWriter = new BitWriter(output);
            bitWriter.writeBits(length, 32);

            HashChain hc = new HashChain(maxWindowLength, minMatchLength, 16);

            byte[] buffer = new byte[2 * maxWindowLength];
            int lookahead = fillWindow(buffer, input);
            while (lookahead != -1) {
                int currentPosition = (2 * maxWindowLength) - lookahead;
                while (currentPosition < (2 * maxWindowLength)) {
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
                        bitWriter.writeBits(offset, 12);
                        bitWriter.writeBits(matchLength, 9);

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

    /**
     * LZ-enkoodaa sisääntulon ulostuloon.
     *
     * @param input Enkoodattava sisääntulo.
     * @param output Ulostulo johon enkoodataan.
     *
     * @return Kirjoitettujen tavujen lukumäärä.
     */
    public static int encode(RandomAccessFile input, OutputStream output) {
        try {
            // Laske ja kirjoita lähtötiedoston pituus
            int length = 0;
            while (input.read() != -1) {
                ++length;
            }
            input.seek(0);

            var bitWriter = new BitWriter(output);
            bitWriter.writeBits(length, 32);

            HashChain hc = new HashChain(maxWindowLength, minMatchLength, 16);

            byte[] buffer = new byte[2 * maxWindowLength];
            int lookahead = fillWindow(buffer, input);
            while (lookahead != -1) {
                int currentPosition = (2 * maxWindowLength) - lookahead;
                while (currentPosition < (2 * maxWindowLength)) {
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
                        bitWriter.writeBits(offset, 12);
                        bitWriter.writeBits(matchLength, 9);

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
