package org.jmv.compress.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestBitWriter {
    private ByteArrayOutputStream output;
    private BitWriter bw;

    @BeforeEach
    public void setUp() {
        output = new ByteArrayOutputStream();
        bw = new BitWriter(output);
    }

    @AfterEach
    public void tearDown() {
        try {
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWriteOneBit() {
        try {
            bw.writeBit(1);
            bw.finish();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        var result = Byte.toUnsignedInt(output.toByteArray()[0]);
        assertEquals(128, result);
    }

    @Test
    public void testWriteZeroBit() {
        try {
            bw.writeBit(0);
            bw.finish();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        var result = Byte.toUnsignedInt(output.toByteArray()[0]);
        assertEquals(0, result);
    }

    @Test
    public void testWriteBits() {
        try {
            bw.writeBits('A', 8);
            bw.finish();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        var result = Byte.toUnsignedInt(output.toByteArray()[0]);
        assertEquals('A', result);
    }
}
