package oop.ex6.main;

import oop.ex6.codeBlocks.CodeBlock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Sjavac
 */
public class Sjavac {
    private Sjavac(String path) throws IOException {
        try {
            File javaFile = new File(path);
            List<String> allLines = Files.readAllLines(javaFile.toPath());
            String[] lines = new String[allLines.size()];
            lines = allLines.toArray(lines);
            CodeBlock mainBlock = new CodeBlock(null, lines);
        } catch (IOException e) {
            throw new IOException("ERROR: code file invalid or missing");
        }
    }


    public static void main(String[] args) {
        try {
            Sjavac sjavac = new Sjavac(args[0]);
        } catch (IOException e) {
            System.out.println(2);
            System.out.println(e.getMessage());
        }
        System.out.println(0);
    }
}
