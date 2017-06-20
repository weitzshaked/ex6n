package oop.ex6.main;

import oop.ex6.Exceptions.LogicalException;
import oop.ex6.Exceptions.SyntaxException;
import oop.ex6.codeBlocks.CodeBlock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Sjavac
 */
public class Sjavac {


    private Sjavac(String path) throws Exception {
        try {
            File javaFile = new File(path);
            List<String> allLines = Files.readAllLines(javaFile.toPath());
            String[] lines = new String[allLines.size()];
            lines = allLines.toArray(lines);
//            CodeBlock mainBlock = GlobalBlock.getInstance(null, lines);
            CodeBlock mainBlock = new CodeBlock(null, lines) {
            };
            mainBlock.linesToBlocks();
        } catch (IOException e) {
            throw new IOException("ERROR: code file invalid or missing");
        } catch (SyntaxException e) {
            throw e;
        } catch (LogicalException e) {
            throw e;
        }
    }


    public static void main(String[] args) {
        try {
            Sjavac sjavac = new Sjavac(args[0]);
            if (!args[0].endsWith(".sjava")) throw new IOException();
            System.out.println(0);
        } catch (IOException e) {
            System.out.println(2);
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(1);
            System.err.println(e);
        }
    }
}
