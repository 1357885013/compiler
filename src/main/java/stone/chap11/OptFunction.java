package stone.chap11;

import stone.chap6.Environment;
import stone.chap7.Function;
import stone.stone.ast.BlockStmnt;
import stone.stone.ast.ParameterList;

public class OptFunction extends Function {
    protected int size;
    public OptFunction(ParameterList parameters, BlockStmnt body,
                       Environment env, int memorySize)
    {
        super(parameters, body, env);
        size = memorySize;
    }
    @Override public Environment makeEnv() { return new ArrayEnv(size, env); }
}
