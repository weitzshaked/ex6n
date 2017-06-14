package oop.ex6.codeBlocks;

import oop.ex6.variables.Variables;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Condition Block Class
 */
public class ConditionBlock extends CodeBlock {

    private String condition;
    private Type type;

    public static final String conditionPattern = "true|false|(\\d+(\\.\\d+)?)";

    public enum Type {
        If,
        While
    }

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
        if(checkOneLiner(condition, conditionPattern)){
            this.condition = condition;
        }
        else {
            Variables variable = findVariable(condition.trim());
            if (variable != null){
                if (variable.canAssign(condition.trim())){
                    this.condition = condition;
                }

            }

        }
        this.type = type;
    }

}
