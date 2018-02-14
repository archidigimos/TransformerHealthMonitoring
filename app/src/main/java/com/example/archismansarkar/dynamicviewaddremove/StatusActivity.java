package com.example.archismansarkar.dynamicviewaddremove;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Archisman Sarkar on 2/9/2018.
 */

public class StatusActivity extends Activity {
    SharedPreferences pref;
    String[] tname;
    LinearLayout statusholder;
    int size = 0;
    Intent sms_intent=getIntent();
    Bundle b = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_page);


        try {
            b = sms_intent.getExtras();
        }
        catch(Exception E){

        }

        pref = getApplicationContext().getSharedPreferences("Transformer", MODE_PRIVATE);

        statusholder = (LinearLayout)findViewById(R.id.status_holder);

        tname = getFavoriteList();

        if(tname!=null) {
            size = tname.length;
            String[] arr;
            arr = new String[2];
            for (int i = 0; i < size; i++) {
                if(!new String(tname[i]).equals("")) {
                    arr = tname[i].split(":");
                    addViewStatic(arr[0], arr[1]);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent activityIntent=new Intent(getApplicationContext(),StartPageActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fadein, R.anim.fadeout);
        getApplicationContext().startActivity(activityIntent, options.toBundle());
    }

    private void addViewStatic(final String data1, final String data2){
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.status_widget, null);
        Button transformer = (Button)addView.findViewById(R.id.tf_);
        transformer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        TextView tid = (TextView)addView.findViewById(R.id.identifier_);
        tid.setText("Name: "+data1+" ID: "+data2);
        intent_reception(data2,tid);

        statusholder.addView(addView);
    }

    private void intent_reception(String transformer_id, TextView t){
        health_status_update_ui(transformer_id,t);
    }

    public void health_status_update_ui(String desiredAddress, TextView t){
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c= getContentResolver().query(uri, null, null ,null,null);
        String senderAddress, smsBody, date;
        String[] smsBodyParts;
        if(c.moveToFirst()) {
            for(int i=0; i < c.getCount(); i++) {
                senderAddress = c.getString(c.getColumnIndexOrThrow("address")).toString();
                if(desiredAddress.equals(senderAddress)) {

                    smsBody = c.getString(c.getColumnIndexOrThrow("body")).toString();

                    smsBody = smsBody.replace("\n","=");
                    smsBodyParts = smsBody.split("=");

                    if(smsBodyParts.length>=12) {
                        dataProcessAndSet(smsBodyParts[1], smsBodyParts[3], smsBodyParts[5], smsBodyParts[7], smsBodyParts[9], smsBodyParts[11], t);
                        break;
                    }
                }
                c.moveToNext();
            }
        }
        c.close();
    }

    public void dataProcessAndSet(String vr, String vy, String vb, String cr, String cy, String cb, TextView t){
        double vR, vY, vB, cR, cY, cB;
        vR = Double.parseDouble(vr);
        vY = Double.parseDouble(vy);
        vB = Double.parseDouble(vb);
        cR = Double.parseDouble(cr);
        cY = Double.parseDouble(cy);
        cB = Double.parseDouble(cb);

        double total = vR+ vY+ vB+ cR+ cY+ cB;
        double threshold = 800.0;

        t.setBackgroundColor(Color.RED);
        if(total>threshold){
            t.setBackgroundColor(Color.RED);
        }
        else{
            t.setBackgroundColor(Color.GREEN);
        }
    }

    private String[] getFavoriteList(){
        String favoriteList = getStringFromPreferences(null,"favorites");
        return convertStringToArray(favoriteList);
    }

    private String getStringFromPreferences(String defaultValue,String key){
        if(pref.contains(key)) {
            String temp = pref.getString(key, defaultValue);
            return temp;
        }
        else return null;
    }

    private String[] convertStringToArray(String str){
        if(str!=null) {
            String[] arr = str.split(",");
            return arr;
        }
        else return null;
    }
}
