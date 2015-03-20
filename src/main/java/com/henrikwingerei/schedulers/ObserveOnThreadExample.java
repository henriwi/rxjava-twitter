package com.henrikwingerei.schedulers;

import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.List;

import static java.util.Arrays.asList;

public class ObserveOnThreadExample {

    public static void main(String[] args) throws InterruptedException {

        // Create and sync on an object that we will use to make sure we don't
        // hit the System.exit(0) call before our threads have had a chance
        // to complete.
        Object waitMonitor = new Object();
        synchronized (waitMonitor) {

            System.out.println("---------------------------------------------------------------------------------------");
            System.out.println("Creating an Observable that does not specify a subscribeOn or an observeOn Scheduler");
            System.out.println("driving thread: " + Thread.currentThread().getName());
            System.out.println("---------------------------------------------------------------------------------------");

            Observable<Integer> observable = Observable.from(asList(1, 2, 3, 4, 5));

            // Dot chain on the observable...
            observable
                // tell the observable to use the io scheduler...I use it instead
                // of the computation scheduler in case you run this example on a
                // single core system...I know the io scheduler will generate a more
                // interesting example.
                .observeOn(Schedulers.io())
                .subscribe(
                    // onNext
                    (i) -> {
                        System.out.println("onNext thread entr: " + Thread.currentThread().getName());
                        System.out.println(i);
                        System.out.println("onNext thread exit: " + Thread.currentThread().getName());
                    },
                    // onError
                    (t) -> {
                        t.printStackTrace();
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
