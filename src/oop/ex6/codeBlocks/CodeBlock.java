package oop.ex6.codeBlocks;

import com.sun.org.apache.xpath.internal.operations.Variable;
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
    public static final String VARIABLE_ASSIGNMENT_PATTERN = "(?<name>\\s*\\D\\w*)((\\s*=\\s*(?<value>.+)?\\s*))(?<ending>;\\s*)";

    private static boolean isGlobal = true;

    public CodeBlock(CodeBlock parent, String[] codeLines) throws Exception {
        this.parent = parent;
        this.codeLines = codeLines;
        this.innerVariables = new ArrayList<>();
        this.innerBlocks = new ArrayList<>();
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
            } //line is the beginning of a code block;
            else if (checkOneLiner(codeLines[i], OPEN_BLOCK_PATTERN)) {
                i = parseBlock(i);
                //line is an assignment of an existing variable:
            } else if (checkOneLiner(codeLines[i], VARIABLE_ASSIGNMENT_PATTERN)) {
                Variables variable = findVariable(matcher.group("name"));
                if(variable != null){
                    variable.updateData(matcher.group("value"));
                    i++;
                }
                else throw new LogicalException();
            }
            // line is a call to a method
            else if (checkOneLiner(codeLines[i], METHOD_CALL_PATTERN)){
                if(isGlobal){
                    throw new LogicalException();
                }
                else {

                }
            }
            isGlobal = false;
        }
        for (CodeBlock block:innerBlocks){
            block.linesToBlocks();
        }
    }

    public CodeBlock getParent() {
        return parent;
    }

    public List<Variables> getInnerVariables() {
        return innerVariables;
    }

    public List<CodeBlock> getInnerBlocks() {
        return innerBlocks;
    }

    public Variables findVariable(String name) {
        CodeBlock codeBlock = this;
        Variables variable;
        while (codeBlock.getInnerVariables() != null) {
            variable = findInnerVariable(codeBlock, name);
            if(variable != null){
                return variable;
            }
            if (codeBlock.getParent() != null) {
                codeBlock = codeBlock.getParent();
            } else return null;
        }
        return null;
    }

    public Variables findInnerVariable(CodeBlock codeBlock, String name){
        for (Variables variable : codeBlock.getInnerVariables()) {
            if (variable.getName().equals(name)) {
                return variable;
            }
        }
        return null;
    }

    /**
     * @param line         to check
     * @param checkPattern to match
     * @return true if line matches pattern
     */
    protected boolean checkOneLiner(String line, String checkPattern) {
        pattern = Pattern.compile(checkPattern);
        matcher = pattern.matcher(line);
        return matcher.matches();
    }


    //    private boolean checkMethod(String line) {
//        return true;
//    }
    private int parseBlock(int i) throws Exception {
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
            innerBlocks.add(BlockFactory.blockFactory(this, codeLines[firstLine], methodLines, isGlobal));
        } catch (Exception e) {
            throw e;
        }
        return i;
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
    }

//    protected Method findMethod() {
//        CodeBlock block = this;
//        for (b method:block.getInnerBlocks())
//    }
}