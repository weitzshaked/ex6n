package oop.ex6.codeBlocks;

import oop.ex6.Exceptions.SyntaxException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BlockFactory Class
 */
public class BlockFactory {

    //todo switch case method, condition
    public static CodeBlock blockFactory(CodeBlock parent, String line, String[] innerLines) throws SyntaxException {
        Pattern pattern = Pattern.compile("/s*(/w+)/s*(\\(/w.*?\\))/s*\\{/s*");
        Matcher matcher = pattern.matcher(line);
        if(matcher.matches()) {
            switch (matcher.group(1)) {
                case "if":
                    return new ConditionBlock(parent, innerLines, matcher.group(2), ConditionBlock.Type.If);
                case "while":
                    return new ConditionBlock(parent, innerLines, matcher.group(2), ConditionBlock.Type.While);
                default:
                    return new Method(parent, innerLines, matcher.group(1), matcher.group(2));
            }
        }
        else {
            throw new SyntaxException("No such Block Type");
        }
    }

}
