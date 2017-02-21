
package com.example.masa.twitterimageretriever;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.api.TimelinesResources;
import twitter4j.conf.ConfigurationBuilder;


public class MyIntentService extends IntentService {

    final static String TAG = "ServiceTest";

    private Twitter twitter;
    private Bitmap  mBitmap;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private static final String PREF_NAME = "setting";

    //private static final String TWITTER_ACCOUNT_NAME = "twitter_account_name";
    private static final String LATEST_TWEET_ID = "latest_tweet_id";


    // Preferenceから抽出した、取得したいタイムラインのユーザー
    //String twitter_account_name;

    // Preferenceから抽出した、最後に取得した最新のつぶやきID
    long latest_tweet_id;


    public Bitmap getBitmapFromURL(String src) {

        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            mBitmap = BitmapFactory.decodeStream(input);
            return mBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


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

        pref = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();

//        twitter_account_name = pref.getString(TWITTER_ACCOUNT_NAME, "@NaN");
        latest_tweet_id = pref.getLong(LATEST_TWEET_ID, 1);
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

        // Preferenceから抽出した、取得したいTwitterAccount名
        //String ta = intent.getStringExtra("ta");

        // 検索の実行
        // QueryResult result = null;

        try {
            TimelinesResources timeline = twitter.timelines();

            Paging paging = new Paging();    // Pagingオブジェクトを作成
            paging.setPage(1);               // ページ番号を指定
            paging.count(200);               // 1ページから取得するツイート数を指定
    		// paging.setMaxId(latest_tweet_id + 1);     // MaxIdよりも後のツイートを取得するよう指定
            paging.setSinceId(latest_tweet_id);  // SinceIdよりも前のツイートを取得するよう指定


            String searchQuery = pref.getString("twitter_account_name", "");
            User user = twitter.showUser(searchQuery);
            long id = user.getId();

            ResponseList tweets = timeline.getUserTimeline(id, paging);

            // 前回のクロール以降、つぶやきが1個でもあれば
            if (tweets.size() > 0) {

                ArrayList<String> imageURLs = new ArrayList<>();

                // 処理1: 前回以降の画像つきツイートから画像を抽出し、ローカル保存
                for (int i = 0; i < tweets.size(); i++) {

                    Status tweet = (Status) tweets.get(i);
                    System.out.println("ID: " + tweet.getId());

                    // 以下、パターン1と2で取得できる画像が違う。
                    // 2のほうが多く取れたが、いずれの場合でも取れなかった画像もいっぱいある。
                    // とりあえずパターン2でいきます。一体どうなっているんだ・・・

                    // パターン1
                    //MediaEntity[] mentitys = tweet.getMediaEntities();

//                    for (MediaEntity m: mentitys) {
//                        imageURLs.add(m.getMediaURL());
//                    }

                    // パターン2
                    MediaEntity[] mentitys = tweet.getExtendedMediaEntities();
                    for (MediaEntity m: mentitys) {
                        imageURLs.add(m.getMediaURL());
                    }

                }


                if (imageURLs != null) {
                    // これ　消すなよ
                    // これは「画像ガチャ」、要するにランダムに1枚を保存する処理です。退避。
//                    int count = imageURLs.size();
//                    int randomIdx = new Random().nextInt(count);
//
//                    mBitmap = getBitmapFromURL(imageURLs.get(randomIdx));
//                    System.out.println(mBitmap + "だぜ");
//
//                    if (mBitmap != null) {
//                        DeviceUtils.saveToFile(getApplicationContext(), mBitmap);
//                        System.out.println("保存してやったり！");
//                    } else {
//                        System.out.println("そもそも画像 なかったアルよ");
//                    }
                    for (String URL : imageURLs) {
                        mBitmap = getBitmapFromURL(URL);
                        DeviceUtils.saveToFile(getApplicationContext(), mBitmap, "AutoCollect");
                        System.out.println("保存してやったり！");
                        Toast.makeText(getApplicationContext(), "fetched", Toast.LENGTH_SHORT);
                    }

                } else {
                    System.out.println("ImageURLは 1個も なかったアルね");
                }

                // 処理2: 最新のつぶやきIDをpreferenceに保存
//                if (tweets.size() > 0) {
                    Status tmp = (Status) tweets.get(0);
                    long l_t_id = tmp.getId();
                    System.out.println("最新のつぶやきIDは " + l_t_id);

                    editor.putLong(LATEST_TWEET_ID, l_t_id);
                    boolean saveResult = editor.commit();
                    System.out.println("保存結果: " + saveResult);
//                }

            } else {
                System.out.println("新着つぶやきは ありませんでした");
            }

        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}