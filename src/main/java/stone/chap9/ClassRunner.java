package stone.chap9;

import stone.chap7.ClosureEvaluator;
import stone.chap8.NativeEvaluator;
import javassist.gluonj.util.Loader;

public class ClassRunner {
    public static void main(String[] args) throws Throwable {
        Loader.run(ClassInterpreter.class, args, ClassEvaluator.class,
                   NativeEvaluator.class, ClosureEvaluator.class);
    }
}
