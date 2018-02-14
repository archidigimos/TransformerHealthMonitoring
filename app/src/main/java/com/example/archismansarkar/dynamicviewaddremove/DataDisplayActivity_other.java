package com.example.archismansarkar.dynamicviewaddremove;

/**
 * Created by Archisman Sarkar on 2/8/2018.
 */

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class DataDisplayActivity_other extends Activity {

    String senderAddress = "";
    String desiredAddress;
    String smsBody = "";
    String smsBodyParts[];
    LinearLayout data_holder;

    String date = "";
    int track = 0;
    SharedPreferences pref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.data_log_other);
        data_holder = (LinearLayout)findViewById(R.id.data_logging_other);

        pref = getApplicationContext().getSharedPreferences("Desired_Sender_Address", MODE_PRIVATE);
        if (track == 0) {
            desiredAddress = pref.getString("desired_address", "+919800644706");
            uiUpdate();
            track = 1;
        }
    }

    @Override
    public void onBackPressed() {
        Intent activityIntent=new Intent(getApplicationContext(),MainActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fadein, R.anim.fadeout);
        getApplicationContext().startActivity(activityIntent, options.toBundle());
    }


    public void uiUpdate(){
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c= getContentResolver().query(uri, null, null ,null,null);

        if(c.moveToFirst()) {
            for(int i=0; i < c.getCount(); i++) {
                //SMSData sms = new SMSData();
                senderAddress = c.getString(c.getColumnIndexOrThrow("address")).toString();
                if(desiredAddress.equals(senderAddress)) {

                    smsBody = c.getString(c.getColumnIndexOrThrow("body")).toString();

                    smsBody = smsBody.replace("\n","=");
                    smsBodyParts = smsBody.split("=");

                    date =  c.getString(c.getColumnIndexOrThrow("date")).toString();

                    if(smsBodyParts.length>=18) {
                        smsBody = "Humidity: " + smsBodyParts[13] + "\n" + " Temperature: " + smsBodyParts[15] + "\n" + " OilLevel: " + smsBodyParts[17] + "\n" + " Time: " + date;
                        addViewStatic(smsBodyParts[13], smsBodyParts[15], smsBodyParts[17], date);
                    }

                }
                c.moveToNext();
            }
        }
        c.close();
    }

    private void addViewStatic(final String hum, final String tempe, final String oillev, final String time_){
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.data_log_other_widget, null);

        TextView humidity_log = (TextView)addView.findViewById(R.id.humidity_other);
        humidity_log.setText(hum);

        TextView temperature_log = (TextView)addView.findViewById(R.id.temperature_other);
        temperature_log.setText(tempe);

        TextView oillevel_log = (TextView)addView.findViewById(R.id.oil_level_other);
        oillevel_log.setText(oillev);

        TextView time_log_other = (TextView)addView.findViewById(R.id.timestamp_other);
        time_log_other.setText(time_);

        data_holder.addView(addView);
    }

}
