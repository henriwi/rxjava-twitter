package com.henrikwingerei.twitter;

import org.junit.Test;
import rx.Observable;
import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

public class Twitter {

    private static Configuration configuration;

    public Twitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("tzFgONp5UNJyQkMvOh7M2wahP")
            .setOAuthConsumerSecret("Pm8QOLfupQVzfoUE2TKv3FBawsL9sN3JwpM1cF7H2Mf7y11mKp")
            .setOAuthAccessToken("117538049-F33zFLtyZH1oejGf3gAYjzKb8GZ3L8AOX5UfHzh1")
            .setOAuthAccessTokenSecret("z4RfwQI4fueZg7HMyzIxDbFgFII5KPNBL2y5sJE8DxZNe");
        configuration = cb.build();
    }

    public void fetchTweets() {
        twitterObservable("usa")
            .map(Status::getText)
            .subscribe((v) -> {
                System.out.println(v);
                CustomWebSocketServlet.broadcastMessage(v);
            });
    }

    /**
     * Test ut metodene sample, window
     */
    @Test
    public void sampleTweets() throws InterruptedException {
//        twitterObservable("usa")
//            .flatMap(status -> Observable.from(asList(status.getHashtagEntities())))
//            .window(180, 3, TimeUnit.SECONDS)
//            .subscribe(
//                window -> window
//                    .groupBy(tag -> tag.getText())
//                    .toMap(tag -> tag.getKey())
//                    .toSortedList()
//                    .take(10)
//                    .subscribe((group) -> {
//                        //Map<String, Integer> map = new HashMap<>();
//                        //map.putIfAbsent((String) group.getKey(), 1);
//                        //map.put((String) group.getKey(), map.get(group.getKey()) + 1);
//                        group.forEach((s) -> {
//                            System.out.println(s);
//                        });
//                    }));

        twitterObservable("usa")
            .flatMap(status -> Observable.from(asList(status.getHashtagEntities())))
            .buffer(4, TimeUnit.SECONDS)
            .subscribe(list -> list
                .forEach((tag) -> {
                        Map<String, Integer> map = new HashMap<>();
                        map.putIfAbsent((String) tag.getText(), 1);
                        map.put((String) tag.getText(), map.get(tag.getText()) + 1);
                        map.forEach((k, v) -> System.out.println(k + ": " + v));
                    }
                ));

        Thread.sleep(10000);
    }

    @Test
    public void tweets2() throws InterruptedException {
        Observable<Status> twitter1 = twitterObservable("twitter").onBackpressureBuffer();
        Observable<Status> twitter2 = twitterObservable("usa").onBackpressureBuffer();

        Observable<String> zipped = Observable.combineLatest(twitter1, twitter2, (v1, v2) -> {
            return "v1: " + v1.getId() + ", v2: " + v2.getId();
        });


        zipped
            .sample(1, TimeUnit.SECONDS)
            .subscribe(s -> System.out.println(s),
                ex -> ex.printStackTrace());

        Thread.sleep(10000);
    }

    public static Observable<Status> twitterObservable(String filter) {
        return Observable.create(subscriber -> {
            final TwitterStream twitterStream = new TwitterStreamFactory(configuration).getInstance();
            twitterStream.addListener(new StatusAdapter() {
                public void onStatus(Status status) {
                    subscriber.onNext(status);
                }

                public void onException(Exception ex) {
                    subscriber.onError(ex);
                }
            });
            twitterStream.filter(new FilterQuery().track(new String[]{filter}));
        });
    }
}
