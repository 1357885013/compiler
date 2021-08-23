package test;


import org.junit.jupiter.api.Test;
import stateMachine.Pattern;
import stateMachine.utils.TestUtil;

class TestMultiSpin {
    static Pattern pattern = Pattern.compile("([a-c][a-c0-1]*1)|(b+2)", true);

    @Test
    public void testRight1() {
        TestUtil.assertEmpty(pattern, "bc2");
        TestUtil.assertEmpty(pattern, "bd2");
        // todo : error
        TestUtil.assertEmpty(pattern, "bbbc2");
        TestUtil.assertEmpty(pattern, "bbbbbbcbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb2");
        TestUtil.assertEmpty(pattern, "3");
    }

    @Test
    public void testAll1() {
        TestUtil.assertEmpty(pattern, "2");
        TestUtil.assertEmpty(pattern, "a2");
        TestUtil.assertEmpty(pattern, "c2");

        TestUtil.assertEmpty(pattern, "aa2");
        TestUtil.assertEmpty(pattern, "ba2");
        TestUtil.assertEmpty(pattern, "ca2");
        TestUtil.assertEmpty(pattern, "ab2");
        TestUtil.assertEmpty(pattern, "cb2");
        TestUtil.assertEmpty(pattern, "ac2");
        TestUtil.assertEmpty(pattern, "bc2");
        TestUtil.assertEmpty(pattern, "cc2");
        TestUtil.assertEmpty(pattern, "a02");
        TestUtil.assertEmpty(pattern, "b02");
        TestUtil.assertEmpty(pattern, "c02");
        TestUtil.assertEquals(pattern, "a12", "a1|");
        TestUtil.assertEquals(pattern, "b12", "b1|");
        TestUtil.assertEquals(pattern, "c10", "c1|");
        TestUtil.assertEquals(pattern, "c1a", "c1|");
        TestUtil.assertEquals(pattern, "c1b", "c1|");

        TestUtil.assertEquals(pattern, "aabc012", "aabc01|");
        TestUtil.assertEquals(pattern, "babc012", "babc01|");
        TestUtil.assertEquals(pattern, "cabc012", "cabc01|");
    }


    @Test
    public void testLeft1() {
        TestUtil.assertSame(pattern, "a1");
        TestUtil.assertSame(pattern, "b1");
        TestUtil.assertSame(pattern, "c1");
    }

    @Test
    public void testLeft2() {
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
    public void testLeft3() {
        TestUtil.assertSame(pattern, "aabc011");
        TestUtil.assertSame(pattern, "babc011");
        TestUtil.assertSame(pattern, "cabc011");
    }

    @Test
    public void testLeft11() {
        TestUtil.assertEmpty(pattern, "d1");
        TestUtil.assertEmpty(pattern, "e1");
        TestUtil.assertEmpty(pattern, "ad1");
        TestUtil.assertEmpty(pattern, "ae1");
        TestUtil.assertEmpty(pattern, "abbe1");
        TestUtil.assertEmpty(pattern, "aaaa");
    }
}