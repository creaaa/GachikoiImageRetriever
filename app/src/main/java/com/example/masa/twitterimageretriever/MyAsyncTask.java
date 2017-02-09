
package com.example.masa.twitterimageretriever;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

public class MyAsyncTask extends AsyncTask<String, String, String> {

    private MainActivity activity;
    private ImageView imageView;

    public MyAsyncTask(Context context) {
        super();
        activity = (MainActivity) context;
        imageView = (ImageView) activity.findViewById(R.id.image_view);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Pre Async!!");
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println("async おわったよ");
        //System.out.println(s);

        // これはできる。もちろん先頭の1個だけだが...
        // Picasso.with(activity).load("http://i.imgur.com/DvpvklR.png").into(imageView);

        String[] asyncURIAry = {
                "http://i.imgur.com/DvpvklR.png",
                "https://cdn-ak.f.st-hatena.com/images/fotolife/h/hogehoge223/20170111/20170111150101.jpg",
                "http://i.imgur.com/DvpvklR.png",
                "https://cdn-ak.f.st-hatena.com/images/fotolife/h/hogehoge223/20170111/20170111150101.jpg",
                "http://i.imgur.com/DvpvklR.png"
        };


        activity.rerenderGridView(asyncURIAry);
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
    protected String doInBackground(String... params) {

        System.out.println("Do!! Do!! Do!!");

        StringBuilder builder = new StringBuilder();

        // 非同期通信
        try {

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

        } catch (Exception e) {

        }

//        catch (IOException e) {
//            e.printStackTrace();
//        }

        return  builder.toString();


    }
}