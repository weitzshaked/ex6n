package oop.ex6.codeBlocks;


/**
 * Condition Block Class
 */
public class ConditionBlock extends CodeBlock {

    private String condition;
    private Type type;

    public enum Type {If, While}

    /**
     * creates a new condition block
     * @param parent block
     * @param codeLines inner code lines
     * @param condition block's condition
     * @param type if/ while block
     * @throws Exception
     */
    public ConditionBlock(CodeBlock parent, String[] codeLines, String condition, Type type) throws Exception {
        super(parent, codeLines);
        //todo check condition pattern
        this.condition = condition;
        this.type = type;
    }
}
