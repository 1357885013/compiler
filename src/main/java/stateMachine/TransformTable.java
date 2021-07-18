package stateMachine;

import stateMachine.utils.CmdUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

public class TransformTable {
    public Map<State, LinkedHashMap<Expression, Set<State>>> trans = new HashMap<>();
    private State startState;

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

    public Set<State> add(State inState, Expression input, Set<State> toStates) {
        Map<Expression, Set<State>> inputs = trans.computeIfAbsent(inState, v -> new LinkedHashMap<>());
        inputs.computeIfAbsent((input), v -> new HashSet<>()).addAll(toStates);
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
        return trans.get(inState).remove(input);
    }

    public LinkedHashMap<Expression, Set<State>> delete(State inState) {
        return trans.remove(inState);
    }

    public boolean delete(State inState, Expression input, State toState) {
        if (!trans.containsKey(inState)) return false;
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

    public void add(State state, LinkedHashMap<Expression, Set<State>> inputToStates) {
        Map<Expression, Set<State>> inputs = trans.get(state);
        if (inputs == null)
            trans.put(state, inputToStates);
        else {
            for (Expression input : inputToStates.keySet()) {
                if (inputs.containsKey(input)) {
                    inputs.get(input).addAll(inputToStates.get(input));
                } else {
                    inputs.put(input, new HashSet<>(inputToStates.get(input)));
                }
            }
        }
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

        String fileName = "ere/test/" + name + fileIndex++ + ".dot";
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
                            output.append(String.valueOf(leftS.getIndex())).append(" -> ").append(String.valueOf(rightS.getIndex())).append("[ label = \"").append(input.toString()).append("\" ];\n");
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
        CmdUtil.run("dot -T png -O " + fileName);
        // 删除dot 文件.
        Arrays.stream(Objects.requireNonNull(directory.listFiles())).filter(ff -> ff.getName().endsWith("dot")).forEach(File::delete);
    }

}
