package org.jmv.compress.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * Luokka tiedostonlukuun jolla voi kelata tiedoston alkuun.
 */
public final class ResetableFileInputStream extends InputStream {
    private final RandomAccessFile raf;
    private long markedPosition = 0;

    /**
     * Konstruktoi uusi kelattava tiedostonlukija.
     *
     * @param file Tiedosto mistä luetaan.
     *
     * @throws FileNotFoundException jos tiedostoa ei löydy.
     */
    public ResetableFileInputStream(File file) throws FileNotFoundException {
        raf = new RandomAccessFile(file, "r");
    }

    /**
     * Yhden tavun lukeminen.
     *
     * @return Luettu tavu.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    final public int read() throws IOException {
        return raf.read();
    }

    /**
     * Lue tavuja taulukkoon.
     *
     * @param buffer Taulukko johon luetaan.
     * @param offset Taulukon kohta johon luetaan.
     * @param length Kuinka monta tavua yritetään lukea.
     *
     * @return Luettujen tavujen lukumäärä.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    final public int read(byte[] buffer, int offset, int length) throws IOException {
        return raf.read(buffer, offset, length);
    }

    /**
     * Sulje tiedostonlukija.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    final public void close() throws IOException {
        raf.close();
    }

    /**
     * Merkitse kelattava kohta.
     */
    final public void mark(int readLimit) {
        try {
            markedPosition = raf.getFilePointer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Kelaa tiedoston merkittyyn kohtaan.
     *
     * @throws IOException jos I/O-poikkeama tapahtui.
     */
    final public void reset() throws IOException {
        raf.seek(markedPosition);
    }
}
