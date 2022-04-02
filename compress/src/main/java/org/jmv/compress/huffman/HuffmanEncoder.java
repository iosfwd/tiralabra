package org.jmv.compress.huffman;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import org.jmv.compress.io.BitWriter;

/**
 * Luokka joka säilöö Huffman-enkoodaukseen käytettävät funktiot.
 */
public class HuffmanEncoder {
    /**
     * Huffman-enkoodaa sisääntulon ulostuloon.
     *
     * @param input Enkoodattava sisääntulo.
     * @param output Ulostulo johon enkoodataan.
     */
    public static void encode(InputStream input, OutputStream output) {
        try {
            var counts = scanCounts(input);

            int size = 0;
            for (int i = 0; i < 256; i++) {
                size += counts[i];
            }

            var root = buildTree(counts);
            var codes = buildCodesFromTree(root);

            var bitWriter = new BitWriter(output);
            bitWriter.writeTree(root);
            bitWriter.writeBits(size, 32);

            input.reset();

            int token = 0;
            while ((token = input.read()) != -1) {
                bitWriter.writeBits(codes[token].code, codes[token].length);
            }
            bitWriter.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Laskee symbolien esiintymät sisääntulosta.
     *
     * @param input Sisääntulo josta symbolien esiintymät lasketaan.
     *
     * @return Symbolien esiintymien lukumäärät taulukoituna.
     */
    private static int[] scanCounts(InputStream input) throws IOException {
        int[] counts = new int[256];

        int token = 0;
        while ((token = input.read()) != -1) {
            ++counts[token];
        }

        return counts;
    }

    /**
     * Rakentaa Huffman-puun taulukoiduista symbolien esiintymistä.
     *
     * @param counts Symbolien esiintymien lukumäärät taulukoituna.
     *
     * @return Huffman-puun juurisolmu.
     */
    private static HuffmanNode buildTree(int[] counts) {
        // Laske kuinka monta erilaista symbolia sisääntulossa esiintyi.
        int elems = 0;
        for (int i = 0; i < 256; ++i) {
            if (counts[i] > 0) {
                ++elems;
            }
        }

        // Alusta lista solmuja jonka avulla puu rakennetaan.
        HuffmanNode[] alphabet = new HuffmanNode[elems];

        int j = 0;
        for (int i = 0; i < 256; ++i) {
            if (counts[i] > 0) {
                alphabet[j++] = new HuffmanNode(i, counts[i], null, null, null);
            }
        }

        // Järjestä solmut esiintymien mukaan.
        Arrays.sort(alphabet, 0, elems, new Comparator<HuffmanNode>() {
                public int compare(HuffmanNode n1, HuffmanNode n2) {
                    return n1.compareTo(n2);
                }
            });

        for (int i = 0; i < elems - 1; ++i) {
            alphabet[i].next = alphabet[i + 1];
        }
        alphabet[elems - 1].next = null;


        // Rakenna Huffman-puu järjestettyjen solmujen listasta.
        var insertPoint = alphabet[0];
        while (alphabet[0].next != null) {
            // Yhdistä kaksi kevyintä solmua.
            var node = new HuffmanNode(-1, alphabet[0].count + alphabet[0].next.count,
                                    null, alphabet[0], alphabet[0].next);

            // Etsi paikka solmulle.
            while (insertPoint.next != null && insertPoint.next.count <= node.count) {
                insertPoint = insertPoint.next;
            }

            node.next = insertPoint.next;
            insertPoint.next = node;
            alphabet[0] = alphabet[0].next.next;
        }

        return alphabet[0];
    }

    /**
     * Rakentaa Huffman-koodisanat Huffman-puusta rekursiivisesti.
     *
     * @param counts Symbolien esiintymien lukumäärät taulukoituna.
     *
     * @return Tavujen Huffman-koodisanat taulukoituna.
     */
    private static HuffmanCodeword[] buildCodesFromTree(HuffmanNode root) {
        HuffmanCodeword[] codes = new HuffmanCodeword[256];

        buildCodes(root, codes, 0, 0);

        return codes;
    }

    /**
     * Rakentaa Huffman-koodisanat rekursiivisesti.
     */
    private static void buildCodes(HuffmanNode n, HuffmanCodeword[] codes, int code, int depth) {
        // Kulje puuta kunnes saavutaan lehtisolmuun.
        if (!n.isLeafNode()) {
            buildCodes(n.leftChild, codes, (code << 1), depth + 1);
            buildCodes(n.rightChild, codes, (code << 1) | 1 , depth + 1);
        } else {
            codes[n.symbol] = new HuffmanCodeword(code, depth);
        }
    }
}
