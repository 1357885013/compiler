package stateMachine;

import stateMachine.utils.CmdUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TransformTable {
    public Map<State, LinkedHashMap<Expression, Set<State>>> trans = new HashMap<>();
    private Map<State, Integer> inputsCount;
    private State startState;
    // 删掉的边不允许再加上了。
    private Set<String> exceptEdge = new HashSet<>();

    public TransformTable(Expression regex) {
        startState = State.start();
        trans.put(startState, new LinkedHashMap<>());
        trans.put(State.end(), new LinkedHashMap<>());
        add(State.start(), regex, State.end());
    }

    public Map<Expression, Set<State>> add(State inState) {
        return trans.computeIfAbsent(inState, v -> new LinkedHashMap<>());
    }

    public Set<State> set(State inState, Expression input, Set<State> toStates) {
        Map<Expression, Set<State>> inputs = trans.computeIfAbsent(inState, v -> new LinkedHashMap<>());
        return inputs.put((input), toStates);
    }

    public Map<State, Integer> countInputsCount() {
        inputsCount = new HashMap<>();
        for (State inState : trans.keySet()) {
            for (Expression input : trans.get(inState).keySet()) {
                for (State state : trans.get(inState).get(input)) {
                    inputsCount.put(state, inputsCount.getOrDefault(state, 0) + 1);
                }
            }
        }
        return inputsCount;
    }

    public Set<State> add(State destState, Expression input, Set<State> object) {
        return add(destState, input, object, null);
    }

    /**
     * 添加 , 同时会把旧状态自旋转换成新状态自旋.
     *
     * @param destState 受
     * @param input     转移项
     * @param object    目标
     * @param srcState
     * @return
     */
    public Set<State> add(State destState, Expression input, Set<State> object, State srcState) {
        Map<Expression, Set<State>> inputs = trans.computeIfAbsent(destState, v -> new LinkedHashMap<>());
        if (object != null) {
            Set<State> states = inputs.computeIfAbsent((input), v -> new HashSet<>());
            states.addAll(object);
            if (srcState != null && object.contains(srcState)) {
                states.remove(srcState);
                states.add(destState);
            }
        }
        // 旧状态自旋转成新状态自旋.
        return inputs.get(input);
    }


    public boolean add(State inState, Expression input, State toState) {
        Map<Expression, Set<State>> inputs = trans.computeIfAbsent(inState, v -> new LinkedHashMap<>());
        Set<State> toStates = inputs.computeIfAbsent((input), v -> new HashSet<>());
        if (toStates.contains(toState))
            return false;
        else
            toStates.add(toState);
        return true;
    }

    public Set<State> keySet() {
        return trans.keySet();
    }

    public LinkedHashMap<Expression, Set<State>> get(State inState) {
        return trans.get(inState);
    }

    public Set<State> get(State inState, Expression input) {
        if (!trans.containsKey(inState)) return null;
        return trans.get(inState).get(input);
    }

    public Set<State> delete(State inState, Expression input) {
        if (!trans.containsKey(inState)) return null;
        addExceptEdge(inState, input);
        return trans.get(inState).remove(input);
    }

    public LinkedHashMap<Expression, Set<State>> delete(State inState) {
        return trans.remove(inState);
    }

    public boolean addExceptEdge(State s, Expression input) {
        return exceptEdge.add(s.getIndex() + "_" + input);
    }

    public boolean canAddEdge(State s, Expression input) {
        return !exceptEdge.contains(s.getIndex() + "_" + input);
    }

    public boolean delete(State inState, Expression input, State toState) {
        if (!trans.containsKey(inState)) return false;
        addExceptEdge(inState, input);
        Set<State> toStates = trans.get(inState).get(input);
        if (toStates == null) return false;
        return toStates.remove(toState);
    }

    public State getStartState() {
        // 寻找新的start state
        if (!trans.containsKey(startState) || trans.get(startState).size() == 0) {
            State[] starts = trans.keySet().stream().filter(State::isStart).toArray(State[]::new);
            if (starts.length > 1)
                System.out.println("找到不止一个 start state");
            else if (starts.length == 0)
                System.out.println("cant find any start state");
            else startState = starts[0];
        }
        return startState;
    }

    public void setStartState(State startState) {
        this.startState = startState;
    }

    public void add(State state, LinkedHashMap<Expression, Set<State>> outputs) {
        add(state, outputs, null);
    }

    /**
     * 添加 , 同时会把旧状态自旋转换成新状态自旋.
     *
     * @param state   目标状态
     * @param outputs 要转移的
     * @param oState  原状态, 主要用来判断自旋.
     */
    public void add(State state, LinkedHashMap<Expression, Set<State>> outputs, State oState) {
        LinkedHashMap<Expression, Set<State>> inputs = trans.getOrDefault(state, new LinkedHashMap<>());
        for (Expression input : outputs.keySet()) {
            if (canAddEdge(state, input)) {
                if (inputs.containsKey(input) && outputs.get(input) != null) {
                    inputs.get(input).addAll(outputs.get(input));

                } else {
                    inputs.put(input, new HashSet<>(outputs.get(input)));
                }
                // 旧状态自旋转成新状态自旋.
                if (oState != null && outputs.get(input).contains(oState)) {
                    inputs.get(input).remove(oState);
                    inputs.get(input).add(state);
                }
            }
        }
        trans.put(state, inputs);
    }

    private static int fileIndex = 1;
    private static File directory = new File("ere/test");

    static {
        // 先删除所有文件
        Arrays.stream(Objects.requireNonNull(directory.listFiles())).forEach(File::delete);
    }

    public void draw() {
        draw("1_Generate");
    }

    public void draw(String name) {
        directory = new File("ere/test");

        String fileName = "ere/test/" + fileIndex++ + "_" + name + ".dot";
        File f = new File(fileName);
        try {
            if (!f.createNewFile()) {
//                System.out.println("文件已存在  " + f.getAbsolutePath());
            }

            try (OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(f))) {
                output.write("digraph{\n" +
                        "rankdir=\"LR\";\n" +
                        "fontname = \"Microsoft YaHei\";\n" +
                        "node [shape = circle, fontname = \"Microsoft YaHei\"];\n" +
                        "edge [fontname = \"Microsoft YaHei\"];\n");
                Set<Integer> starts = new HashSet<>();
                Set<Integer> ends = new HashSet<>();

                for (State leftS : trans.keySet()) {
                    if (leftS.isStart()) starts.add(leftS.getIndex());
                    if (leftS.isEnd()) ends.add(leftS.getIndex());
                    for (Expression input : trans.get(leftS).keySet()) {
                        for (State rightS : trans.get(leftS).get(input)) {
                            if (rightS.isStart()) starts.add(rightS.getIndex());
                            if (rightS.isEnd()) ends.add(rightS.getIndex());
                            String content = input.toString();
                            if (content.equals("\\")) content = "\\\\";
                            output.append(String.valueOf(leftS.getIndex())).append(" -> ").append(String.valueOf(rightS.getIndex())).append("[ label = \"").append(content).append("\" ];\n");
                        }
                    }
                }
                for (Integer start : starts) {
                    output.append(String.valueOf(start)).append(" [ style = bold ];\n");
                }
                for (Integer end : ends) {
                    output.append(String.valueOf(end)).append(" [ shape = doublecircle ];\n");
                }
                output.write("}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        CmdUtil.run("dot -T png -O " + f.getAbsolutePath());
        CmdUtil.run("dot -T png -O " + fileName);
        // 删除dot 文件.
//            Arrays.stream(Objects.requireNonNull(directory.listFiles())).filter(ff -> ff.getName().endsWith("dot")).forEach(File::delete);
    }

}
