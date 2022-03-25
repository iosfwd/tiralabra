package org.jmv.compress.huffman;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestHuffmanEncoder {
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
