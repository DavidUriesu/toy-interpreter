package interpreter.controller;

import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IntValue;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GarbageCollectorTest {
    @Test
    void extractsAddressesFromReferenceValues() {
        var intType = new IntType();
        List<IValue> values = List.of(
                new IntValue(99),
                new RefValue(2, new RefType(intType)),
                new RefValue(3, intType),
                new RefValue(3, intType)
        );

        assertEquals(Set.of(2, 3), GarbageCollector.getAddrFromSymTable(values));
    }

    @Test
    void safeCollectorKeepsIndirectlyReachableCells() {
        Map<Integer, IValue> heap = Map.of(
                1, new IntValue(20),
                2, new RefValue(1, new IntType()),
                3, new IntValue(30),
                4, new IntValue(99)
        );

        var kept = GarbageCollector.safeGarbageCollector(Set.of(2, 3), heap);

        assertEquals(Set.of(1, 2, 3), kept.keySet());
    }
}