package stone.chap14;

import stone.chap11.EnvOptimizer.EnvEx2;
import stone.chap6.Environment;
import stone.chap8.Natives;

public class TypedNatives extends Natives {
    protected TypeEnv typeEnv;
    public TypedNatives(TypeEnv te) { typeEnv = te; }
    protected void append(Environment env, String name, Class<?> clazz,
                          String methodName, TypeInfo type, Class<?> ... params)
    {
        append(env, name, clazz, methodName, params);
        int index = ((EnvEx2)env).symbols().find(name);
        typeEnv.put(0, index, type);
    }
    protected void appendNatives(Environment env) {
        append(env, "print", stone.chap14.java.print.class, "m",
               TypeInfo.function(TypeInfo.INT, TypeInfo.ANY),
               Object.class);
        append(env, "read", stone.chap14.java.read.class, "m",
                TypeInfo.function(TypeInfo.STRING));
        append(env, "length", stone.chap14.java.length.class, "m",
               TypeInfo.function(TypeInfo.INT, TypeInfo.STRING),
               String.class);
        append(env, "toInt", stone.chap14.java.toInt.class, "m",
               TypeInfo.function(TypeInfo.INT, TypeInfo.ANY),
               Object.class);
        append(env, "currentTime", stone.chap14.java.currentTime.class, "m",
               TypeInfo.function(TypeInfo.INT)); 
    }
}
