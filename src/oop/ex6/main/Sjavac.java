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

    public static final String ERR_IO = "ERROR: code file invalid or missing";

    /**
     * creates a CodeBlock from a sjava file
     * @param path to file
     * @throws Exception
     */
    private Sjavac(String path) throws Exception {
        try {
            File javaFile = new File(path);
            List<String> allLines = Files.readAllLines(javaFile.toPath());
            String[] lines = new String[allLines.size()];
            lines = allLines.toArray(lines);
            CodeBlock mainBlock = new CodeBlock(null, lines);
            mainBlock.linesToBlocks();}
        catch (IOException e) {
            throw new IOException(ERR_IO);
        } catch (SyntaxException e) {
            throw e;
        } catch (LogicalException e) {
            throw e;
        }
    }


    public static void main(String[] args) {
        try {
            Sjavac sjavac = new Sjavac(args[0]);
            if (!args[0].endsWith(".sjava")) throw new IOException(ERR_IO);
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
