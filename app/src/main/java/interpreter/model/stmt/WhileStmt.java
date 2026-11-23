package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.BoolValue;

public record WhileStmt(IExp cond, IStmt body) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var value = cond.eval(state.getSymTable(), state.getHeap());

        if (!(value instanceof BoolValue(boolean condition))) {
            throw new StatementException("while: condition must be boolean");
        }

        if (condition) {
            var stack = state.getExeStack();
            stack.push(this);
            stack.push(body);
        }

        return state;
    }

    @Override
    public String toString() {
        return "(while (" + cond + ") " + body + ")";
    }
}
