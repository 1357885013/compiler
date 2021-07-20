package test;


import org.junit.jupiter.api.Test;
import stateMachine.Pattern;
import stateMachine.utils.TestUtil;

class TestExcept {
    static Pattern pattern = Pattern.compile("([^abc]1)|([^ade]2)|([^fg]3)|([^ghijklmn]4)|[t]5", true);

    @Test
    public void test9() {
        TestUtil.assertEmpty(pattern, "a1");
        TestUtil.assertEmpty(pattern, "b1");
        TestUtil.assertEmpty(pattern, "c1");
        TestUtil.assertSame(pattern, "d1");
        TestUtil.assertSame(pattern, "e1");
        TestUtil.assertSame(pattern, "f1");
        TestUtil.assertSame(pattern, "g1");
        TestUtil.assertSame(pattern, "h1");
        TestUtil.assertSame(pattern, "i1");
        TestUtil.assertSame(pattern, "j1");
        TestUtil.assertSame(pattern, "k1");
        TestUtil.assertSame(pattern, "l1");
        TestUtil.assertSame(pattern, "m1");
        TestUtil.assertSame(pattern, "n1");
        TestUtil.assertSame(pattern, "t1");
    }

    @Test
    public void test1() {
        TestUtil.assertEmpty(pattern, "a1");
        TestUtil.assertSame(pattern, "b2");
        TestUtil.assertSame(pattern, "c2");
        TestUtil.assertEmpty(pattern, "d2");
        TestUtil.assertEmpty(pattern, "e2");
        TestUtil.assertSame(pattern, "f2");
        TestUtil.assertSame(pattern, "g2");
        TestUtil.assertSame(pattern, "h2");
        TestUtil.assertSame(pattern, "i2");
        TestUtil.assertSame(pattern, "j2");
        TestUtil.assertSame(pattern, "k2");
        TestUtil.assertSame(pattern, "l2");
        TestUtil.assertSame(pattern, "m2");
        TestUtil.assertSame(pattern, "n2");
        TestUtil.assertSame(pattern, "t2");
    }

    @Test
    public void test2() {
        TestUtil.assertSame(pattern, "a3");
        TestUtil.assertSame(pattern, "b3");
        TestUtil.assertSame(pattern, "c3");
        TestUtil.assertSame(pattern, "d3");
        TestUtil.assertSame(pattern, "e3");
        TestUtil.assertEmpty(pattern, "f3");
        TestUtil.assertEmpty(pattern, "g3");
        TestUtil.assertSame(pattern, "h3");
        TestUtil.assertSame(pattern, "i3");
        TestUtil.assertSame(pattern, "j3");
        TestUtil.assertSame(pattern, "k3");
        TestUtil.assertSame(pattern, "l3");
        TestUtil.assertSame(pattern, "m3");
        TestUtil.assertSame(pattern, "n3");
        TestUtil.assertSame(pattern, "t3");
    }

    @Test
    public void test3() {
        TestUtil.assertSame(pattern, "a4");
        TestUtil.assertSame(pattern, "b4");
        TestUtil.assertSame(pattern, "c4");
        TestUtil.assertSame(pattern, "d4");
        TestUtil.assertSame(pattern, "e4");
        TestUtil.assertSame(pattern, "f4");
        TestUtil.assertEmpty(pattern, "g4");
        TestUtil.assertEmpty(pattern, "h4");
        TestUtil.assertEmpty(pattern, "i4");
        TestUtil.assertEmpty(pattern, "j4");
        TestUtil.assertEmpty(pattern, "k4");
        TestUtil.assertEmpty(pattern, "l4");
        TestUtil.assertEmpty(pattern, "m4");
        TestUtil.assertEmpty(pattern, "n4");
        TestUtil.assertSame(pattern, "t4");
    }

    @Test
    public void test4() {
        TestUtil.assertSame(pattern, "t5");
    }

}