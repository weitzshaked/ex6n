package oop.ex6.codeBlocks;

import oop.ex6.Exceptions.SyntaxException;

/**
 * Condition Block Class
 */
public class ConditionBlock extends CodeBlock {

    private String condition;
    private Type type;

    public enum Type {If, While}


    public ConditionBlock(CodeBlock parent, String[] codeLines, String condition, Type type) throws SyntaxException {
        super(parent, codeLines);
        //todo check condition pattern
        this.condition = condition;
        this.type = type;
    }
}
