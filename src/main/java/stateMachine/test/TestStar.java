package stateMachine.test;


import org.junit.jupiter.api.Test;
import stateMachine.Pattern;

import static stateMachine.TestAll.testEach;

class TestStar {

    @Test
    public void test1() {
        Pattern pattern;


        pattern = Pattern.compile("a*");
        testEach(pattern,
                new String[]{"aaa", "aaa|"},
                new String[]{"aaab", "aaa|"},
                new String[]{"bbbbbb", ""},
                new String[]{"", ""},
                new String[]{"a\nb", "a|"}
        );

        pattern = Pattern.compile(".*");
        testEach(pattern,
                new String[]{"abc", "abc|"},
                new String[]{"abcabc", "abcabc|"},
                new String[]{"", ""},
                new String[]{"a\nb", "a\nb|"}
        );

        pattern = Pattern.compile(".*bc");
        testEach(pattern,
                new String[]{"abc", "abc|"},
                new String[]{"abcabc", "abcabc|"},
                new String[]{"bc", "bc|"},
                new String[]{"asfds", ""},
                new String[]{"a\nbc", "a\nbc|"}
        );

    }
}