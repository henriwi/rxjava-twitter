package com.henrikwingerei.subject;

import org.junit.Test;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

import static com.henrikwingerei.Util.createList;

public class Subjects {


    @Test
    public void asyncSubject() {
        //Opprett et AsyncSubject som subcriber og skriver ut alle verdiene
        //Async Subject returnerer kun det siste eventet som Observable emitter (etter onCompleted)
        AsyncSubject<Integer> subject = AsyncSubject.create();

        subject.subscribe(v -> System.out.println(v));

        Observable.from(createList(1, 10))
            .subscribe(subject::onNext, subject::onError, subject::onCompleted);
    }

    @Test
    public void behaviorSubject() {
        //BehaviorSubject gjør det mulig å multicaste eventer til flere subscribers
        BehaviorSubject<Integer> subject = BehaviorSubject.create();

        subject.subscribe(v -> System.out.println("Subscriber 1: " + v));
        subject.subscribe(v -> System.out.println("Subscriber 2: " + v));

        Observable.from(createList(1, 10))
            .subscribe(subject::onNext, subject::onError, subject::onCompleted);
    }

    @Test
    public void publishSubject() throws InterruptedException {
        PublishSubject<Integer> subject = PublishSubject.create();

        subject.subscribe(v -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Subscriber 1: " + v);
        });

        Observable.from(createList(1, 10))
            .subscribeOn(Schedulers.computation())
            .subscribe(subject::onNext, subject::onError, subject::onCompleted);

        System.out.println("Subscribes 2");

        subject.subscribe(v -> System.out.println("Subscriber 2: " + v));

        Thread.sleep(10000);
    }
}
