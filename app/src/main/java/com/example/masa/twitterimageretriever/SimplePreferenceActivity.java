
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
    private static final String SET_CRAWLER_DURATION = "set_crawler_duration";
    private static final String GACHA_DURATION = "gacha_duration";


    private static final String DURATION = "duration";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    EditText et;
    RadioGroup rg1;
    RadioGroup rg2;
    //RadioButton rb;
    //RadioButton rb2;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref);
        //
        preferences = SimplePreferenceActivity.this.getSharedPreferences(PREF_NAME,
        Context.MODE_PRIVATE);
        editor = preferences.edit();

        et   = (EditText) findViewById(R.id.editText);
        rg1  = (RadioGroup) findViewById(R.id.RG1);
        rg2  = (RadioGroup) findViewById(R.id.RG2);
        //rb = (RadioButton) findViewById(rg1.getCheckedRadioButtonId());
        //rb2 = (RadioButton) findViewById(rg2.getCheckedRadioButtonId());


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
                editor.putString(DURATION, String.valueOf(rb.getText()));
                editor.putString(DURATION, String.valueOf(rb2.getText()));

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


        // 2回目以降、設定したTwitterアカウントを自動再ロード
        String twitterName = preferences.getString(TWITTER_ACCOUNT_NAME, "No Data");
        if (twitterName != "No Data") {
            et.setText(twitterName);
        }


        String set_crawler_duration = preferences.getString(SET_CRAWLER_DURATION, "No Data");
        if (set_crawler_duration != "No Data") {

            RadioButton rb = (RadioButton) findViewById(rg1.getCheckedRadioButtonId());
            RadioButton rb2 = (RadioButton) findViewById(rg2.getCheckedRadioButtonId());


//            switch (set_crawler_duration) {
//                case "1 hour":
                    rb.setChecked(true);
//                case "1 day":
                    //rb.setChecked(true);
//                case "OFF":
//                    rb.setChecked(true);
//            }
        }





//        rget.setText(twitterName);

        String set_gacha_duration = preferences.getString(GACHA_DURATION, "No Data");
        if (set_gacha_duration == "No Data") {
            return;
        }
//        et.setText(twitterName);


    }
}