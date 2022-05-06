package org.jmv.compress.lz;

import java.util.Arrays;

/**
 * Tietynlaisen hajautustaulun toteuttava luokka jolla haetaan osumia
 * Lempel-Ziv enkoodaukseen. Tallentaa ikkunan kaikkien
 * alimerkkijonojen sijainnin. Laskee hash-arvon alimerkkijonon
 * ensimmäisten merkkien mukaan, ja tallentaa alimerkkijonon sijainnin
 * ikkunassa. head-taulukkoon on tallennettu jokaisen hash-arvon
 * viimeisin sijainti ikkunassa, ja prev-taulukon avulla tallennetaan
 * saman hash-arvon muut sijainnit.
 */
public final class HashChain {
    private final int windowLength;
    private final int windowMask;
    private final int minMatchLength;
    private final int matchLimit;
    private final int[] head;
    private final int[] prev;
    private int matchPosition = 0;
    private int matchLength = 0;

    /**
     * Konstruktoi uusi hajautustaulu.
     *
     * @param windowLength LZ-ikkunan pituus.
     * @param minMatchLength Pienin hyväksytty osuman pituus.
     * @param matchLimit Kuinka monta osumaa enintään tarkastetaan parasta haettaessa.
     */
    public HashChain(int windowLength, int minMatchLength, int matchLimit) {
        this.windowLength = windowLength;
        windowMask = windowLength - 1;
        this.minMatchLength = minMatchLength;
        this.matchLimit = matchLimit;

        this.head = new int[windowLength];
        this.prev = new int[windowLength];
        Arrays.fill(this.head, -windowLength);
        Arrays.fill(this.prev, -windowLength);
    }

    /**
     * Päivitä hajautustaulun alkioita LZ-ikkunaa siirrettäessä.
     *
     * @param n Kuinka monta tavua ikkunaa siirrettiin.
     */
    public final void moveWindow(int n) {
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
    private final int hash(byte[] buffer, int pos) {
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
    private final int findMatchLength(byte[] buffer, int pos, int matchPos, int longestMatch) {
        int matchLen = 0;

        if (pos + longestMatch >= buffer.length) {
            return 0;
        } else if (buffer[pos] != buffer[matchPos] &&
                   buffer[pos + longestMatch] != buffer[matchPos + longestMatch]) {
            return 0;
        }

        for (int i = 0; (pos + i) < buffer.length && (matchPos + i) < pos; ++i) {
            if (buffer[pos + i] != buffer[matchPos + i]) {
                return matchLen;
            }
            ++matchLen;
        }

        return matchLen;
    }

    /**
     * Hae pisin osuma jollekin LZ-puskurin kohdalle ja päivitä
     * hajautustaulukkoa.
     *
     * @param buffer LZ-puskuri.
     * @param pos LZ-puskurin kohta jolle haetaan osumaa.
     */
    public final void findLongestMatch(byte[] buffer, int pos) {
        final int insh = hash(buffer, pos);
        int next = head[insh];

        // matchPosition = 0;
        // matchLength = 0;

        final int windowBegin = pos - windowLength;
        int found = 0;
        int longestMatch = 0;
        int matchPos = 0;
        while (next > windowBegin && found < matchLimit) {
            // final int length = findMatchLength(buffer, pos, next);
            final int length = findMatchLength(buffer, pos, next, longestMatch);

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
    public final int getMatchPosition() {
        return matchPosition;
    }

    /**
     * Palauta osuman pituus.
     *
     * @return Osuman pituus.
     */
    public final int getMatchLength() {
        return matchLength;
    }
}
