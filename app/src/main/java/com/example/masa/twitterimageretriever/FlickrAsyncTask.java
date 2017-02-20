//
//package com.example.masa.twitterimageretriever;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.view.View;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//
//
//public class FlickrAsyncTask extends AsyncTask<Void, String, ArrayList<String>> {
//
//    private MainActivity activity;
//
//
//    public FlickrAsyncTask(Context context) {
//        super();
//        activity = (MainActivity) context;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        System.out.println("Flickr Pre Async!!");
//        //
//        activity.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    protected void onPostExecute(ArrayList<String> resultImageURLs) {
//        System.out.println("Flickr async おわったよ。どれだけとれてるかな？");
//        System.out.println(resultImageURLs);
//
//        activity.findViewById(R.id.progressBar).setVisibility(View.GONE);
//        activity.rerenderGridView(resultImageURLs);
//    }
//
//    @Override
//    protected void onProgressUpdate(String... values) {
//        super.onProgressUpdate(values);
//        System.out.println("Flickr on progress Async!!");
//    }
//
//    @Override
//    protected void onCancelled() {
//        super.onCancelled();
//        //
//        System.out.println("Flickr on cancel Async!!");
//        activity.findViewById(R.id.progressBar).setVisibility(View.GONE);
//    }
//
//    @Override
//    protected ArrayList<String> doInBackground(Void... params) {
//        return setURLConnection();
//    }
//
//    ////  以下、独自実装
//    public ArrayList<String> setURLConnection() {
//
//        StringBuilder builder = new StringBuilder();
//
//        String baseURL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=4852fcab11abc3afa216b6c960fc102e&format=json";
//        String text            = "&text=玉井詩織";
//        String min_upload_date = "";
////        String min_upload_date = "&min_upload_date=2013-01-01 03:20:45";
//        String max_upload_date = "";
////        String max_upload_date = "&max_upload_date=2014-12-31 03:20:45";
//        String per_page = "&per_page=100";
//
//
//        // たとえば、pagesが1しかないレスポンスのとき、 page=4 とすると、1個も取れなくなる。(page1の分さえも。)注意。
//        String page = "&page=1";
//
//        try {
//            URL url = new URL(baseURL + text + min_upload_date + max_upload_date + per_page + page);
////            URL url = new URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=4852fcab11abc3afa216b6c960fc102e&text=trump&format=json");
//
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");
//
//            // 取得したレスポンスを読み込み
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    con.getInputStream(), "UTF-8"
//            ));
//
//            String line;
//
//            while ((line = reader.readLine()) != null) {
//                builder.append(line);
//            }
//
//            String s = builder.toString();
//            // flickr APIの場合、なんと、これないと死ぬ。(JSONレスポンスの先頭に変なの入ってるからっぽい。) やばい。超ハマった。
//            s = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
//
//            try {
//
//                ArrayList<String> resultImgURL = new ArrayList<String>();
//
//                JSONObject json = new JSONObject(s);
//                System.out.println("おら！結果1だ！" + json);
//                //
//                JSONObject photos0 = json.getJSONObject("photos");
//                System.out.println("おら！結果2だ！" + photos0);
//                //
//                JSONArray photos = photos0.getJSONArray("photo");
//                System.out.println("おら！結果3だ！" + photos);
//
//                String farm = "";
//                String server = "";
//                String id = "";
//                String secret = "";
//
//                System.out.println("flickr URL個数: " + photos.length());
//
//                for (int i = 0; i < photos.length(); i++) {
//
//                    JSONObject photo = photos.getJSONObject(i);
//
//                    farm = photo.getString("farm");
//                    server = photo.getString("server");
//                    id = photo.getString("id");
//                    secret = photo.getString("secret");
//
//                    String tmp = "http://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret + ".jpg";
//                    System.out.println("抽出URLは: " + tmp);
//                    resultImgURL.add(tmp);
//                }
//
//                return resultImgURL;
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//}