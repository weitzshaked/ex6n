package oop.ex6.main;

import oop.ex6.codeBlocks.CodeBlock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Sjavac
 */
public class Sjavac
{
    public Sjavac(String path){
        try {
            File javaFile = new File(path);
            List<String> allLines = Files.readAllLines(javaFile.toPath());
            String[] lines = new String[allLines.size()];
            lines = allLines.toArray(lines);
            CodeBlock mainBlock = new CodeBlock(null, lines);
        }
        catch (IOException e){
            System.out.println(2);
        }
    }


    public static void main(String[] args) {
        Sjavac sjavac = new Sjavac(args[0]);
        System.out.println(0);
    }
}
