package org.jmv.compress.lz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.util.Arrays;

import org.jmv.compress.huffman.HuffmanTable;
import org.jmv.compress.io.BitWriter;
import org.jmv.compress.util.BinaryLogarithm;

/**
 * Luokka joka säilöö LZHuffman-enkoodaukseen käytettävät funktiot.
 */
public final class LZHuffmanEncoder {
    private final int windowLength;
    private final int minMatchLength;
    private final int maxMatchLength;
    private final int offsetBitLength;
    private final int matchLengthBitLength;
    private final HashChain hc;

    /**
     * Konstruktoi uusi LZHuffman-enkoodaaja.
     *
     * @param windowLength Ikkunan pituus.
     * @param minMatchLength Osuman vähimmäispituus.
     * @param maxMatchLength Osuman enimmäispituus.
     */
    public LZHuffmanEncoder(int windowLength, int minMatchLength, int maxMatchLength, int maxMatchCount) {
        this.windowLength = windowLength;
        this.minMatchLength = minMatchLength;
        this.maxMatchLength = maxMatchLength;
        this.hc = new HashChain(windowLength, minMatchLength, maxMatchCount);
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
    private final int fillWindow(byte[] buffer, InputStream input) throws IOException {
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
     * LZHuffman-enkoodaa sisääntulon ulostuloon.
     *
     * @param input Enkoodattava sisääntulo.
     * @param output Ulostulo johon enkoodataan.
     *
     * @return Kirjoitettujen tavujen lukumäärä.
     */
    public final int encode(InputStream input, OutputStream output) {
        try {
            //Laske ja kirjoita lähtötiedoston pituus
            final var counts = scanCounts(input);
            input.reset();

            final int length = Arrays.stream(counts).sum();

            final var ht = new HuffmanTable(counts);

            final var bitWriter = new BitWriter(output);
            bitWriter.writeTree(ht.getRoot());
            bitWriter.writeBits(length, 32);
            bitWriter.writeBits(windowLength, 32);
            bitWriter.writeBits(minMatchLength, 32);
            bitWriter.writeBits(maxMatchLength, 32);

            final var bis = new BufferedInputStream(input);

            final byte[] buffer = new byte[2 * windowLength];
            int lookahead = fillWindow(buffer, bis);
            while (lookahead != -1) {
                int currentPosition = (2 * windowLength) - lookahead;
                while (currentPosition < (2 * windowLength)) {
                    // Etsi pisin osuma nykyiselle kohdalle.
                    hc.findLongestMatch(buffer, currentPosition);
                    final int matchPosition = hc.getMatchPosition();
                    int matchLength = hc.getMatchLength();

                    // Jos osuma oli liian lyhyt, koodaa se Huffman-enkoodattuna literaalina.
                    // Muutoin koodaa se (offset, pituus)-parina.
                    if (matchLength < minMatchLength) {
                        bitWriter.writeBit(0);
                        int literal = Byte.toUnsignedInt(buffer[currentPosition]);
                        bitWriter.writeBits(ht.lookupCode(literal), ht.lookupLength(literal));

                        ++currentPosition;
                    } else {
                        if (matchLength > maxMatchLength) {
                            matchLength = maxMatchLength;
                        }
                        final int offset = -(matchPosition - currentPosition);
                        bitWriter.writeBit(1);
                        bitWriter.writeBits(offset, offsetBitLength);
                        bitWriter.writeBits(matchLength - minMatchLength, matchLengthBitLength);

                        currentPosition += matchLength;
                    }
                }
                // Täytä puskuri ja siirrä ikkunaa.
                lookahead = fillWindow(buffer, bis);
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
