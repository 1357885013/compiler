package stateMachine.utils;

import org.junit.jupiter.api.Assertions;
import stateMachine.Matcher;
import stateMachine.Pattern;

public class TestUtil {
    private static int allCount = 0;
    private static int successCount = 0;
    private static int failureCount = 0;


    public static void testEach(Pattern pattern, String[]... inputs) {
        System.out.println("--------------------------------");
        System.out.println("\033[30;46;1m" + pattern.getRegex() + "\033[0m");
        if (inputs != null)
            for (String[] input : inputs) {
                allCount++;
                String result = search(pattern, input[0]);
                Assertions.assertEquals(result, input[1]);
//                if (result.equals(input[1])) {
//                    System.out.println("\033[32;2m  success    " + input[0].replace("\n", "\\n").replace("\t", "\\t") + "\033[0m");
//                    successCount++;
//                } else {
//                    System.out.println("\033[31;2m  error    " + input[0].replace("\n", "\\n").replace("\t", "\\t") + "\033[0m");
//                    System.out.println("          expect : " + input[1].replace("\n", "\\n").replace("\t", "\\t"));
//                    System.out.println("          actual : " + result.replace("\n", "\\n").replace("\t", "\\t"));
//                    failureCount++;
//                }
            }
    }

    public static String search(Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        StringBuilder sb = new StringBuilder();
        int pos = 0;
        int endPos = input.length() - 1;
        while (pos <= endPos) {      //不是一次把一行里的所有结果都匹配出来，而是一个一个的匹配
            matcher.region(pos, endPos);  //核心语句, 设置搜索区域
            if (matcher.lookingAt()) { // 从区域的头部开始查找匹配,不用匹配全部
                // 识别到了
                if (matcher.group(0) != null)
                    if (!matcher.group(0).equals(""))
                        sb.append(matcher.group(0)).append("|");
                pos = matcher.end() + 1;
            } else
                break;
//                System.out.println("bad token at line ");
        }
        return sb.toString();
    }

    public static void assertSame(Pattern pattern, String input) {
        Assertions.assertEquals(input + "|", search(pattern, input));
    }

    public static void assertEmpty(Pattern pattern, String input) {
        Assertions.assertEquals("", search(pattern, input));
    }

    public static void assertEquals(Pattern pattern, String input, String except) {
        Assertions.assertEquals(except, search(pattern, input));
    }

}
