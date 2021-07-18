package stateMachine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Config {
    public static final HashSet<Character> keywords = new HashSet<Character>(Arrays.asList('[',']','(',')','{','}','*','+','?','.','|'));
    public static final HashSet<Character> keywordsInMiddleBrace = new HashSet<Character>(Arrays.asList(']','-'));
}
