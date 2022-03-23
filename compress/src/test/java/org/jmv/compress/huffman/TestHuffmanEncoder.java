package org.jmv.compress.huffman;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class TestHuffmanEncoder {
    @Test
    public void testBuildTree() {
        int[] counts = new int[256];
        counts['A'] = 5;
        counts['B'] = 2;
        counts['C'] = 1;
        counts['D'] = 1;
        counts['R'] = 2;

        var root = HuffmanEncoder.buildTree(counts);
        assertEquals('A', root.leftChild.symbol);
        assertEquals('C', root.rightChild.leftChild.leftChild.symbol);
        assertEquals('D', root.rightChild.leftChild.rightChild.symbol);
        assertEquals('B', root.rightChild.rightChild.leftChild.symbol);
        assertEquals('R', root.rightChild.rightChild.rightChild.symbol);
    }

    @Test
    public void testBuildCodes() {
        int[] counts = new int[256];
        counts['A'] = 5;
        counts['B'] = 2;
        counts['C'] = 1;
        counts['D'] = 1;
        counts['R'] = 2;

        var root = HuffmanEncoder.buildTree(counts);

        var codes = HuffmanEncoder.buildCodesFromTree(root);
        assertEquals(0, codes['A'].code);
        assertEquals(1, codes['A'].length);
        assertEquals(6, codes['B'].code);
        assertEquals(3, codes['B'].length);
        assertEquals(4, codes['C'].code);
        assertEquals(3, codes['C'].length);
        assertEquals(5, codes['D'].code);
        assertEquals(3, codes['D'].length);
        assertEquals(7, codes['R'].code);
        assertEquals(3, codes['R'].length);
    }

    @Test
    public void testEncode() {
        String testString = "A_DEAD_DAD_CEDED_A_BAD_BABE_A_BEADED_ABACA_BED\n";
        var input = new ByteArrayInputStream(testString.getBytes());
        var output = new ByteArrayOutputStream();

        HuffmanEncoder.encode(input, output);
        var result = output.toByteArray();
        assertEquals(28, result.length);
    }
}
