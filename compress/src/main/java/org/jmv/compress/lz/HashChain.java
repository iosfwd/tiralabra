package org.jmv.compress.lz;

import java.util.Arrays;

/**
 * Tietynlaisen hajautustaulun toteuttava luokka jolla haetaan osumia Lempel-Ziv enkoodaukseen.
 */
public class HashChain {
    private int windowLength;
    private int windowMask;
    private int minMatchLength;
    private int[] head;
    private int[] prev;
    private int matchLimit;
    private int matchPosition = 0;
    private int matchLength = 0;
    /**
     * Konstruktoi uusi bittitason kirjoittaja OutputStream-luokasta.
     *
     * @param windowLength LZ-ikkunan pituus.
     * @param minMatchLength Pienin hyväksytty osuman pituus.
     * @param matchLimit Kuinka monta osumaa tarkastetaan parasta haettaessa.
     */
    public HashChain(int windowLength, int minMatchLength, int matchLimit) {
        this.windowLength = windowLength;
        windowMask = windowLength - 1;

        this.minMatchLength = minMatchLength;
        this.head = new int[windowLength];
        this.prev = new int[windowLength];
        Arrays.fill(this.head, -windowLength);
        Arrays.fill(this.prev, -windowLength);

        this.matchLimit = matchLimit;
    }

    /**
     * Päivitä hajautustaulun alkioita LZ-ikkunaa siirrettäessä.
     *
     * @param n Kuinka monta tavua ikkunaa siirrettiin.
     */
    public void moveWindow(int n) {
        for (int i = 0; i < windowLength; ++i) {
            head[i] -= n;
            prev[i] -= n;
        }
    }

    /**
     * Laske hash LZ-puskurin jollekin kohdalle.
     *
     * @param buffer LZ-puskuri.
     * @param pos LZ-puskurin kohta josta hash lasketaan.
     */
    private int hash(byte[] buffer, int pos) {
        int h = 0;
        for (int i = 0; i < minMatchLength && (pos + i) < buffer.length; ++i) {
            h = (h << 5) ^ (int)buffer[pos + i];
            h &= windowMask;
        }
        return h;
    }

    /**
     * Laske osuman pituus.
     *
     * @param buffer LZ-puskuri.
     * @param pos LZ-puskurin kohta jolle löytyi osuma.
     * @param matchPos Kohta josta osuma löytyi.
     */
    private int findMatchLength(byte[] buffer, int pos, int matchPos) {
        int matchLen = 0;

        for (int i = 0; (pos + i) < buffer.length && (matchPos + i) < pos; ++i) {
            if (buffer[pos + i] != buffer[matchPos + i]) {
                return matchLen;
            }
            ++matchLen;
        }

        return matchLen;
    }

    /**
     * Hae pisin osuma jollekin LZ-puskurin kohdalle.
     *
     * @param buffer LZ-puskuri.
     * @param pos LZ-puskurin kohta jolle haetaan osumaa.
     */
    public void findLongestMatch(byte[] buffer, int pos) {
        int insh = hash(buffer, pos);
        int next = head[insh];

        int windowBegin = pos - windowLength;
        int found = 0;
        int longestMatch = 0;
        int matchPos = 0;
        while (next > windowBegin && found < matchLimit) {
            int length = findMatchLength(buffer, pos, next);

            if (length > longestMatch) {
                longestMatch = length;
                matchPos = next;
            }

            next = prev[next & windowMask];
            ++found;
        }

        prev[pos & windowMask] = head[insh];
        head[insh] = pos;

        matchPosition = matchPos;
        matchLength = longestMatch;
    }

    /**
     * Palauta osuman kohta.
     *
     * @return Osuman kohta.
     */
    public int getMatchPosition() {
        return matchPosition;
    }

    /**
     * Palauta osuman pituus.
     *
     * @return Osuman pituus.
     */
    public int getMatchLength() {
        return matchLength;
    }
}
