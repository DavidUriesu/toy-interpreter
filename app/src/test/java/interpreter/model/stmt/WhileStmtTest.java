package interpreter.model.stmt;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.*;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WhileStmtTest {
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 3})
    void countdownRunsUntilTheCounterReachesZero(int start) throws MyException {
        var counter = new VarExp("v");
        var zero = new ValueExp(new IntValue(0));
        var one = new ValueExp(new IntValue(1));

        var condition = new RelationalExp(RelOp.GT, counter, zero);
        var decrement = new AssignStmt("v", new ArithExp(ArithOp.SUB, counter, one));
        var body = new CompStmt(new PrintStmt(counter), decrement);
        var state = PrgState.of(new WhileStmt(condition, body));
        state.getSymTable().put("v", new IntValue(start));

        var controller = new Controller(new Repository(state, "unused.log"), false);
        for (int step = 0; step < 20 && ! state.getExeStack().isEmpty(); step++) {
            controller.oneStep(state);
        }

        List<IntValue> expected = new ArrayList<>();
        for (int value = start; value > 0; value--) {
            expected.add(new IntValue(value));
        }

        assertTrue(state.getExeStack().isEmpty(), "The loop should terminate");
        assertEquals(expected, state.getOut().toList());
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void nonBooleanConditionThrows() {
        var condition = new ValueExp(new IntValue(1));
        var state = PrgState.of(new WhileStmt(condition, new NopStmt()));
        var controller = new Controller(new Repository(state, "unused.log"), false);

        assertThrows(StatementException.class, () -> controller.oneStep(state));
    }
}
