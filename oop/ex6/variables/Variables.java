package oop.ex6.variables;

import oop.ex6.Exceptions.LogicalException;
import oop.ex6.codeBlocks.CodeBlock;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Variable Class
 */
public class Variables {

    public static final String ERR_FINAL_INITIALIZE = "final variable must be initialized";
    public static final String ERR_ASSIGNMENT = "bad variable assignment";

    private boolean hasData = false;
    private String name, type;
    private boolean isFinal = false;
    private CodeBlock codeBlock;

    /**
     * enum Types of variables and their regex patterns
     */
    private enum Types {
        INT("int", "-?\\d+"),
        STRING("String", "\".*\""),
        DOUBLE("double", "-?\\d+(\\.\\d+)?"),
        BOOLEAN("boolean", "true|false|\\d+(\\.\\d+)?"),
        CHAR("char", "\'(.|\\w|\\s)?\'");

        private final String pattern, name;

        Types(String typeName, String typePattern) {
            pattern = typePattern;
            name = typeName;
        }
    }

    /**
     * variables constructor
     * @param codeBlock
     * @param type
     * @param data
     * @param name
     * @param isFinal
     * @throws LogicalException
     */
    public Variables(CodeBlock codeBlock, String type, String data, String name, boolean isFinal) throws LogicalException {
        this.name = name;
        this.type = type;
        this.codeBlock = codeBlock;
        if (data != null) {
            updateData(data);
        }
        this.isFinal = isFinal;
        if(isFinal && data == null) throw new LogicalException(ERR_FINAL_INITIALIZE);
    }

    /**
     * updates variable data
     * @param data
     * @throws LogicalException
     */
    public void updateData(String data) throws LogicalException {
        if (isFinal) {
            throw new LogicalException(ERR_ASSIGNMENT);
        } else {
            for (Types types : Types.values()) {
                if (types.name.equals(type)) {
                    Pattern pattern = Pattern.compile(types.pattern);
                    Matcher matcher = pattern.matcher(data);
                    if (matcher.matches()) {
                        this.hasData = true;
                        break;
                    } else {
                        if (canAssign(data, codeBlock, type)) {
                            hasData = true;
                        } else {
                            throw new LogicalException(ERR_ASSIGNMENT);
                        }
                    }
                }

            }
        }
    }

    public boolean isFinal() {
        return isFinal;
    }

    /**
     * checks if assignment is legal
     * @param data
     * @param firstCodeBlock
     * @param typeToMatch
     * @return true if legal, false if not
     * @throws LogicalException
     */
    public static boolean canAssign(String data, CodeBlock firstCodeBlock, String typeToMatch) throws LogicalException {
        Variables variable = firstCodeBlock.findVariable(data);
        if (variable != null) {
            if (variable.hasData()) {
                if (typeToMatch.equals(variable.getType())) {
                    return true;
                } else {
                    switch (typeToMatch) {
                        case "boolean":
                            return variable.getType().equals("int") || variable.getType().equals("double");
                        case "double":
                            return variable.getType().equals("int");
                    }
                }
            }
        }
        throw new LogicalException(ERR_ASSIGNMENT);
    }


    /**
     * checks if variable has data
     * @return boolean
     */
    public boolean hasData() {
        return hasData;
    }

    /**
     * sets hasData to true
     */
    public void setHasData() {
        this.hasData = true;
    }

    /**
     * @return variables name
     */
    public String getName() {
        return name;
    }

    /**
     * @return variables type
     */
    public String getType() {
        return type;
    }
}
