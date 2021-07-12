package stateMachine.test;


import org.junit.jupiter.api.Test;
import stateMachine.Pattern;

import static stateMachine.TestAll.testEach;

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

        pattern = Pattern.compile(".+");
        testEach(pattern,
                new String[]{"abc", "abc|"},
                new String[]{"abcabc", "abcabc|"},
                new String[]{"", ""},
                new String[]{"a\nb", "a\nb|"}
        );

        pattern = Pattern.compile(".+bc");
        testEach(pattern,
                new String[]{"abc", "abc|"},
                new String[]{"abcabc", "abcabc|"},
                new String[]{"bc", ""},
                new String[]{"asfds", ""},
                new String[]{"a\nbc", "a\nbc|"}
        );


    }
}