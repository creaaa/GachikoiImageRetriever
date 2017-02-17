
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
    private static final String DURATION = "duration";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    EditText et;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref);
        //
        preferences = SimplePreferenceActivity.this.getSharedPreferences(PREF_NAME,
        Context.MODE_PRIVATE);
        editor = preferences.edit();

        et   = (EditText) findViewById(R.id.editText);

        findViewById(R.id.OKButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("OK touched");
                //
                preferences = SimplePreferenceActivity.this.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
                editor = preferences.edit();

                RadioGroup rg = (RadioGroup) findViewById(R.id.RG1);
                RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());

                RadioGroup rg2 = (RadioGroup) findViewById(R.id.RG2);
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

        if (twitterName == "No Data") {
            return;
        }
        et.setText(twitterName);
    }
}