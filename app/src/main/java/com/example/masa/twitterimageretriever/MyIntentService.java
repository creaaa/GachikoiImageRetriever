package com.example.masa.twitterimageretriever;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import twitter4j.MediaEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class MyIntentService extends IntentService {

    final static String TAG = "ServiceTest";

    private Twitter twitter;

    private Bitmap mBitmap;


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
    }

    public MyIntentService(String name) {
        super(name);
        Log.i(TAG, "constructor");
    }

    public MyIntentService() {
        super(TAG);
        System.out.println("ちんちくりん");
    }


    String s = "初期値";

    @Override protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "onHandleIntent");

        // 検索の実行
        QueryResult result = null;

        try {
            // 検索文字列を設定する
            Query query = new Query("ゆっふぃー -rt");
            query.setCount(100);  // 最大20tweetにする（デフォルトは15)

            result = this.twitter.search(query);

//            String s = result.getTweets().get(0).getText();
//            Log.d(TAG, s);

            ArrayList<String> imageURLs = new ArrayList<>();

            for (twitter4j.Status tweet: result.getTweets()) {
                MediaEntity[] mentitys = tweet.getMediaEntities();
                for(MediaEntity m: mentitys){
                    imageURLs.add(m.getMediaURL());
                }
            }

            if (imageURLs != null) {

                int count = imageURLs.size();
                int randomIdx = new Random().nextInt(count);

//                System.out.println(imageURLs.get(randomIdx));

                mBitmap = getBitmapFromURL(imageURLs.get(randomIdx));
                System.out.println(mBitmap + "だぜ");

//                if (mBitmap != null) {
//                    DeviceUtils.saveToFile(getApplicationContext(), mBitmap);
//                    System.out.println("保存してやったり！");
//                } else {
//                    System.out.println("そもそも画像 なかったアルよ");
//                }

                if (mBitmap != null) {
                    // saveToFile(getApplicationContext(), mBitmap);
                    DeviceUtils.saveToFile(getApplicationContext(), mBitmap);
                    System.out.println("保存してやったり！");
                } else {
                    System.out.println("そもそも画像 なかったアルよ");
                }

            }

        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }


//
//    public void saveToFile(Context context, Bitmap bitmap) {
//
//        if (!sdcardWriteReady()) {
//            Toast.makeText(context, "This device can't save image to SDcard...", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//
//            // 引数にファイルもしくはディレクトリ名を指定し、該当するFileオブジェクトを生成
//            // 要は、/storage/emulated/0/Pictureshogehoge に対し、Fileクラスのオブジェクトである「file」を対応させた。
//            // これにより、今後このパスにあるファイルを、fileオブジェクトで扱える(=読み書きできる)ようになる。
//            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" +
//                    Environment.DIRECTORY_PICTURES + "/" +
//                    context.getResources().getString(R.string.path_image_stroage));
//
//            // 必要なすべての親ディレクトリを含めてディレクトリが生成された場合はtrue、
//            // 生成されなかった場合は false ※ここ、「既に存在した場合は」falseかも。
////            Boolean res = null;
//
//            if (!file.exists()) {
////                res = file.mkdirs();
//                file.mkdirs();
//            }
////
////            if (res == false) {
////                System.out.println("ディレクトリは作られていない");
////            }
//
//            String AttachName = file.getAbsolutePath() + "/" + System.currentTimeMillis() + "." + context.getResources().getString(R.string.extension_save_image);
////            String AttachName = file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
//
//            FileOutputStream out = new FileOutputStream(AttachName);
//
//
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            out.flush();
//            out.close();
//            mediaScan(context, new String[]{AttachName}, file);
//
//        } catch (Exception e) {
//            //Toast.makeText(context, "save failed...", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//            //
//        }
//    }
//
//
//    private static void mediaScan(Context context, String[] paths, File file) {
//
//        String[] mimeTypes = {"image/jpeg"};
//         MediaScannerConnection.scanFile(context, paths, mimeTypes, null);
//
////        Uri contentUri = Uri.fromFile(file);
////        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
////        context.sendBroadcast(mediaScanIntent);
//    }
//
//
//    private static boolean sdcardWriteReady() {
//        String state = Environment.getExternalStorageState();
//        return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
//    }

}