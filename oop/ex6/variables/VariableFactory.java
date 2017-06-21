package oop.ex6.variables;

import oop.ex6.Exceptions.LogicalException;
import oop.ex6.codeBlocks.CodeBlock;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Variable Factory
 */
public class VariableFactory {

    public static final String ERR_VARIABLE_NAME_EXISTS = "variable name already defined ";
    public static final String ERR_VARIABLE_TYPE = "no such variable type ";
    public static final String VARIABLE_PATTERN = "(?<name>\\s*\\D[A-Za-z0-9_]*\\s*)((?<equal>=\\s*)(?<value>.*))?";


    /**
     * factory for creating a variable
     * @param codeBlock
     * @param type
     * @param isFinal
     * @param nameAndVal
     * @return new variable
     * @throws LogicalException
     */
    public static Variables variableFactory(CodeBlock codeBlock, String type, boolean isFinal, String nameAndVal) throws LogicalException {
        Pattern pattern = Pattern.compile(VARIABLE_PATTERN);
        Matcher matcher = pattern.matcher(nameAndVal);
        String val = null;
        if (matcher.matches()) {
            if (matcher.group("value") != null) {
                val = matcher.group("value").trim();
            }
            //checks if name exists in block
            if(codeBlock.findInnerVariable(codeBlock, matcher.group("name"))!= null){
                throw new LogicalException(ERR_VARIABLE_NAME_EXISTS);
            }
        }

        switch (type) {
            case "int":
                return new Variables(codeBlock, type, val, matcher.group("name").trim(), isFinal);
            case "String":
                return new Variables(codeBlock, type, val, matcher.group("name").trim(), isFinal);
            case "double":
                return new Variables(codeBlock, type, val, matcher.group("name").trim(), isFinal);
            case "boolean":
                return new Variables(codeBlock, type, val, matcher.group("name").trim(), isFinal);
            case "char":
                return new Variables(codeBlock, type, val, matcher.group("name").trim(), isFinal);
            default:
                throw new LogicalException(ERR_VARIABLE_TYPE);
        }
    }
}
