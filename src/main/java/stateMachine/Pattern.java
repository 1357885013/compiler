package stateMachine;

import stateMachine.utils.Util;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Pattern {
    //state change table : state - > chat - > set(state)
    private TransformTable trans;

    private int stateIndex = 0;
    private String regex;

    public void print() {
        trans.draw();
    }

    private boolean drawUniteLine = true;

    public static void main(String[] args) throws IOException {
        Pattern pattern;

//        pattern = Pattern.compile("((//.*?(\\n|$))|(/\\*.*?\\*/))|([a-zA-Z_][a-zA-Z0-9_]*)|(\\d+)|(\"((\\\\\")|.)*?\")|(\\+\\+|--|\\+=|-\\+|\\*=|/=|&&|\\|\\||!=|==|>=|<=)|(\\{|\\}|\\[|\\]|\\(|\\)|\\+|\\-|\\*|/|=|&|\\||!|:|;|,|<|>|'|\\\"|\\.)|(\\b)", true);
//        pattern = Pattern.compile("(\\+\\+|--|\\+=|-\\+|\\*=|/=|&&|\\|\\||!=|==|>=|<=)|(\\{|\\}|\\[|\\]|\\(|\\)|\\+|\\-|\\*|/|=|&|\\||!|:|;|,|<|>|'|\\\"|\\.)", true);
//        pattern = Pattern.compile("--|\\+=|-\\+|\\*=|/=|&&|\\|\\||!=|==|>=|<=|\\{|\\}|\\[|\\]|\\(|\\)|\\+|\\-|\\*|/|=|&|\\||!|:|;|,|<|>|'|\\\"|\\.", true);

//        pattern = Pattern.compile("ca(.|a)*", true);
        pattern = Pattern.compile("a|b|c|dA|bB|cC", true);

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
//        pattern = Pattern.compile("([^abc]1)|([^ade]2)|([^fg]3)|([^ghijklmn]4)", true);
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
        // 先去空边
        pattern.deleteEmptyLine();
        if (debug)
            pattern.trans.draw("3_DeleteEmptyLine");
        // 顺序处理 . 和 ^[
        pattern.progressDotAndExcept();
        if (debug)
            pattern.trans.draw("4_ProgressDotAndExcept");
        // 多个自旋 -> 大回环
        pattern.changeSpin2Circle();
        if (debug)
            pattern.trans.draw("4_ChangeSpin2Circle");
        // 合并同input边, 不用考虑 . 和 ^[
        pattern.uniteMultipleOutputStates();
        if (debug)
            pattern.trans.draw("5_UniteMultipleOutputStates");
        pattern.deleteUnreachableState();
        if (debug)
            pattern.trans.draw("6_DeleteUnreachableState");
        return pattern;
    }

    private void changeSpin2Circle() {
        Map<State, Set<Side>> spins = new HashMap<>();
        for (State leftS : trans.keySet()) {
            for (Expression input : trans.get(leftS).keySet()) {
                for (State rightS : trans.get(leftS).get(input)) {
                    if (leftS != rightS)
                        // 如果包含和输入相同的自旋
                        if (trans.get(rightS, input) != null && trans.get(rightS, input).contains(rightS)) {
                            // 记录在案
                            spins.putIfAbsent(rightS, new HashSet<>(List.of(new Side(leftS, input))));
                            spins.get(rightS).add(new Side(leftS, input));
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
                for (Side side : spins.get(rightState)) {
                    State leftState = side.getState();
                    Expression input = side.getInput();
                    trans.delete(blueprint, input, rightState);
                }
                for (Side side : spins.get(rightState)) {
                    State leftState = side.getState();
                    Expression input = side.getInput();
                    // 复制每个回旋的状态
                    State nn = new State(stateIndex++, blueprint);
                    trans.add(nn, trans.get(blueprint));
                    // 创建 自旋
                    trans.add(nn, input, nn);
                    newStates.put(input, nn);
                    // 老的输入指向新的
                    trans.delete(leftState, input, rightState);
                    trans.add(leftState, input, nn);
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
        Map<State, Integer> states = new HashMap<>();
        for (State leftS : trans.keySet()) {
            if (!states.containsKey(leftS)) states.put(leftS, 0);
            for (Expression input : trans.get(leftS).keySet()) {
                for (State rightS : trans.get(leftS).get(input)) {
                    states.put(rightS, states.getOrDefault(rightS, 0) + 1);
                }
            }
        }
        for (State state : states.keySet()) {
            if (states.get(state) == 0 && !state.isStart())
                trans.delete(state);
        }
    }

    private boolean isIntersect(String left, String right) {
        if (left.equals(right) || left.equals(Expression.dot) || right.equals(Expression.dot)) return true;
        if (left.charAt(0) == '^' && right.charAt(0) == '^') return false;
        if (left.charAt(0) == '^' && right.length() == 1 && right.charAt(0) == left.charAt(1)) return false;
        if (right.charAt(0) == '^' && left.length() == 1 && left.charAt(0) == right.charAt(1)) return false;
        System.out.println("error in compare for intersect:  " + left + "  " + right);
        return false;
    }

    private void deleteEmptyLine1(State leftState) {
        for (State rightState : trans.get(leftState, Expression.emptyInput)) {
            if (trans.get(rightState).containsKey(Expression.emptyInput))
                deleteEmptyLine1(rightState);
            trans.add(leftState, trans.get(rightState));
            if (rightState.isStart()) leftState.setStartTrue();
            if (rightState.isEnd()) leftState.setEndTrue();
            rightState.getGroupIndex().forEach(leftState::addGroupIndex);
        }
        trans.delete(leftState, Expression.emptyInput);
    }

    // 若只有一个空边input就两个合成一个, 若除了空边还有其它边, 就复制右边的到左边,其它不变.
    private void deleteEmptyLine() {
        boolean end;
        do {
            end = true;
            Set<State> transKeys = new HashSet<>(trans.keySet());
            for (State leftState : transKeys) {
                Map<Expression, Set<State>> inputToStates = trans.get(leftState);
                Set<Expression> inputs = new HashSet<>(inputToStates.keySet());
                // 如果有空边输入
                if (inputs.contains(Expression.emptyInput)) {
                    // todo:双向空边,就合成一个状态.
                    deleteEmptyLine1(leftState);
                    end = false;

//                    HashSet<State> resolved = new HashSet<>();
//                    if (inputs.size() > 1) {
//                        // 把每一个 input 和 回旋相等的都处理完再处理别的.
//                        // 这是单向空边, 只复制一下就行.
//                        for (State rightState : trans.get(leftState, Expression.emptyInput)) {
//                            for (String targetInput : trans.get(rightState).keySet()) {
//                                if (trans.get(rightState, targetInput).contains(rightState)) {
//                                    if (trans.get(leftState).keySet().contains(targetInput)) {
//                                        // 找到了 .* 结构
//                                        // 把除了自旋结构之外的都转移到 leftState
//                                        for (String eachInput : trans.get(rightState).keySet()) {
//                                            if (!eachInput.equals(targetInput)) {
//                                                trans.add(leftState, eachInput, new HashSet<>(trans.get(rightState, eachInput)));
//                                            }
//                                        }
//                                        resolved.add(rightState);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    String input = "_@";
//                    Set<State> replaceStates = new HashSet<>(inputToStates.get(input));
//                    for (State state : resolved) {
//                        replaceStates.remove(state);
//                    }
//
//                    // 把左右两个状态合成个新的状态
//                    // todo: 左右groupIndex 合成到一个state里的情况
//                    State newState = State.build(stateIndex++, leftState, replaceStates);
//
//                    // left : leftState
//                    // right : toState
//
//                    trans.delete(leftState, Expression.emptyInput);
//                    // 用 新状态替换所有老状态 在 转换表里
//
//                    replaceStates.add(leftState);
//
//                    for (State state11 : new HashSet<>(trans.keySet())) {
//                        if (replaceStates.contains(state11)) {
//                            trans.add(newState, trans.get(state11));
//                            trans.delete(state11);
//                        }
//                    }
//
//                    for (State state11 : trans.keySet()) {
//                        for (String input11 : trans.get(state11).keySet()) {
//                            Set<State> toSet = trans.get(state11, input11);
//                            for (State replaceState : replaceStates) {
//                                if (toSet.contains(replaceState)) {
//                                    toSet.removeAll(replaceStates);
//                                    toSet.add(newState);
//                                    break;
//                                }
//                            }
//                        }
//                    }
//
//                    end = false;
                }
            }
        } while (!end);
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

    private String getKeyOfStates(Set<State> states) {
        return Arrays.toString(states.stream().map(State::getIndex).sorted().toArray(Integer[]::new));
    }

    private void uniteMultipleOutputStates() {
        boolean end;
        Map<String, State> cache = new HashMap<>();
        do {
            end = true;
            Set<State> transKeys = new HashSet<>(trans.keySet());
            out:
            //nowState  nowInput  nowOutStates
            for (State nowState : transKeys) {
                Map<Expression, Set<State>> inputToStates = trans.get(nowState);
                Set<Expression> inputToStatesKeys = new HashSet<>(inputToStates.keySet());
                for (Expression nowInput : inputToStatesKeys) {
                    Set<State> nowOutStates = inputToStates.get(nowInput);
                    // 如果一个状态一个输入有多个输出
                    if (nowOutStates.size() > 1) {
                        // 看是不是有状态包含另外的状态, 是的话就合成到别的状态上.
                        // 暂时是 多个状态有相同input的自旋或内部指向时就复制到输出最少且只有这一个输入的状态上.
                        boolean needCreateNew = true;
                        Set<State> t = new HashSet<>();
                        Map<Expression, Integer> counts = new HashMap<>();
                        State simpleState = null;
                        int minOutputCount = Integer.MAX_VALUE;
                        // 统计所有状态的输入边数量
                        Map<State, Integer> inputsCount = trans.countInputsCount();
                        // 上面的值的偏移量, 有的状态的值会少或者多就在这里记录.
                        Map<State, Integer> inputsCountDiff = new HashMap<>();

                        for (State state : nowOutStates) {
                            if (needCreateNew)
                                if (trans.get(state) != null)
                                    for (Expression expression : trans.get(state).keySet()) {
                                        // 自旋
                                        boolean hasSpin = false;
                                        if (trans.get(state, expression).contains(state)) {
                                            counts.put(expression, counts.getOrDefault(expression, 0) + 1);
                                            inputsCountDiff.put(state, inputsCountDiff.getOrDefault(state, 0) + 1);
                                            hasSpin = true;
                                        }
                                        // 被自己人指向,  或者说有内部路径
                                        Set<State> intersection;
                                        if ((intersection = Util.isIntersect(trans.get(state, expression), nowOutStates)).size() > (hasSpin ? 1 : 0)) {
                                            counts.put(expression, counts.getOrDefault(expression, 0) + 1);
                                            for (State eachState : intersection) {
                                                inputsCountDiff.put(eachState, inputsCountDiff.getOrDefault(eachState, 0) + 1);
                                            }
                                        }
                                        if (counts.getOrDefault(expression, 0) >= 2) {
                                            // 有相同自旋
                                            needCreateNew = false;
                                        }
                                    }
//                            if (trans.get(state) != null)
//                                if (inputsCount.getOrDefault(state, 0) - inputsCountDiff.getOrDefault(state, 0) == 1 && trans.get(state).size() < minOutputCount) {
//                                    minOutputCount = trans.get(state).size();
//                                    simpleState = state;
//                                }
                        }
                        if (!needCreateNew) {
                            for (State state : nowOutStates) {
                                // 只有这一个输入才能当受体. 否则从其他边进入这个状态就会出错.
                                if (inputsCount.getOrDefault(state, 0) - inputsCountDiff.getOrDefault(state, 0) == 1) {
                                    simpleState = state;
                                    break;
                                }
                            }
                        }
                        if (!needCreateNew && simpleState != null) {
                            // nowInput 上 的其它状态的输出复制到simpleState上.
                            // todo: 状态分解, 有多个输入的状态分成多个状态,让每个状态只有一个输入.
                            for (State state : new HashSet<>(nowOutStates)) {
                                if (state != simpleState) {
                                    if (trans.get(state) != null) {
                                        trans.add(simpleState, trans.get(state),state);
                                    }
                                    simpleState.copy(state);
                                    trans.delete(nowState, nowInput, state);
                                }
                            }
                        } else {
                            // 合成个新的状态
                            State newState = null;
                            String oldStateKey = getKeyOfStates(nowOutStates);
                            // 检查是否有相同的集合转过了
                            if (cache.containsKey(oldStateKey))
                                newState = cache.get(oldStateKey);
                            // 从缓存里拿出来的状态是自身的话就再新建一个状态.
                            if (newState == null || newState == nowState) {
                                newState = State.build(stateIndex++, nowOutStates);
                                cache.put(oldStateKey, newState);
                            }
                            // 将这多个状态替换成新状态
                            HashSet<State> newStates = new HashSet<>();
                            newStates.add(newState);
                            trans.get(nowState).replace(nowInput, newStates);
//                        trans.delete(nowState, nowInput);
//                        trans.add(nowState, nowInput, newState);

                            // 复制旧状态上的输出到新状态上
                            for (State oldState : new HashSet<>(nowOutStates)) {
                                if (trans.get(oldState) != null)
                                    // 转移输出
                                    for (Expression in : trans.get(oldState).keySet()) {
                                        trans.add(newState, in, trans.get(oldState, in),oldState);
                                    }
                            }
                        }
                        // 删除 , when 联通双环结构,  then 删除其中一个
//                        for (State state : nowOutStates) {
//                            trans.delete(state);
//                        }
                        end = false;
                        if (drawUniteLine)
                            trans.draw("41_合并状态" + nowState.getIndex() + "-" + nowInput.toString() + "%" + (simpleState == null ? "1" : "2") + "--");

                        break out;

                    }
                }
            }
        } while (!end);
    }

    private int findMidBraceEnd(Expression e, int begin) {
        if (e.charAt(begin).equalsKeyword('[')) {
            Expression.Node now;
            while (!(now = e.charAt(++begin)).isEmpty) {
                if (now.equalsKeyword(']'))
                    return begin;
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

    private int findOrEnd(Expression e, int begin) {
        while (true) {
            begin = findNodeEnd(e, begin);
            Expression.Node next = e.charAt(begin + 1);
            if (next.isEmpty || !next.equalsKeyword('|'))
                return begin;
            else
                begin += 2;
        }
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
//        System.out.println("------ " + regex);
        // 初始化
        trans = new TransformTable(regex);
        int stateGroupIndex = 1;
        // 循环找能展开的正则分式
        boolean end;
        do {
            end = true;
            Set<State> transKeys = new HashSet<>(trans.keySet());
            for (State inState : transKeys) {
                Map<Expression, Set<State>> inputToStates = trans.get(inState);
                Set<Expression> inputToStatesKeys = new HashSet<>(inputToStates.keySet());
                input:
                for (Expression input : inputToStatesKeys) {
                    Set<State> toState = inputToStates.get(input);

                    boolean handled = false;
                    boolean and = false;
                    if (canSplit(input)) {
                        end = false;
                        State andState = inState;
                        for (int i = 0; i < input.contents.length; ) {
                            int nodeEnd = findNodeEnd(input, i);

                            // 整个都是括号
                            if (i == 0 && nodeEnd == input.length() - 1 && input.charAt(nodeEnd).equalsKeyword(')')) {
                                // 添加一个去掉括号的边
                                trans.add(inState, input.substring(1, input.length() - 1), new HashSet<>(trans.get(inState, input)));
                                inState.addGroupIndex(stateGroupIndex);
                                for (State state : trans.get(inState, input)) {
                                    state.addGroupIndex(stateGroupIndex);
                                }
                                stateGroupIndex++;
                                // 删除原来的边
                                trans.delete(inState, input);
                            }

                            Expression.Node next = input.charAt(nodeEnd + 1);
                            // 如果是 OR 结构
                            if (next.equalsKeyword('|')) {
                                // 把 OR 部分看成AND的子部分, 直接统一处理了
                                nodeEnd = findOrEnd(input, nodeEnd + 2);
                                // 如果整个式子都是 OR 结构
                                if (!and && nodeEnd == input.length() - 1) {
                                    // 处理 OR

                                    for (int j = 0; j < input.contents.length; ) {
                                        nodeEnd = findNodeEnd(input, j);
//                                        trans.add(inState, input.substring(j, nodeEnd + 1), new HashSet<>(trans.get(inState, input)));
                                        // 如果toState里有 endState且endState没有任何转换路径
                                        for (State state : trans.get(inState, input)) {
                                            if (state.isEnd() && (trans.get(state) == null || trans.get(state).size() == 0)) {
//                                                 把endState 换成一个新的
                                                trans.add(inState, input.substring(j, nodeEnd + 1), new State(stateIndex++, state));
                                            } else
//                                                 直接添加
                                                trans.add(inState, input.substring(j, nodeEnd + 1), state);
                                        }
                                        j = nodeEnd + 2;
                                    }
                                    // 删除原有的转换路径
                                    trans.delete(inState, input);
                                    break;
                                }
                            }
                            // 不是最后一位就是and结构
                            if (and || nodeEnd != input.length() - 1) {
                                // AND 结构
                                and = true;
                                // 如果是最后一位
                                if (nodeEnd == input.length() - 1) {
                                    trans.add(andState, input.substring(i, nodeEnd + 1), toState);
                                    // 删除原有的转换路径
                                    trans.delete(inState, input);
                                } else {
                                    State temp = State.build(stateIndex++);
                                    trans.add(andState, input.substring(i, nodeEnd + 1), temp);
                                    andState = temp;
                                }
                                i = nodeEnd + 1;
                            } else {
                                // 不是 or 也 不是 and

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
                                break;
                            }
                        }

                        // 输出中间步骤
                        trans.draw();
                    }
                }
//                trans.deleteDelayedNow();
            }
        } while (!end);
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

    private int getSuffixEndIndex(String input, int i) {
        if (input.charAt(i) == '{') {
            while (input.charAt(i) != '}') i++;
        }
        if (i + 1 <= input.length() - 1 && input.charAt(i + 1) == '?')
            i++;
        return i;
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
