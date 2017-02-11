
package com.example.masa.twitterimageretriever;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class MyAsyncTask extends AsyncTask<Void, String, List<Status>> {

    private MainActivity activity;
    private Twitter twitter;


    public MyAsyncTask(Context context, Twitter twitter) {
        super();
        activity = (MainActivity) context;
        this.twitter = twitter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Pre Async!!");
    }

    @Override
    protected void onPostExecute(List<twitter4j.Status> s) {

        System.out.println("async おわったよ");

        ArrayAdapter<String> imageURLs;
        ArrayList<String> me = new ArrayList<String>();

        for (twitter4j.Status tweet: s) {
            //ツイート本文に画像URLが含まれていたら取り出す
            MediaEntity[] mentitys = tweet.getMediaEntities();
            for(MediaEntity m: mentitys){
                me.add(m.getMediaURL());
            }
        }

        System.out.println("さて、どんだけとれてるかな？");
        System.out.println(me);

        activity.rerenderGridView(me);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        System.out.println("on progress Async!!");

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        System.out.println("on cancel Async!!");

    }


    @Override
    protected List<twitter4j.Status> doInBackground(Void... params) {

        System.out.println("Do!! Do!! Do!!");

        // 検索の実行
        QueryResult result = null;

        try {
            // 検索文字列を設定する
            Query query = new Query("nintendo switch");
//            query.setLocale("ja");	// 日本語のtweetに限定する
            query.setCount(500000);		// 最大20tweetにする（デフォルトは15）

            result = this.twitter.search(query);

        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return result.getTweets();
    }
}