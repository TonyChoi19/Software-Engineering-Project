package main;

import java.io.Serializable;

public class Function extends Square implements Serializable {

    /**
     * Constructor
     * @param pos The position of the function square
     * @param name The name of the function square
     */
    public Function(int pos, String name) {
        this.pos = pos;
        this.name = name;
    }

    @Override
    protected boolean isFunction() {
        return true;
    }
}
