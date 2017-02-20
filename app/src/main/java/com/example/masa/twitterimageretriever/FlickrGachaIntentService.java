
package com.example.masa.twitterimageretriever;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;


public class FlickrGachaIntentService extends IntentService {

    final static String TAG = "Flickrガチャ";

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


//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }

//    public FlickrGachaIntentService(String name) {
//        super(name);
//        Log.i(TAG, "constructor");
//    }

    public FlickrGachaIntentService() {
        super(TAG);
        System.out.println("フリッカー！！");
    }


    @Override protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "onHandleIntent in Flickrガチャ");

        StringBuilder builder = new StringBuilder();

        String baseURL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=4852fcab11abc3afa216b6c960fc102e&format=json";

        String oshi = intent.getStringExtra("oshi_name");

        System.out.println("心に刻み給え、汝の推しの名前は" + oshi + "である");

        //String text            = "&text=" + oshi;
        String text            = "&text=" + "寺嶋由芙";

        String min_upload_date = "";
//        String min_upload_date = "&min_upload_date=2013-01-01 03:20:45";
        String max_upload_date = "";
//        String max_upload_date = "&max_upload_date=2014-12-31 03:20:45";
        String per_page = "&per_page=100";

        // たとえば、pagesが1しかないレスポンスのとき、 page=4 とすると、1個も取れなくなる。(page1の分さえも。)注意。

        //String page = "&page=1";
        String page = "";


        try {

            URL url = new URL(baseURL + text + min_upload_date + max_upload_date + per_page + page);

            System.out.println("発行URL: " + url);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // 取得したレスポンスを読み込み
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), "UTF-8"
            ));

            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            String s = builder.toString();

            System.out.println("ストリーム0: " + s);

            // flickr APIの場合、なんと、これないと死ぬ。(JSONレスポンスの先頭に変なの入ってるからっぽい。) やばい。超ハマった。
            s = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);

            System.out.println("ストリーム: " + s);





            try {
                ArrayList<String> resultImgURL = new ArrayList<String>();

                JSONObject json = new JSONObject(s);
                System.out.println("おら！結果1だ！" + json);
                //
                JSONObject photos0 = json.getJSONObject("photos");
                System.out.println("おら！結果2だ！" + photos0);
                //
                JSONArray photos = photos0.getJSONArray("photo");
                System.out.println("おら！結果3だ！" + photos);

                String farm = "";
                String server = "";
                String id = "";
                String secret = "";

                System.out.println("flickr URL個数: " + photos.length());

                if (photos.length() == 0) {
                    System.out.println("早期リターン");
                    return;
                }

                for (int i = 0; i < photos.length(); i++) {

                    JSONObject photo = photos.getJSONObject(i);

                    farm = photo.getString("farm");
                    server = photo.getString("server");
                    id = photo.getString("id");
                    secret = photo.getString("secret");

                    String tmp = "http://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret + ".jpg";
                    System.out.println("抽出URLは: " + tmp);
                    resultImgURL.add(tmp);
                }

                // return resultImgURL;

                // 処理2: 「画像ガチャ」、要するに画像ランダムに1枚を保存する処理
                int count = resultImgURL.size();
                int randomIdx = new Random().nextInt(count);

                mBitmap = getBitmapFromURL(resultImgURL.get(randomIdx));
                System.out.println(mBitmap + "だぜ");

                if (mBitmap != null) {
                    DeviceUtils.saveToFile(getApplicationContext(), mBitmap, "Gacha");
                    System.out.println("ランダムガチャ 保存してやったり！");
                } else {
                    System.out.println("そもそも画像 なかったアルよ");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}