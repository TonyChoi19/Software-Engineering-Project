import java.io.Serializable;

public class Function extends Square implements Serializable {

    public Function(int pos, String name) {
        this.pos = pos;
        this.name = name;
    }

    @Override
    protected boolean isFunction() {
        return true;
    }
}
