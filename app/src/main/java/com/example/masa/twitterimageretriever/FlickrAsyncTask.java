
package com.example.masa.twitterimageretriever;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class FlickrAsyncTask extends AsyncTask<Void, String, Void> {

    private MainActivity activity;

    ArrayList<String> imageURLs = new ArrayList<String>();


    public FlickrAsyncTask(Context context) {
        super();
        activity = (MainActivity) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Flickr Pre Async!!");
        //
        activity.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Void t) {
        System.out.println("Flickr async おわったよ。どれだけとれてるかな？");
        System.out.println(imageURLs);

        activity.findViewById(R.id.progressBar).setVisibility(View.GONE);
        activity.rerenderGridView(imageURLs);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        System.out.println("Flickr on progress Async!!");
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        //
        System.out.println("Flickr on cancel Async!!");
        activity.findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    @Override
    protected Void doInBackground(Void... params) {
        imageURLs.add("http://pbs.twimg.com/media/CylhKeLUAAAJmsu.jpg");

        setURLConnection();

        return null;
    }

    ////  以下、独自実装
    public void setURLConnection() {

        StringBuilder builder = new StringBuilder();

        try {
//            URL url = new URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=4852fcab11abc3afa216b6c960fc102e&text=寺嶋由芙&format=json");
            URL url = new URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=4852fcab11abc3afa216b6c960fc102e&text=trump&format=json");

            //URL url = new URL("http://www.wings.msn.to/");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // 取得したレスポンスを読み込み
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), "UTF-8"
            ));

            String line;
            //StringBuilder builder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            String s = builder.toString();
            s = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);


            try {
                 JSONObject json = new JSONObject(s);
                //JSONArray json = new JSONArray(s);

                System.out.println("おら！結果だ！" + json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }






    // ika kopipe

//    private HashMap<String, String> parseJson(String json) {
//        JSONObject jsonObj = null;
//        try {
//            jsonObj = new JSONObject(json);
//        } catch (JSONException e) {
//            Log.e(TAG, "jsonObj：" + e.toString());
//            return null;
//        }
//
//        JSONObject photos = null;
//        try {
//            photos = jsonObj.getJSONObject("photos");
//        } catch (JSONException e) {
//            Log.e(TAG, "photos：" + e.toString());
//            return null;
//        }
//
//        JSONArray photoArray = null;
//        try {
//            photoArray = photos.getJSONArray("photo");
//        } catch (JSONException e) {
//            Log.e(TAG, "photoArray：" + e.toString());
//            return null;
//        }
//
//        JSONObject photoObj = null;
//        Random rnd = new Random(System.currentTimeMillis());
//        int ran = rnd.nextInt(photoArray.length());
//        try {
//            photoObj = photoArray.getJSONObject(ran);
//        } catch (JSONException e) {
//            Log.e(TAG, "photoObj：" + e.toString());
//            return null;
//        }
//
//        HashMap<String, String> returnValue = new HashMap<String, String>();
//        try {
//            returnValue.put("farm", photoObj.getString("farm"));
//            returnValue.put("server", photoObj.getString("server"));
//            returnValue.put("id", photoObj.getString("id"));
//            returnValue.put("secret", photoObj.getString("secret"));
//        } catch (JSONException e) {
//            Log.e(TAG, "photoSearchItem：" + e.toString());
//            return null;
//        }
//
//        return returnValue;
//    }
//
//
//    public Bitmap getFlickrImage(){
//        String json = getPhotoData("0c38f12818af42840055523fe245afb5", "techbooster");
//        HashMap<String, String> searchResult = parseJson(json);
//
//        StringBuilder photoUrlSb = new StringBuilder();
//        photoUrlSb.append("http://farm");
//        photoUrlSb.append(searchResult.get("farm"));
//        photoUrlSb.append(".staticflickr.com/");
//        photoUrlSb.append(searchResult.get("server"));
//        photoUrlSb.append("/");
//        photoUrlSb.append(searchResult.get("id"));
//        photoUrlSb.append("_");
//        photoUrlSb.append(searchResult.get("secret"));
//        photoUrlSb.append(".jpg");
//
//        HttpClient client = new DefaultHttpClient();
//        HttpParams httpparams = client.getParams();
//        HttpConnectionParams.setConnectionTimeout(httpparams, 10000);
//        HttpConnectionParams.setSoTimeout(httpparams, 10000);
//        HttpGet httpGet = new HttpGet();
//        // URIの設定
//        try {
//            httpGet.setURI(new URI(photoUrlSb.toString()));
//        } catch (URISyntaxException e) {
//            Log.e(TAG, "httpGet.setURI：" + e.toString());
//            return null;
//        }
//
//        HttpResponse response = null;
//        Bitmap bitmap = null;
//        try {
//            response = client.execute(httpGet);
//            if (response.getStatusLine().getStatusCode() < 400) {
//                InputStream in = response.getEntity().getContent();
//                bitmap = BitmapFactory.decodeStream(in);
//                if (in != null) {
//                    in.close();
//                }
//                Log.d(TAG, String.valueOf(bitmap));
//                return bitmap;
//            }
//        } catch (ClientProtocolException e) {
//            Log.e(TAG, "getReaouse：" + e.toString());
//            return null;
//        } catch (IOException e) {
//            Log.e(TAG, "getReaouse：" + e.toString());
//            return null;
//        }
//
//        return bitmap;
//    }
//
//
//    private String getPhotoData(String appkey, String keyword){
//        HttpClient client = new DefaultHttpClient();
//        HttpParams params = client.getParams();
//        HttpConnectionParams.setConnectionTimeout(params, 10000);
//        HttpConnectionParams.setSoTimeout(params, 10000);
//
//        // Uri作成
//        URI uri = null;
//        try {
//            uri = new URI(
//                    "http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key="
//                            + appkey + "&text" + keyword + "&tags=" + keyword + "&format=json&nojsoncallback=1");
//        } catch (URISyntaxException e) {
//            Log.e(TAG, e.toString());
//        }
//        // 通信開始
//        HttpUriRequest httpRequest = new HttpGet(uri);
//
//        HttpResponse httpResponse = null;
//        try {
//            httpResponse = client.execute(httpRequest);
//        } catch (ClientProtocolException e) {
//            Log.e(TAG, "httpRequest：" + e.toString());
//        } catch (IOException e) {
//            Log.e(TAG, "httpRequest：" + e.toString());
//        }
//
//        if (httpResponse == null) {
//            return "";
//        }
//
//        // jsonの取得
//        String json = "";
//
//        if (httpResponse != null
//                && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//            HttpEntity httpEntity = httpResponse.getEntity();
//            try {
//                json = EntityUtils.toString(httpEntity);
//            } catch (ParseException e) {
//                Log.e(TAG, "EntityUtils：" + e.toString());
//            } catch (IOException e) {
//                Log.e(TAG, "EntityUtils：" + e.toString());
//            }
//        }
//
//        return json;
//    }


}