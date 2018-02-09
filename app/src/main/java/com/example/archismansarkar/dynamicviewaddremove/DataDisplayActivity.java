package com.example.archismansarkar.dynamicviewaddremove;

/**
 * Created by Archisman Sarkar on 2/8/2018.
 */

import android.app.Activity;
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


public class DataDisplayActivity extends Activity {

    String senderAddress = "";
    String desiredAddress;
    String smsBody = "";
    String smsBodyParts[];
    LinearLayout data_holder;

    String date = "";
    int track = 0;
    SharedPreferences pref;

    private Boolean exit = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent sms_intent=getIntent();
        Bundle b=sms_intent.getExtras();

        setContentView(R.layout.data_log);
        data_holder = (LinearLayout)findViewById(R.id.data_logging);

        pref = getApplicationContext().getSharedPreferences("Desired_Sender_Address", MODE_PRIVATE);
        if(b!=null) {
            Toast.makeText(this, "Address: " + b.getString("sms_address") + "\n Message: " + b.getString("sms_body"), Toast.LENGTH_LONG).show();
            SMSData sms = new SMSData();
            senderAddress = b.getString("sms_address");

            desiredAddress = pref.getString("desired_address", "+919800644706");
            if (desiredAddress.equals(senderAddress)) {

                smsBody = b.getString("sms_body");

                smsBody = smsBody.replace("\n", "=");
                smsBodyParts = smsBody.split("=");
                date = b.getString("sms_date");
                if (smsBodyParts.length >= 12) {
                    smsBody = "VR: " + smsBodyParts[1] + "\n" + " VY: " + smsBodyParts[3] + "\n" + " VB: " + smsBodyParts[5] + "\n" + "CR: " + smsBodyParts[7] + "\n" + " CY: " + smsBodyParts[9] + "\n" + " CB: " + smsBodyParts[11] + " Time: " + date;
                    addViewStatic(smsBodyParts[1], smsBodyParts[3], smsBodyParts[5], smsBodyParts[7], smsBodyParts[9], smsBodyParts[11], date);
                }
            }
        }
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
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getApplicationContext().startActivity(activityIntent);
    }


    public void uiUpdate(){
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c= getContentResolver().query(uri, null, null ,null,null);

        if(c.moveToFirst()) {
            for(int i=0; i < c.getCount(); i++) {
                SMSData sms = new SMSData();

                senderAddress = c.getString(c.getColumnIndexOrThrow("address")).toString();
                if(desiredAddress.equals(senderAddress)) {

                    smsBody = c.getString(c.getColumnIndexOrThrow("body")).toString();

                    smsBody = smsBody.replace("\n","=");
                    smsBodyParts = smsBody.split("=");

                    date =  c.getString(c.getColumnIndexOrThrow("date")).toString();
                    if(smsBodyParts.length>=12) {
                        smsBody = "VR: " + smsBodyParts[1] + "\n" + " VY: " + smsBodyParts[3] + "\n" + " VB: " + smsBodyParts[5] + "\n" + "CR: " + smsBodyParts[7] + "\n" + " CY: " + smsBodyParts[9] + "\n" + " CB: " + smsBodyParts[11] + " Time: " + date;
                        addViewStatic(smsBodyParts[1], smsBodyParts[3], smsBodyParts[5], smsBodyParts[7], smsBodyParts[9], smsBodyParts[11], date);
                    }
                }
                c.moveToNext();
            }
        }
        c.close();
    }

    private void addViewStatic(final String vr, final String vy, final String vb, final String cr, final String cy, final String cb, final String time){
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.data_log_widget, null);

        TextView vr_log = (TextView)addView.findViewById(R.id.VoltageR_log);
        vr_log.setText(vr);

        TextView vy_log = (TextView)addView.findViewById(R.id.VoltageY_log);
        vy_log.setText(vy);

        TextView vb_log = (TextView)addView.findViewById(R.id.VoltageB_log);
        vb_log.setText(vb);

        TextView cr_log = (TextView)addView.findViewById(R.id.CurrentR_log);
        cr_log.setText(cr);

        TextView cy_log = (TextView)addView.findViewById(R.id.CurrentY_log);
        cy_log.setText(cy);

        TextView cb_log = (TextView)addView.findViewById(R.id.CurrentB_log);
        cb_log.setText(cb);

        TextView time_log = (TextView)addView.findViewById(R.id.Timestamp_log);
        time_log.setText(time);

        data_holder.addView(addView);
    }

}
