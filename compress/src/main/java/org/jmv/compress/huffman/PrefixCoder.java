package org.jmv.compress.huffman;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Prefiksikoodauksen säilövä luokka.
 */
public final class PrefixCoder {
    /**
     * Laske prefiksikoodit taulukolle.
     *
     * @param codes Taulukko Huffman-koodisanoja.
     */
    public final static void buildPrefixCodes(HuffmanCodeword[] codes) {
        Arrays.sort(codes, new Comparator<HuffmanCodeword>() {
                public int compare(HuffmanCodeword c1, HuffmanCodeword c2) {
                    return Integer.compare(c1.symbol, c2.symbol);
                }
            });

        Arrays.sort(codes, new Comparator<HuffmanCodeword>() {
                public int compare(HuffmanCodeword c1, HuffmanCodeword c2) {
                    return Integer.compare(c1.length, c2.length);
                }
            });

        codes[0].code = 0;
        for (int i = 1; i < codes.length; ++i) {
            codes[i].code = (codes[i - 1].code + 1) << (codes[i].length - codes[i - 1].length);
        }
    }
}
