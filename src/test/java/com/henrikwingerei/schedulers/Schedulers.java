package com.henrikwingerei.schedulers;

import org.junit.Test;
import rx.Observable;

import static java.lang.Thread.currentThread;

public class Schedulers {

    // Eksmpel på scheduler
    @Test
    public void scheduler() throws InterruptedException {
        Observable<Integer> observable = Observable.create(s -> {
            try {
                System.out.println("Subscribing on: " + currentThread().getName());
                s.onNext(1);
                s.onCompleted();
            } catch (Exception e) {
                s.onError(e);
            }
        });

        observable
            .observeOn(rx.schedulers.Schedulers.newThread())
            .subscribeOn(rx.schedulers.Schedulers.newThread())
            .subscribe(v -> System.out.println("Observing on: " + currentThread().getName()));

        Thread.sleep(1000);
    }

}
