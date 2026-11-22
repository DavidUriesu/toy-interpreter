package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.RefValue;

public record WriteHeapStmt(String varName, IExp exp) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        var heap = state.getHeap();

        if (!symTable.isDefined(varName)) {
            throw new StatementException("wH: " + varName + " is not declared");
        }
        if (!(symTable.lookup(varName) instanceof RefValue ref)) {
            throw new StatementException("wH: " + varName + " is not a ref");
        }

        int address = ref.address();
        if (!heap.isDefined(address)) {
            throw new StatementException("wH: address " + address + " is not in the heap");
        }

        var value = exp.eval(symTable, heap);
        if (!value.getType().equals(ref.locationType())) {
            throw new StatementException("wH: expected " + ref.locationType() + ", got " + value.getType());
        }

        heap.update(address, value);
        return state;
    }

    @Override
    public String toString() {
        return "wH(" + varName + ", " + exp + ")";
    }
}
