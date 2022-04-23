package org.jmv.compress.io;

import java.io.IOException;
import java.io.InputStream;

import org.jmv.compress.huffman.HuffmanNode;

/**
 * Luokka bittitason lukemiseen.
 */
public final class BitReader {
    private final InputStream input;
    private int buffer;
    private int currentPosition;
    private final int bufferSize = 8;
    private boolean eof = false;
    private int markedPosition = -1;
    private int markedBuffer;
    private int bytesRead = 0;
    private int bytesReadAtMark = -1;

    /**
     * Konstruktoi uusi bittitason lukija InputStream-luokasta.
     *
     * @param input Sisääntulo mistä luetaan.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    public BitReader(InputStream input) throws IOException {
        this.input = input;

        fillBuffer();
    }

    /**
     * Yhden bitin lukeminen.
     *
     * @return Luetun bitin arvo.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    public final int readBit() throws IOException {
        --currentPosition;
        int b = ((buffer >>> currentPosition) & 1);

        if (currentPosition == 0) {
            fillBuffer();
        }

        return b;
    }

    /**
     * Monen bitin lukeminen. Lukee annetun pituuden verran bittejä.
     *
     * @param length Luettava pituus.
     *
     * @return Luettu pätkä bittejä kokonaislukuna.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    public final int readBits(int length) throws IOException {
        int x = 0;

        for (int i = 0; i < length; ++i) {
            x |= (readBit() << (length - 1 - i));
        }

        return x;
    }

    /**
     * Puskurin täyttö.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    private final void fillBuffer() throws IOException {
        var b = input.read();

        if (b == -1) {
            eof = true;
            return;
        }

        buffer = b;
        currentPosition = 8;
        ++bytesRead;
    }

    /**
     * Tarkistaa onko sisääntulo vielä luettavissa.
     *
     * @return Totuusarvo sisääntulon tilasta.
     */
    public final boolean available() {
        return !eof;
    }

    /**
     * Sisääntulon sulkeminen.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    public final void close() throws IOException {
        input.close();
    }

    /**
     * Lukee serialisoidun Huffman-puun sisääntulosta.
     *
     * @return Huffman-puun juurisolmu.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    public final HuffmanNode readTree() throws IOException {
        var leaf = readBit();
        if (leaf == 1) {
            return new HuffmanNode(readBits(8), -1, null, null, null);
        } else {
            return new HuffmanNode(-1, -1, null, readTree(), readTree());
        }
    }

    /**
     * Tallenna paikka.
     */
    public final void mark() {
        input.mark(4096);
        markedPosition = currentPosition;
        markedBuffer = buffer;
        bytesReadAtMark = bytesRead;
    }

    /**
     * Palaa tallennetulle paikalle.
     */
    public final void reset() throws IOException {
        if (markedPosition == -1) {
            input.reset();
            fillBuffer();
        } else if (bytesReadAtMark != bytesRead) {
            input.reset();
            currentPosition = markedPosition;
            buffer = markedBuffer;
        } else {
            currentPosition = markedPosition;
            buffer = markedBuffer;
        }
    }
}
