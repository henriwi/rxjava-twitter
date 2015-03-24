package com.henrikwingerei;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Util {

    public static List<Integer> createList(int start, int end) {
        return IntStream.range(start, end)
            .boxed()
            .collect(toList());
    }

}
