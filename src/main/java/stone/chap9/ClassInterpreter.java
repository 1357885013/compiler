package stone.chap9;

import stone.chap6.BasicInterpreter;
import stone.chap7.NestedEnv;
import stone.chap8.Natives;
import stone.stone.ClassParser;
import stone.stone.ParseException;

public class ClassInterpreter extends BasicInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new ClassParser(), new Natives().environment(new NestedEnv())); 
    }
}
