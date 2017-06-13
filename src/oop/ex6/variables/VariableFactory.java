package oop.ex6.variables;

import oop.ex6.Exceptions.LogicalException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Variable Factory
 */
public class VariableFactory {

    public static Variables variableFactory(String type, boolean isFinal, String nameAndVal) throws Exception {
        Pattern pattern = Pattern.compile("(?<name>\\s*\\w+)((?<equal>\\s*=\\s*)(?<value>.+\\w+.*))?");
        Matcher matcher = pattern.matcher(nameAndVal);
        String val = null;
        if (matcher.matches()) {
            if (matcher.group("value") != null) {
                val = matcher.group("value");
            }
        }
        //todo check if name exists
        switch (type) {
            case "int":
                return new Variables(type, val, matcher.group("name").trim(), isFinal);
            case "String":
                return new Variables(type, val, matcher.group("name"), isFinal);
            case "double":
                return new Variables(type, val, matcher.group("name"), isFinal);
            case "boolean":
                return new Variables(type, val, matcher.group("name"), isFinal);
            case "char":
                return new Variables(type, val, matcher.group("name"), isFinal);
            default:
                throw new LogicalException();
        }
    }
}
