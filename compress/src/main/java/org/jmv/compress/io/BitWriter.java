package org.jmv.compress.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;

import org.jmv.compress.huffman.HuffmanNode;

/**
 * Luokka bittitason kirjoittamiseen.
 */
public class BitWriter {
    private final BufferedOutputStream output;
    private int buffer;
    private int currentPosition;
    private final int bufferSize = 8;
    private int bytesWritten = 0;

    /**
     * Konstruktoi uusi bittitason kirjoittaja OutputStream-luokasta.
     *
     * @param output Ulostulo mihin kirjoitetaan.
     */
    public BitWriter(OutputStream output) {
        this.output = new BufferedOutputStream(output);
        buffer = 0;
        currentPosition = 0;
    }

    /**
     * Yhden bitin kirjoitus. Kirjoittaa kokonaisluvusta vähiten merkitsevän bitin arvon.
     *
     * @param b Kirjoitettava kokonaisluku.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    public final void writeBit(int b) throws IOException {
        b &= 1;
        buffer <<= 1;
        buffer |= b;

        ++currentPosition;

        if (currentPosition == bufferSize) {
            flush();
        }
    }

    /**
     * Monen bitin kirjoitus. Kirjoittaa kokonaisluvusta annetun pituuden verran
     * bittejä vähiten merkitsevästä bitistä lähtien.
     *
     * @param x Kirjoitettava kokonaisluku.
     * @param length Kirjoitettavan osan pituus.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    public final void writeBits(int x, int length) throws IOException {
        for (int i = 0; i < length; ++i) {
            int b = (x >>> (length - 1 - i)) & 1;
            writeBit(b);
        }
    }

    /**
     * Tyhjentää puskurissa olevat bitit ulostuloon.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    public final void flush() throws IOException {
        if (currentPosition == 0) {
            return;
        }

        buffer = (buffer << (8 - currentPosition));
        output.write(buffer);
        ++bytesWritten;

        buffer = 0;
        currentPosition = 0;
    }

    /**
     * Tyhjentää puskurin ja ulostulon.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    public final void finish() throws IOException {
        flush();
        output.flush();
    }

    /**
     * Tyhjentää puskurin ja ulostulon sekä sulkee sen.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    public final void close() throws IOException {
        finish();
        output.close();
    }

    /**
     * Serialisoi Huffman-puun levylle.
     *
     * @param node Huffman-puun solmu.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    public final void writeTree(HuffmanNode node) throws IOException {
        if (node.isLeafNode()) {
            writeBit(1);
            writeBits(node.symbol, 8);
            return;
        }
        writeBit(0);
        writeTree(node.leftChild);
        writeTree(node.rightChild);
    }

    public final int getBytesWritten() {
        return bytesWritten;
    }
}
