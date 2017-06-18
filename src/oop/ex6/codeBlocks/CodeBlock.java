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

    protected List<ConditionBlock> conditions;
    protected List<Method> methods;
    protected List<Variables> innerVariables;

    protected CodeBlock parent;
    protected String[] codeLines;

    protected Matcher matcher;

    protected Pattern pattern;

    public static final String IGNORE_LINE_PATTERN = "^\\//.+|\\s*|\\s*return;";
    public static final String VARIABLE_PATTERN = "(?<final>\\s*final\\s+)?(?<type>\\w*\\s+)(?<nameAndValues>(\\D\\w*(\\s*=\\s*\\.+)?(\\s*,))*" +
            "(\\s*\\D\\w*(\\s*=\\s*.+)?\\s*))(?<ending>;\\s*)";
    public static final String OPEN_BLOCK_PATTERN = ".*?\\{\\s*";
    public static final String CLOSE_BLOCK_PATTERN = "\\s*\\}\\s*";
    public static final String METHOD_CALL_PATTERN = "\\s*(?<methodName>\\D[A-Za-z0-9_]*\\s*)\\((?<params>(\\w+,)*\\s*(\\w+)?)\\)\\s*;";
    public static final String VARIABLE_ASSIGNMENT_PATTERN = "(?<name>\\s*\\D[A-Za-z0-9_]*)((\\s*=\\s*(?<value>.+)?\\s*))(?<ending>;\\s*)";
    public static final String METHOD_PATTERN = "\\s*(?<returnStatement>\\D+\\s+)(?<name>\\D[a-zA-Z0-9_]*)\\((?<params>\\w.*\\s*)*\\)\\s*\\{\\s*";
    public static final String CONDITION_PATTERN = "\\s*(?<type>\\D+\\s*)(\\((?<condition>\\w.*?)\\))\\s*\\{\\s*";

    //    private static boolean isGlobal = true;
    protected static int currentLine = 0;


    public CodeBlock(CodeBlock parent, String[] codeLines) throws Exception {
        this.parent = parent;
        this.codeLines = codeLines;
        this.innerVariables = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.methods = new ArrayList<>();
    }


    /**
     * parses code lines to blocks
     *
     * @throws Exception
     */
    public void linesToBlocks() throws Exception {
        currentLine = 0;
        while (currentLine < codeLines.length) {
            //if line should be ignored (empty or comment);
            if (checkOneLiner(codeLines[currentLine], IGNORE_LINE_PATTERN)) {
                currentLine++;
            } //line is a variable declaration;
            else if (checkOneLiner(codeLines[currentLine], VARIABLE_PATTERN)) {
                parseVariableLine();
            } //line is the beginning of a method;
            else if (checkOneLiner(codeLines[currentLine], METHOD_PATTERN)) {
                if (!this.hasParent()) {
                    String methodStatement = codeLines[currentLine];
                    String[] innerCodeLines = parseBlock();
                    //reset matcher to method pattern
                    checkOneLiner(methodStatement, METHOD_PATTERN);
                    this.methods.add(new Method(this, innerCodeLines, matcher.group("name"), matcher.group("params"), matcher.group("returnStatement")));
                } else throw new LogicalException("method declared in wrong block " + currentLine);
                //line is the beginning of a condition block;
            } else if (checkOneLiner(codeLines[currentLine], CONDITION_PATTERN)) {
                String conditionStatement = codeLines[currentLine];
                String[] codeLines = parseBlock();
                //reset to condition pattern
                checkOneLiner(conditionStatement, CONDITION_PATTERN);
                conditions.add(new ConditionBlock(this, codeLines, matcher.group("condition"), matcher.group("type")));
            }//line is an assignment of an existing variable;
            else if (checkOneLiner(codeLines[currentLine], VARIABLE_ASSIGNMENT_PATTERN)) {
                Variables variable = findVariable(matcher.group("name"));
                if (variable != null) {
                    variable.updateData(matcher.group("value"));
                    currentLine++;
                } else throw new LogicalException("no such variable " + currentLine);
            }
            // line is a call to a method
            else if (checkOneLiner(codeLines[currentLine], METHOD_CALL_PATTERN)) {
                if (!this.hasParent()) {
                    throw new LogicalException("method called in global scope");
                } else {
                    Method method = findMethod(matcher.group("methodName"));
                    if (method != null) {
                        method.methodCall(matcher.group("params"));
                    } else throw new LogicalException("no such method " + currentLine + 1);
                }
            } else throw new SyntaxException("bad line syntax " + (currentLine + 1));
        }
//        isGlobal = false;
        for (Method method : methods) {
            method.linesToBlocks();
        }
        for (ConditionBlock block : conditions) {
            block.linesToBlocks();
        }
    }

    /**
     * @return block's parent block
     */
    public CodeBlock getParent() {
        return parent;
    }

    /**
     * @return block's inner variables
     */
    public List<Variables> getInnerVariables() {
        return innerVariables;
    }

    public List<ConditionBlock> getConditions() {
        return conditions;
    }

    /**
     * @return block's methods
     */
    public List<Method> getMethods() {
        return methods;
    }

    /**
     * finds a variable by name from current block up to global block
     *
     * @param name
     * @return the variable if found, null otherwise
     */
    public Variables findVariable(String name) {
        CodeBlock codeBlock = this;
        Variables variable;
        while (codeBlock.getInnerVariables() != null) {
            variable = findInnerVariable(codeBlock, name);
            if (variable != null) {
                return variable;
            }
            if (codeBlock.getParent() != null) {
                codeBlock = codeBlock.getParent();
            } else return null;
        }
        return null;
    }

    /**
     * finds a variable by name in block received
     *
     * @param codeBlock
     * @param name
     * @return variable if found, null otherwise
     */
    public Variables findInnerVariable(CodeBlock codeBlock, String name) {
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

    /**
     * parses a block from opening to close brackets
     *
     * @return String[] of code lines
     * @throws Exception
     */
    private String[] parseBlock() throws Exception {
        int firstLine = currentLine + 1;
        int openCounter = 1, closedCounter = 0;
        currentLine++;
        while (openCounter != closedCounter && currentLine < codeLines.length) {
            if (checkOneLiner(codeLines[currentLine], OPEN_BLOCK_PATTERN)) {
                openCounter++;
            } else if (checkOneLiner(codeLines[currentLine], CLOSE_BLOCK_PATTERN)) {
                closedCounter++;
            }
            currentLine++;
        }
        return Arrays.copyOfRange(codeLines, firstLine, currentLine - 1);
    }

    /**
     * parses variable line, adds variable to block
     *
     * @throws Exception
     */
    protected void parseVariableLine() throws Exception {
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
        currentLine++;
    }

    /**
     * checks if block has parent
     *
     * @return boolean
     */
    protected boolean hasParent() {
        return parent != null;
    }

    /**
     * finds a method by name from current block up to global block
     *
     * @param name
     * @return method if found, null otherwise
     */
    protected Method findMethod(String name) {
        CodeBlock codeBlock = this;
        while (codeBlock.hasParent()) {
            codeBlock = codeBlock.getParent();
            for (Method method : codeBlock.getMethods()) {
                if (method.getName().equals(name)) {
                    return method;
                }
            }
        }
        return null;
    }
}