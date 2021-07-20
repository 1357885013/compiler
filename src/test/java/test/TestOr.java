package test;


import org.junit.jupiter.api.Test;
import stateMachine.Pattern;
import stateMachine.utils.TestUtil;

class TestOr {
    static Pattern pattern = Pattern.compile("a|b|c|dA|bB|cC", true);

    @Test
    public void test1() {
        TestUtil.assertSame(pattern, "aABC");
        TestUtil.assertSame(pattern, "bABC");
        TestUtil.assertSame(pattern, "cABC");
        TestUtil.assertSame(pattern, "dABC");
        TestUtil.assertSame(pattern, "abBC");
        TestUtil.assertSame(pattern, "bbBC");
        TestUtil.assertSame(pattern, "cbBC");
        TestUtil.assertSame(pattern, "dbBC");
        TestUtil.assertSame(pattern, "abcC");
        TestUtil.assertSame(pattern, "bbcC");
        TestUtil.assertSame(pattern, "cbcC");
        TestUtil.assertSame(pattern, "dbcC");
        TestUtil.assertSame(pattern, "aAcC");
        TestUtil.assertSame(pattern, "bAcC");
        TestUtil.assertSame(pattern, "cAcC");
        TestUtil.assertSame(pattern, "dAcC");
    }

    @Test
    public void test2() {
        TestUtil.assertEmpty(pattern, "aabC");
        TestUtil.assertEmpty(pattern, "babC");
        TestUtil.assertEmpty(pattern, "cabC");
        TestUtil.assertEmpty(pattern, "dabC");
        TestUtil.assertEmpty(pattern, "abbC");
        TestUtil.assertEmpty(pattern, "bbbC");
        TestUtil.assertEmpty(pattern, "cbbC");
        TestUtil.assertEmpty(pattern, "dbbC");
        TestUtil.assertEmpty(pattern, "aacC");
        TestUtil.assertEmpty(pattern, "bacC");
        TestUtil.assertEmpty(pattern, "cacC");
        TestUtil.assertEmpty(pattern, "dacC");
    }

    @Test
    public void test3() {
        TestUtil.assertEmpty(pattern, "aab");
        TestUtil.assertEmpty(pattern, "bab");
        TestUtil.assertEmpty(pattern, "cab");
        TestUtil.assertEmpty(pattern, "dab");
        TestUtil.assertEmpty(pattern, "abb");
        TestUtil.assertEmpty(pattern, "bbb");
        TestUtil.assertEmpty(pattern, "cbb");
        TestUtil.assertEmpty(pattern, "dbb");
        TestUtil.assertEmpty(pattern, "aac");
        TestUtil.assertEmpty(pattern, "bac");
        TestUtil.assertEmpty(pattern, "cac");
        TestUtil.assertEmpty(pattern, "dac");
    }

    @Test
    public void test4() {
        TestUtil.assertEmpty(pattern, "aabCC");
        TestUtil.assertEmpty(pattern, "babCC");
        TestUtil.assertEmpty(pattern, "cabCC");
        TestUtil.assertEmpty(pattern, "dabCC");
        TestUtil.assertEmpty(pattern, "abbCC");
        TestUtil.assertEmpty(pattern, "bbbCC");
        TestUtil.assertEmpty(pattern, "cbbCC");
        TestUtil.assertEmpty(pattern, "dbbCC");
        TestUtil.assertEmpty(pattern, "aacCC");
        TestUtil.assertEmpty(pattern, "bacCC");
        TestUtil.assertEmpty(pattern, "cacCC");
        TestUtil.assertEmpty(pattern, "dacCC");
    }

}