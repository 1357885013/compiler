package stone.chap12;

import stone.chap8.NativeEvaluator;
import javassist.gluonj.util.Loader;

public class ObjOptRunner {
    public static void main(String[] args) throws Throwable {
        Loader.run(ObjOptInterpreter.class, args, ObjOptimizer.class,
                                                  NativeEvaluator.class);
    }
}
