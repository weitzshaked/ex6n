package oop.ex6.variables;

/**
 * Variable Class
 */
public class Variables<T> {

    private T data;
    private String name, type;
    private boolean isFinal;

    public Variables(String type, T data, String name, boolean isFinal){
        this.data = data;
        this.name = name;
        this.type = type;
        this.isFinal = isFinal;
    }


}
