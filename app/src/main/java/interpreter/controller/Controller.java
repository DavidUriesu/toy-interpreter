package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.repository.IRepository;

public class Controller {
    private final IRepository repo;
    private final boolean displayFlag;

    public Controller(IRepository repo, boolean displayFlag) {
        this.repo = repo;
        this.displayFlag = displayFlag;
    }

    public PrgState oneStep(PrgState state) throws MyException {
        var stack = state.getExeStack();

        if (stack.isEmpty()) {
           throw new StatementException("Execution stack is empty");
        }

        IStmt statement = stack.pop();
        return statement.execute(state);
    }

    public void allStep() throws MyException {
        PrgState state = repo.getCrtPrg();
        display(state);

        while (!state.getExeStack().isEmpty()) {
            oneStep(state);
            display(state);
        }
    }

    private void display(PrgState state) {
        if (displayFlag) {
            System.out.println(state);
            System.out.println();
        }
    }
}
