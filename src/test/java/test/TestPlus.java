package test;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stateMachine.Pattern;
import stateMachine.utils.TestUtil;

import static stateMachine.utils.TestUtil.testEach;

class TestPlus {

    @Test
    public void test1() {
        Pattern pattern;
        pattern = Pattern.compile("a+");
        testEach(pattern,
                new String[]{"aaa", "aaa|"},
                new String[]{"aa", "aa|"},
                new String[]{"a", "a|"},
                new String[]{"bc", ""},
                new String[]{"", ""}
        );
    }

    @Test
    public void test2() {
        Pattern pattern;
        pattern = Pattern.compile(".+");
        testEach(pattern,
                new String[]{"abc", "abc|"},
                new String[]{"abcabc", "abcabc|"},
                new String[]{"", ""},
                new String[]{"a\nb", "a\nb|"}
        );
    }

    @Test
    public void test3() {
        Pattern pattern;
        pattern = Pattern.compile(".+bc");
        Assertions.assertEquals("abc|", TestUtil.search(pattern, "abc"));
        Assertions.assertEquals("abbc|", TestUtil.search(pattern, "abbc"));
        Assertions.assertEquals("", TestUtil.search(pattern, "bc"));
        Assertions.assertEquals("", TestUtil.search(pattern, "asfds"));
        Assertions.assertEquals("a\nbc|", TestUtil.search(pattern, "a\nbc"));
        Assertions.assertEquals("abcabc|", TestUtil.search(pattern, "abcabc"));
        Assertions.assertEquals("abcbc|", TestUtil.search(pattern, "abcbc"));
    }
}