package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.stmt.*;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    private static PrgState createState(IStmt program) {
        return new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );
    }

    @Test
    void repositoryReturnsProvidedState() {
        PrgState state = createState(new NopStmt());
        Repository repository = new Repository(state);

        assertSame(state, repository.getCrtPrg());
    }

    @Test
    void oneStepPopsAndExecutesStatement() throws MyException {
        PrgState state = createState(
                new VarDeclStmt("x", new IntType())
        );

        Controller controller = new Controller(
                new Repository(state), false
        );

        assertSame(state, controller.oneStep(state));

        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void oneStepOnEmptyStackThrows() throws MyException {
        PrgState state = createState(new NopStmt());
        state.getExeStack().pop();

        Controller controller = new Controller(
                new Repository(state), false
        );

        assertThrows(
                StatementException.class,
                () -> controller.oneStep(state)
        );
    }

    @Test
    void oneStepExpandsCompositionWithoutExecutingItsChildren()
            throws MyException {

        IStmt first = new VarDeclStmt("x", new IntType());
        IStmt second = new PrintStmt(new VarExp("x"));

        PrgState state = createState(new CompStmt(first, second));

        Controller controller = new Controller(
                new Repository(state), false
        );

        controller.oneStep(state);

        assertFalse(state.getSymTable().isDefined("x"));
        assertEquals(0, state.getOut().size());
        assertSame(first, state.getExeStack().pop());
        assertSame(second, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void allStepExecutesDeclarationAssignmentAndPrint()
            throws MyException {

        IStmt program = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt(
                                "v", new ValueExp(new IntValue(2))
                        ),
                        new PrintStmt(new VarExp("v"))
                )
        );

        PrgState state = createState(program);

        Controller controller = new Controller(
                new Repository(state), false
        );

        controller.allStep();

        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(2), state.getSymTable().lookup("v"));
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void executionErrorStopsBeforeFollowingStatement() {
        IStmt program = new CompStmt(
                new AssignStmt(
                        "missing", new ValueExp(new IntValue(10))
                ),
                new PrintStmt(new ValueExp(new IntValue(99)))
        );

        PrgState state = createState(program);

        Controller controller = new Controller(
                new Repository(state), false
        );

        assertThrows(
                StatementException.class,
                () -> controller.allStep()
        );

        assertEquals(0, state.getOut().size());
        assertFalse(state.getSymTable().isDefined("missing"));
        assertFalse(state.getExeStack().isEmpty());
    }
}