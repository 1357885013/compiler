package stateMachine.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CmdUtil {
    private static final ProcessBuilder process = new ProcessBuilder();

    public static boolean run(String cmd) {
        System.out.println("run : " + cmd);
        ProcessBuilder process = new ProcessBuilder(cmd.split(" "));
        Process p;
        try {
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            if (p.getErrorStream().available() > 0) {
                System.out.println("error : ");
                reader = new BufferedReader(new InputStreamReader(p.getErrorStream(), StandardCharsets.UTF_8));
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(cmd.split(" ")));
            System.out.print("error");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
