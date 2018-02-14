package com.example.archismansarkar.dynamicviewaddremove;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Archisman Sarkar on 2/9/2018.
 */

public class StartPageActivity extends Activity {
    Button add_remove_view_tf , tf_health_status;
    private Boolean exit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }

        add_remove_view_tf = (Button)findViewById(R.id.arvtf);
        tf_health_status = (Button)findViewById(R.id.ths);

        add_remove_view_tf.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                Intent activityIntent=new Intent(getApplicationContext(),MainActivity.class);
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fadein, R.anim.fadeout);
                getApplicationContext().startActivity(activityIntent, options.toBundle());
            }});

        tf_health_status.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                Intent activityIntent=new Intent(getApplicationContext(),StatusActivity.class);
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fadein, R.anim.fadeout);
                getApplicationContext().startActivity(activityIntent, options.toBundle());
            }});
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
            System.exit(0);
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
}
