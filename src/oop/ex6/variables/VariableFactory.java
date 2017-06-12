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
        Pattern pattern = Pattern.compile("(?<name>\\s*\\w+)(?<equal>\\s*=\\s*)?(?<val>.?\\w+.*)?");
        Matcher matcher = pattern.matcher(nameAndVal);
        String val;
        System.out.println(matcher.group("val"));
        if(matcher.group("val")!= null) {
            val = matcher.group("val");
            switch (type) {
                case "int":
                    pattern = pattern.compile("-?\\d+");
                    matcher = pattern.matcher(val);
                    if(matcher.matches()) {
                        return new Variables<Integer>(type, Integer.parseInt(val), matcher.group("name"), isFinal);
                    }
                case "String":
                    return new Variables<String>(type, val,matcher.group("name"),isFinal);
                case "double":
                    pattern = pattern.compile("\\d+/.\\d+");
                    matcher = pattern.matcher(val);
                    if(matcher.matches()) {
                        return new Variables<Double>(type, Double.parseDouble(val), matcher.group("name"), isFinal);
                    }
                case "boolean":
                    if(val.equals("true")||val.equals("false")) {
                        return new Variables<Boolean>(type, Boolean.getBoolean(val), matcher.group("name"), isFinal);
                    }
                case "char":
                    if(val.length()==1) {
                        return new Variables<Character>(type, val.charAt(0), matcher.group("name"), isFinal);
                    }
                default:
                    throw new LogicalException("1");
            }
        }
        else {
            throw new SyntaxException("1");
        }
    }

}
