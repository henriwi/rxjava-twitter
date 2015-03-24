package com.henrikwingerei.twitter;

import rx.Observable;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.io.IOException;

public class Twitter {

    public void fetchTweets() throws IOException {
        // Bruk metoden twitterObservabe() til å subscribe på twitter-streamen
        // Metoden CustomWebSocketServlet.broadcastMessage(v) kan brukes til å sende data til klienten

        twitterObservable()
            .subscribe((v) -> {
                System.out.println(v);
                CustomWebSocketServlet.broadcastMessage(v.getText());
            });
    }

    public Observable<Status> twitterObservable() {
        return Observable.create(subscriber -> {
            final TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
            twitterStream.addListener(new StatusAdapter() {
                public void onStatus(Status status) {
                    subscriber.onNext(status);
                }

                public void onException(Exception ex) {
                    subscriber.onError(ex);
                }
            });
            twitterStream.sample();
        });
    }
}
