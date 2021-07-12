package stone.stone;

import stone.stone.ast.Fun;

import static stone.stone.Parser.rule;

public class ClosureParser extends FuncParser {
    public ClosureParser() {
        primary.insertChoice(rule(Fun.class)
                                 .sep("fun").ast(paramList).ast(block));
    }
}
