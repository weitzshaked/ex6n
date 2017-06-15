package oop.ex6.codeBlocks;

import oop.ex6.Exceptions.LogicalException;
import oop.ex6.Exceptions.SyntaxException;
import oop.ex6.variables.VariableFactory;

import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Method Class
 */
public class Method extends CodeBlock {

    private String name;
    private int paramNum = 0;
    public static final String PARAM_PATTERN = "((?<modifier>final )?\\s*(?<type>\\D+)\\s+(?<name>\\D+[A-Za-z0-9_]*))";
    public static final String PARAM_LINE_PATTERN = "(?<params>" + PARAM_PATTERN + "," + PARAM_PATTERN + ")";
    public static final String GENERIC_PARAM_DATA = "0";

    public Method(CodeBlock parent, String[] codeLines, String name, String parameters, String returnStatement) throws Exception {
        super(parent, codeLines);
        if (returnStatement.equals("void")) {
            this.name = name;
            if (checkOneLiner(parameters, PARAM_LINE_PATTERN)) {
                boolean isFinal;
                String paramsString = matcher.group("params");
                String[] params = paramsString.split(",");
                for (String str : params) {
                    pattern = Pattern.compile(PARAM_PATTERN);
                    matcher = pattern.matcher(str);
                    if (matcher.matches()) {
                        isFinal = false;
                        if (matcher.group("modifier") != null) {
                            isFinal = true;
                        }
                        String type = matcher.group("type");
                        innerVariables.add(VariableFactory.variableFactory(this, type, isFinal, str));
                        innerVariables.get(paramNum).updateData(GENERIC_PARAM_DATA);
                        paramNum++;
                    } else throw new SyntaxException();
                }
            } else if (parameters.trim().length() != 0) {
                throw new SyntaxException();
            }
        }
        else throw new SyntaxException();
    }

    public String getName() {
        return name;
    }

    public void methodCall(String paramLine) throws LogicalException{
        String[] params = paramLine.split(",");
        if(params.length != paramNum){
            throw new LogicalException();
        }
        else{
            for (int i=0; i<paramNum;i++){
                innerVariables.get(i).updateData(params[i]);
            }
        }
        currentLine++;
    }
}
