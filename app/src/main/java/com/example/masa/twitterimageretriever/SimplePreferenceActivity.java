
package com.example.masa.twitterimageretriever;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SimplePreferenceActivity extends AppCompatActivity {

    private static final String PREF_NAME = "setting";

    private static final String TWITTER_ACCOUNT_NAME = "twitter_account_name";
    private static final String CRAWLER_DURATION = "crawler_duration";
    private static final String GACHA_DURATION = "gacha_duration";


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    EditText et;
    RadioGroup rg1;
    RadioGroup rg2;

    // 再設定時、もし別のtwitterNameが入力されたら、最新つぶやきIDをリセットする。そのために使う
    String previousTwitterName;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref);
        //
        preferences = SimplePreferenceActivity.this.getSharedPreferences(PREF_NAME,
        Context.MODE_PRIVATE);
        editor = preferences.edit();
        //



        et   = (EditText) findViewById(R.id.editText);
        rg1  = (RadioGroup) findViewById(R.id.RG1);
        rg2  = (RadioGroup) findViewById(R.id.RG2);


        // 2回目以降、設定したTwitterアカウントを自動再ロード
        String twitterName = preferences.getString(TWITTER_ACCOUNT_NAME, "No Data");
        if (twitterName != "No Data") {
            et.setText(twitterName);
            previousTwitterName = twitterName;
        }

        // 2回目以降、設定した自動クローラ周期を自動再ロード
        String set_crawler_duration = preferences.getString(CRAWLER_DURATION, "No Data");
        if (set_crawler_duration != "No Data") {

            RadioButton rb = null;

            switch (set_crawler_duration) {

                case "1 hour":
                    rb = (RadioButton) findViewById(R.id.radioButton2);
                    break;
                case "1 day":
                    rb = (RadioButton) findViewById(R.id.radioButton3);
                    break;
                case "OFF":
                    rb = (RadioButton) findViewById(R.id.OFFradioButton);
                    break;
                default:
                    System.out.println("なんでもない");
            }

            rb.setChecked(true);

        } else {
            System.out.println("おわた");
        }

        // 2回目以降、設定した自動ガチャ周期を自動再ロード
        String set_gacha_duration = preferences.getString(GACHA_DURATION, "No Data");

        if (set_gacha_duration != "No Data") {

            RadioButton rb2 = null;

            switch (set_gacha_duration) {
                case "ON":
                    rb2 = (RadioButton) findViewById(R.id.radioButton4);
                    break;
                case "OFF":
                    rb2 = (RadioButton) findViewById(R.id.radioButton5);
                    break;
                default:
                    System.out.println("なんでもない");
            }

            rb2.setChecked(true);

        } else {
            System.out.println("おわた");
        }

        // set event listener

        findViewById(R.id.OKButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("OK touched");
                //
                preferences = SimplePreferenceActivity.this.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
                editor = preferences.edit();

                // 選択されているラジオボタンを定義
                RadioButton rb = (RadioButton) findViewById(rg1.getCheckedRadioButtonId());
                RadioButton rb2 = (RadioButton) findViewById(rg2.getCheckedRadioButtonId());

                editor.putString(TWITTER_ACCOUNT_NAME, String.valueOf(et.getText()));

                if (!previousTwitterName.equals(String.valueOf(et.getText()))) {
                    System.out.println("違う twitterNameが 入力されました");
                    editor.putLong("latest_tweet_id", 1);
                }


                editor.putString(CRAWLER_DURATION, String.valueOf(rb.getText()));
                editor.putString(GACHA_DURATION, String.valueOf(rb2.getText()));

                System.out.println("ツイ垢: " + et.getText() +
                                   ", クローラ周期: " + rb.getText() +
                                   ", ガチャ周期: "  + rb2.getText()
                );

                editor.commit();
                finish();
            }
        });

        findViewById(R.id.DismissButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CANCEL touched");
                finish();
            }
        });

    }
}