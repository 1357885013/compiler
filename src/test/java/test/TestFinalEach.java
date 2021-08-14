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
        Pattern pattern = Pattern.compile("\\*(\\\\\\*|.)*?\\*", true);
        TestUtil.assertSame(pattern, "aaa");
        TestUtil.assertSame(pattern, "aaaaaa");
        TestUtil.assertEquals(pattern, "aaaaaaaaa", "aaaaaa|aaa|");
        TestUtil.assertEmpty(pattern, "aa");
        TestUtil.assertEquals(pattern, "aaaa", "aaa|");
        TestUtil.assertEquals(pattern, "aaaaa", "aaa|");
    }

    @Test
    public void test311() {
        // todo: error
        Pattern pattern = Pattern.compile("a(a|.)*?a", true);
        TestUtil.assertSame(pattern, "aaa");
        TestUtil.assertSame(pattern, "aaaaaa");
        TestUtil.assertEquals(pattern, "aaaaaaaaa", "aaaaaa|aaa|");
        TestUtil.assertEmpty(pattern, "aa");
        TestUtil.assertEquals(pattern, "aaaa", "aaa|");
        TestUtil.assertEquals(pattern, "aaaaa", "aaa|");
    }

    @Test
    public void test4() {
        Pattern pattern = Pattern.compile("(\\+\\+|--|\\+=|-\\+|\\*=|/=|&&|\\|\\||!=|==|>=|<=)", true);
        TestUtil.assertSame(pattern, "abc");
        TestUtil.assertSame(pattern, "aabc");
        TestUtil.assertSame(pattern, "ababc");
        TestUtil.assertSame(pattern, "abcabc");
        TestUtil.assertEmpty(pattern, "aba");
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