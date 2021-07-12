package stone.chap6;

import stone.stone.BasicParser;
import stone.stone.Lexer;
import stone.stone.ParseException;
import stone.stone.Token;
import stone.stone.ast.ASTree;
import stone.stone.ast.NullStmnt;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class BasicFileInterpreter {
    public static void main(String[] args)
        throws ParseException, FileNotFoundException
    {
        run(new BasicParser(), new BasicEnv(), args[0]);
    }

    public static void run(BasicParser bp, Environment env, String fileName)
        throws ParseException, FileNotFoundException
    {
        FileReader reader = new FileReader(fileName);
        Lexer lexer = new Lexer(reader);
        while (lexer.peek(0) != Token.EOF) {
            ASTree t = bp.parse(lexer);
            if (!(t instanceof NullStmnt)) {
                Object r = ((BasicEvaluator.ASTreeEx)t).eval(env);
                System.out.println("=> " + r);
            }
        }
    }
}
