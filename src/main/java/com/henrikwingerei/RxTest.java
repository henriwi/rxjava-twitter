package main.java.com.henrikwingerei;


import org.junit.Before;
import org.junit.Test;
import rx.Observable;

import java.util.Arrays;
import java.util.List;

public class RxTest {

    private static List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    @Before
    public void setUp() {
  /*      Observable observable = getObservable();
        observable.subscribe(
                v -> System.out.println(v),
                v -> System.out.println("ERROR: " + v),
                () -> System.out.println("COMPLETE")
        );*/
    }

    @Test
    public void test1() {
        System.out.println("**** START TEST 1 ****");
        Observable.from(list)
                .filter(v -> v < 5)
                .subscribe(System.out::println);
        System.out.println("**** END TEST 1 ****");
    }

    @Test
    public void test2() {
        System.out.println("**** START TEST 2 ****");
        Observable<Integer> observable = Observable.from(list);

        observable
                .filter(v -> v % 2 == 0)
                .map(v -> Double.valueOf(v))
                .subscribe(System.out::println);
        System.out.println("**** END TEST 2 ****");
    }

    @Test
    public void test3() {
        Observable<Integer> observable = Observable.create(s -> {
            try {
                s.onNext(1);
                s.onCompleted();
            } catch (Exception e) {
                s.onError(e);
            }
        });

        observable
                .map(v -> v * 2)
                .subscribe(System.out::println);

    }

    private Observable<Integer> getObservable() {
        return Observable.create(subscriber ->
        {
            list.forEach(subscriber::onNext);
            subscriber.onCompleted();
        });
    }

}
