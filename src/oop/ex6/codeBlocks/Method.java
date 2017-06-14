package oop.ex6.codeBlocks;

import oop.ex6.Exceptions.SyntaxException;
import oop.ex6.variables.Variables;

/**
 * Method Class
 */
public class Method extends CodeBlock {

    private String name;
    private int paramNum = 0;

    public Method(CodeBlock parent, String[] codeLines, String name, String parameters) throws Exception{
        super(parent, codeLines);
        //todo split parameters, create
        this.name = name;
        //todo add param to inner variables
        linesToBlocks();
    }
}
