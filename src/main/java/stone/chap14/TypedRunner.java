package stone.chap14;

import stone.chap8.NativeEvaluator;
import javassist.gluonj.util.Loader;

public class TypedRunner {
    public static void main(String[] args) throws Throwable {
        Loader.run(TypedInterpreter.class, args, TypeChecker.class,
                                                 NativeEvaluator.class);
    }
}
