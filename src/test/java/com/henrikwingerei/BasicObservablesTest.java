package com.henrikwingerei;

import com.henrikwingerei.basic.BasicObservables;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class BasicObservablesTest {

    private final BasicObservables basicObservables = new BasicObservables();

    @Test
    public void testObservableFromList() {
        basicObservables.observableFromList(createList(1, 50));
    }

    @Test
    public void filterMapObservableTest() {
        basicObservables.filterMapObservable(createList(1, 50));
    }

    @Test
    public void observableFromTwoLists() {
        basicObservables.observableFromTwoLists(createList(1, 10), createList(1, 10));
    }

    @Test
    public void observableFromCreate() {
        basicObservables.observableFromCreate(createList(1, 10));
    }

    private static List<Integer> createList(int start, int end) {
        return IntStream.range(start, end).boxed().collect(toList());
    }

}
