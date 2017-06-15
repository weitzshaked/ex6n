package oop.ex6.codeBlocks;

import oop.ex6.Exceptions.LogicalException;
import oop.ex6.Exceptions.SyntaxException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BlockFactory Class
 */
public class BlockFactory {

    public static ConditionBlock blockFactory(CodeBlock parent, String line, String[] innerLines, boolean isGlobal) throws Exception {
        Pattern pattern = Pattern.compile("\\s*(?<blocktype>\\w+)\\s*(?<name>\\D[a-zA-Z0-9_]*)?(\\((?<params>\\w.*?)\\))\\s*\\{\\s*");
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            switch (matcher.group("blocktype")) {
                case "if":
                    return new ConditionBlock(parent, innerLines, matcher.group(2), ConditionBlock.Type.If);
                case "while":
                    return new ConditionBlock(parent, innerLines, matcher.group(2), ConditionBlock.Type.While);
                default:
                    throw new SyntaxException();
            }
        } else {
            throw new SyntaxException();
            //"No such Block Type"
        }
    }

}
