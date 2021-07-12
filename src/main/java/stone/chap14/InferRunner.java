package stone.chap14;

import stone.chap8.NativeEvaluator;
import javassist.gluonj.util.Loader;

public class InferRunner {
    public static void main(String[] args) throws Throwable {
        Loader.run(TypedInterpreter.class, args, InferFuncTypes.class,
                                                 NativeEvaluator.class);
    }
}
