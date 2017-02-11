
package com.example.masa.twitterimageretriever;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity {

    // UI
    private SearchView searchView;
    private String searchWord = "";

    GridAdapter adapter;


    @SuppressWarnings("FieldCanBeLocal")
    private IconList il;
    // 要素をArrayListで設定。なぜIntegerか？このIntegerは RのIDだからだ！！！！！
    private List<Integer> iconList = new ArrayList<Integer>();

    Twitter tw;

    ArrayList<String> oreoreImageURLs = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        Log.i("u", "ogehoge");


        il = new IconList();
        for (int i = 0; i < il.maxNum() ; i++){
            iconList.add(il.getIcon(i));
        }

        // GridViewのインスタンスを生成
        GridView gridview = (GridView) findViewById(R.id.gridview);

        // BaseAdapter を継承したGridAdapterのインスタンスを生成
        // 子要素のレイアウトファイル grid_items.xml を activity_main.xml に inflate するためにGridAdapterに引数として渡す
        adapter = new GridAdapter(this.getApplicationContext(), R.layout.grid_items, iconList);

        // gridViewにadapterをセット
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(adapter);
        //
        gridview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        gridview.setMultiChoiceModeListener(new Callback());

        // OAuth認証用設定
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(getString(R.string.twitter_consumer_key));
        configurationBuilder.setOAuthConsumerSecret(getString(R.string.twitter_consumer_secret));
        configurationBuilder.setOAuthAccessToken(getString(R.string.twitter_access_token));
        configurationBuilder.setOAuthAccessTokenSecret(getString(R.string.twitter_access_token_secret));

        // Twitterオブジェクトの初期化
        this.tw = new TwitterFactory(configurationBuilder.build()).getInstance();
        //
        oreoreImageURLs.add("http://l-tool.little-net.com/tools/cart/0401/sample/img/sample.jpg");
        oreoreImageURLs.add("http://l-tool.little-net.com/tools/cart/0401/sample/img/sample.jpg");
        oreoreImageURLs.add("http://l-tool.little-net.com/tools/cart/0401/sample/img/sample.jpg");
        oreoreImageURLs.add("http://l-tool.little-net.com/tools/cart/0401/sample/img/sample.jpg");
        oreoreImageURLs.add("http://l-tool.little-net.com/tools/cart/0401/sample/img/sample.jpg");
    }


    void rerenderGridView(ArrayList<String> imageURLs) {

        oreoreImageURLs = imageURLs;

        // AsyncTaskからのカウントを受け取り表示する
        adapter.notifyDataSetChanged();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search_menu_search_view);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        // 虫眼鏡アイコンを最初表示するかの設定
        this.searchView.setIconifiedByDefault(true);
        // Submitボタンを表示するかどうか
        this.searchView.setSubmitButtonEnabled(false);


        if (!searchWord.equals("")) {
            // TextView.setTextみたいなもの
            searchView.setQuery(searchWord, false);
        } else {
            String queryHint = getResources().getString(R.string.search_menu_query_hint_text);
            // placeholderみたいなもの
            searchView.setQueryHint(queryHint);
        }

        searchView.setOnQueryTextListener(onQueryTextListener);

        return true;
    }


    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String searchWord) {
            // SubmitボタンorEnterKeyを押されたら呼び出されるメソッド
            System.out.println("submit touched");

            String imageURL = null;
            String APIUri = "https://qiita.com/api/v2/items";

            // 非同期通信でURLを取得する
            MyAsyncTask task = new MyAsyncTask(MainActivity.this, tw);
            //task.execute(APIUri);
            task.execute();

            // false: 続いて↓のonQuery~がコールされる
            // true:  呼び出されない
            return setSearchWord(searchWord); // false
            //return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // 入力される度に呼び出される
            System.out.println("input");
            return false;
        }
    };


    private boolean setSearchWord(String searchWord) {

        ActionBar actionBar = this.getSupportActionBar();
        //noinspection ConstantConditions,ConstantConditions
        actionBar.setTitle(searchWord);
        actionBar.setDisplayShowTitleEnabled(true);

        if (searchWord != null && !searchWord.equals("")) {
            // searchWordがあることを確認
            this.searchWord = searchWord;
        }

        // 虫眼鏡アイコンを隠す
        this.searchView.setIconified(false);
        // SearchViewを隠す
        this.searchView.onActionViewCollapsed();
        // Focusを外す
        this.searchView.clearFocus();


        return false;
    }


    ///////////
    // 　UI  //
    ///////////

    class ViewHolder {
        ImageView imageView;

        Picasso mPicasso;
    }


    // BaseAdapter を継承した GridAdapter クラスのインスタンス生成
    class GridAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        private LayoutInflater inflater;
        private int layoutId;
        private List<Integer> icList = new ArrayList<Integer>();

        @Override
        public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {

            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ImageView imageView = (ImageView)inflater.inflate(R.layout.image, null);
            Picasso.with(getApplicationContext()).load(oreoreImageURLs.get(position)).resize(1250,1250).centerCrop().into(imageView);

            imageView.setOnLongClickListener(new View.OnLongClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public boolean onLongClick(View v) {

                    try {
                        // sdcardフォルダを指定
                        File root = Environment.getExternalStorageDirectory();

                        // 日付でファイル名を作成　
                        Date mDate = new Date();
                        SimpleDateFormat fileName = new SimpleDateFormat("yyyyMMdd_HHmmss");

                        // 保存処理開始
                        FileOutputStream fos = null;
                        fos = new FileOutputStream(new File(root, fileName.format(mDate) + ".jpg"));

//                        ImageView sampleImageView = (ImageView)findViewById(R.id.dialog_imageView);
                        // ただし、グリッドの1個めのが画像が保存されてしまうのだが...
                        Bitmap mBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

                        // jpegで保存
                        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                        System.out.println("kitaw");

                        // 保存処理終了
                        fos.close();

                    } catch (Exception e) {
                        Log.e("Error", "" + e.toString());
                    }

                    return true;
                }
            });

            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(imageView);
            dialog.show();
        }


        public GridAdapter(Context context, int layoutId, List<Integer> iconList) {
            super();
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.layoutId = layoutId;
            icList = iconList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                // main.xml の <GridView .../> に grid_items.xml を inflate して convertView とする
                convertView = inflater.inflate(layoutId, parent, false);
                // ViewHolder を生成
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            // icList(もともと iconList = RのIDが格納されている) の中のIDを、
            // positionを使い取得している。
            // これにより、位置ごとに異なるimageをセットしている。
            // つまり、 position を使うことが重要！！！！！
            // ※ これは当然動きます
            //holder.imageView.setImageResource(icList.get(position));



//            String[] imageURLs = {
//                    "http://i.imgur.com/DvpvklR.png",
//                    "https://cdn-ak.f.st-hatena.com/images/fotolife/h/hogehoge223/20170111/20170111150101.jpg",
//                    "https://cdn-ak.f.st-hatena.com/images/fotolife/h/hogehoge223/20170111/20170111150101.jpg",
//                    "https://cdn-ak.f.st-hatena.com/images/fotolife/h/hogehoge223/20170111/20170111150101.jpg",
//                    "https://cdn-ak.f.st-hatena.com/images/fotolife/h/hogehoge223/20170111/20170111150101.jpg",
//                    "https://cdn-ak.f.st-hatena.com/images/fotolife/h/hogehoge223/20170111/20170111150101.jpg",
//                    "https://cdn-ak.f.st-hatena.com/images/fotolife/h/hogehoge223/20170111/20170111150101.jpg",
//                    "https://cdn-ak.f.st-hatena.com/images/fotolife/h/hogehoge223/20170111/20170111150101.jpg",
//                    "https://cdn-ak.f.st-hatena.com/images/fotolife/h/hogehoge223/20170111/20170111150101.jpg",
//
//                    // 10個, 11個め。使われない。
//                    "https://cdn-ak.f.st-hatena.com/images/fotolife/h/hogehoge223/20170111/20170111150101.jpg",
//                    "https://cdn-ak.f.st-hatena.com/images/fotolife/h/hogehoge223/20170111/20170111150101.jpg"
//            };

            Picasso.with(getApplicationContext()).load(oreoreImageURLs.get(position)).into(holder.imageView);


            // とおる。全部満たしてる。
            //Picasso.with(getApplicationContext()).load("http://i.imgur.com/DvpvklR.png").into(holder.imageView);

//            Picasso.with(getApplicationContext()).load(imageURL).into(holder.imageView);
//            Picasso.with(getBaseContext()).load(android.R.drawable.ic_media_play).into(holder.imageView);

            return convertView;
        }

        // gridViewの個数はここでキマる
        @Override
        public int getCount() {
            // 全要素数を返す

            // return iconList.size();
            // return 5;
            return oreoreImageURLs.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }


    private class Callback implements GridView.MultiChoiceModeListener {

        // これだけいつ呼ばれるか不明
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // アクションアイテム選択時
            System.out.println("きた1");
            return true;
        }


        // 以下、時系列順
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // アクションモード初期化処理
            System.out.println("きた2");

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // アクションモード表示事前処理
            System.out.println("きた4");

            return true;
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position,
                                              long id, boolean checked) {
            // アクションモード時のアイテムの選択状態変更時
            System.out.println("きた5");
        }


        @Override
        public void onDestroyActionMode(ActionMode mode) {

            System.out.println("きた3");

            // 決定ボタン押下時
//            final int checkedCount = get().getCheckedItemCount();
//            SparseBooleanArray list = getListView().getCheckedItemPositions();
//            String str = "";
//
//            for(int i=0;i<GENRES.length;i++){
//                boolean checked = list.get(i);
//                if (checked == true){
//                    str = str+GENRES[i] + " ";
//                }
//            }
//
//            Intent intent = new Intent(getApplicationContext(), ResultDisp.class);
//            intent.putExtra("checked_list", str);
//            startActivity(intent);
        }
    }
}