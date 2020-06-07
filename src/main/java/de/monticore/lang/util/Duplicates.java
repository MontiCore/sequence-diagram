package de.monticore.lang.util;

import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public final class Duplicates<T> implements UnaryOperator<List<T>> {

    @Override
    public List<T> apply(List<T> ts) {
        return ts.stream()
                .filter(e -> Collections.frequency(ts, e) > 1)
                .distinct()
                .collect(Collectors.toList());
    }
}
