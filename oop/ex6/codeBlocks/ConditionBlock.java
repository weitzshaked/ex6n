package oop.ex6.codeBlocks;

import oop.ex6.Exceptions.LogicalException;
import oop.ex6.Exceptions.SyntaxException;
import oop.ex6.variables.Variables;


/**
 * Condition Block Class
 */
public class ConditionBlock extends CodeBlock {

    private String[] conditions;
    private String type;

    public static final String CONDITION_PATTERN = "\\s*(true|false|(\\d+(\\.\\d+)?))\\s*";
    public static final String SEPARATOR ="(\\|{2})|(&{2})";
    public static final String ERR_CONDITION = "bad condition ";

    /**
     * creates a new conditionsLine block
     *
     * @param parent         block
     * @param codeLines      inner code lines
     * @param conditionsLine block's conditionsLine
     * @param type           if/ while block
     * @throws Exception
     */
    public ConditionBlock(CodeBlock parent, String[] codeLines, String conditionsLine, String type) throws LogicalException, SyntaxException {
        super(parent, codeLines);
        this.type = type;
        this.conditions = conditionsLine.split(SEPARATOR);
        for (String condition : conditions) {
            if (!checkOneLiner(condition, CONDITION_PATTERN)) {
                if (!Variables.canAssign(condition.trim(), parent, "boolean")) {
                    throw new LogicalException(ERR_CONDITION);
                }
            }
        }
    }
}
