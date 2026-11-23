package interpreter.controller;

import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

import java.util.*;
import java.util.stream.Collectors;

public final class GarbageCollector {
    private GarbageCollector() {
    }

    public static Set<Integer> getAddrFromSymTable(Collection<IValue> values) {
        return values.stream()
                .filter(value -> value instanceof RefValue)
                .map(value -> ((RefValue) value).address())
                .collect(Collectors.toSet());
    }

    public static Map<Integer, IValue> unsafeGarbageCollector(
            Set<Integer> roots, Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(entry -> roots.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Set<Integer> reachableAddresses(
            Set<Integer> roots, Map<Integer, IValue> heap) {
        Set<Integer> reachable = new HashSet<>();
        var pending = new ArrayDeque<>(roots);

        while (!pending.isEmpty()) {
            int address = pending.removeFirst();
            IValue value = heap.get(address);

            if (value == null || !reachable.add(address)) {
                continue;
            }

            if (value instanceof RefValue ref) {
                pending.addLast(ref.address());
            }
        }

        return reachable;
    }

    public static Map<Integer, IValue> safeGarbageCollector(
            Set<Integer> roots, Map<Integer, IValue> heap) {
        var reachable = reachableAddresses(roots, heap);

        return heap.entrySet().stream()
                .filter(entry -> reachable.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
