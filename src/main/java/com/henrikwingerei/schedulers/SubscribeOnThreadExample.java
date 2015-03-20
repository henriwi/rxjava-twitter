package com.henrikwingerei.schedulers;

import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.List;

import static java.util.Arrays.asList;

public class SubscribeOnThreadExample {

    public static void main(String[] args) throws InterruptedException {

        // Create and sync on an object that we will use to make sure we don't
        // hit the System.exit(0) call before our threads have had a chance
        // to complete.
        Object waitMonitor = new Object();
        synchronized (waitMonitor) {

            System.out.println("---------------------------------------------------------------------------------------");
            System.out.println("Creating an Observable that specifies an subscribeOn, but not an observeOn Scheduler");
            System.out.println("driving thread: " + Thread.currentThread().getName());
            System.out.println("---------------------------------------------------------------------------------------");

            // ...and wrap it in an Observable
            Observable<Integer> observable = Observable.from(asList(1, 2, 3, 4, 5));

            // dot chain call on the observable list to...
            observable
                // make sure that the subsciber driver code executes on a new thread...
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                    // onNext function
                    (i) -> {
                        System.out.println("onNext thread entr: " + Thread.currentThread().getName());
                        System.out.println(i);
                        System.out.println("onNext thread exit: " + Thread.currentThread().getName());
                    },
                    // onError function
                    (t) -> {
                        t.printStackTrace(); // Just do the simplest thing for the sake of example.
                    },
                    // onCompleted
                    () -> {
                        System.out.println("onCompleted()");

                        // Since we have completed...we sync on the waitMonitor
                        // and then call notify to wake up the "main" thread.
                        synchronized (waitMonitor) {
                            waitMonitor.notify();
                        }
                    }
                );

            // Wait until the onCompleted method wakes us up.
            waitMonitor.wait();
        }

        System.exit(0);
    }
}
