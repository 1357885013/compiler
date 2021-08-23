package stateMachine;

import java.util.HashSet;
import java.util.Set;

public class StateSet extends State {
    public Set<State> states = new HashSet<>();
    public Set<Expression> inputs = new HashSet<>();

    public static StateSet build(int index) {
        StateSet state = new StateSet();
        state.setIndex(index);
        return state;
    }
}
