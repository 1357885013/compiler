package test;


import org.junit.jupiter.api.Test;
import stateMachine.Pattern;
import stateMachine.utils.TestUtil;

class TestBraceDotStar {
    static Pattern pattern = Pattern.compile("ca(.|a)*", true);

    @Test
    public void test1() {
        TestUtil.assertSame(pattern, "ca");
    }

    @Test
    public void test2() {
        TestUtil.assertSame(pattern, "caa");
        TestUtil.assertSame(pattern, "caaa");
        TestUtil.assertSame(pattern, "caaaaaaaa");
    }

    @Test
    public void test3() {
        TestUtil.assertSame(pattern, "cad");
        TestUtil.assertSame(pattern, "cadddd");
        TestUtil.assertSame(pattern, "cadddddddd");
    }

    @Test
    public void test4() {
        TestUtil.assertSame(pattern, "cad");
        TestUtil.assertSame(pattern, "cadefg");
        TestUtil.assertSame(pattern, "cadefghijk");
    }

    @Test
    public void test5() {
        TestUtil.assertEmpty(pattern, "ccad");
        TestUtil.assertEmpty(pattern, "aad");
        TestUtil.assertEmpty(pattern, "fsaafd");
    }
}