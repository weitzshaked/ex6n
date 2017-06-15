package oop.ex6.codeBlocks;

/**
 * Method Class
 */
public class Method extends CodeBlock {

    private String name;
    private int paramNum = 0;
    public static final String parameterPattern = "";

    public Method(CodeBlock parent, String[] codeLines, String name, String parameters) throws Exception{
        super(parent, codeLines);
        //todo split parameters, create
        this.name = name;
        //todo add param to inner variables
        linesToBlocks();
    }



}
