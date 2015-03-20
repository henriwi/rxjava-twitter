package com.henrikwingerei.basic;

import rx.Observable;

import java.util.List;

public class BasicObservables {

    //Opprett av Observable ut fra listen
    //Skriv ut verdiene
    public void observableFromList(List<Integer> list) {
        Observable.from(list)
            .subscribe(System.out::println);
    }

    //Opprett av Observable ut fra listen
    //Filtrer ut alle partalls-verdier
    //Gang aller verdier med 2
    //Skriv ut verdiene
    public void filterMapObservable(List<Integer> list) {
        Observable.from(list)
            .filter(v -> v % 2 == 0)
            .map(v -> v * 2)
            .subscribe(System.out::println);
    }

    public void observableFromTwoLists(List<Integer> list1, List<Integer> list2) {
        //Opprett av Observable fra hver liste
        //Slå de to Observable sammen
        //Skriv ut summen

        Observable<Integer> o1 = Observable.from(list1);
        Observable<Integer> o2 = Observable.from(list2);

        Observable.zip(o1, o2, (v1, v2) -> v1 + v2)
            .subscribe(System.out::println);
    }

    public void observableFromCreate(List<Integer> list) {
        Observable.create((subscriber) -> {
            list.forEach(v -> subscriber.onNext(v));
            subscriber.onCompleted();
        })
            .subscribe(System.out::println);
    }
}
