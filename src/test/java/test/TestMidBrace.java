package test;


import org.junit.jupiter.api.Test;
import stateMachine.Pattern;

import static stateMachine.utils.TestUtil.testEach;

class TestMidBrace {

    @Test
    public void test1() {
        Pattern pattern;


        pattern = Pattern.compile("[abc]");
        testEach(pattern,
                new String[]{"abc", "a|b|c|"},
                new String[]{"abda", "a|b|"},
                new String[]{"", ""},
                new String[]{"d", ""},
                new String[]{"a", "a|"}
        );
    }

    @Test
    public void test2() {
        Pattern pattern;
        pattern = Pattern.compile("[^abc]");
        testEach(pattern,
                new String[]{"a", ""},
                new String[]{"b", ""},
                new String[]{"c", ""},
                new String[]{"d", "d|"},
                new String[]{"\n", "\n|"},
                new String[]{"f", "f|"}
        );

    }

    @Test
    public void test3() {
        Pattern pattern;
        pattern = Pattern.compile("([^abc]1)|([^ade]2)|([^fg]3)|([^ghijklmn]4)|[t]5");
        testEach(pattern,
                new String[]{"a1", ""},
                new String[]{"b1", ""},
                new String[]{"c1", ""},
                new String[]{"d1", "d1|"},
                new String[]{"e1", "e1|"},
                new String[]{"f1", "f1|"},
                new String[]{"g1", "g1|"},
                new String[]{"h1", "h1|"},
                new String[]{"i1", "i1|"},
                new String[]{"j1", "j1|"},
                new String[]{"k1", "k1|"},
                new String[]{"l1", "l1|"},
                new String[]{"m1", "m1|"},
                new String[]{"n1", "n1|"},
                new String[]{"t1", "t1|"},

                new String[]{"a1", ""},
                new String[]{"b2", "b2|"},
                new String[]{"c2", "c2|"},
                new String[]{"d2", ""},
                new String[]{"e2", ""},
                new String[]{"f2", "f2|"},
                new String[]{"g2", "g2|"},
                new String[]{"h2", "h2|"},
                new String[]{"i2", "i2|"},
                new String[]{"j2", "j2|"},
                new String[]{"k2", "k2|"},
                new String[]{"l2", "l2|"},
                new String[]{"m2", "m2|"},
                new String[]{"n2", "n2|"},
                new String[]{"t2", "t2|"},

                new String[]{"a3", "a3|"},
                new String[]{"b3", "b3|"},
                new String[]{"c3", "c3|"},
                new String[]{"d3", "d3|"},
                new String[]{"e3", "e3|"},
                new String[]{"f3", ""},
                new String[]{"g3", ""},
                new String[]{"h3", "h3|"},
                new String[]{"i3", "i3|"},
                new String[]{"j3", "j3|"},
                new String[]{"k3", "k3|"},
                new String[]{"l3", "l3|"},
                new String[]{"m3", "m3|"},
                new String[]{"n3", "n3|"},
                new String[]{"t3", "t3|"},

                new String[]{"a4", "a4|"},
                new String[]{"b4", "b4|"},
                new String[]{"c4", "c4|"},
                new String[]{"d4", "d4|"},
                new String[]{"e4", "e4|"},
                new String[]{"f4", "f4|"},
                new String[]{"g4", ""},
                new String[]{"h4", ""},
                new String[]{"i4", ""},
                new String[]{"j4", ""},
                new String[]{"k4", ""},
                new String[]{"l4", ""},
                new String[]{"m4", ""},
                new String[]{"n4", ""},
                new String[]{"t4", "t4|"},

                new String[]{"t5", "t5|"}


        );

    }
}