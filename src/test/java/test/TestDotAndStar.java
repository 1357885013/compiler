package test;


import org.junit.jupiter.api.Test;
import stateMachine.Pattern;
import stateMachine.utils.TestUtil;

class TestDotAndStar {
    static Pattern pattern = Pattern.compile("ab.*12", true);

    @Test
    public void test1() {
        TestUtil.assertSame(pattern, "ab12");
        TestUtil.assertSame(pattern, "abc12");
        TestUtil.assertSame(pattern, "abcd12");
        TestUtil.assertSame(pattern, "abcde12");
        TestUtil.assertSame(pattern, "abcdef12");
        TestUtil.assertSame(pattern, "abcdefg12");
        TestUtil.assertSame(pattern, "abcdefgh12");
        TestUtil.assertSame(pattern, "abcdefghj12");
        TestUtil.assertSame(pattern, "abcdefghjk12");
    }

    @Test
    public void test2() {
        TestUtil.assertSame(pattern, "ab111112");
        TestUtil.assertSame(pattern, "ab2222112");
        TestUtil.assertSame(pattern, "ab212");
        TestUtil.assertSame(pattern,"ab1111212");
    }
    @Test
    public void test3() {
        TestUtil.assertSame(pattern, "ab1212");
        TestUtil.assertSame(pattern, "ab121212");
    }
    @Test
    public void test4() {
        TestUtil.assertEmpty(pattern, "ab1");
        TestUtil.assertEmpty(pattern, "ab11");
        TestUtil.assertEmpty(pattern, "ab2");
        TestUtil.assertEmpty(pattern, "ab22");
        TestUtil.assertEmpty(pattern, "1ab22");
        TestUtil.assertEmpty(pattern, "ab1ab22");
        TestUtil.assertEmpty(pattern, "");
    }
}