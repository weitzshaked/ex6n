package oop.ex6.codeBlocks;

/**
 * Method Class
 */
public class Method extends CodeBlock {

    private String name;
    private int paramNum = 0;
    public static final String parameterPattern = "\\s*((?<type>\\D+)\\s(?<\\D+\\.+>))*";

    public Method(CodeBlock parent, String[] codeLines, String name, String parameters, String returnStatement) throws Exception{
        super(parent, codeLines);
        this.name = name;
        //todo split parameters, create

        this.name = name;
        //todo add param to inner variables
    }



}
