package stateMachine.test;


import org.junit.jupiter.api.Test;
import stateMachine.Pattern;

import static stateMachine.TestAll.testEach;

public class TestAll {
    private static int allCount = 0;
    private static int successCount = 0;
    private static int failureCount = 0;


    public static void main(String[] args) {
        Pattern pattern;





        // todo: {0,4}  // 要加上匹配次数么, 匹配几次进入下一状态
        // : [^ ]
        // todo: $ ^
        // todo: 贪婪模式
        // todo: 反向预查

        System.out.println();
        System.out.println();
//        System.out.println("==================================================");
        System.out.print("\033[32;2m  " + successCount + "\033[0m");
        System.out.print("/");
        System.out.print("\033[37;2m" + allCount + "\033[0m");

        System.out.println("      \033[31;2m" + failureCount + "\033[0m");
    }
}
