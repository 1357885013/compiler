package stone.chap13;

import stone.chap11.EnvOptInterpreter;
import stone.chap8.Natives;
import stone.stone.FuncParser;
import stone.stone.ParseException;

public class VmInterpreter extends EnvOptInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new FuncParser(),
            new Natives().environment(new StoneVMEnv(100000, 100000, 1000)));
    }
}
