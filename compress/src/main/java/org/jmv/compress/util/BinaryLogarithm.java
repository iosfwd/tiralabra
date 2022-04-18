package org.jmv.compress.util;

/**
 * Luokka kaksikantaisen logaritmin laskemiseen.
 */
public class BinaryLogarithm {
    /**
     * Laske 32 bittisen kokonaisluvun alas päin pyöristetty kaksikantainen logaritmi.
     *
     * @param n 32 bittinen kokonaisluku.
     *
     * @return floor(log2(n)).
     */
    public static int log2(int n) {
        return 31 - Integer.numberOfLeadingZeros(n);
    }
}
