package org.jmv.compress.huffman;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestHuffman {
    @Test
    public void testEncodeDecode() {
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

    @Test
    public void testRandomEncodeDecode() {
        Random rand = new Random(1);

        String s = "";

        for (int i = 0; i < 4096; ++i) {
            int r = rand.nextInt() % 10;
            switch (r) {
            case 0: s += "A"; break;
            case 1: s += "B"; break;
            case 2: s += "C"; break;
            case 3: s += "D"; break;
            case 4: s += "E"; break;
            case 5: s += "F"; break;
            case 6: s += "G"; break;
            case 7: s += "H"; break;
            case 8: s += "I"; break;
            case 9: s += "J"; break;
            default: s += "K"; break;
            }
        }

        var input = new ByteArrayInputStream(s.getBytes());
        var output = new ByteArrayOutputStream();

        HuffmanEncoder.encode(input, output);
        var encoded = output.toByteArray();

        input.reset();
        output.reset();

        HuffmanDecoder.decode(new ByteArrayInputStream(encoded), output);
        var result = output.toString();
        assertEquals(s, result);
    }
}
