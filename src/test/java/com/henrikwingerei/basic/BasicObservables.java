package com.henrikwingerei.basic;

import org.junit.Test;
import rx.Observable;

import java.util.List;

import static com.henrikwingerei.Util.createList;

public class BasicObservables {

    //Opprett av Observable ut fra listen
    //Skriv ut verdiene
    @Test
    public void observableFromList() {
        Observable.from(createList(1, 10))
            .subscribe(System.out::println);
    }

    //Opprett av Observable ut fra listen
    //Filtrer ut alle partalls-verdier
    //Gang aller verdier med 2
    //Skriv ut verdiene
    @Test
    public void filterMapObservable() {
        Observable.from(createList(1, 10))
            .filter(v -> v % 2 == 0)
            .map(v -> v * 2)
            .subscribe(System.out::println);
    }

    //Opprett av Observable fra hver liste
    //Slå de to Observable sammen
    //Skriv ut summen
    @Test
    public void observableFromTwoLists() {
        Observable<Integer> o1 = Observable.from(createList(1, 5));
        Observable<Integer> o2 = Observable.from(createList(6, 10));

        Observable.zip(o1, o2, (v1, v2) -> v1 + v2)
            .subscribe(System.out::println);
    }

    // Opprett Observable vha metoden create
    @Test
    public void observableFromCreate() {
        List<Integer> list = createList(1, 10);
        Observable.create((subscriber) -> {
            list.forEach(subscriber::onNext);
            subscriber.onCompleted();
        })
            .subscribe(System.out::println);
    }
}
