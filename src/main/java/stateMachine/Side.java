package stateMachine;

import java.util.Objects;

public class Side {
    private Expression input;
    private State state;

    public Side(State state,Expression input ) {
        this.input = input;
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Side)) return false;
        Side side = (Side) o;
        return Objects.equals(getInput(), side.getInput()) && Objects.equals(getState(), side.getState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInput(), getState());
    }

    public Expression getInput() {
        return input;
    }

    public void setInput(Expression input) {
        this.input = input;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
