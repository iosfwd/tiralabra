package org.jmv.compress.lz;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestLZ {
    @Test
    public void testEncodeDecode() {
        String inputString = "how_can_a_clam_cram_in_a_clean_cream_can$";
        var input = new ByteArrayInputStream(inputString.getBytes());
        var output = new ByteArrayOutputStream();

        var lzenc = new LZEncoder(4096, 3, 258, 16);
        lzenc.encode(input, output);

        var r1 = output.toByteArray();
        output.reset();

        LZDecoder.decode(new ByteArrayInputStream(r1), output);

        var r2 = new String(output.toByteArray());
        assertEquals(inputString, r2);
    }

    @Test
    public void testRandomEncodeDecode() {
        Random rand = new Random(1);

        String s = "";

        for (int i = 0; i < (3*4096); ++i) {
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

        var lzenc = new LZEncoder(4096, 3, 258, 16);
        lzenc.encode(input, output);

        var r1 = output.toByteArray();
        output.reset();

        LZDecoder.decode(new ByteArrayInputStream(r1), output);

        var r2 = new String(output.toByteArray());
        assertEquals(s, r2);
    }
}
