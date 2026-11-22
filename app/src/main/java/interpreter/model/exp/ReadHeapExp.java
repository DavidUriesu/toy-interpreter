package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

public record ReadHeapExp(IExp exp) implements IExp {
    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException {
        IValue value = exp.eval(symTable, heap);

        if (!(value instanceof RefValue ref)) {
            throw new ExpressionException("rH: " + exp + " is not a reference");
        }

        int address = ref.address();
        if (!heap.isDefined(address)) {
            throw new ExpressionException("rH: address " + address + " is not in the heap");
        }

        return heap.get(address);
    }

    @Override
    public String toString() {
        return "rH(" + exp + ")";
    }
}
