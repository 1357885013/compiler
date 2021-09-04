package stateMachine;

import java.util.*;

public class Matcher {

    private CharSequence input;
    private TransformTable trans;
    private Map<Integer, int[]> groups;
    private int start = 0;
    private int end = -1;
    private int endLoc = 0;
    private State nowState;

    // \b \w \d 这些


    public Matcher(TransformTable trans, CharSequence input) {
        this.groups = new HashMap<>();
        this.input = input;
        this.trans = trans;
    }

    public Integer[] getGroups() {
        return groups.keySet().toArray(new Integer[]{});
    }

    public String group(int index) {
        int[] locs = groups.get(index);
        if (locs == null) return null;
        return (String) input.subSequence(locs[0], locs[1] + 1);
    }

    public Matcher region(int start) {
        this.start = start;
        this.end = -1;
        return this;
    }

    public Matcher region(int start, int end) {
        this.start = start;
        this.end = end;

        return this;
    }


    // 查找下一个
    public boolean lookingAt() {
        this.groups.clear();
        nowState = trans.getStartState();
        int loc = this.start;
        if (this.end == -1)
            this.end = input.length() - 1;
        boolean crossEnd = false;
        Set<State> temp;

        char input;
        while (loc <= this.end) {
            temp = null;
            input = this.input.charAt(loc);
            LinkedHashMap<Expression, Set<State>> inputs = trans.get(nowState);

            // 查找下一个状态
            Set<State> nextStateSet = trans.get(nowState, new Expression(new Expression.Node(input, false)));

            if (nextStateSet == null && trans.get(nowState) != null) {
                for (Expression input1 : inputs.keySet()) {
                    // 查找 except 总式
                    if ((input1.charAt(0).content == '^' && (!input1.substring(1).contains(new Expression(new Expression.Node(input, false)))))) {
                        nextStateSet = trans.get(nowState, input1);
                        break;
                    }
                    // 查找 except 子式
                    if (nextStateSet == null && (input1.charAt(0).content == '_' && (input1.charAt(1).content == input))) {
                        nextStateSet = trans.get(nowState, input1);
                    }
                    // 缓存dot
                    if (input1.equalsKeyword('.'))
                        temp = trans.get(nowState, input1);
                }

//                int expectLength = 0;
//                //找  .   [^abc]  ^ $
//                for (Expression in : trans.get(nowState).keySet()) {
//                    if (in.equalsKeyword('.'))
//                        temp = trans.get(nowState, in);
//                    // todo: 可能 有的单个比别的两个合起来都长
//                    if (in.charAt(0).equals('^') && !in.substring(1).contains(new Expression(input, false))) {
//                        if (in.length() > expectLength) {
//                            nextStateSet = trans.get(nowState, in);
//                            expectLength = in.length();
//                        }
//                    }
//                }
                if (nextStateSet == null) nextStateSet = temp;
            }

            // does not have next state
            if (nextStateSet == null || nextStateSet.size() == 0) {
                return crossEnd;
            }
            State nextState = nextStateSet.toArray(new State[0])[0];
            // 记录结束状态 和 group(0)
            if (nextState.isEnd()) {
                this.groups.put(0, new int[]{this.start, loc});
                crossEnd = true;
                endLoc = loc;
            }
            // 记录 group
            if (nextState.getGroupIndex().size() > 0) {
                for (Integer groupIndex : nextState.getGroupIndex()) {
                    int[] gins = this.groups.get(groupIndex);
                    if (gins == null)
                        this.groups.put(groupIndex, new int[]{loc, -1});
                    else
                        gins[1] = loc;
                }
            }
            nowState = nextState;
            loc++;
        }
        for (Integer integer : new HashSet<>(this.groups.keySet())) {
            if (groups.get(integer)[1] == -1)
                groups.remove(integer);
        }

        return crossEnd;
    }

    // 获取当前识别的结尾
    public int end() {
        return endLoc;
    }
}
