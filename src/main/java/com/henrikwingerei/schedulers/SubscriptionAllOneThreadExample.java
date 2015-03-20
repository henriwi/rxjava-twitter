package com.henrikwingerei.schedulers;

import rx.Observable;

import java.util.List;

import static java.util.Arrays.asList;

public class SubscriptionAllOneThreadExample {

    public static void main(String[] args) {

        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("Creating an Observable that does not specify a subscribeOn or an observeOn Scheduler");
        System.out.println("driving thread: " + Thread.currentThread().getName());
        System.out.println("---------------------------------------------------------------------------------------");

        // Get the list of integers that I seem to like best...
        List<Integer> intList = asList(1, 2, 3, 4, 5);

        // Wrap the list in an Observable
        Observable<Integer> observable = Observable.from(intList);

        // Subscribe...
        observable.subscribe(
            // onNext function
            (i) -> {
                // Println the name of the current thread on entry and exit so that we
                // can see a few interesting pieces of information...
                System.out.println("onNext thread entr: " + Thread.currentThread().getName());
                System.out.println(i);
                System.out.println("onNext thread exit: " + Thread.currentThread().getName());
            },
            // onError function
            (t) -> {
                t.printStackTrace(); // Should we get an error...just do the simplest thing.
            },
            // onCompleted function
            () -> {
                System.out.println("onCompleted()");
            }
        );

        System.exit(0);
    }
}
