package stone.chap7;

import stone.chap6.BasicInterpreter;
import stone.stone.ClosureParser;
import stone.stone.ParseException;

public class ClosureInterpreter extends BasicInterpreter{
    public static void main(String[] args) throws ParseException {
        run(new ClosureParser(), new NestedEnv());
    }
}
