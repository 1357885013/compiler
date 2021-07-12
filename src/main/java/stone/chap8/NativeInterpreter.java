package stone.chap8;

import stone.chap6.BasicInterpreter;
import stone.chap7.NestedEnv;
import stone.stone.ClosureParser;
import stone.stone.ParseException;

public class NativeInterpreter extends BasicInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new ClosureParser(),
            new Natives().environment(new NestedEnv()));
    }
}
