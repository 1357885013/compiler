package test;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stateMachine.Pattern;
import stateMachine.utils.TestUtil;

class TestAll {

    @Test
    public void test1() {
        Pattern pattern = Pattern.compile("(aa(bb)?)+", true);
        TestUtil.assertSame(pattern, "aabbaa");
        TestUtil.assertSame(pattern, "aa");
        TestUtil.assertSame(pattern, "aabb");
        TestUtil.assertEmpty(pattern, "a");
        TestUtil.assertEmpty(pattern, "ab");
    }

    @Test
    public void test2() {
        Pattern pattern = Pattern.compile("((a|b)?b)+", true);
        TestUtil.assertSame(pattern, "b");
        TestUtil.assertSame(pattern, "abab");
        TestUtil.assertSame(pattern, "abbbb");
        TestUtil.assertSame(pattern, "abbbb");
        TestUtil.assertSame(pattern, "babab");
        TestUtil.assertSame(pattern, "bbbb");
        TestUtil.assertSame(pattern, "ab");
        TestUtil.assertSame(pattern, "ababab");
        TestUtil.assertEmpty(pattern, "a");
        TestUtil.assertEmpty(pattern, "aa");
    }

    @Test
    public void test3() {
        Pattern pattern = Pattern.compile("(aaa)?aaa", true);
        TestUtil.assertSame(pattern, "aaa");
        TestUtil.assertSame(pattern, "aaaaaa");
        TestUtil.assertEquals(pattern, "aaaaaaaaa", "aaaaaa|aaa|");
        TestUtil.assertEmpty(pattern, "aa");
        TestUtil.assertEquals(pattern, "aaaa", "aaa|");
        TestUtil.assertEquals(pattern, "aaaaa", "aaa|");
    }

    @Test
    public void test4() {
        Pattern pattern = Pattern.compile("(a(b(c)?)?)?abc", true);
        TestUtil.assertSame(pattern, "abc");
        TestUtil.assertSame(pattern, "aabc");
        TestUtil.assertSame(pattern, "ababc");
        TestUtil.assertSame(pattern, "abcabc");
        TestUtil.assertEmpty(pattern, "aba");
        TestUtil.assertEmpty(pattern, "aa");
    }

    @Test
    public void test5() {
        Pattern pattern = Pattern.compile("(a(b(c))).*", true);
        TestUtil.assertSame(pattern, "abc");
        TestUtil.assertSame(pattern, "abcde");
        TestUtil.assertSame(pattern, "abcdef");
        TestUtil.assertSame(pattern, "abcdefghi");
        TestUtil.assertEmpty(pattern, "bcde");
        TestUtil.assertEmpty(pattern, "cde");
    }

    @Test
    public void test6() {
        Pattern pattern = Pattern.compile("z(a|ac)b", true);
        TestUtil.assertSame(pattern, "zacb");
        TestUtil.assertSame(pattern, "zab");
        TestUtil.assertEmpty(pattern, "zb");
        TestUtil.assertEmpty(pattern, "zaab");
        TestUtil.assertEmpty(pattern, "za");
        TestUtil.assertEmpty(pattern, "zac");
    }

    @Test
    public void test7() {
        Pattern pattern = Pattern.compile("[abc]+", true);
        TestUtil.assertSame(pattern, "abc");
        TestUtil.assertSame(pattern, "abcabc");
        TestUtil.assertSame(pattern, "ab");
        TestUtil.assertSame(pattern, "abab");
        TestUtil.assertSame(pattern, "a");
        TestUtil.assertSame(pattern, "ab");
        TestUtil.assertEmpty(pattern, "dab");
    }

    @Test
    public void test8() {
        Pattern pattern = Pattern.compile("[abc]+[def]+[ghi]+", true);
        TestUtil.assertSame(pattern, "aaddgg");
        TestUtil.assertSame(pattern, "abcdefghi");
        TestUtil.assertSame(pattern, "abcabcdefdefghighi");
        TestUtil.assertEmpty(pattern, "ai");
        TestUtil.assertEmpty(pattern, "ad");
        TestUtil.assertEmpty(pattern, "dg");
    }

    @Test
    public void test9() {
        Pattern pattern = Pattern.compile("abc[\\w]", true);
        TestUtil.assertSame(pattern, "abcd");
        TestUtil.assertSame(pattern, "abcdefghi");
        TestUtil.assertSame(pattern, "abcabcdefdefghighi");
        TestUtil.assertEmpty(pattern, "a#");
        TestUtil.assertEmpty(pattern, "a#");
        TestUtil.assertEmpty(pattern, "d#");
    }

    @Test
    public void test10() {
        Pattern pattern = Pattern.compile("(a+b)+", true);
        TestUtil.assertSame(pattern, "ababab");
        TestUtil.assertSame(pattern, "aabaab");
        TestUtil.assertSame(pattern, "aabab");
        TestUtil.assertEmpty(pattern, "bb");
        TestUtil.assertEmpty(pattern, "aaa");
    }

    @Test
    public void test11() {
        Pattern pattern = Pattern.compile(".bc|def", true);
        TestUtil.assertSame(pattern, "abc");
        TestUtil.assertSame(pattern, "bbc");
        TestUtil.assertSame(pattern, "cbc");
        TestUtil.assertSame(pattern, "dbc");
        TestUtil.assertSame(pattern, "ebc");
        TestUtil.assertSame(pattern, "fbc");
        TestUtil.assertSame(pattern, "gbc");
        TestUtil.assertSame(pattern, "hbc");

        TestUtil.assertSame(pattern, "def");

        TestUtil.assertEmpty(pattern, "bc");
        TestUtil.assertEmpty(pattern, "de");
    }
}