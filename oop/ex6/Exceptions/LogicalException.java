package oop.ex6.Exceptions;

/**
 * Logical Exception
 */
public class LogicalException extends Exception {

    public LogicalException(String message){
        System.err.println(message);
    }

}
