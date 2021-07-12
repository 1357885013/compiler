package stone.chap7;

import stone.chap6.BasicInterpreter;
import stone.stone.FuncParser;
import stone.stone.ParseException;

public class FuncInterpreter extends BasicInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new FuncParser(), new NestedEnv());
    }
}
