package org.jmv.compress.huffman;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class TestHuffmanDecoder {
    @Test
    public void testDecode() {
        String testString = "A_DEAD_DAD_CEDED_A_BAD_BABE_A_BEADED_ABACA_BED\n";
        var input = new ByteArrayInputStream(testString.getBytes());
        var output = new ByteArrayOutputStream();

        HuffmanEncoder.encode(input, output);
        var encoded = output.toByteArray();

        input.reset();
        output.reset();

        HuffmanDecoder.decode(new ByteArrayInputStream(encoded), output);
        var result = output.toString();
        assertEquals(testString, result);
    }
}
