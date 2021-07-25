package test;


import org.junit.jupiter.api.Test;
import stateMachine.Pattern;
import stateMachine.utils.TestUtil;

class TestOr {
    static Pattern pattern = Pattern.compile("a|b|c|dA|bB|cC", true);

    @Test
    public void test1() {
        TestUtil.assertSame(pattern, "a");
        TestUtil.assertSame(pattern, "b");
        TestUtil.assertSame(pattern, "c");
        TestUtil.assertSame(pattern, "dA");
        TestUtil.assertSame(pattern, "bB");
        TestUtil.assertSame(pattern, "cC");
    }

    @Test
    public void test2() {
        TestUtil.assertEquals(pattern, "aA", "a|");
        TestUtil.assertEquals(pattern, "aB", "a|");
        TestUtil.assertEquals(pattern, "aC", "a|");
        TestUtil.assertEquals(pattern, "bA", "b|");
        TestUtil.assertEquals(pattern, "bC", "b|");
        TestUtil.assertEquals(pattern, "cA", "c|");
        TestUtil.assertEquals(pattern, "cB", "c|");
        TestUtil.assertEmpty(pattern, "A");
        TestUtil.assertEmpty(pattern, "B");
        TestUtil.assertEmpty(pattern, "C");
    }

    @Test
    public void test3() {
        TestUtil.assertEmpty(pattern, "d");
        TestUtil.assertEmpty(pattern, "e");
        TestUtil.assertEmpty(pattern, "f");
        TestUtil.assertEmpty(pattern, "g");
        TestUtil.assertEmpty(pattern, "dD");
        TestUtil.assertEmpty(pattern, "gG");
        TestUtil.assertEmpty(pattern, "hH");
        TestUtil.assertEmpty(pattern, "jlki");
        TestUtil.assertEmpty(pattern, "p;,");
        TestUtil.assertEmpty(pattern, "pi09");
        TestUtil.assertEmpty(pattern, "0l;.");
        TestUtil.assertEmpty(pattern, "135");
    }

}