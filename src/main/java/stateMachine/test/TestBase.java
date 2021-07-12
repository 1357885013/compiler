package stateMachine.test;


import org.junit.jupiter.api.Test;
import stateMachine.Pattern;

import static stateMachine.TestAll.testEach;

class TestBase {

    @Test
    public void and() {
        Pattern pattern;
        pattern = Pattern.compile("abc");
        testEach(pattern,
                new String[]{"abc", "abc|"},
                new String[]{"abcabc", "abc|abc|"},
                new String[]{"", ""},
                new String[]{"ab", ""}
        );
    }
    @Test
    public void or() {
        Pattern pattern;
        pattern = Pattern.compile("a|b|c");
        testEach(pattern,
                new String[]{"abc", "a|b|c|"},
                new String[]{"abda", "a|b|"},
                new String[]{"", ""},
                new String[]{"d", ""},
                new String[]{"a", "a|"}
        );
    }
    @Test
    public void andAndOr() {
        Pattern pattern;
        pattern = Pattern.compile(".bc|def");
        testEach(pattern,
                new String[]{"dbc", "dbc|"},
                new String[]{"abc", "abc|"},
                new String[]{"cbc", "cbc|"},
                new String[]{"def", "def|"},
                new String[]{"", ""},
                new String[]{"a\nb", ""}
        );
    }
}