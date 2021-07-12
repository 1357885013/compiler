package stone.chap12;

import stone.chap11.ArrayEnv;
import stone.chap11.OptFunction;
import stone.chap6.Environment;
import stone.stone.ast.BlockStmnt;
import stone.stone.ast.ParameterList;

public class OptMethod extends OptFunction {
    OptStoneObject self;
    public OptMethod(ParameterList parameters, BlockStmnt body,
                     Environment env, int memorySize, OptStoneObject self)
    {
        super(parameters, body, env, memorySize);
        this.self = self;
    }
    @Override public Environment makeEnv() {
        ArrayEnv e = new ArrayEnv(size, env);
        e.put(0, 0, self);
        return e;
    }
}
