package oop.ex6.variables;

import oop.ex6.Exceptions.LogicalException;
import oop.ex6.Exceptions.SyntaxException;
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

    private enum Types{
        INT("int","-?\\d+"),
        STRING("String",".*"),
        DOUBLE("double","\\d+/.\\d+"),
        BOOLEAN("boolean","true | false"),
        CHAR("char",". | \\w | \\s");

        private final String pattern, name;

        Types(String typeName, String typePattern){
            pattern = typePattern;
            name = typeName;
        }
    }

    public Variables(CodeBlock codeBlock, String type, String data, String name, boolean isFinal) throws Exception{
        this.codeBlock = codeBlock;
        //todo final
        if(data != null) {
            for (Types types: Types.values()) {
                if(types.name.equals(type)){
                    Pattern pattern = Pattern.compile(types.pattern);
                    Matcher matcher = pattern.matcher(data);
                    if(matcher.matches()){
                        this.hasData = true;
                        break;
                    }
                    //todo if a = b
                    else {
                        Variables variable = codeBlock.hasVariable(data);
                        if(variable!= null){
                            if(variable.getType().equals(type)){
                                hasData = true;
                            }
                        }
                        else {
                            throw new LogicalException();
                        }
                    }
                }
            }
        }
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
