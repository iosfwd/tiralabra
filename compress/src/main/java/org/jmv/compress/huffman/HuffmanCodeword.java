package org.jmv.compress.huffman;

/**
 * Dataluokka Huffman-koodisanoille.
 */
public class HuffmanCodeword {
    /**
     * Huffman-koodi.
     */
    public int code;
    /**
     * Huffman-koodin bittipituus.
     */
    public int length;

    /**
     * Konstruktoi tyhjän koodisanan.
     */
    public HuffmanCodeword() {
        code = 0;
        length = 0;
    }

    /**
     * Konstruktoi koodisanan.
     *
     * @param code Koodi.
     * @param length Koodin pituus.
     */
    public HuffmanCodeword(int code, int length) {
        this.code = code;
        this.length = length;
    }
}
