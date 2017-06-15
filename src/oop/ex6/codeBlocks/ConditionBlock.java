package oop.ex6.codeBlocks;

import oop.ex6.Exceptions.LogicalException;
import oop.ex6.variables.Variables;


/**
 * Condition Block Class
 */
public class ConditionBlock extends CodeBlock {

    private String[] conditions;
    private String type;

    public static final String conditionPattern = "(true|false|(\\d+(\\.\\d+)?)";


    /**
     * creates a new conditionsLine block
     *
     * @param parent         block
     * @param codeLines      inner code lines
     * @param conditionsLine block's conditionsLine
     * @param type           if/ while block
     * @throws Exception
     */
    public ConditionBlock(CodeBlock parent, String[] codeLines, String conditionsLine, String type) throws Exception {
        super(parent, codeLines);
        this.type = type;
        this.conditions = conditionsLine.split("(\\|{2})|(&{2})");
        for (String condition : conditions) {
            if (!checkOneLiner(condition, conditionPattern) ||
                    !Variables.canAssign(condition, parent, "boolean")) {
                throw new LogicalException();
            }
        }
    }
}
