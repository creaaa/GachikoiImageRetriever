
package com.example.masa.twitterimageretriever;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class DecideOshiActivity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private static final String PREF_NAME = "setting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oshi);
        //
        System.out.println("おし！");

        pref = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText e = (EditText) findViewById(R.id.editText2);
                String   s = e.getText().toString();
                editor.putString("oshi_name", s);
                System.out.println(s + " を一生の推しにしました。");

                System.out.println("result: " + editor.commit());

                finish();
            }
        });
    }
}
