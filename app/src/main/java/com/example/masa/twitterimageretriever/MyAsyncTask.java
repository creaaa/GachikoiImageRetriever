
package com.example.masa.twitterimageretriever;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

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
    private ImageView imageView;

    private Twitter twitter;


    public MyAsyncTask(Context context, Twitter twitter) {
        super();
        //
        activity = (MainActivity) context;
        //imageView = (ImageView) activity.findViewById(R.id.image_view);
        //
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

        // これはできる。もちろん先頭の1個だけだが...
        // Picasso.with(activity).load("http://i.imgur.com/DvpvklR.png").into(imageView);



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

        String[] asyncURIAry = {
                "http://i.imgur.com/DvpvklR.png",
                "https://cdn-ak.f.st-hatena.com/images/fotolife/h/hogehoge223/20170111/20170111150101.jpg",
                "http://i.imgur.com/DvpvklR.png",
                "https://cdn-ak.f.st-hatena.com/images/fotolife/h/hogehoge223/20170111/20170111150101.jpg",
                "http://i.imgur.com/DvpvklR.png"
        };


//        activity.rerenderGridView(asyncURIAry);
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

        // 非同期通信
//        try {

//            URL url = new URL(params[0]);
//
//            System.out.println("URL: " + url + "だぜ");
//
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");
//            //
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    con.getInputStream(), "UTF-8"));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                builder.append(line);
//            }
//        catch (IOException e) {
//            e.printStackTrace();
//        }


        //////////////// できます ///////////////////
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

        //////////////// できます ///////////////////
    }
}