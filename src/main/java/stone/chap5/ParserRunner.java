package stone.chap5;
import stone.stone.*;
import stone.stone.ast.ASTree;

public class ParserRunner {
    public static void main(String[] args) throws ParseException {
        Lexer l = new Lexer(new CodeDialog());
        BasicParser bp = new BasicParser();
        while (l.peek(0) != Token.EOF) {//在词法分析器里取一个token看
            ASTree ast = bp.parse(l);
            System.out.println("=> " + ast.toString());
        }
    }
}
