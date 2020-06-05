package de.monticore.lang.sd.util;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Duplicates {
    public static List<String> inStringList(List<String> stringList) {
        return stringList.stream()
                .filter(e -> Collections.frequency(stringList, e) > 1)
                .distinct()
                .collect(Collectors.toList());
    }
}
