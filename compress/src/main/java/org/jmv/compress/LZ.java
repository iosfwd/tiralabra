package org.jmv.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jmv.compress.io.ResetableFileInputStream;
import org.jmv.compress.lz.LZDecoder;
import org.jmv.compress.lz.LZEncoder;

public class LZ {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Not enough arguments!");
            System.err.println("Usage: LZ [d|e] path/to/input/file path/to/output/file");
            System.exit(1);
        }

        String mode = args[0];

        if (mode.length() != 1 || (!mode.equals("d") && !mode.equals("e"))) {
            System.err.println("Mode must be either d or e!");
            System.err.println("Usage: LZ [d|e] path/to/input/file path/to/output/file");
            System.exit(1);
        }

        var inFile = new File(args[1]);
        var outFile = new File(args[2]);

        if (!inFile.exists()) {
            System.err.println("Input file does not exists!");
            System.exit(1);
        }

        var original = inFile.length();

        try {
            if (mode.equals("d")) {

                var input = new FileInputStream(inFile);
                var output = new FileOutputStream(outFile);

                long start = System.nanoTime();

                int decoded = LZDecoder.decode(input, output);

                long timeTaken = System.nanoTime() - start;

                System.out.println("Decoded " + original + " bytes to " + decoded + " bytes");
                System.out.printf("Achieved compression ratio of %.3f\n", ((double)original / (double)decoded));
                System.out.printf("Time taken to decode was %.5f seconds\n", ((double)timeTaken / (double)1000000000));
            }

            if (mode.equals("e")) {
                var input = new ResetableFileInputStream(inFile);
                var output = new FileOutputStream(outFile);

                long start = System.nanoTime();

                var lzenc = new LZEncoder(4096, 3, 258, 16);
                int encoded = lzenc.encode(input, output);

                long timeTaken = System.nanoTime() - start;

                System.out.println("Encoded " + original + " bytes to " + encoded + " bytes");
                System.out.printf("Achieved compression ratio of %.3f\n", ((double)encoded / (double)original));
                System.out.printf("Time taken to encode was %.5f seconds\n", ((double)timeTaken / (double)1000000000));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.exit(0);
    }
}
