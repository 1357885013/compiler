package stateMachine.utils;

import stateMachine.State;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Util {
    private static void mapToArray(String t) {
        String[] mapItems = t.split(",");
        Map<String, String> map = new HashMap<>();
        for (String mapItem : mapItems) {
            String[] split = mapItem.split(":");
            map.put(split[0], split[1]);
        }
        for (int i = 0; i <= 29; i++) {
            if (map.containsKey(Integer.toString(i))) {
                System.out.print(map.get(Integer.toString(i)));
                System.out.print(",");
            } else
                System.out.print("-1,");
        }

    }

    public static void main(String[] args) {
        mapToArray(" 0:2,22:2,23:2,21:45,26:46");
    }

    public static Set<State> isIntersect(Set<State> states, Set<State> oldStates) {
        Set<State> t = new HashSet<>(states);
        t.retainAll(oldStates);
        return t;
//        for (State state : states) {
//            if (oldStates.contains(state))
//                return true;
//        }
//        return false;
    }
}
