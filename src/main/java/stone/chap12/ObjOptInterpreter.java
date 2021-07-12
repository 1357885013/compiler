package stone.chap12;

import stone.chap11.EnvOptInterpreter;
import stone.chap11.ResizableArrayEnv;
import stone.chap8.Natives;
import stone.stone.ClassParser;
import stone.stone.ParseException;

public class ObjOptInterpreter extends EnvOptInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new ClassParser(),
            new Natives().environment(new ResizableArrayEnv()));
    }
}
