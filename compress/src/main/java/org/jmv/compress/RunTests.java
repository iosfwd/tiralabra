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
    public static void main(String[] args) {

        int lzWindowLength = 1 << 15;
        int lzMinMatchLength = 3;
        int lzMaxMatchLength = 258;
        int lzMaxMatches = 128;

        System.out.println("Running tests on test material!");

        try {
            System.out.print("\u001B[31m");
            System.out.println("Running LZ encoding on dna.1MB");
            System.out.print("\u001B[0m");
            var lzenc = new LZEncoder(lzWindowLength, lzMinMatchLength, lzMaxMatchLength, lzMaxMatches);
            var inFile = new File("test_material/dna.1MB");
            var outFile = new File("test_material/dna.1MB.lzenc");
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

        try {
            System.out.print("\u001B[32m");
            System.out.println("Running LZ decoding on dna.1MB.lzenc");
            System.out.print("\u001B[0m");
            var inFile = new File("test_material/dna.1MB.lzenc");
            var outFile = new File("test_material/dna.1MB.lzdec");
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

        try {
            System.out.print("\u001B[31m");
            System.out.println("Running Huffman encoding on dna.1MB");
            System.out.print("\u001B[0m");
            var inFile = new File("test_material/dna.1MB");
            var outFile = new File("test_material/dna.1MB.huffenc");
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

        try {
            System.out.print("\u001B[32m");
            System.out.println("Running Huffman decoding on dna.1MB.huffenc");
            System.out.print("\u001B[0m");
            var inFile = new File("test_material/dna.1MB.huffenc");
            var outFile = new File("test_material/dna.1MB.huffdec");
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

        try {
            System.out.print("\u001B[31m");
            System.out.println("Running LZHuffman encoding on dna.1MB");
            System.out.print("\u001B[0m");
            var lzhufenc = new LZHuffmanEncoder(lzWindowLength, lzMinMatchLength, lzMaxMatchLength, lzMaxMatches);
            var inFile = new File("test_material/dna.1MB");
            var outFile = new File("test_material/dna.1MB.lzhufenc");
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

        try {
            System.out.print("\u001B[32m");
            System.out.println("Running LZHuffman decoding on dna.1MB.lzhufenc");
            System.out.print("\u001B[0m");
            var inFile = new File("test_material/dna.1MB.lzhufenc");
            var outFile = new File("test_material/dna.1MB.lzhufdec");
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


        try {
            System.out.print("\u001B[31m");
            System.out.println("Running LZ encoding on english.1MB");
            System.out.print("\u001B[0m");
            var lzenc = new LZEncoder(lzWindowLength, lzMinMatchLength, lzMaxMatchLength, lzMaxMatches);
            var inFile = new File("test_material/english.1MB");
            var outFile = new File("test_material/english.1MB.lzenc");
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

        try {
            System.out.print("\u001B[32m");
            System.out.println("Running LZ decoding on english.1MB.lzenc");
            System.out.print("\u001B[0m");
            var inFile = new File("test_material/english.1MB.lzenc");
            var outFile = new File("test_material/english.1MB.lzdec");
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

        try {
            System.out.print("\u001B[31m");
            System.out.println("Running Huffman encoding on english.1MB");
            System.out.print("\u001B[0m");
            var inFile = new File("test_material/english.1MB");
            var outFile = new File("test_material/english.1MB.huffenc");
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

        try {
            System.out.print("\u001B[32m");
            System.out.println("Running Huffman decoding on english.1MB.huffenc");
            System.out.print("\u001B[0m");
            var inFile = new File("test_material/english.1MB.huffenc");
            var outFile = new File("test_material/english.1MB.huffdec");
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

        try {
            System.out.print("\u001B[31m");
            System.out.println("Running LZHuffman encoding on english.1MB");
            System.out.print("\u001B[0m");
            var lzhufenc = new LZHuffmanEncoder(lzWindowLength, lzMinMatchLength, lzMaxMatchLength, lzMaxMatches);
            var inFile = new File("test_material/english.1MB");
            var outFile = new File("test_material/english.1MB.lzhufenc");
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

        try {
            System.out.print("\u001B[32m");
            System.out.println("Running LZHuffman decoding on english.1MB.lzhufenc");
            System.out.print("\u001B[0m");
            var inFile = new File("test_material/english.1MB.lzhufenc");
            var outFile = new File("test_material/english.1MB.lzhufdec");
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

        try {
            System.out.print("\u001B[31m");
            System.out.println("Running LZ encoding on xml.1MB");
            System.out.print("\u001B[0m");
            var lzenc = new LZEncoder(lzWindowLength, lzMinMatchLength, lzMaxMatchLength, lzMaxMatches);
            var inFile = new File("test_material/xml.1MB");
            var outFile = new File("test_material/xml.1MB.lzenc");
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

        try {
            System.out.print("\u001B[32m");
            System.out.println("Running LZ decoding on xml.1MB.lzenc");
            System.out.print("\u001B[0m");
            var inFile = new File("test_material/xml.1MB.lzenc");
            var outFile = new File("test_material/xml.1MB.lzdec");
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

        try {
            System.out.print("\u001B[31m");
            System.out.println("Running Huffman encoding on xml.1MB");
            System.out.print("\u001B[0m");
            var inFile = new File("test_material/xml.1MB");
            var outFile = new File("test_material/xml.1MB.huffenc");
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

        try {
            System.out.print("\u001B[32m");
            System.out.println("Running Huffman decoding on xml.1MB.huffenc");
            System.out.print("\u001B[0m");
            var inFile = new File("test_material/xml.1MB.huffenc");
            var outFile = new File("test_material/xml.1MB.huffdec");
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

        try {
            System.out.print("\u001B[31m");
            System.out.println("Running LZHuffman encoding on xml.1MB");
            System.out.print("\u001B[0m");
            var lzhufenc = new LZHuffmanEncoder(lzWindowLength, lzMinMatchLength, lzMaxMatchLength, lzMaxMatches);
            var inFile = new File("test_material/xml.1MB");
            var outFile = new File("test_material/xml.1MB.lzhufenc");
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

        try {
            System.out.print("\u001B[32m");
            System.out.println("Running LZHuffman decoding on xml.1MB.lzhufenc");
            System.out.print("\u001B[0m");
            var inFile = new File("test_material/xml.1MB.lzhufenc");
            var outFile = new File("test_material/xml.1MB.lzhufdec");
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
}
