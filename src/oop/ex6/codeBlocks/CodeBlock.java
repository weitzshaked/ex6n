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
 * this abstract class represents a general code block
 */
public abstract class CodeBlock {
    public static final String IGNORE_LINE_PATTERN = "^\\//.+|\\s*|\\s*return;";
    public static final String VARIABLE_PATTERN = "\\s*(?<final>final\\s+)?(?<type>[A-Za-z]+\\s+)(?<nameAndValues>(_?[A-Za-z0-9_]+(\\s*=\\s*\\.+)?\\s*,)*" +
            "(\\s*_?[A-Za-z0-9]+(\\s*=\\s*.+)?\\s*))(?<ending>;\\s*)";
    public static final String OPEN_BLOCK_PATTERN = ".*?\\{\\s*";
    public static final String CLOSE_BLOCK_PATTERN = "\\s*\\}\\s*";
    public static final String METHOD_CALL_PATTERN = "\\s*(?<methodName>[A-Za-z][A-Za-z0-9_]*\\s*)\\(\\s*(?<params>(.+,)*\\s*(.+)?)\\)\\s*;\\s*";
    public static final String VARIABLE_ASSIGNMENT_PATTERN = "(?<name>\\s*_?[A-Za-z0-9_]+\\s+)((=\\s*(?<value>.+)?\\s*))(?<ending>;\\s*)";
    public static final String METHOD_PATTERN = "\\s*(?<returnStatement>[A-Za-z]+\\s+)(?<name>[A-Za-z][a-zA-Z0-9_]*\\s*)\\(\\s*(?<params>\\w.*\\s*)*\\)\\s*\\{\\s*";
    public static final String CONDITION_PATTERN = "\\s*(?<type>[A-Za-z]+\\s*)\\((?<condition>.*)\\)\\s*\\{\\s*";

    public static final String ERR_METHOD_NOT_IN_GLOBAL_BLOCK = "methods can only be declared within the global block";
    public static final String ERR_VARIABLE_NOT_FOUND = "you can only assign value to an existing variable";
    public static final String ERR_METHOD_CALL_IN_GLOBAL = "methods can not be called in the global scopes";
    public static final String ERR_METHOD_NOT_FOUND = "the method that was called was not found";
    public static final String ERR_INVALID_LINE = "invalid line";

    private List<ConditionBlock> conditions;
    private List<Method> methods;
    List<Variables> innerVariables;
    private CodeBlock parent;
    private String[] codeLines;
    Matcher matcher;
    Pattern pattern;
    static int currentLine = 0;


    public CodeBlock(CodeBlock parent, String[] codeLines) throws LogicalException, SyntaxException {
        this.parent = parent;
        this.codeLines = codeLines;
        innerVariables = new ArrayList<>();
        conditions = new ArrayList<>();
        methods = new ArrayList<>();
    }

    /**
     * parses code lines to variables and blocks.
     *
     * @throws LogicalException whenever a logical error is found while parsing the code.
     * @throws SyntaxException  whenever a syntax error is found while parsing the code.
     */
    public void linesToBlocks() throws LogicalException, SyntaxException {
        currentLine = 0;
        while (currentLine < codeLines.length) {
            //if line should be ignored (empty or comment):
            if (checkOneLiner(codeLines[currentLine], IGNORE_LINE_PATTERN)) {
                currentLine++;
            } //line is a variable declaration:
            else if (checkOneLiner(codeLines[currentLine], VARIABLE_PATTERN)) {
                parseVariableLine();
            } //line is the beginning of a method:
            else if (checkOneLiner(codeLines[currentLine], METHOD_PATTERN)) {
                if (!this.hasParent()) {
                    String methodStatement = codeLines[currentLine];
                    String[] innerCodeLines = parseBlock();
                    //reset matcher to method pattern:
                    checkOneLiner(methodStatement, METHOD_PATTERN);
                    methods.add(new Method(this, innerCodeLines, matcher.group("name").trim(), matcher.group("params"), matcher.group("returnStatement").trim()));
                } else throw new LogicalException(ERR_METHOD_NOT_IN_GLOBAL_BLOCK);
                //line is the beginning of a condition block:
            } else if (checkOneLiner(codeLines[currentLine], CONDITION_PATTERN)) {
                String conditionStatement = codeLines[currentLine];
                String[] codeLines = parseBlock();
                checkOneLiner(conditionStatement, CONDITION_PATTERN);
                conditions.add(new ConditionBlock(this, codeLines, matcher.group("condition").trim(), matcher.group("type").trim()));
            }
            //line is an assignment of an existing variable;
            else if (checkOneLiner(codeLines[currentLine], VARIABLE_ASSIGNMENT_PATTERN)) {
                Variables variable = findVariable(matcher.group("name").trim());
                if (variable != null) {
                    this.innerVariables.add(new Variables(this, variable.getType(), null, variable.getName(), variable.isFinal()));
                    innerVariables.get(innerVariables.size() - 1).updateData(matcher.group("value"));
                    currentLine++;
                } else throw new LogicalException(ERR_VARIABLE_NOT_FOUND);
            }
            // line is a method call:
            else if (checkOneLiner(codeLines[currentLine], METHOD_CALL_PATTERN)) {
                if (!this.hasParent()) {
                    throw new LogicalException(ERR_METHOD_CALL_IN_GLOBAL);
                } else {
                    Method method = findMethod(matcher.group("methodName").trim());
                    if (method != null) {
                        method.methodCall(matcher.group("params"));
                    } else throw new LogicalException(ERR_METHOD_NOT_FOUND);
                }
            } else throw new SyntaxException(ERR_INVALID_LINE);
        }
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

    /**
     * @return block's methods
     */
    public List<Method> getMethods() {
        return methods;
    }

    /**
     * finds a variable by name from current block up to global block
     *
     * @param name the name of the variable to look for.
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
     * finds a variable by name in a specific block.
     *
     * @param codeBlock the relevant CodeBlock.
     * @param name the name of the variable to look for.
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
     * parses a block that starts with '{' and ends with '}'.
     *
     * @return String[] of code lines
     * @throws Exception
     */
    private String[] parseBlock() throws LogicalException {
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
    protected void parseVariableLine() throws LogicalException, SyntaxException {
        boolean isFinal = false;
        if (matcher.group("final") != null) {
            isFinal = true;
        }
        String type = matcher.group("type").trim();
        String namesAndValues = matcher.group("nameAndValues");
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