package test;


import org.junit.jupiter.api.Test;
import stateMachine.Pattern;
import stateMachine.utils.TestUtil;

class TestMultiSpin {
    static Pattern pattern = Pattern.compile("([a-c][a-c0-1]*1)|(b+2)", true);

    @Test
    public void test1() {
        TestUtil.assertSame(pattern, "a11");
        TestUtil.assertSame(pattern, "aabc011");
        TestUtil.assertSame(pattern, "babc011");
        TestUtil.assertSame(pattern, "cabc011");
        TestUtil.assertSame(pattern, "a1");
        TestUtil.assertSame(pattern, "b1");
        TestUtil.assertSame(pattern, "c1");
    }
    @Test
    public void test11() {
        TestUtil.assertEmpty(pattern,"d1");
        TestUtil.assertEmpty(pattern,"e1");
        TestUtil.assertEmpty(pattern,"ad1");
        TestUtil.assertEmpty(pattern,"ae1");
        TestUtil.assertEmpty(pattern,"abbe1");
        TestUtil.assertEmpty(pattern,"aaaa");
    }

    @Test
    public void test2() {
        TestUtil.assertSame(pattern, "b2");
        TestUtil.assertSame(pattern, "bb2");
        TestUtil.assertSame(pattern, "bbb2");
        TestUtil.assertSame(pattern,"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb2");
    }
    @Test
    public void test22() {
        TestUtil.assertEmpty(pattern, "bc2");
        TestUtil.assertEmpty(pattern, "bd2");
        TestUtil.assertEmpty(pattern, "bbbc2");
        TestUtil.assertEmpty(pattern,"bbbbbbcbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb2");
        TestUtil.assertEmpty(pattern,"2");
    }
}