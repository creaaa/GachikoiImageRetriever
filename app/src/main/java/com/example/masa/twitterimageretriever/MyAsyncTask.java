
package com.example.masa.twitterimageretriever;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;

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

    ArrayList<String> imageURLs = new ArrayList<String>();

    // Tweet IDを管理
    // long maxId = 999999999999999999L;



    public MyAsyncTask(Context context, Twitter twitter) {
        super();
        activity = (MainActivity) context;
        this.twitter = twitter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Pre Async!!");
        //
        activity.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(List<twitter4j.Status> s) {
        System.out.println("async おわったよ。どれだけとれてるかな？");
        System.out.println(imageURLs);

        activity.findViewById(R.id.progressBar).setVisibility(View.GONE);

        // TweetIDをMainActivityに逆流してあげる
        // activity.maxId = this.maxId;

        System.out.println("見事！ main acticityのIDは" + activity.maxId + "ですぞ");

        activity.rerenderGridView(imageURLs);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        System.out.println("on progress Async!!");

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        //
        System.out.println("on cancel Async!!");
        activity.findViewById(R.id.progressBar).setVisibility(View.GONE);
    }


    @Override
    protected List<twitter4j.Status> doInBackground(Void... params) {

        System.out.println("Do!! Do!! Do!!");
        return searchTwitterByQuery();
    }


    public List<twitter4j.Status> searchTwitterByQuery() {

        // 検索の実行
        QueryResult result = null;


        try {
            // 検索文字列を設定する
            Query query = new Query("ゆっふぃー -rt");
//            query.setLocale("ja");	// 日本語のtweetに限定する
            query.setCount(100);  // 最大20tweetにする（デフォルトは15）


            result = this.twitter.search(query);

            // 最大1500件（15ページ）なので15回ループ
//            for (int i = 1; i <= 7; i++) {

            for (int i = 1; i <= 1; i++) {

                query.setMaxId(activity.maxId);

                // System.out.println("なるほど、今のsinceIDは " + maxId + "だ。");


                result = twitter.search(query);
                System.out.println("ヒット数 : " + result.getTweets().size());
                System.out.println("ページ数 : " + new Integer(i).toString());

                // 検索結果を見てみる
                for (twitter4j.Status tweet: result.getTweets()) {
//                    // 本文
//                    String str = tweet.getText();
//                    java.util.Date hiduke = tweet.getCreatedAt();
//                    System.out.println(hiduke + str);

                    //ツイート本文に画像URLが含まれていたら取
                    // 単数枚
                    MediaEntity[] mentitys = tweet.getMediaEntities();
                    for(MediaEntity m: mentitys){
                        imageURLs.add(m.getMediaURL());
                    }

//                    // 複数枚...ほんとうにこれでできてんのか？ → なんかダメだった
//                    MediaEntity[] arrMediaExt = tweet.getExtendedMediaEntities();
//                    for (MediaEntity m: arrMediaExt) {
//                        imageURLs.add(m.getMediaURL());
//                    }
//
//                    URLEntity[] entity = tweet.getURLEntities();
//                    for (URLEntity urlEntity: entity) {
//                        imageURLs.add(urlEntity.getExpandedURL());
//                    }

                    activity.maxId = tweet.getId();
                }

                if (result.hasNext()) {
                    query = result.nextQuery();
                    SystemClock.sleep(1000);
                } else {
                    break;
                }
            }

        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return result.getTweets();
    }
}