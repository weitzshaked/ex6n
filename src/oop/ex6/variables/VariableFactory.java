package oop.ex6.variables;

import oop.ex6.Exceptions.LogicalException;
import oop.ex6.codeBlocks.CodeBlock;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Variable Factory
 */
public class VariableFactory {

    public static Variables variableFactory(CodeBlock codeBlock, String type, boolean isFinal, String nameAndVal) throws Exception {
        Pattern pattern = Pattern.compile("(?<name>\\s*\\D[A-Za-z0-9_]*\\s*)((?<equal>=\\s*)(?<value>.*))?");
        Matcher matcher = pattern.matcher(nameAndVal);
        String val = null;
        if (matcher.matches()) {
            if (matcher.group("value") != null) {
                val = matcher.group("value").trim();
            }
            //checks if name exists in block
            if(codeBlock.findInnerVariable(codeBlock, matcher.group("name"))!= null){
                throw new LogicalException("variable name already defined ");
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
                throw new LogicalException("no such variable type ");
        }
    }
}
