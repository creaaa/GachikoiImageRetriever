package com.example.masa.twitterimageretriever;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class MyIntentService extends IntentService {

    final static String TAG = "ServiceTest";

    private Twitter twitter;


    @Override
    public void onCreate() {
        super.onCreate();
        //
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(getString(R.string.twitter_consumer_key));
        configurationBuilder.setOAuthConsumerSecret(getString(R.string.twitter_consumer_secret));
        configurationBuilder.setOAuthAccessToken(getString(R.string.twitter_access_token));
        configurationBuilder.setOAuthAccessTokenSecret(getString(R.string.twitter_access_token_secret));

        // Twitterオブジェクトの初期化
        this.twitter = new TwitterFactory(configurationBuilder.build()).getInstance();
    }

    public MyIntentService(String name) {
        super(name);
        Log.i(TAG, "constructor");
    }

    public MyIntentService() {
        super(TAG);
        System.out.println("ちんちくりん");
    }


    @Override protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "onHandleIntent");

        // 検索の実行
        QueryResult result = null;

        try {
            // 検索文字列を設定する
            Query query = new Query("ゆっふぃー -rt");
            query.setCount(100);  // 最大20tweetにする（デフォルトは15)

            result = this.twitter.search(query);

            String s = result.getTweets().get(0).getText();
            Log.d(TAG, s);



        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}