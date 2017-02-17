
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

                RadioGroup rg = (RadioGroup) findViewById(R.id.RadioGroup);
                RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());

                editor.putString(TWITTER_ACCOUNT_NAME, String.valueOf(et.getText()));
                editor.putString(DURATION, String.valueOf(rb.getText()));

                System.out.println("ツイ垢: " + et.getText() + ", 期間: " + rb.getText());

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

        String twitterName = preferences.getString(TWITTER_ACCOUNT_NAME, "No Data");

        if (twitterName == "No Data") {
            return;
        }
        et.setText(twitterName);
    }
}