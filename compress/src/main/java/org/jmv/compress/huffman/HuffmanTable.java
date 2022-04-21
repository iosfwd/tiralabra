package org.jmv.compress.huffman;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Huffman-koodit taulukoiva luokka.
 */
public class HuffmanTable {
    private HuffmanCodeword[] table = new HuffmanCodeword[256];
    private HuffmanNode root;

    /**
     * Konstruktoi koodit ja taulukko tavujen esiintymistä.
     *
     * @param counts Eri tavujen esiintymät sisältävä taulukko.
     */
    public HuffmanTable(int[] counts) {
        buildTree(counts);
        calculateLengths(root, 0);
        buildCanonicalCodes();
    }

    /**
     * Konstruktoi koodit ja taulukko Huffman-puusta.
     *
     * @param root Puun juurisolmu.
     */
    public HuffmanTable(HuffmanNode root) {
        this.root = root;
        calculateLengths(root, 0);
        buildCanonicalCodes();
    }

    /**
     * Konstruktoi Huffman-puu eri tavujen esiintymistä.
     *
     * @param root Puun juurisolmu.
     */
    private void buildTree(int[] counts) {
        // Laske kuinka monta erilaista symbolia sisääntulossa esiintyi.
        int size = (int)Arrays.stream(counts).filter(n -> n > 0).count();

        // Alusta lista solmuja jonka avulla puu rakennetaan.
        HuffmanNode[] alphabet = new HuffmanNode[size];

        int j = 0;
        for (int i = 0; i < 256; ++i) {
            if (counts[i] > 0) {
                alphabet[j++] = new HuffmanNode(i, counts[i], null, null, null);
            }
        }

        // Järjestä solmut esiintymien mukaan.
        Arrays.sort(alphabet, new Comparator<HuffmanNode>() {
                public int compare(HuffmanNode n1, HuffmanNode n2) {
                    return Integer.compare(n1.symbol, n2.symbol);
                }
            });

        Arrays.sort(alphabet, new Comparator<HuffmanNode>() {
                public int compare(HuffmanNode n1, HuffmanNode n2) {
                    return n1.compareTo(n2);
                }
            });

        for (int i = 0; i < size - 1; ++i) {
            alphabet[i].next = alphabet[i + 1];
        }
        alphabet[size - 1].next = null;

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

        root =  alphabet[0];
    }

    /**
     * Laske Huffman-koodeille pituudet kulkemalla puuta rekursiivisesti.
     *
     * @param root Puun juurisolmu.
     * @param depth Syvyys.
     */
    private void calculateLengths(HuffmanNode n, int depth) {
        if (!n.isLeafNode()) {
            calculateLengths(n.leftChild, depth + 1);
            calculateLengths(n.rightChild, depth + 1);
        } else {
            table[n.symbol] = new HuffmanCodeword();
            table[n.symbol].symbol = n.symbol;
            table[n.symbol].length = depth;
        }
    }

    /**
     * Laske kaanoniset Huffman-esitykset koodeille.
     *
     * @param codes Huffman-koodien taulukko.
     */
    private void buildCanonicalCodes() {
            var canonicalCodes = Arrays.stream(table)
                .filter(c -> c != null).toArray(HuffmanCodeword[]::new);
            PrefixCoder.buildPrefixCodes(canonicalCodes);

            for(int i = 0; i < canonicalCodes.length; ++i) {
                table[canonicalCodes[i].symbol].code = canonicalCodes[i].code;
            }
    }

    /**
     * Hae koodi symbolille.
     *
     * @param symbol Symboli.
     *
     * @return Symbolin koodi.
     */
    public int lookupCode(int symbol) {
        return table[symbol].code;
    }

    /**
     * Hae koodin pituus symbolille.
     *
     * @param symbol Symboli.
     *
     * @return Symbolin koodin pituus.
     */
    public int lookupLength(int symbol) {
        return table[symbol].length;
    }

    /**
     * Hae kooditaulukko.
     *
     * @return Kooditaulukko.
     */
    public HuffmanCodeword[] getTable() {
        return table;
    }

    /**
     * Hae Huffman-puun juurisolmu.
     *
     * @return Puun juurisolmu
     */
    public HuffmanNode getRoot() {
        return root;
    }
}
