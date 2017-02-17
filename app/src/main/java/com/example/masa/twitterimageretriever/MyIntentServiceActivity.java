
package com.example.masa.twitterimageretriever;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import twitter4j.Twitter;


public class MyIntentServiceActivity extends AppCompatActivity {

    final static String TAG = "ServiceTest";

    Twitter twitter;

    Button scheduleButton;
    Button cancelButton;

    @Override protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_intent_service);
        //
        scheduleButton = (Button) findViewById(R.id.schedule_button);
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleService();
            }
        });

        cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelService();
            }
        });
    }


    protected void scheduleService() {

        Log.d(TAG, "scheduleService()");

        Context context = getBaseContext();

        Intent intent = new Intent(context, MyIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 30000, pendingIntent);
    }


    protected void cancelService(){ Context context = getBaseContext();

        System.out.println("キャンセル 来てるんよ");

        Intent intent = new Intent(context, MyIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}