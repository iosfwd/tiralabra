package org.jmv.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jmv.compress.io.ResetableFileInputStream;
import org.jmv.compress.huffman.HuffmanDecoder;
import org.jmv.compress.huffman.HuffmanEncoder;
import org.jmv.compress.lz.LZDecoder;
import org.jmv.compress.lz.LZEncoder;
import org.jmv.compress.lz.LZHuffmanDecoder;
import org.jmv.compress.lz.LZHuffmanEncoder;

public class RunTests {
    private static final int lzWindowLength = 1 << 15;
    private static final int lzMinMatchLength = 3;
    private static final int lzMaxMatchLength = 258;
    private static final int lzMaxMatches = 128;

    private static void testLZEncode(String inFileName, String outFileName) {
        try {
            System.out.print("\u001B[31m");
            System.out.println("Running LZ encoding on " + inFileName);
            System.out.print("\u001B[0m");
            var lzenc = new LZEncoder(lzWindowLength, lzMinMatchLength, lzMaxMatchLength, lzMaxMatches);
            var inFile = new File(inFileName);
            var outFile = new File(outFileName);
            var original = inFile.length();
            var input = new ResetableFileInputStream(inFile);
            var output = new FileOutputStream(outFile);

            long start = System.nanoTime();

            int encoded = lzenc.encode(input, output);

            long timeTaken = System.nanoTime() - start;

            System.out.println("    Encoded " + original + " bytes to " + encoded + " bytes");
            System.out.printf("    Achieved compression ratio of %.3f\n", ((double)encoded / (double)original));
            System.out.printf("    Time taken to encode was %.5f seconds\n", ((double)timeTaken / (double)1000000000));

            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void testLZDecode(String inFileName, String outFileName) {
        try {
            System.out.print("\u001B[32m");
            System.out.println("Running LZ decoding on " + inFileName);
            System.out.print("\u001B[0m");
            var inFile = new File(inFileName);
            var outFile = new File(outFileName);
            var original = inFile.length();
            var input = new FileInputStream(inFile);
            var output = new FileOutputStream(outFile);

            long start = System.nanoTime();

            int decoded = LZDecoder.decode(input, output);

            long timeTaken = System.nanoTime() - start;

            System.out.println("    Decoded " + original + " bytes to " + decoded + " bytes");
            System.out.printf("    Achieved compression ratio of %.3f\n", ((double)original / (double)decoded));
            System.out.printf("    Time taken to decode was %.5f seconds\n", ((double)timeTaken / (double)1000000000));

            input.close();
            output.close();

            inFile.delete();
            outFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void testHuffmanEncode(String inFileName, String outFileName) {
        try {
            System.out.print("\u001B[31m");
            System.out.println("Running Huffman encoding on " + inFileName);
            System.out.print("\u001B[0m");
            var inFile = new File(inFileName);
            var outFile = new File(outFileName);
            var original = inFile.length();
            var input = new ResetableFileInputStream(inFile);
            var output = new FileOutputStream(outFile);

            long start = System.nanoTime();

            int encoded = HuffmanEncoder.encode(input, output);

            long timeTaken = System.nanoTime() - start;

            System.out.println("    Encoded " + original + " bytes to " + encoded + " bytes");
            System.out.printf("    Achieved compression ratio of %.3f\n", ((double)encoded / (double)original));
            System.out.printf("    Time taken to encode was %.5f seconds\n", ((double)timeTaken / (double)1000000000));

            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    private static void testHuffmanDecode(String inFileName, String outFileName) {
        try {
            System.out.print("\u001B[32m");
            System.out.println("Running Huffman decoding on " + inFileName);
            System.out.print("\u001B[0m");
            var inFile = new File(inFileName);
            var outFile = new File(outFileName);
            var original = inFile.length();
            var input = new ResetableFileInputStream(inFile);
            var output = new FileOutputStream(outFile);

            long start = System.nanoTime();

            int decoded = HuffmanDecoder.decode(input, output);

            long timeTaken = System.nanoTime() - start;

            System.out.println("    Decoded " + original + " bytes to " + decoded + " bytes");
            System.out.printf("    Achieved compression ratio of %.3f\n", ((double)original / (double)decoded));
            System.out.printf("    Time taken to decode was %.5f seconds\n", ((double)timeTaken / (double)1000000000));

            input.close();
            output.close();

            inFile.delete();
            outFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void testLZHuffmanEncode(String inFileName, String outFileName) {
        try {
            System.out.print("\u001B[31m");
            System.out.println("Running LZHuffman encoding on " + inFileName);
            System.out.print("\u001B[0m");
            var lzhufenc = new LZHuffmanEncoder(lzWindowLength, lzMinMatchLength, lzMaxMatchLength, lzMaxMatches);
            var inFile = new File(inFileName);
            var outFile = new File(outFileName);
            var original = inFile.length();
            var input = new ResetableFileInputStream(inFile);
            var output = new FileOutputStream(outFile);

            long start = System.nanoTime();

            int encoded = lzhufenc.encode(input, output);

            long timeTaken = System.nanoTime() - start;

            System.out.println("    Encoded " + original + " bytes to " + encoded + " bytes");
            System.out.printf("    Achieved compression ratio of %.3f\n", ((double)encoded / (double)original));
            System.out.printf("    Time taken to encode was %.5f seconds\n", ((double)timeTaken / (double)1000000000));

            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void testLZHuffmanDecode(String inFileName, String outFileName) {
        try {
            System.out.print("\u001B[32m");
            System.out.println("Running LZHuffman decoding on " + inFileName);
            System.out.print("\u001B[0m");
            var inFile = new File(inFileName);
            var outFile = new File(outFileName);
            var original = inFile.length();
            var input = new ResetableFileInputStream(inFile);
            var output = new FileOutputStream(outFile);

            long start = System.nanoTime();

            int decoded = LZHuffmanDecoder.decode(input, output);

            long timeTaken = System.nanoTime() - start;

            System.out.println("    Decoded " + original + " bytes to " + decoded + " bytes");
            System.out.printf("    Achieved compression ratio of %.3f\n", ((double)original / (double)decoded));
            System.out.printf("    Time taken to decode was %.5f seconds\n", ((double)timeTaken / (double)1000000000));

            input.close();
            output.close();

            inFile.delete();
            outFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        System.out.println("Running tests on test material!");

        testLZEncode("test_material/dna.50MB", "test_material/dna.50MB.lzenc");
        System.gc();

        testLZDecode("test_material/dna.50MB.lzenc", "test_material/dna.50MB.lzdec");
        System.gc();

        testHuffmanEncode("test_material/dna.50MB", "test_material/dna.50MB.huffenc");
        System.gc();

        testHuffmanDecode("test_material/dna.50MB.huffenc", "test_material/dna.50MB.huffdec");
        System.gc();

        testLZHuffmanEncode("test_material/dna.50MB", "test_material/dna.50MB.lzhufenc");
        System.gc();

        testLZHuffmanDecode("test_material/dna.50MB.lzhufenc", "test_material/dna.50MB.lzhufdec");
        System.gc();

        testLZEncode("test_material/english.50MB", "test_material/english.50MB.lzenc");
        System.gc();

        testLZDecode("test_material/english.50MB.lzenc", "test_material/english.50MB.lzdec");
        System.gc();

        testHuffmanEncode("test_material/english.50MB", "test_material/english.50MB.huffenc");
        System.gc();

        testHuffmanDecode("test_material/english.50MB.huffenc", "test_material/english.50MB.huffdec");
        System.gc();

        testLZHuffmanEncode("test_material/english.50MB", "test_material/english.50MB.lzhufenc");
        System.gc();

        testLZHuffmanDecode("test_material/english.50MB.lzhufenc", "test_material/english.50MB.lzhufdec");
        System.gc();

        testLZEncode("test_material/xml.50MB", "test_material/xml.50MB.lzenc");
        System.gc();

        testLZDecode("test_material/xml.50MB.lzenc", "test_material/xml.50MB.lzdec");
        System.gc();

        testHuffmanEncode("test_material/xml.50MB", "test_material/xml.50MB.huffenc");
        System.gc();

        testHuffmanDecode("test_material/xml.50MB.huffenc", "test_material/xml.50MB.huffdec");
        System.gc();

        testLZHuffmanEncode("test_material/xml.50MB", "test_material/xml.50MB.lzhufenc");
        System.gc();

        testLZHuffmanDecode("test_material/xml.50MB.lzhufenc", "test_material/xml.50MB.lzhufdec");
        System.gc();
    }
}
