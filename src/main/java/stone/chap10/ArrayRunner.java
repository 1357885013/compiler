package stone.chap10;

import stone.chap7.ClosureEvaluator;
import stone.chap8.NativeEvaluator;
import stone.chap9.ClassEvaluator;
import stone.chap9.ClassInterpreter;
import javassist.gluonj.util.Loader;

public class ArrayRunner {
    public static void main(String[] args) throws Throwable {
        Loader.run(ClassInterpreter.class, args, ClassEvaluator.class,
                   ArrayEvaluator.class, NativeEvaluator.class,
                   ClosureEvaluator.class);
    }
}
