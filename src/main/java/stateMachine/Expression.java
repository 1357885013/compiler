package stateMachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Expression {
    Node[] contents;

    public static final Expression emptyInput = new Expression('@', true);
    public static final Expression dot = new Expression('.', true);

    /***
     * 初步处理表达式
     * - 转义处理
     * - \w \d 等处理
     ***/
    public Expression(String expression) {
        ArrayList<Node> temps = new ArrayList<>(expression.length());
        boolean isInMiddleBrace = false;
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '\\') {
                if (i == expression.length() - 1) {
                    break;
                } else {
                    char next = expression.charAt(i + 1);
                    boolean resolved = false;

                    // \ 之后如果是 w d s S
                    if (next == 'w') {
                        temps.add(new Node('[', true));
                        temps.addAll(Arrays.stream("a-zA-Z0-9".chars().toArray()).mapToObj(a -> {
                            if (a == '-')
                                return new Node((char) a, true);
                            else
                                return new Node((char) a, false);
                        }).collect(Collectors.toList()));
                        temps.add(new Node(']', true));
                        resolved = true;
                    } else if (next == 'd') {
                        temps.add(new Node('[', true));
                        temps.addAll(Arrays.stream("0-9".chars().toArray()).mapToObj(a -> {
                            if (a == '-')
                                return new Node((char) a, true);
                            else
                                return new Node((char) a, false);
                        }).collect(Collectors.toList()));
                        temps.add(new Node(']', true));
                        resolved = true;
                    } else if (next == 's') {
                        temps.add(new Node('[', true));
                        temps.addAll(Arrays.stream(" \r\f\t\n".chars().toArray()).mapToObj(a -> new Node((char) a, false)).collect(Collectors.toList()));
                        temps.add(new Node(']', true));
                        resolved = true;
                    } else if (next == 'S') {
                        temps.add(new Node('[', true));
                        temps.addAll(Arrays.stream("\r\f\t".chars().toArray()).mapToObj(a -> new Node((char) a, false)).collect(Collectors.toList()));
                        temps.add(new Node(']', true));
                        resolved = true;
                    }

                    // \之后如果是关键字那就是非关键字
                    if (!resolved)
                        temps.add(new Node(expression.charAt(i + 1), false));
                    i++;
                }
            } else if (c == '[') {
                if (isInMiddleBrace)
                    temps.add(new Node(expression.charAt(i), false));
                else {
                    temps.add(new Node(expression.charAt(i), true));
                    // 中括号 第一个^ 才是关键字
                    if (expression.charAt(i + 1) == '^') {
                        temps.add(new Node('^', true));
                        i++;
                    }
                    isInMiddleBrace = true;
                }
            } else if (c == ']') {
                if (isInMiddleBrace) {
                    temps.add(new Node(expression.charAt(i), true));
                    isInMiddleBrace = false;
                } else
                    temps.add(new Node(expression.charAt(i), false));
            } else if (isInMiddleBrace && Config.keywordsInMiddleBrace.contains(c)) {
                temps.add(new Node(expression.charAt(i), true));
            } else if (!isInMiddleBrace && Config.keywords.contains(c)) {
                temps.add(new Node(expression.charAt(i), true));
            } else {
                temps.add(new Node(expression.charAt(i), false));
            }
        }
        contents = temps.toArray(new Node[0]);
    }

    public Expression() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expression)) return false;
        Expression that = (Expression) o;
        if (this.length() != that.length())
            return false;
        for (int i = 0; i < this.contents.length; i++) {
            if (!this.contents[i].equals(that.contents[i]))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(contents);
    }

    public Expression(Expression input) {
        this.contents = new Node[input.contents.length];
        System.arraycopy(input.contents, 0, this.contents, 0, input.contents.length);
    }

    public Expression(Node[] contents) {
        this.contents = contents;
    }

    public Expression(Node n) {
        this.contents = new Node[]{n};
    }

    public Expression(char c, boolean isKeyword) {
        this.contents = new Node[]{new Node(c, isKeyword)};
    }

    @Override
    public String toString() {
        return Arrays.stream(contents).map(a -> (a.isKeyword ? "_" : "") + String.valueOf(a.content)).collect(Collectors.joining());
//        return contents.length > 0 ? String.valueOf(contents[0].content) : "";
    }

    public Node charAt(int i) {
        if (i >= contents.length) return Node.empty();
        return contents[i];
    }

    public int length() {
        return contents.length;
    }

    public boolean equalsKeyword(char c) {
        if (contents.length != 1) return false;
        return contents[0].equalsKeyword(c);
    }

    public boolean equals(Node n) {
        if (contents.length != 1) return false;
        return contents[0].equals(n);
    }

    public Expression substring(int i) {
        Expression newOne = new Expression();
        int len = (this.contents.length - i);
        newOne.contents = new Node[len];
        System.arraycopy(this.contents, i, newOne.contents, 0, len);
        return newOne;
    }

    public Expression substring(int begin, int end) {
        Expression newOne = new Expression();
        int len = Math.min(this.contents.length - begin, end - begin);
        newOne.contents = new Node[len];
        System.arraycopy(this.contents, begin, newOne.contents, 0, len);
        return newOne;
    }

    public boolean contains(Expression e) {
        for (Node content : contents) {
            if (content.equals(e.charAt(0))) return true;
        }
        return false;
    }

    public Expression[] split(String c) {
        if (c.length() > 1) return new Expression[0];
        List<Expression> list = new ArrayList<Expression>();
        List<Node> each = new ArrayList<Node>();

        for (Node content : contents) {
            if (c.length() == 0 || content.equalsKeyword(c.charAt(0))) {
                if (each.size() != 0) {
                    list.add(new Expression(each.toArray(new Node[0])));
                    each.clear();
                }
            } else {
                each.add(content);
            }
        }
        return new Expression[0];
    }


    public static class Node {
        // 转义了就不是关键字了.
        boolean isKeyword = true;
        boolean isEmpty = false;
        char content;

        public Node() {
            isEmpty = true;
        }

        static Node empty() {
            return new Node();
        }

        public Node(char content, boolean isKeyword) {
            this.isKeyword = isKeyword;
            this.content = content;
        }

        public Node(String content, boolean isKeyword) {
            this.isKeyword = isKeyword;
            this.content = content.charAt(0);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node that = (Node) o;
            if (this.isEmpty && that.isEmpty) return true;
            return this.content == that.content && this.isKeyword == that.isKeyword;
        }

        @Override
        public int hashCode() {
            return content;
        }

        public Node(char content) {
            this.content = content;
        }

        public Node(String content) {
            this.content = content.charAt(0);
        }

        public boolean equalsKeyword(char other) {
            return !this.isEmpty && isKeyword && content == other;
        }

        public boolean equals(char other) {
            return !this.isEmpty && content == other;
        }
    }
}
