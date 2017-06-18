package oop.ex6.codeBlocks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Netta on 13/06/2017.
 */
public class GlobalBlock extends CodeBlock {

//    private static GlobalBlock instance = null;
    protected List<Method> methods;

    //TODO think how we can make sure that no method is called in the global scope.
    public GlobalBlock(CodeBlock parent, String[] codeLines) throws Exception{
        super(parent,codeLines);
        this.methods = new ArrayList<>();
    }

//    public static GlobalBlock getInstance(CodeBlock parent, String[] codeLines)throws Exception {
//        if(instance == null) {
//            instance = new GlobalBlock(parent,codeLines);
//        }
//        return instance;
//    }
}

