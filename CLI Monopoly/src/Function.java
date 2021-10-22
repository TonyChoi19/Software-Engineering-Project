public class Function extends Square{

    public Function(int pos, String name) {
        this.pos = pos;
        this.name = name;
    }

    @Override
    protected boolean isFunction() {
        return true;
    }
}
