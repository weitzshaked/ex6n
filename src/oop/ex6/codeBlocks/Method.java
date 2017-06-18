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
    public static final String PARAM_PATTERN = "((?<modifier>final )?\\s*(?<type>[A-Za-z]+\\s+)(?<name>[A-Za-z][A-Za-z0-9_]*\\s*))";
    public static final String PARAM_LINE_PATTERN = "(?<params>((?<modifier>final )?\\s*(?<type>\\D+)\\s+(?<name>\\D+[A-Za-z0-9_]*),)*((final )?\\s*(\\D+)\\s+(\\D+[A-Za-z0-9_]*)))";
    public static final String GENERIC_PARAM_DATA = "0";
    public static final String RETURN = "\\s*return;";

    /**
     * @param parent
     * @param codeLines
     * @param name
     * @param parameters
     * @param returnStatement
     * @throws Exception
     */
    public Method(CodeBlock parent, String[] codeLines, String name, String parameters, String returnStatement) throws Exception {
        super(parent, codeLines);
        pattern = Pattern.compile(RETURN);
        matcher = pattern.matcher(codeLines[codeLines.length - 1]);
        if (!matcher.matches()) throw new SyntaxException("no return " + currentLine--);
        if (returnStatement.trim().equals("void")) {
            this.name = name.trim();
            if (parameters != null) {
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
                            String type = matcher.group("type").trim();
                            innerVariables.add(VariableFactory.variableFactory(this, type, isFinal, matcher.group("name")));
                            innerVariables.get(paramNum).setHasData();
                            paramNum++;
                        } else throw new SyntaxException("bad params ");
                    }
                }
            }
//            } else if (parameters.trim().length() != 0) {
//                throw new SyntaxException("bad params syntax ");
//            }
        } else throw new SyntaxException("void missing ");
    }


    /**
     * @return method name
     */
    public String getName() {
        return name;
    }

    /**
     * checks method call
     *
     * @param paramLine
     * @throws LogicalException
     */
    public void methodCall(String paramLine) throws LogicalException {
        String[] params = paramLine.split(",");
        if (paramLine.equals("") && paramNum == 0) {
            currentLine++;
            return;
        } else if (paramNum != params.length) {
            throw new LogicalException("wrong num of params " + currentLine);
        } else {
            for (int i = 0; i < paramNum; i++) {
                innerVariables.get(i).updateData(params[i].trim());
            }
        }

        currentLine++;
    }
}
