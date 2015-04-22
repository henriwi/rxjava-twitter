package com.henrikwingerei.twitter;

import com.google.gson.Gson;
import rx.Observable;
import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

public class Twitter {

    public void fetchTweets() throws IOException {
        twitterObservable()
            .filter(s -> s.getGeoLocation() != null)
            .sample(1, TimeUnit.SECONDS)
            .subscribe((s) -> {
                String json = new Gson().toJson(s);
                CustomWebSocketServlet.broadcastMessage(json);
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
