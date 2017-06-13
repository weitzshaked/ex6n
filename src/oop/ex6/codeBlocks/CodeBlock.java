package oop.ex6.codeBlocks;

import oop.ex6.Exceptions.LogicalException;
import oop.ex6.Exceptions.SyntaxException;
import oop.ex6.variables.VariableFactory;
import oop.ex6.variables.Variables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CodeBlocks abstract Class
 */
public abstract class CodeBlock {

    protected List<CodeBlock> innerBlocks;
    protected List<Variables> innerVariables;

    protected CodeBlock parent;
    protected String[] codeLines;

    private Matcher matcher;

    private Pattern pattern;

    public static final String IGNORE_LINE_PATTERN = "^///.+|\\s+";
    //public static final String SEMICOLOMPATTERN = ".*?;/s*";
    public static final String VARIABLE_PATTERN = "(?<final>\\s*final\\s+)?(?<type>\\w*\\s+)(?<nameAndValues>(\\D\\w*(\\s*=\\s*\\.+)?(\\s*,))*" +
            "(\\s*\\D\\w*(\\s*=\\s*.+)?\\s*))(?<ending>;\\s*)";
    //public static final String SINGLE_VARIABLE_PATTERN = "(\\w*\\s+)(\\D\\w*(\\s*=\\s*\\w+)?)";
    public static final String OPEN_BLOCK_PATTERN = ".*?\\{\\s*";
    public static final String CLOSE_BLOCK_PATTERN = "\\s*\\}\\s*";
    public static final String METHOD_CALL_PATTERN = "\\s*(?<methodName>\\D\\w*)\\s*\\((?<param>\\D\\w*,)*\\s*(?<lastParam>\\D\\w*)\\s*;";

    public CodeBlock(CodeBlock parent, String[] codeLines) throws Exception {
        this.parent = parent;
        this.codeLines = codeLines;
        this.innerVariables = new ArrayList<>();
        this.innerBlocks = new ArrayList<>();
        linesToBlocks();
    }

    protected void linesToBlocks() throws Exception {
        int i = 0;
        while (i < codeLines.length) {
            //if line should be ignored (empty or comment);
            if (checkOneLiner(codeLines[i], IGNORE_LINE_PATTERN)) {
                i++;
            } //line is a variable declaration;
            else if (checkOneLiner(codeLines[i], VARIABLE_PATTERN)) {
                parseVariableLine(codeLines[i]);
                i++;
            } //line is a code block;
            else if (checkOneLiner(codeLines[i], OPEN_BLOCK_PATTERN)) {
                parseBlock(i);
            }// TODO handle the situation when line is a method decleration

        }
    }

    public CodeBlock getParent() {
        return parent;
    }

    public List<Variables> getInnerVariables() {
        return innerVariables;
    }
    public Variables hasVariable(String name)throws LogicalException{
        CodeBlock codeBlock = this;
        while (codeBlock.getParent()!= null) {
            for (Variables variable : codeBlock.getInnerVariables()) {
                if (variable.getName().matches(matcher.group("name"))) {
                    return variable;
                }
            }
            codeBlock = codeBlock.getParent();
        } return null;
    }
    /**
     * @param line         to check
     * @param checkPattern to match
     * @return true if line matches pattern
     */
    private boolean checkOneLiner(String line, String checkPattern) {
        pattern = Pattern.compile(checkPattern);
        matcher = pattern.matcher(line);
        return matcher.matches();
    }


    //    private boolean checkMethod(String line) {
//        return true;
//    }
    private void parseBlock(int i) throws Exception {
        try {
            int firstLine = i;
            int openCounter = 1, closedCounter = 0;
            i++;
            while (openCounter != closedCounter && i < codeLines.length) {
                if (checkOneLiner(codeLines[i], OPEN_BLOCK_PATTERN)) {
                    openCounter++;
                } else if (checkOneLiner(codeLines[i], CLOSE_BLOCK_PATTERN)) {
                    closedCounter++;
                }
                i++;
            }
            String[] methodLines = Arrays.copyOfRange(codeLines, firstLine, i - 1);
            innerBlocks.add(BlockFactory.blockFactory(this, codeLines[firstLine], methodLines));
        } catch (Exception e) {
            throw e;
        }
    }

    private void parseVariableLine(String line) throws Exception {
        boolean isFinal = false;
        if (matcher.group("final") != null) {
            isFinal = true;
        }
        String type = matcher.group("type").trim();
        String namesAndValues = matcher.group("nameAndValues").trim();
        String[] nameAndValuesArray = namesAndValues.split(",");
        for (String str : nameAndValuesArray) {
            innerVariables.add(VariableFactory.variableFactory(this, type, isFinal, str));
        }
//        try {
//            for (int i = 0; i < numOfVariables; i++) {
//                innerVariables.add(VariableFactory.variableFactory(type, isFinal, matcher.group(i)));
//            }
//            while (matcher.group("val1") != null) {
//                innerVariables.add(VariableFactory.variableFactory(type, isFinal, matcher.group("val1")));
//            }
//            innerVariables.add(VariableFactory.variableFactory(type, isFinal, matcher.group("mainval")));
//        } catch (Exception e) {
//            System.out.println("bad");
//        }
    }
}