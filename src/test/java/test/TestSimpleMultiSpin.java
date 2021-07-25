package test;


import org.junit.jupiter.api.Test;
import stateMachine.Pattern;
import stateMachine.utils.TestUtil;

class TestSimpleMultiSpin {
    static Pattern pattern = Pattern.compile("([a-c][a-c0-1]*1)", true);

    @Test
    public void test1() {
        TestUtil.assertSame(pattern, "a1");
        TestUtil.assertSame(pattern, "b1");
        TestUtil.assertSame(pattern, "c1");
    }

    @Test
    public void test2() {
        TestUtil.assertSame(pattern, "aa1");
        TestUtil.assertSame(pattern, "ba1");
        TestUtil.assertSame(pattern, "ca1");
        TestUtil.assertSame(pattern, "ab1");
        TestUtil.assertSame(pattern, "bb1");
        TestUtil.assertSame(pattern, "cb1");
        TestUtil.assertSame(pattern, "ac1");
        TestUtil.assertSame(pattern, "bc1");
        TestUtil.assertSame(pattern, "cc1");
        TestUtil.assertSame(pattern, "a01");
        TestUtil.assertSame(pattern, "b01");
        TestUtil.assertSame(pattern, "c01");
        TestUtil.assertSame(pattern, "a11");
        TestUtil.assertSame(pattern, "b11");
        TestUtil.assertSame(pattern, "c11");
    }

    @Test
    public void test3() {
        TestUtil.assertSame(pattern, "aabc011");
        TestUtil.assertSame(pattern, "babc011");
        TestUtil.assertSame(pattern, "cabc011");
    }

    @Test
    public void test11() {
        TestUtil.assertEmpty(pattern, "d1");
        TestUtil.assertEmpty(pattern, "e1");
        TestUtil.assertEmpty(pattern, "ad1");
        TestUtil.assertEmpty(pattern, "ae1");
        TestUtil.assertEmpty(pattern, "abbe1");
        TestUtil.assertEmpty(pattern, "aaaa");
    }
}