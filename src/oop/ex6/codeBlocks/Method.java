package oop.ex6.codeBlocks;

import oop.ex6.variables.Variables;

/**
 * Method Class
 */
public class Method extends CodeBlock {

    private String name;
    private Variables[] parameters;

    public Method(CodeBlock parent, String[] codeLines, String name, String parameters) {
        super(parent, codeLines);
        //todo split parameters, create
        this.name = name;
    }

}
