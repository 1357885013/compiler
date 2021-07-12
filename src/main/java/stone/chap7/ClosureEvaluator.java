package stone.chap7;

import stone.chap6.Environment;
import javassist.gluonj.*;
import stone.stone.ast.ASTree;
import stone.stone.ast.Fun;

import java.util.List;

@Require(FuncEvaluator.class)
@Reviser public class ClosureEvaluator {
    @Reviser public static class FunEx extends Fun {
        public FunEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
            return new Function(parameters(), body(), env);
        }
    }
}
