package test;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stateMachine.Pattern;
import stateMachine.utils.TestUtil;

import static stateMachine.utils.TestUtil.testEach;

class TestPlus {

    @Test
    public void test1() {
        Pattern pattern = Pattern.compile("a+", true);
        TestUtil.assertSame(pattern, "aaa");
        TestUtil.assertSame(pattern, "aa");
        TestUtil.assertSame(pattern, "a");
        TestUtil.assertEmpty(pattern, "bc");
        TestUtil.assertEmpty(pattern, "");
    }

    @Test
    public void testDot() {
        Pattern pattern = Pattern.compile(".+", true);
        TestUtil.assertSame(pattern, "abc");
        TestUtil.assertSame(pattern, "abcabc");
        TestUtil.assertSame(pattern, "a\nb");
        TestUtil.assertSame(pattern, "bfdsajoc");
        TestUtil.assertEmpty(pattern, "");
    }

    @Test
    public void testAll() {
        Pattern pattern = Pattern.compile(".+bc", true);
        TestUtil.assertSame(pattern, "abc");
        TestUtil.assertSame(pattern, "abbc");
        TestUtil.assertEmpty(pattern, "bc");
        TestUtil.assertEmpty(pattern, "asfds");
        TestUtil.assertSame(pattern, "a\nbc");
        TestUtil.assertSame(pattern, "abcabc");
        TestUtil.assertSame(pattern, "abcbc");
    }
}