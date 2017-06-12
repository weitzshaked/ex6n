package oop.ex6.codeBlocks;

/**
 * Condition Block Class
 */
public class ConditionBlock extends CodeBlock {

    private String condition;
    public enum Type {If,While}
    private Type type;

    public ConditionBlock(CodeBlock parent, String[] codeLines, String condition, Type type){
        super(parent, codeLines);
        //todo check condition pattern
        this.condition = condition;
        this.type = type;
    }
}
