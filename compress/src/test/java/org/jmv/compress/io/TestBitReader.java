package org.jmv.compress.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestBitReader {
    private ByteArrayInputStream input;
    private BitReader br;

    @AfterEach
    public void tearDown() {
        try {
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadOneBit() {
        byte[] arr = { (byte)128 };
        input = new ByteArrayInputStream(arr);

        try {
            br = new BitReader(input);

            var result = br.readBit();
            assertEquals(1, result);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadZeroBit() {
        byte[] arr = { 0 };
        input = new ByteArrayInputStream(arr);

        try {
            br = new BitReader(input);

            var result = br.readBit();
            assertEquals(0, result);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadByte() {
        byte[] arr = { 'A' };
        input = new ByteArrayInputStream(arr);

        try {
            br = new BitReader(input);

            var result = br.readBits(8);
            assertEquals('A', result);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadInt() {
        byte[] arr = { (byte)0x7F, (byte)0xFF, (byte)0xFF, (byte)0xFF  };
        input = new ByteArrayInputStream(arr);

        try {
            br = new BitReader(input);

            var result = br.readBits(32);
            assertEquals(0x7FFFFFFF, result);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAvailable() {
        byte[] arr = { (byte)0x7F, (byte)0xFF, (byte)0xFF, (byte)0xFF  };
        input = new ByteArrayInputStream(arr);

        try {
            br = new BitReader(input);
            int x = 0;
            while (br.available()) {
                x = (x << 1 ) | br.readBit();
            }

            assertEquals(0x7FFFFFFF, x);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
