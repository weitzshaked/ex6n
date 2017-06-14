package oop.ex6.variables;

import oop.ex6.Exceptions.LogicalException;
import oop.ex6.codeBlocks.CodeBlock;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Variable Class
 */
public class Variables {


    private boolean hasData = false;
    private String name, type;
    private boolean isFinal = false;
    private CodeBlock codeBlock;

    private enum Types {
        INT("int", "-?\\d+"),
        STRING("String", "\".*\""),
        DOUBLE("double", "\\d+(\\.\\d+)?"),
        BOOLEAN("boolean", "true|false|\\d+(\\.\\d+)?"),
        CHAR("char", "\'(. | \\w | \\s)?\'");

        private final String pattern, name;

        Types(String typeName, String typePattern) {
            pattern = typePattern;
            name = typeName;
        }
    }

    public Variables(CodeBlock codeBlock, String type, String data, String name, boolean isFinal) throws Exception {
        this.name = name;
        this.type = type;
        this.codeBlock = codeBlock;
        if (data != null) {
            updateData(data);
        }
        this.isFinal = isFinal;
    }

    public void updateData(String data) throws LogicalException {
        if (isFinal) {
            throw new LogicalException();
        } else {
            for (Types types : Types.values()) {
                if (types.name.equals(type)) {
                    Pattern pattern = Pattern.compile(types.pattern);
                    Matcher matcher = pattern.matcher(data);
                    if (matcher.matches()) {
                        this.hasData = true;
                        break;
                    } else {
                        if (canAssign(data)) {
                            hasData = true;
                        } else {
                            throw new LogicalException();
                        }
                    }
                }

            }
        }
    }

    public boolean canAssign(String data) throws LogicalException {
        Variables variable = codeBlock.findVariable(data);
        if (variable != null) {
            if (variable.hasData()) {
                if (type.equals(variable.getType())) {
                    return true;
                } else {
                    switch (type) {
                        case "boolean":
                            return variable.getType().equals("int") || variable.getType().equals("double");
                        case "double":
                            return variable.getType().equals("int");
                    }
                }
            }
        }
        throw new LogicalException();
    }


    public boolean hasData() {
        return hasData;
    }


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
