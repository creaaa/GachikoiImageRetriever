
package com.example.masa.twitterimageretriever;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity {

    // UI
    private SearchView searchView;
    private String searchWord = "";

    private Button button;

    GridAdapter adapter;

    // AsyncTaskから逆流してくるTweet ID
    long maxId = 999999999999999999L;


    @SuppressWarnings("FieldCanBeLocal")
    //private IconList il;
    // 要素をArrayListで設定。なぜIntegerか？このIntegerは RのIDだからだ！！！！！
    // private List<Integer> iconList = new ArrayList<Integer>();

    static Twitter twitter;

    ArrayList<String> oreoreImageURLs = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        // 1. OAuth認証がまだなら、認証画面へ遷移
        if (!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(this, TwitterOAuthActivity.class);
            startActivity(intent);
            finish();
        }


        // 2. UI描画。GridViewのインスタンスを生成
        GridView gridview = (GridView) findViewById(R.id.gridview);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.this.button.getVisibility() == View.VISIBLE) {
                    button.setVisibility(View.GONE);
                }
                new MyAsyncTask(MainActivity.this, twitter).execute();
                System.out.println("まあ、これ先にくるよね。。main acticityのIDは 当然" + MainActivity.this.maxId + "ですぞ");
            }
        });

        // 3. Adapterの生成・セット
        adapter = new GridAdapter(this.getApplicationContext(), R.layout.grid_items, oreoreImageURLs);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(adapter);
        gridview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        gridview.setMultiChoiceModeListener(new Callback());
        gridview.setOnScrollListener(adapter);

        // 空のときのビュー
        gridview.setEmptyView(button);


        // 4. Twitterオブジェクトの初期化
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

        configurationBuilder.setOAuthConsumerKey(getString(R.string.twitter_consumer_key));
        configurationBuilder.setOAuthConsumerSecret(getString(R.string.twitter_consumer_secret));
        configurationBuilder.setOAuthAccessToken(getString(R.string.twitter_access_token));
        configurationBuilder.setOAuthAccessTokenSecret(getString(R.string.twitter_access_token_secret));

        this.twitter = new TwitterFactory(configurationBuilder.build()).getInstance();


        // 5. サービスの起動
    }


    /* Callback */

    void rerenderGridView(ArrayList<String> imageURLs) {

        oreoreImageURLs = null;
        oreoreImageURLs = imageURLs;

        oreoreImageURLs.add("http://cdn4.iconfinder.com/data/icons/basic-work-elements/154/download-load-file-512.png");

        // AsyncTaskからのカウントを受け取り表示する
        adapter.notifyDataSetChanged();
    }


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


    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String searchWord) {

            // SubmitボタンorEnterKeyを押されたら呼び出されるメソッド
            System.out.println("submit touched");

            if (MainActivity.this.button.getVisibility() == View.VISIBLE) {
                button.setVisibility(View.GONE);
            }

            // Twitter APIを叩く。非同期通信でURLを取得する
//            MyAsyncTask task = new MyAsyncTask(MainActivity.this, tw);
//            task.execute();

            new MyAsyncTask(MainActivity.this, twitter).execute();

            System.out.println("まあ、これ先にくるよね。。main acticityのIDは 当然" + MainActivity.this.maxId + "ですぞ");

            // Flickr APIを叩く。
            //new FlickrAsyncTask(MainActivity.this).execute();


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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.crawler:
                startActivity(new Intent(this, SimplePreferenceActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
        
    }


    // BaseAdapter を継承した GridAdapter クラスのインスタンス生成
    class GridAdapter extends BaseAdapter implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

        class ViewHolder {
            ImageView imageView;
        }

        private LayoutInflater inflater;
        private int layoutId;
        // private List<String> icList = new ArrayList<String>();


        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(firstVisibleItem + visibleItemCount + 1 >= totalItemCount) {
                //oreoreImageURLs.add("http://www.google.ca/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&ved=0ahUKEwjDqaTxgonSAhVhzVQKHQdmD2sQjBwIBA&url=http%3A%2F%2Fwww.titech.ac.jp%2Fnews%2Fimg%2Fn000946_iwata_pic1.jpg&psig=AFQjCNGdQuTzJp3lBRbLgSqBOdxLFioO8Q&ust=1486936128504967");
                //oreoreImageURLs.add("http://www.google.ca/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&ved=0ahUKEwiPpur2gonSAhVmhlQKHfCJAOoQjBwIBA&url=https%3A%2F%2Fi.ytimg.com%2Fvi%2FKmDMc0KZe38%2Fmaxresdefault.jpg&psig=AFQjCNGdQuTzJp3lBRbLgSqBOdxLFioO8Q&ust=1486936128504967");
                //oreoreImageURLs.add("http://blog-img.esuteru.com/image/article/201609/075b245397099c622fc25d6724ffd946.jpg");
                System.out.println("いま　ふえた");
                //notifyDataSetChanged();  // gridViewだからか？ これがないとだめっぽい
            } else {
                System.out.println("きてる　きてる");
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {

            System.out.println("positionの位置だ？それは " + position + "だ！");
            System.out.println("画像の個数だ？それは " + parent.getCount() + "だ！");

            if (position + 1 == parent.getCount()) {
                System.out.println("お前の正体は、リロードボタンだ！");

                // ここに再ロード処理を書く
                new MyAsyncTask(MainActivity.this, twitter).execute();
                System.out.println("まあ、これ先にくるよね。。main acticityのIDは 当然" + MainActivity.this.maxId + "ですぞ");

                return;
            }


            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ImageView imageView = (ImageView)inflater.inflate(R.layout.image, null);
            Picasso.with(getApplicationContext()).load(oreoreImageURLs.get(position)).resize(1250,1250).centerCrop().into(imageView);

            imageView.setOnLongClickListener(new View.OnLongClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public boolean onLongClick(View v) {

                    Bitmap mBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

                    DeviceUtils.saveToFile(MainActivity.this, mBitmap);

//                    try {
//                        // sdcardフォルダを指定
//                        File root = Environment.getExternalStorageDirectory();
//
//                        // 日付でファイル名を作成　
//                        Date mDate = new Date();
//                        // 4.4の俺の実機だとだめだ
//                        //SimpleDateFormat fileName = new SimpleDateFormat("yyyyMMdd_HHmmss");
//
//                        String fileName = String.valueOf(new Random().nextInt(1000));
//
//                        // 保存処理開始
//                        FileOutputStream fos = null;
////                        fos = new FileOutputStream(new File(root, fileName.format(mDate) + ".jpg"));
//                        fos = new FileOutputStream(new File(root, fileName + ".jpg"));
//
////                        ImageView sampleImageView = (ImageView)findViewById(R.id.dialog_imageView);
//                        // ただし、グリッドの1個めのが画像が保存されてしまうのだが...
//                        Bitmap mBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
//
//                        // jpegで保存
//                        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//
//                        // 保存処理終了
//                        fos.close();
//
//                        Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
//
//                    } catch (Exception e) {
//                        Log.e("Error", "" + e.toString());
//                    }
                    return true;
                }
            });

            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(imageView);
            dialog.show();
        }


        public GridAdapter(Context context, int layoutId, List<String> iconList) {
            super();
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.layoutId = layoutId;
            // icList = iconList;
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

            System.out.println("positionの位置だ？それは " + position + "だ！");

            Picasso.with(getApplicationContext()).load(oreoreImageURLs.get(position)).into(holder.imageView);

            return convertView;
        }

        // gridViewの個数はここでキマる
        @Override
        public int getCount() {
            // 全要素数を返す
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

            MainActivity.this.findViewById(R.id.image_view).setBackgroundColor(Color.RED);

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            System.out.println("きた3");
        }
    }
}