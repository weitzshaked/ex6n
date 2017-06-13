package oop.ex6.codeBlocks;

import oop.ex6.Exceptions.SyntaxException;

/**
 * Created by Netta on 13/06/2017.
 */
public class GlobalBlock extends CodeBlock {
    private static GlobalBlock instance = null;
    //TODO think how we can make sure that no method is called in the global scope.
    private GlobalBlock(CodeBlock parent, String[] codeLines) throws SyntaxException{
        super(parent,codeLines);
    }
    public static GlobalBlock getInstance(CodeBlock parent, String[] codeLines)throws SyntaxException {
        if(instance == null) {
            instance = new GlobalBlock(parent,codeLines);
        }
        return instance;
    }
}
//3241412
