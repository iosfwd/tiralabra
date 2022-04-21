package org.jmv.compress.huffman;

/**
 * Huffman-solmu Huffman-puun rakentamiseen.
 */
public class HuffmanNode implements Comparable<HuffmanNode> {
    /**
     * Solmun symboli.
     */
    public int symbol;
    /**
     * Symbolin esiintymät koodattavassa datassa.
     */
    public int count;
    /**
     * Osoitin seuraavaan Huffman-solmuun. Käytetään puun rakentamisessa.
     */
    public HuffmanNode next;
    /**
     * Osoitin solmun vasempaan lapseen.
     */
    public HuffmanNode leftChild;
    /**
     * Osoitin solmun oikeaan lapseen.
     */
    public HuffmanNode rightChild;

    /**
     * Konstruktoi tyhjä solmu.
     */
    public HuffmanNode() {
        this.symbol = 0;
        this.count = 0;
        this.next = null;
        this.leftChild = null;
        this.rightChild = null;
    }

    /**
     * Konstruktoi solmu.
     *
     * @param symbol Symboli
     * @param count Symbolin esiintymät
     * @param next Seuraava solmu
     * @param leftChild vasen lehti
     * @param rightChild oikea lehti
     */
    public HuffmanNode(int symbol, int count, HuffmanNode next, HuffmanNode leftChild, HuffmanNode rightChild) {
        this.symbol = symbol;
        this.count = count;
        this.next = next;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    /**
     * Tarkista onko solmu lehtisolmu, eli onko sillä lapsia.
     *
     * @return Palauttaa toden jos solmu on lehtisolmu.
     */
    public final boolean isLeafNode() {
        return (leftChild == null && rightChild == null);
    }

    /**
     * Vertailee solmuja niiden symbolin esiintymien mukaan.
     *
     * @param node Solmu johon vertaillaan.
     *
     * @return Palauttaa vertailun tuloksen.
     */
    @Override
    public final int compareTo(HuffmanNode node) {
        return Integer.compare(count, node.count);
    }
}
