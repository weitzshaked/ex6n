//package oop.ex6.codeBlocks;
//
//import oop.ex6.Exceptions.LogicalException;
//import oop.ex6.Exceptions.SyntaxException;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * BlockFactory Class
// */
//public class BlockFactory {
//
//    public static ConditionBlock blockFactory(CodeBlock parent, String condition, String type, String[] innerLines) throws Exception {
//        switch (type) {
//            case "if":
//                return new ConditionBlock(parent, innerLines, condition, type);
//            case "while":
//                return new ConditionBlock(parent, innerLines, condition, type);
//            default:
//                throw new SyntaxException();
//        }
//    }
//
//}
