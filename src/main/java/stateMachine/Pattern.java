package stateMachine;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Pattern {
    //state change table : state - > chat - > set(state)
    private TransformTable trans;

    private int stateIndex = 0;
    private String regex;

    private boolean drawUniteLine = true;

    public static void main(String[] args) throws IOException {
        Pattern pattern;

//        pattern = Pattern.compile("((//.*?(\\n|$))|(/\\*.*?\\*/))|([a-zA-Z_][a-zA-Z0-9_]*)|(\\d+)|(\"((\\\\\")|.)*?\")|(\\+\\+|--|\\+=|-\\+|\\*=|/=|&&|\\|\\||!=|==|>=|<=)|(\\{|\\}|\\[|\\]|\\(|\\)|\\+|\\-|\\*|/|=|&|\\||!|:|;|,|<|>|'|\\\"|\\.)|(\\b)", true);
//        pattern = Pattern.compile("(\\+\\+|--|\\+=|-\\+|\\*=|/=|&&|\\|\\||!=|==|>=|<=)|(\\{|\\}|\\[|\\]|\\(|\\)|\\+|\\-|\\*|/|=|&|\\||!|:|;|,|<|>|'|\\\"|\\.)", true);
//        pattern = Pattern.compile("--|\\+=|-\\+|\\*=|/=|&&|\\|\\||!=|==|>=|<=|\\{|\\}|\\[|\\]|\\(|\\)|\\+|\\-|\\*|/|=|&|\\||!|:|;|,|<|>|'|\\\"|\\.", true);

//        pattern = Pattern.compile("ca(.|a)*", true);
//        pattern = Pattern.compile("a|b|c|dA|bB|cC", true);

//        pattern = Pattern.compile("([a-c][a-c0-2]*)|(d+)", true);
//        pattern = Pattern.compile("((\\+\\+)|(--))|((\\+=)|(-\\+)|(\\*=))", true);
//        pattern = Pattern.compile("([ab][AB]*)", true);  // end上有自旋
        // todo: 有错误
        // todo: 合成状态时死循环
//        pattern = Pattern.compile("//.*?(\\n|$)", true);
//        pattern = Pattern.compile("([a-b][a-b0-1]*1)|(b+2)", true); //((a|b)(a|b|0|1)*1)|(b+2)
//        pattern = Pattern.compile("([a-b][a-b0-1]*1)", true);
//        pattern = Pattern.compile("([a-b]*)|(b+)", true);
//        pattern = Pattern.compile("(b*)|(b+)", true);

//        pattern = Pattern.compile(".*bc", true);
//        pattern = Pattern.compile("b*?ca", true);
//        pattern = Pattern.compile("[^abc]", true);
//        pattern = Pattern.compile(".+bc", true);
//        pattern = Pattern.compile("(a*|b*)*", true);
//        pattern = Pattern.compile("([^abc]1)|([^ade]2)|([^fg]3)|([^ghijklmn]4)|[t]5", true);
        pattern = Pattern.compile("([^abc]1)|([^ade]2)|([^fg]3)|([^ghijklmn]4)|(g2)|(a2)|(.9)", true);
//        pattern = Pattern.compile("ab*?c", true);
//        pattern = Pattern.compile("ab*(abc)|(ade)", true);
//        pattern = Pattern.compile("cab*?",true);
//        pattern = Pattern.compile("(ca.b)|(.d.s)", true);
//        pattern = Pattern.compile("(a|b|[ccc][as]|d)", true);

        // todo: 看不出对错
//        pattern = Pattern.compile("a.*cd", true);

//        testResolveMBrace();

        pattern.trans.draw();
    }

    public static void print(TransformTable trans, String stage) {
        System.out.println("-----------  " + stage);
        for (State s : trans.keySet()) {
            System.out.println(s);
            for (Expression s1 : trans.get(s).keySet()) {
                System.out.println("   " + s1 + "     " + Arrays.toString(trans.get(s).get(s1).toArray()));
            }
        }
    }

    public static void print(TransformTable trans) {
        print(trans, "end");
    }

    public Matcher matcher(CharSequence input) {
        Matcher matcher = new Matcher(trans, input);
        return matcher;
    }

    public static Pattern compile(String regex) {
        return compile(regex, false);
    }

    public static Pattern compile(String regex, boolean debug) {
        Pattern pattern = new Pattern();
        pattern.regex = regex;
        pattern.parse(new Expression(regex));
        if (debug)
            pattern.trans.draw("2_Initial");
        // 合并同input边, 不用考虑 . 和 ^[
        pattern.uniteMultipleOutputStates();
        if (debug)
            pattern.trans.draw("5_UniteMultipleOutputStates");
        // 顺序处理 . 和 ^[
        pattern.progressDotAndExcept();
        if (debug)
            pattern.trans.draw("4_ProgressDotAndExcept");
        // 多个自旋 -> 大回环
//        pattern.changeSpin2Circle();
//        pattern.deleteEmptyLine();
//        if (debug)
//            pattern.trans.draw("4_ChangeSpin2Circle");
        pattern.deleteUnreachableState();
        if (debug)
            pattern.trans.draw("6_DeleteUnreachableState");
        return pattern;
    }

    private void changeSpin2Circle() {
        // state input states
        Map<State, Map<Expression, Set<State>>> spins = new HashMap<>();
        for (State leftS : trans.keySet()) {
            for (Expression input : trans.get(leftS).keySet()) {
                for (State rightS : trans.get(leftS).get(input)) {
                    if (leftS != rightS)
                        // 如果包含和输入相同的自旋
                        if (trans.get(rightS, input) != null && trans.get(rightS, input).contains(rightS)) {
                            // 记录在案,
                            spins.computeIfAbsent(rightS, v -> new HashMap<>()).computeIfAbsent(input, v -> new HashSet<>()).add(leftS);
                        }
                }
            }
        }
        // 这个状态就多个自旋
        // spins : rightState -> { leftState, input }
        for (State rightState : spins.keySet()) {
            if (spins.get(rightState).size() > 1) {
                // 处理
                // 创建新的, 除了自旋其它输出全部复制, 状态也复制.
                Map<Expression, State> newStates = new HashMap<>();
                // 先全部复制.
                State blueprint = new State(stateIndex++, rightState);
                trans.add(blueprint, trans.get(rightState));

                // 删除 本来的状态
                for (Expression input : spins.get(rightState).keySet()) {
                    trans.delete(blueprint, input, rightState);
                }
                for (Expression input : spins.get(rightState).keySet()) {
                    // 复制每个回旋的状态
                    State nn = new State(stateIndex++, blueprint);
                    trans.add(nn, trans.get(blueprint));
                    // 创建 自旋
                    trans.add(nn, input, nn);
                    newStates.put(input, nn);
                    // 老的输入指向新的
                    for (State leftState : spins.get(rightState).get(input)) {
                        trans.delete(leftState, input, rightState);
                        trans.add(leftState, input, nn);
                    }
                    // 老的输出指向新的
                    trans.delete(rightState, input, rightState);
                    trans.add(rightState, input, nn);
                }
                // 新的之间互指
                for (Expression newInput : newStates.keySet())
                    for (Expression newInput1 : newStates.keySet())
                        if (newInput != newInput1)
                            trans.add(newStates.get(newInput), newInput1, newStates.get(newInput1));

            }
        }
    }

    private void deleteUnreachableState() {
        // 删除没有输入边的非start state
        boolean stop;
        boolean first = true;
        Map<State, Integer> states = new HashMap<>();

        do {
            stop = true;
            if (first)
                // 统计 输入边的数量
                for (State leftS : trans.keySet()) {
                    if (!states.containsKey(leftS)) states.put(leftS, 0);
                    for (Expression input : trans.get(leftS).keySet()) {
                        for (State rightS : trans.get(leftS).get(input)) {
                            states.put(rightS, states.getOrDefault(rightS, 0) + 1);
                        }
                    }
                }
            first = false;
            Iterator<Map.Entry<State, Integer>> iterator = states.entrySet().iterator();
            while (iterator.hasNext()) {
                State state = iterator.next().getKey();
                if (states.get(state) == 0 && !state.isStart()) {
                    if (trans.get(state) != null)
                        for (Expression input : trans.get(state).keySet()) {
                            // 删除之前先减去指向的状态.
                            for (State stateRight : trans.get(state, input)) {
                                int count = states.getOrDefault(stateRight, 1);
                                states.put(stateRight, --count);
                            }
                        }
                    trans.delete(state);
                    iterator.remove();
                    stop = false;
                }
            }
        } while (!stop);
    }

    private boolean isIntersect(String left, String right) {
        if (left.equals(right) || left.equals(Expression.dot) || right.equals(Expression.dot)) return true;
        if (left.charAt(0) == '^' && right.charAt(0) == '^') return false;
        if (left.charAt(0) == '^' && right.length() == 1 && right.charAt(0) == left.charAt(1)) return false;
        if (right.charAt(0) == '^' && left.length() == 1 && left.charAt(0) == right.charAt(1)) return false;
        System.out.println("error in compare for intersect:  " + left + "  " + right);
        return false;
    }

    private boolean getAllStatesCanReach(State which, Set<State> state, Set<Expression> symbols) {
        boolean isEnd = which.isEnd();
        state.add(which);
        if (trans.get(which) != null)
            symbols.addAll(trans.get(which).keySet());
        LinkedHashMap<Expression, Set<State>> inputs = trans.get(which);
        if (inputs == null || inputs.isEmpty())
            return isEnd;
        if (inputs.containsKey(Expression.emptyInput)) {
            for (State eachState : inputs.get(Expression.emptyInput)) {
                if (!state.contains(eachState))
                    if (getAllStatesCanReach(eachState, state, symbols)) isEnd = true;
            }
        }
        return isEnd;
    }

    /**
     * 获取所有空边连接着的状态
     *
     * @param whichs 输入的状态集
     * @param state  输出 状态
     * @param inputs 输出 inputs
     */
    private boolean getAllStatesCanReach(Set<State> whichs, Set<State> state, Set<Expression> inputs) {
        boolean isEnd = false;
        for (State which : whichs) {
            if (getAllStatesCanReach(which, state, inputs)) isEnd = true;
        }
        return isEnd;
    }

    private StateSet getAllStatesCanReach(State state) {
        StateSet newState = StateSet.build(stateIndex++);
        if (getAllStatesCanReach(state, newState.states, newState.inputs)) newState.setEndTrue();
        return newState;
    }

    private StateSet getAllStatesCanReachBySymbol(StateSet whichs, Expression input) {
        StateSet newState = StateSet.build(stateIndex++);

        for (State which : whichs.states) {
            Set<State> toStates = trans.get(which, input);
            if (toStates == null) continue;
            if (getAllStatesCanReach(toStates, newState.states, newState.inputs)) newState.setEndTrue();
        }
        newState.inputs.remove(Expression.emptyInput);
        return newState;
    }

    private String getKeyOfStates(Set<State> states) {
        return Arrays.toString(states.stream().map(State::getIndex).sorted().toArray(Integer[]::new));
    }

    // 弄一个新的
    private void uniteMultipleOutputStates() {
        stateIndex = 1;

        List<StateSet> progress = new LinkedList<>();
        StateSet start = getAllStatesCanReach(trans.getStartState());
        // 记录有谁指向自己, 自己替换的时候不用遍历
        Map<State, Map<State, Expression>> index = new LinkedHashMap<>();
        start.setStart(true);
        TransformTable t = new TransformTable(start);
        progress.add(start);
        Map<String, State> cache = new HashMap<>();
        while (!progress.isEmpty()) {
            StateSet nowState = progress.get(0);
            // 从progress队列里取出来的是一个state集.
            String nowStateKey = getKeyOfStates(nowState.states);
            if (!cache.containsKey(nowStateKey)) {
                // 先遍历每一个输入,得到每个输出states.
                // todo:input 为空时
                for (Expression input : nowState.inputs) {
                    StateSet newOutState = getAllStatesCanReachBySymbol(nowState, input);
                    t.add(nowState, input, newOutState);
                }
                // 处理 ^
                progressExceptAndDot(t, nowState);
                if (t.get(nowState) != null)
                    for (Expression input : new HashSet<>(t.get(nowState).keySet())) {
                        StateSet newOutState = (StateSet) t.get(nowState, input).toArray()[0];
                        String oldStateKey = getKeyOfStates(newOutState.states);
                        if (cache.containsKey(oldStateKey)) {
                            t.delete(nowState, input);
                            t.add(nowState, input, cache.get(oldStateKey));
                        } else {
                            progress.add(newOutState);
                            t.add(nowState, input, newOutState);
                            // 索引一下.
                            Map<State, Expression> indexEach = index.getOrDefault(newOutState, new HashMap<>());
                            indexEach.put(nowState, input);
                            index.put(newOutState, indexEach);
                        }
                    }
                cache.put(getKeyOfStates(nowState.states), nowState);
            } else {
                for (State indexKey : index.get(nowState).keySet()) {
                    Set<State> states = t.get(indexKey, index.get(nowState).get(indexKey));
                    states.remove(nowState);
                    states.add(cache.get(nowStateKey));
                }
            }
            progress.remove(0);
        }
        this.trans = t;
    }

    private void progressExceptAndDot(TransformTable t, State nowState) {
        // toState 事只有一个, 是stateSet
        Map<Expression, Set<Character>> excepts = new HashMap<>();
        char max = Character.MIN_VALUE, min = Character.MAX_VALUE;
        StateSet states = StateSet.build(stateIndex++);
        // 统计 except 的情况
        if (t.get(nowState) != null)
            for (Expression input : t.get(nowState).keySet()) {
                // 是 ^
                if (input.charAt(0).equalsKeyword('^')) {
                    excepts.put(input, Arrays.stream(input.substring(1).contents).map(a -> a.content).collect(Collectors.toSet()));
                    max = (char) Math.max((int) (excepts.get(input).stream().max(Character::compare).get()), (int) max);
                    min = (char) Math.min((int) (excepts.get(input).stream().min(Character::compare).get()), (int) min);
                    StateSet states1 = (StateSet) t.get(nowState, input).toArray()[0];
                    states.states.addAll(states1.states);
                    states.inputs.addAll(states1.inputs);
                }
            }
        // todo: 把except加到正常的上面
        for (Expression input : excepts.keySet()) {
            // 是 ^
            if (input.charAt(0).equalsKeyword('^')) {
                for (Expression rightInput : t.get(nowState).keySet()) {
                    // 不是本身 且 不是 .  不是 ^
                    if (!rightInput.equals(input) && !rightInput.charAt(0).equalsKeyword('^') && !rightInput.equalsKeyword('.'))
                        // 如果相交
                        if (!input.substring(1).contains(rightInput)) { // 全部转移
                            ((StateSet) t.get(nowState, rightInput).toArray()[0]).states.addAll(((StateSet) t.get(nowState, input).toArray()[0]).states);
                            ((StateSet) t.get(nowState, rightInput).toArray()[0]).inputs.addAll(((StateSet) t.get(nowState, input).toArray()[0]).inputs);
                        }
                }
            }
        }

        if (!excepts.isEmpty() && excepts.size() > 2) {
            // 拼接最大的
            String allS = "^";
            for (int c = min; c <= max; c++) {
                allS += ((char) c);
            }
            Expression all = new Expression(allS);
            // 添加最大的
            t.add(nowState, all, states);

            // 添加中间的小的
            for (int c = min; c <= max; c++) {
                StateSet s = StateSet.build(stateIndex++);
                for (Expression expression : excepts.keySet()) {
                    if (!excepts.get(expression).contains((char) c)) {
                        s.states.addAll(((StateSet) t.get(nowState, expression).toArray()[0]).states);
                        s.inputs.addAll(((StateSet) t.get(nowState, expression).toArray()[0]).inputs);
                        // 添加
                    }
                }
                t.add(nowState, new Expression("_" + (char) c), s);
            }


            for (Expression input : excepts.keySet()) {
                t.delete(nowState, input);
            }
        }

        // 处理 .
        Set<State> allState = trans.get(nowState, Expression.dot);
        if (allState != null && !allState.isEmpty()) {
            // 把. 指向的状态的所有输出复制到其它边指向的状态上
            for (Expression otherInput : trans.get(nowState).keySet())
                if (!otherInput.equalsKeyword('.'))
                    for (State otherState : trans.get(nowState, otherInput))
                        for (State eachAllState : allState)
                            trans.add(otherState, trans.get(eachAllState));

            // 删除后重新添加,让它排在后面
            trans.add(nowState, Expression.dot, trans.delete(nowState, Expression.dot));
        }
    }

    private void progressDotAndExcept() {
        // (所有输入都处理了,再处理自己)
        Map<State, HashSet<State>> allInStates = new HashMap<>();
        Map<State, Boolean> resolved = new HashMap<>();
        State nowState = null;
        // 初始化
        for (State state : trans.keySet()) {
            if (state.isStart())
                nowState = state;
            else
                allInStates.put(state, new HashSet<State>());
            resolved.put(state, false);
        }

        // 统计指向某状态的边的数量
        for (State state : trans.keySet()) {
            for (Expression input : trans.get(state).keySet()) {
                for (State toState : trans.get(state, input)) {
                    HashSet<State> states = allInStates.get(toState);
                    // 结束状态只有输入没有输出
                    if (states != null && !toState.equals(state))
                        states.add(state);
                }
            }
        }

        Set<Expression> excepts = new HashSet<>();
        do {
            if (trans.get(nowState) != null) {
                excepts.clear();
                for (Expression input : trans.get(nowState).keySet()) {
                    // 是欠处理的边不?
                    // 是 .
                    if (input.equalsKeyword('.')) {
                        // 是 ^
                    } else if (input.charAt(0).equalsKeyword('^')) {
                        excepts.add(input.substring(1));
                        for (Expression rightInput : trans.get(nowState).keySet()) {
                            // 不是本身 且 不是 .  不是 ^
                            if (!rightInput.equals(input) && !rightInput.charAt(0).equalsKeyword('^') && !rightInput.equalsKeyword('.'))
                                // 如果相交
                                if (!input.substring(1).contains(rightInput))
                                    // 全部挨个转移
                                    for (State leftState : trans.get(nowState, input)) {
                                        // 是否转移成功
                                        trans.add(nowState, rightInput, leftState);
                                    }
                        }
                    }
                }
                // 处理 所有 ^,
                // 计算新的 总的
                //todo:
//                if (excepts.size() > 1) {
//
//                    State finalNowState = nowState;
//                    String expectAll = "^" + excepts.stream().flatMap(a -> Stream.of(a.contents)).distinct().sorted().collect(Collectors.joining(""));
//                    trans.add(nowState, expectAll, excepts.stream().flatMap(a -> (trans.get(finalNowState, "^" + a).stream())).collect(Collectors.toSet()));
//                    for (Expression except : excepts) {
//                        String key = "^" + excepts.stream().filter(a -> !a.equals(except)).flatMap(a -> Stream.of(a.split(""))).distinct().sorted().collect(Collectors.joining(""));
//                        Set<State> value = excepts.stream().filter(a -> !a.equals(except)).flatMap(a -> (trans.get(finalNowState, "^" + a).stream())).collect(Collectors.toSet());
//                        trans.add(nowState, key, value);
//                    }
//                    for (Expression except : excepts) {
//                        // 删除后重新添加,让它排在后面
//                        trans.add(nowState, "^" + except, trans.delete(nowState, "^" + except));
//                    }
//                }
                // 处理 .
                Set<State> allState = trans.get(nowState, Expression.dot);
                if (allState != null && !allState.isEmpty()) {
                    // 把. 指向的状态的所有输出复制到其它边指向的状态上
                    for (Expression otherInput : trans.get(nowState).keySet())
                        if (!otherInput.equalsKeyword('.'))
                            for (State otherState : trans.get(nowState, otherInput))
                                for (State eachAllState : allState)
                                    trans.add(otherState, trans.get(eachAllState));

                    // 删除后重新添加,让它排在后面
                    trans.add(nowState, Expression.dot, trans.delete(nowState, Expression.dot));
                }
            }
            resolved.put(nowState, true);
            State temp = null;
            //删除allInStates的同时 寻找下一个所有边都处理的状态
            for (State state : allInStates.keySet()) {
                allInStates.get(state).remove(nowState);
                if (allInStates.get(state).isEmpty() && temp == null)
                    temp = state;
            }
            if (temp != null)
                allInStates.remove(temp);
            nowState = temp;

        } while (nowState != null);
        // 处理失败
        if (resolved.values().contains(false))
            System.out.println("有边没被处理, 在. 和 [^] 阶段");
    }

    private int findMidBraceEnd(Expression e, int begin) {
        Stack<Boolean> braces = new Stack<Boolean>();
        if (e.charAt(begin).equalsKeyword('[')) {
            braces.push(true);
            begin++;
            Expression.Node now;
            while (!(now = e.charAt(begin++)).isEmpty) {
                if (now.equalsKeyword('['))
                    braces.add(true);
                else if (now.equalsKeyword(']')) {
                    if (braces.peek()) {
                        braces.pop();
                        if (braces.empty())
                            return begin - 1;
                    }
                }
            }
        }
        return begin;
    }

    private int findBraceEnd(Expression e, int begin) {
        Stack<Boolean> braces = new Stack<Boolean>();
        if (e.charAt(begin).equalsKeyword('(')) {
            braces.push(true);
            begin++;
            Expression.Node now;
            while (!(now = e.charAt(begin++)).isEmpty) {
                if (now.equalsKeyword('('))
                    braces.add(true);
                else if (now.equalsKeyword(')')) {
                    if (braces.peek()) {
                        braces.pop();
                        if (braces.empty())
                            return begin - 1;
                    }
                }
            }
        }
        return begin;
    }

    private int findNodeEnd(Expression e, int begin) {
        if (e.charAt(begin).equalsKeyword('(')) {
            begin = findBraceEnd(e, begin);
        } else if (e.charAt(begin).equalsKeyword('['))
            begin = findMidBraceEnd(e, begin);
        Expression.Node now = e.charAt(begin + 1);
        Expression.Node next = e.charAt(begin + 2);
        if (now.equalsKeyword('*') || now.equalsKeyword('+')) {
            if (next.equalsKeyword('?'))
                begin += 2;
            else
                begin++;
        } else if (now.equalsKeyword('?'))
            begin++;
        return begin;
    }

    public void parse(Expression regex) {
        // 初始化 状态表
        trans = new TransformTable(regex);
        int stateGroupIndex = 1;
        // 循环找能展开的正则分式
        boolean or;
        do {
            or = true;
            Set<State> transKeys = new HashSet<>(trans.keySet());
            for (State inState : transKeys) {
                Map<Expression, Set<State>> inputToStates = trans.get(inState);
                Set<Expression> inputToStatesKeys = new HashSet<>(inputToStates.keySet());
                for (Expression input : inputToStatesKeys) {
                    Set<State> toState = inputToStates.get(input);

                    if (canSplit(input)) {
                        or = false;
                        State lastAndState = inState;
                        for (int i = 0; i < input.contents.length; ) {
                            int nodeEnd = findNodeEnd(input, i);

                            // 整个都是括号
                            if (i == 0 && nodeEnd == input.length() - 1)
                                if (input.charAt(nodeEnd).equalsKeyword(')')) {
                                    trans.add(inState, input.substring(1, input.length() - 1), new HashSet<>(trans.get(inState, input)));
                                    inState.addGroupIndex(stateGroupIndex);
                                    for (State state : trans.get(inState, input)) {
                                        state.addGroupIndex(stateGroupIndex);
                                    }
                                    stateGroupIndex++;
                                    trans.delete(inState, input);
                                } else {
                                    // 没有 or 也 没有 and
                                    progressSmallestUnit(inState, input);
                                    break;
                                }

                            Expression.Node next = input.charAt(nodeEnd + 1);
                            // 如果是 OR 结构
                            if (next.equalsKeyword('|')) {
                                // or 结构的第一部分
                                // 把 已经识别处理的and结构当成or的子部分.
                                for (State state : toState) {
                                    if (trans.get(state) == null || trans.get(state).size() == 0) {
                                        State newEndState = State.build(stateIndex++);
                                        newEndState.copy(state);
                                        trans.add(lastAndState, input.substring(i, nodeEnd + 1), newEndState);
                                    } else
                                        trans.add(lastAndState, input.substring(i, nodeEnd + 1), state);
                                }
                                lastAndState = inState;
                                i = nodeEnd + 2;
                                continue;
                            }
                            // AND 结构
                            if (nodeEnd == input.length() - 1) {
                                trans.add(lastAndState, input.substring(i, nodeEnd + 1), toState);
                                // 删除原有的转换路径
                                trans.delete(inState, input);
                            } else {
                                State temp = State.build(stateIndex++);
                                trans.add(lastAndState, input.substring(i, nodeEnd + 1), temp);
                                lastAndState = temp;
                            }
                            i = nodeEnd + 1;
                        }

                        // 输出中间步骤
//                        trans.draw();
                    }
                }
            }
        } while (!or);
    }

    private void progressSmallestUnit(State inState, Expression input) {
        if ((input.charAt(input.length() - 1).equalsKeyword('?'))) {
            if ((input.charAt(input.length() - 1).equalsKeyword('*'))) {
                Expression baseInput = input.substring(0, input.length() - 2);
                for (State rightState : trans.get(inState, input)) {
                    trans.add(rightState, baseInput, rightState);
                }
                trans.add(inState, baseInput, new HashSet<>(trans.get(inState, input)));
                trans.add(inState, Expression.emptyInput, new HashSet<>(trans.get(inState, input)));
                trans.delete(inState, input);
            } else if ((input.charAt(input.length() - 1).equalsKeyword('+'))) {
                trans.add(inState, input.substring(0, input.length() - 2), new HashSet<>(trans.get(inState, input)));
                // 结束状态 自旋
                for (State s : trans.get(inState, input))
                    trans.add(s, input.substring(0, input.length() - 2), s);
                trans.delete(inState, input);
            } else {
                trans.add(inState, input.substring(0, input.length() - 1), new HashSet<>(trans.get(inState, input)));
                trans.add(inState, Expression.emptyInput, new HashSet<>(trans.get(inState, input)));
                trans.delete(inState, input);
            }
        } else if ((input.charAt(input.length() - 1).equalsKeyword('*'))) {
            // 有状态的输出复制到左状态上, 右状态上加自旋, x* 改成x
            Expression baseInput = input.substring(0, input.length() - 1);
            for (State rightState : trans.get(inState, input)) {
                trans.add(rightState, baseInput, rightState);
            }
            trans.add(inState, baseInput, new HashSet<>(trans.get(inState, input)));
            trans.add(inState, Expression.emptyInput, new HashSet<>(trans.get(inState, input)));
            trans.delete(inState, input);
        } else if ((input.charAt(input.length() - 1).equalsKeyword('+'))) {
            trans.add(inState, input.substring(0, input.length() - 1), new HashSet<>(trans.get(inState, input)));
            // 结束状态 自旋
            for (State s : trans.get(inState, input))
                trans.add(s, input.substring(0, input.length() - 1), s);
            trans.delete(inState, input);
        } else if (input.charAt(0).equalsKeyword('[') && input.charAt(input.length() - 1).equalsKeyword(']')) {
            boolean except = input.charAt(1).equalsKeyword('^');
            Set<State> toStates = trans.get(inState, input);
            Set<Expression> each = resolveMBrace(input.substring(except ? 2 : 1, input.length() - 1));
            if (except) {
                List<Expression.Node> t = each.stream().map(a -> a.charAt(0)).sorted(Comparator.comparingInt(a -> a.content)).collect(Collectors.toList());
                t.add(0, new Expression.Node('^', true));
                trans.add(inState, new Expression(t.toArray(new Expression.Node[0])), new HashSet<>(toStates));
            } else {
                for (Expression s : each) {
                    trans.add(inState, s, new HashSet<>(toStates));
                }
            }
            trans.delete(inState, input);
        }
    }

    private boolean canSplit(Expression regex) {
        // todo: _. 无法匹配
        //  _.
        // _@
        // ^abc
        // \^abc
        // _@

        if (regex.charAt(0).equalsKeyword('^')) return false;
        return regex.length() > 1;
    }


    private Set<Expression> resolveMBrace(Expression body) {
        Set<Expression> result = new HashSet<>();
        while (body.charAt(0).equals('[') && body.charAt(body.length() - 1).equals(']')) {
            body = body.substring(1, body.length() - 1);
        }
        for (int i = 0; i < body.length(); i++) {
            if (i < body.length() - 2 && body.charAt(i + 1).equalsKeyword('-')) {
                result.addAll(resolveMBraceIn(body.substring(i, i + 3)));
                i += 2;
            }
            result.add(new Expression(body.charAt(i)));
        }
        return result;
    }

    private Set<Expression> resolveMBraceIn(Expression body) {
        Set<Expression> result = new HashSet<>();
        if (!body.charAt(1).equalsKeyword('-')) {
            System.out.println("a-b 错误");
            return result;
        }
        if (body.charAt(0).content >= body.charAt(2).content)
            return new HashSet<>(Arrays.asList(new Expression(body.charAt(0)), new Expression(body.charAt(0)), new Expression(body.charAt(0))));
        for (int i = body.charAt(0).content; i <= body.charAt(2).content; i++) {
            result.add(new Expression((char) i, false));
        }
        return result;
    }


    private static void testResolveMBrace() {
        Pattern pattern = new Pattern();

        Set<Expression> strings = pattern.resolveMBrace(new Expression("a-zA-Z0-9"));
        strings.forEach(System.out::println);
        System.out.println("----");
        strings = pattern.resolveMBrace(new Expression("z-a-0-"));
        strings.forEach(System.out::println);
        System.out.println("----");
        strings = pattern.resolveMBrace(new Expression("-z-a-0-"));
        strings.forEach(System.out::println);
        System.out.println("----");
        strings = pattern.resolveMBrace(new Expression("-a-c-0-"));
        strings.forEach(System.out::println);
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
