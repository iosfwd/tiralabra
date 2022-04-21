package org.jmv.compress.huffman;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Kaanonisten Huffman-koodien dekoodaukseen käytettävien taulukkojen ja funktioiden säilövä luokka.
 */
public class HuffmanCodebook {
    private int[] alphabet;
    private int[] first;
    private int[] codes;
    private int depth;
    private int length = 0;

    /**
     * Rakenna dekooderi kooditaulukosta.
     *
     * @param table Taulukko kaanonisia Huffman-koodeja.
     */
    public HuffmanCodebook(HuffmanCodeword[] table) {
        var canonicalCodes = Arrays.stream(table)
            .filter(c -> c != null).toArray(HuffmanCodeword[]::new);

        Arrays.sort(canonicalCodes, new Comparator<HuffmanCodeword>() {
                public int compare(HuffmanCodeword c1, HuffmanCodeword c2) {
                    return Integer.compare(c1.symbol, c2.symbol);
                }
            });

        Arrays.sort(canonicalCodes, new Comparator<HuffmanCodeword>() {
                public int compare(HuffmanCodeword c1, HuffmanCodeword c2) {
                    return Integer.compare(c1.length, c2.length);
                }
            });

        alphabet = new int[canonicalCodes.length+1];
        for (int i = 0; i < canonicalCodes.length; ++i) {
            alphabet[i+1] = canonicalCodes[i].symbol;
        }

        depth = canonicalCodes[canonicalCodes.length - 1].length;

        first = new int[depth + 1];
        codes = new int[depth + 1];

        // Tallenna ensimmäisen l-pitkän koodin paikka aakkostossa first-taulukkoon
        for (int i = canonicalCodes.length; i > 0; --i) {
            first[canonicalCodes[i - 1].length] = i;
            codes[canonicalCodes[i - 1].length] = canonicalCodes[i - 1].code;
        }

        for (int i = depth; i > 0; --i) {
            if (first[i] == 0) {
                first[i] = first[i + 1];
                codes[i] = codes[i + 1] >>> 1;
            }
        }
    }

    /**
     * Dekoodaa kaanoninen Huffman-koodi.
     *
     * @param code depth-bittiä pitkä pätkä sisääntulosta.
     *
     * @return Dekoodattu symboli.
     */
    public int decode(int code) {
        // Binäärihae oikea koodin pituus.
        int lowerBound = 0;
        int upperBound = depth - 1;
        int len;

        while (lowerBound <= upperBound) {
            len = (lowerBound + upperBound) / 2;
            if (!(code < codes[len + 1] << (depth - len - 1))) {
                lowerBound = len + 1;
            } else if (!(codes[len] << (depth - len) <= code)) {
                upperBound = len - 1;
            } else {
                code >>>= depth - len;
                length = len;
                return alphabet[first[len] + code - codes[len]];
            }
        }

        length = depth;
        return alphabet[first[depth] + code - codes[depth]];
    }

    /**
     * Hae viimeksi dekoodatun koodin pituus.
     *
     * @return Viimeksi dekoodatun koodin pituus.
     */
    public int getLength() {
        return length;
    }

    /**
     * Hae maksimi koodin pituus.
     *
     * @return Maksimi koodin pituus.
     */
    public int getDepth() {
        return depth;
    }
}
