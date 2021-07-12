package stone.chap13;

import stone.chap6.Environment;
import stone.chap7.Function;
import stone.stone.ast.BlockStmnt;
import stone.stone.ast.ParameterList;

public class VmFunction extends Function {
    protected int entry;
    public VmFunction(ParameterList parameters, BlockStmnt body,
                      Environment env, int entry)
    {
        super(parameters, body, env);
        this.entry = entry;
    }
    public int entry() { return entry; }
}
