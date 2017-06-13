package oop.ex6.variables;

import oop.ex6.Exceptions.SyntaxException;
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
        Matcher digits;
        String val;
        if (matcher.matches()) {
            if (matcher.group("value") != null) {
                val = matcher.group("value");
                switch (type) {
                    case "int":
                        pattern = pattern.compile("-?\\d+");
                        digits = pattern.matcher(val);
                        if (digits.matches()) {
                            return new Variables<Integer>(type, Integer.parseInt(val),
                                    matcher.group("name").trim(), isFinal);
                        }
                    case "String":
                        return new Variables<String>(type, val, matcher.group("name"), isFinal);
                    case "double":
                        pattern = pattern.compile("\\d+/.\\d+");
                        digits = pattern.matcher(val);
                        if (digits.matches()) {
                            return new Variables<Double>(type, Double.parseDouble(val), matcher.group("name"), isFinal);
                        }
                    case "boolean":
                        if (val.equals("true") || val.equals("false")) {
                            return new Variables<Boolean>(type, Boolean.getBoolean(val), matcher.group("name"), isFinal);
                        }
                    case "char":
                        if (val.length() == 1) {
                            return new Variables<Character>(type, val.charAt(0), matcher.group("name"), isFinal);
                        }
                    default:
                        throw new LogicalException("1");
                }
            } else {
                throw new SyntaxException("1");
            }
        }
        else {
            //todo
            throw new SyntaxException("1");
        }
    }

}
