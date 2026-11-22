package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.RefValue;

public record NewStmt(String varName, IExp exp) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();

        if (!symTable.isDefined(varName)) {
            throw new StatementException("new: " + varName + " is not defined");
        }
        if (!(symTable.lookup(varName) instanceof RefValue ref)) {
            throw new StatementException("new: " + varName + " is not a reference");
        }

        var heap = state.getHeap();
        var value = exp.eval(symTable, heap);

        if (!value.getType().equals(ref.locationType())) {
            throw new StatementException("new: expected " + ref.locationType() + ", got " + value.getType());
        }

        int address = heap.allocate(value);
        symTable.update(varName, new RefValue(address, ref.locationType()));
        return state;
    }

    @Override
    public String toString() {
        return "new(" + varName + ", " + exp + ")";
    }
}
