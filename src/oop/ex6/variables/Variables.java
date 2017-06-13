package oop.ex6.variables;

import oop.ex6.Exceptions.SyntaxException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Variable Class
 */
public class Variables {

    private boolean hasData=false;
    private String name, type;
    private boolean isFinal = false;

    private enum Types{
        INT("int","-?\\d+"),
        STRING("String",""),
        DOUBLE("double","\\d+/.\\d+"),
        BOOLEAN("boolean","true | false"),
        CHAR("char",". | \\w | \\s");

        private final String pattern, name;

        Types(String typeName, String typePattern){
            pattern = typePattern;
            name = typeName;
        }

    }


    public Variables(String type, String data, String name, boolean isFinal) throws SyntaxException{
        if(data != null) {
            for (Types types: Types.values()) {
                if(types.name.equals(type)){
                    Pattern pattern = Pattern.compile(types.pattern);
                    Matcher matcher = pattern.matcher(data);
                    if(matcher.matches()){
                        this.hasData = true;
                        break;
                    }
                    else {
                        throw new SyntaxException();
                    }
                }
            }
        }
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
    }

}
