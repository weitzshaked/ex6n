package oop.ex6.codeBlocks;

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
public class CodeBlock {

    protected List<CodeBlock> inerBlocks;
    protected List<Variables> inerVariables;

    protected CodeBlock parent;
    protected String[] codeLines;

    private Matcher matcher;
    private Pattern pattern;

    public static final String IGNOREPATTERN = "^///.+|\\s+";
    //public static final String SEMICOLOMPATTERN = ".*?;/s*";
    public static final String VARIABLEPATTERN = "(?<final>\\s*final\\s+)?(?<type>\\w*\\s+)(?<val1>\\D\\w*(\\s*=\\s*\\.+)?(\\s*,))*" +
            "(?<mainval>\\s*\\D\\w*(\\s*=\\s*.+)?\\s*)(?<ending>;\\s*)";
    public static final String SINGLEVARIABLE = "(\\w*\\s+)(\\D\\w*(\\s*=\\s*\\w+)?)";
    public static final String OPENMETHOD = ".*?\\{\\s*";
    public static final String CLOSEDMETHOD = "\\s*\\}\\s*";

    public CodeBlock(CodeBlock parent, String[] codeLines) throws SyntaxException {
        this.parent = parent;
        this.codeLines = codeLines;
        this.inerVariables = new ArrayList<>();
        this.inerBlocks = new ArrayList<>();
        linesToBlocks();
    }

    protected void linesToBlocks() throws SyntaxException{
        int i = 0;
        while (i < codeLines.length) {
            if (checkOneLiner(codeLines[i], IGNOREPATTERN)) {
                i++;
            } else if (checkOneLiner(codeLines[i], VARIABLEPATTERN)) {
                parseVariableLine(codeLines[i]);
                i++;
            } else {
                if (checkOneLiner(codeLines[i], OPENMETHOD)) {
                    try {
                        int firstLine = i;
                        int openCounter = 1, closedCounter = 0;
                        i++;
                        while (openCounter != closedCounter && i < codeLines.length) {
                            if (checkOneLiner(codeLines[i], OPENMETHOD)) {
                                openCounter++;
                            } else if (checkOneLiner(codeLines[i], CLOSEDMETHOD)) {
                                closedCounter++;
                            }
                            i++;
                        }
                        String[] methodLines = Arrays.copyOfRange(codeLines, firstLine, i - 1);
                        inerBlocks.add(BlockFactory.blockFactory(this, codeLines[firstLine], methodLines));
                    } catch (SyntaxException e) {
                        throw e;
                    }
                }

            }
        }
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


    private boolean checkMethod(String line) {
        //todo
        return true;
    }

    private void parseVariableLine(String line) {
        boolean isFinal = false;
        if (matcher.group("final") != null) {
            isFinal = true;
        }
        String type = matcher.group("type").trim();
        try {
            //todo run on all val1
            while (matcher.group("val1") != null) {
                inerVariables.add(VariableFactory.variableFactory(type, isFinal, matcher.group("val1")));
            }
            inerVariables.add(VariableFactory.variableFactory(type, isFinal, matcher.group("mainval")));
        } catch (Exception e) {
            System.out.println("bad");
        }
    }
}

