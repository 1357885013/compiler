package test;


import org.junit.jupiter.api.Test;
import stateMachine.Pattern;
import stateMachine.utils.TestUtil;

class TestExceptComplex {
    static Pattern pattern = Pattern.compile("([^abc]1)|([^ade]2)|([^fg]3)|([^ghijklmn]4)|(g5)|(a6)|(.7)", true);

    @Test
    public void test1() {
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
    public void test2() {
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
    public void test3() {
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
    public void test4() {
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
    public void test5() {
        TestUtil.assertEmpty(pattern, "a5");
        TestUtil.assertEmpty(pattern, "b5");
        TestUtil.assertEmpty(pattern, "c5");
        TestUtil.assertEmpty(pattern, "d5");
        TestUtil.assertEmpty(pattern, "e5");
        TestUtil.assertEmpty(pattern, "f5");
        TestUtil.assertSame(pattern, "g5");
        TestUtil.assertEmpty(pattern, "h5");
        TestUtil.assertEmpty(pattern, "i5");
        TestUtil.assertEmpty(pattern, "j5");
        TestUtil.assertEmpty(pattern, "k5");
        TestUtil.assertEmpty(pattern, "l5");
        TestUtil.assertEmpty(pattern, "m5");
        TestUtil.assertEmpty(pattern, "n5");
        TestUtil.assertEmpty(pattern, "t5");
    }

    @Test
    public void test6() {
        TestUtil.assertSame(pattern, "a6");
        TestUtil.assertEmpty(pattern, "b6");
        TestUtil.assertEmpty(pattern, "c6");
        TestUtil.assertEmpty(pattern, "d6");
        TestUtil.assertEmpty(pattern, "e6");
        TestUtil.assertEmpty(pattern, "f6");
        TestUtil.assertEmpty(pattern, "g6");
        TestUtil.assertEmpty(pattern, "h6");
        TestUtil.assertEmpty(pattern, "i6");
        TestUtil.assertEmpty(pattern, "j6");
        TestUtil.assertEmpty(pattern, "k6");
        TestUtil.assertEmpty(pattern, "l6");
        TestUtil.assertEmpty(pattern, "m6");
        TestUtil.assertEmpty(pattern, "n6");
        TestUtil.assertEmpty(pattern, "t6");
    }

    @Test
    public void test7() {
        TestUtil.assertSame(pattern, "a7");
        TestUtil.assertSame(pattern, "b7");
        TestUtil.assertSame(pattern, "c7");
        TestUtil.assertSame(pattern, "d7");
        TestUtil.assertSame(pattern, "e7");
        TestUtil.assertSame(pattern, "f7");
        TestUtil.assertSame(pattern, "g7");
        TestUtil.assertSame(pattern, "h7");
        TestUtil.assertSame(pattern, "i7");
        TestUtil.assertSame(pattern, "j7");
        TestUtil.assertSame(pattern, "k7");
        TestUtil.assertSame(pattern, "l7");
        TestUtil.assertSame(pattern, "m7");
        TestUtil.assertSame(pattern, "n7");
        TestUtil.assertSame(pattern, "t7");

        TestUtil.assertSame(pattern, "17");
        TestUtil.assertSame(pattern, "27");
        TestUtil.assertSame(pattern, "37");
        TestUtil.assertSame(pattern, "o7");
        TestUtil.assertSame(pattern, "p7");
        TestUtil.assertSame(pattern, "q7");
        TestUtil.assertSame(pattern, "x7");
        TestUtil.assertSame(pattern, "y7");
        TestUtil.assertSame(pattern, "z7");
    }
}