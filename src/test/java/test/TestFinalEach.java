package test;


import org.junit.jupiter.api.Test;
import stateMachine.Pattern;
import stateMachine.utils.TestUtil;

class TestFinalEach {

    @Test
    public void test1() {
        Pattern pattern = Pattern.compile("((//.*?(\\n|$))|(/\\*.*?\\*/))", true);
        TestUtil.assertSame(pattern, "/*fdsafdsafdf*/");
        TestUtil.assertSame(pattern, "/*fdsafdsafd/*fdsaf*/");
        TestUtil.assertSame(pattern, "/******fdsafdsaf*/");
        TestUtil.assertEmpty(pattern, "/asdfs**/");
        TestUtil.assertEmpty(pattern, "sfds");
    }

    @Test
    public void test2() {
        Pattern pattern = Pattern.compile("([a-zA-Z_][a-zA-Z0-9_]*)", false);
        TestUtil.assertSame(pattern, "absdfeASD3213____");
        TestUtil.assertSame(pattern, "hfdosab42319unf928290hjf0t32fre34243");
        TestUtil.assertSame(pattern, "a");
        TestUtil.assertSame(pattern, "b");
        TestUtil.assertEmpty(pattern, "0");
        TestUtil.assertEmpty(pattern, "9aa");
    }

    @Test
    public void test3() {
        Pattern pattern = Pattern.compile("\\d+", true);
        TestUtil.assertSame(pattern, "132180");
        TestUtil.assertSame(pattern, "13821038209");
        TestUtil.assertEmpty(pattern, "a1");
    }

    @Test
    public void test31() {
        // todo: error
        // 只要里面没有 另一个注释的开头就行.
//        Pattern pattern = Pattern.compile("\\\\\\*(\\\\\\*|.)*?\\*\\\\", true);
//        TestUtil.assertSame(pattern, "\\*this is a word,and this is cant split*\\");
//        TestUtil.assertSame(pattern, "\\*this is a *word,and * this is cant split*\\");
//        TestUtil.assertSame(pattern, "\\*this is a \\word,and\\ this is cant split*\\");
//        TestUtil.assertSame(pattern, "\\*this is a \\*word,and this \\* is cant split*\\");
//        TestUtil.assertSame(pattern, "\\*this is a \\*word,and this \\* \\ is cant split*\\");
//        TestUtil.assertSame(pattern, "\\***\\");
//        TestUtil.assertSame(pattern, "\\**\\");
//        TestUtil.assertEmpty(pattern, "\\*");
//        TestUtil.assertEmpty(pattern, "*\\");
//        TestUtil.assertEmpty(pattern, "\\*abc");
//        TestUtil.assertEmpty(pattern, "abc*\\");
//        TestUtil.assertEmpty(pattern, "\\*abc*");
//        TestUtil.assertEmpty(pattern, "*abc*\\");
//        TestUtil.assertEmpty(pattern, "*abc*\\");
//        TestUtil.assertEmpty(pattern, "baaa");
//        TestUtil.assertEmpty(pattern, "baa");
//        TestUtil.assertEmpty(pattern, "ba");
//        TestUtil.assertEmpty(pattern, "b\\a");
//        TestUtil.assertSame(pattern, "***");
    }

    @Test
    public void test310() {
        // todo : error
//        Pattern pattern = Pattern.compile("a(\\a|.)*?a", true);
//        TestUtil.assertSame(pattern, "a\\aa");
//        TestUtil.assertSame(pattern, "a\\a\\a\\a\\aa");
//        TestUtil.assertSame(pattern, "aaba\\aaaaa");
//        TestUtil.assertSame(pattern, "aa");
//        TestUtil.assertSame(pattern, "a\\aaa");
//        TestUtil.assertSame(pattern, "aa\\aaa");
//        TestUtil.assertEmpty(pattern, "a");
//        TestUtil.assertEmpty(pattern, "baaa");
//        TestUtil.assertEmpty(pattern, "baa");
//        TestUtil.assertEmpty(pattern, "ba");
//        TestUtil.assertEmpty(pattern, "b\\a");
//        TestUtil.assertSame(pattern, "aaaabaaa");
    }

    @Test
    public void test3100() {
        Pattern pattern = Pattern.compile("a(0a|b)*?a", true);
        TestUtil.assertSame(pattern, "a0aa");
        TestUtil.assertSame(pattern, "a0a0a0a0aa");
        TestUtil.assertSame(pattern, "abbb0abbba");
        TestUtil.assertSame(pattern, "aa");
        TestUtil.assertSame(pattern, "a0aba");
        TestUtil.assertSame(pattern, "ab0aba");
        TestUtil.assertEmpty(pattern, "a");
        TestUtil.assertEmpty(pattern, "baaa");
        TestUtil.assertEmpty(pattern, "baa");
        TestUtil.assertEmpty(pattern, "ba");
        TestUtil.assertEmpty(pattern, "b0a");
        TestUtil.assertEmpty(pattern, "a0baa");
        TestUtil.assertSame(pattern, "abbbbbba");
    }

    @Test
    public void test311() {
        Pattern pattern = Pattern.compile("a(ba|.)*?a", true);
        TestUtil.assertSame(pattern, "aaa");
        TestUtil.assertSame(pattern, "aaaaaa");
        TestUtil.assertSame(pattern, "aaaaaaaaa");
        TestUtil.assertSame(pattern, "aa");
        TestUtil.assertSame(pattern, "aaaa");
        TestUtil.assertSame(pattern, "aaaaa");
        TestUtil.assertEmpty(pattern, "a");
        TestUtil.assertEmpty(pattern, "baaa");
        TestUtil.assertEmpty(pattern, "baa");
        TestUtil.assertEmpty(pattern, "ba");
        TestUtil.assertSame(pattern, "aaaabaaa");
    }

    @Test
    public void test4() {
        Pattern pattern = Pattern.compile("(\\+\\+|--|\\+=|-=|\\*=|/=|&&|\\|\\||!=|==|>=|<=)", true);
        TestUtil.assertSame(pattern, "++");
        TestUtil.assertSame(pattern, "--");
        TestUtil.assertSame(pattern, "+=");
        TestUtil.assertSame(pattern, "-=");
        TestUtil.assertSame(pattern, "*=");
        TestUtil.assertSame(pattern, "/=");
        TestUtil.assertSame(pattern, "&&");
        TestUtil.assertSame(pattern, "||");
        TestUtil.assertSame(pattern, "!=");
        TestUtil.assertSame(pattern, "==");
        TestUtil.assertSame(pattern, ">=");
        TestUtil.assertSame(pattern, "<=");
        TestUtil.assertEmpty(pattern, "aa");
    }

    @Test
    public void test5() {
        Pattern pattern = Pattern.compile("(\\{|\\}|\\[|\\]|\\(|\\)|\\+|\\-|\\*|/|=|&|\\||!|:|;|,|<|>|'|\\\"|\\.)", true);
        TestUtil.assertSame(pattern, "abc");
        TestUtil.assertSame(pattern, "abcde");
        TestUtil.assertSame(pattern, "abcdef");
        TestUtil.assertSame(pattern, "abcdefghi");
        TestUtil.assertEmpty(pattern, "bcde");
        TestUtil.assertEmpty(pattern, "cde");
    }

    @Test
    public void test6() {
        Pattern pattern = Pattern.compile("(\\b)", true);
        TestUtil.assertSame(pattern, "zacb");
        TestUtil.assertSame(pattern, "zab");
        TestUtil.assertEmpty(pattern, "zb");
        TestUtil.assertEmpty(pattern, "zaab");
        TestUtil.assertEmpty(pattern, "za");
        TestUtil.assertEmpty(pattern, "zac");
    }
}