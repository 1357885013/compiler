package test;


import org.junit.jupiter.api.Test;
import stateMachine.Pattern;

import static stateMachine.utils.TestUtil.testEach;

class TestFinally {

    @Test
    public void test1() {
        Pattern pattern;
        pattern = Pattern.compile("((//.*?(\\n|$))|(/\\*.*?\\*/))|([a-zA-Z_][a-zA-Z0-9_]*)|(\\d+)|(\"((\\\\\")|.)*?\")|(\\+\\+|--|\\+=|-\\+|\\*=|/=|&&|\\|\\||!=|==|>=|<=)|(\\{|\\}|\\[|\\]|\\(|\\)|\\+|\\-|\\*|/|=|&|\\||!|:|;|,|<|>|'|\\\"|\\.)|(\\b)");
        testEach(pattern,
                new String[]{"public int a=10", "public| |int| |a|=|10|"},
                new String[]{"    private static void testEach(Pattern pattern, String[]... inputs) {\n" +
                        "        System.out.println(\"--------------------------------\");\n" +
                        "        System.out.println(\"\\033[30;46;1m\" + pattern.getRegex() + \"\\033[0m\");\n" +
                        "        if (inputs != null)\n" +
                        "            for (String[] input : inputs) {\n" +
                        "                allCount++;\n" +
                        "                String result = search(pattern, input[0]);\n" +
                        "                if (result.equals(input[1])) {\n" +
                        "                    System.out.println(\"\\033[32;2m  success    \" + input[0].replace(\"\\n\", \"\\\\n\").replace(\"\\t\", \"\\\\t\") + \"\\033[0m\");\n" +
                        "                    successCount++;\n" +
                        "                } else {\n" +
                        "                    System.out.println(\"\\033[31;2m  error    \" + input[0].replace(\"\\n\", \"\\\\n\").replace(\"\\t\", \"\\\\t\") + \"\\033[0m\");\n" +
                        "                    failureCount++;\n" +
                        "                }\n" +
                        "            }\n" +
                        "    }", "++|"}
        );
    }
}