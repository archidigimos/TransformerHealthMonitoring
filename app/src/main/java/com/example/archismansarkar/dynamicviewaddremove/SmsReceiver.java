package com.example.archismansarkar.dynamicviewaddremove;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by Archisman Sarkar on 9/8/2017.
 */

public class SmsReceiver extends BroadcastReceiver
{
    SharedPreferences health_status;
    String[] contact_list;
    int size = 0;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Get Bundle object contained in the SMS intent passed in

        health_status = context.getSharedPreferences("Transformer", Context.MODE_PRIVATE);

        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm = null;
        String sms_address ="";
        String sms_body ="";
        String smsBodyParts[];

        contact_list = getFavoriteList();

        if (bundle != null)
        {
            // Get the SMS message
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsm = new SmsMessage[pdus.length];
            for (int i=0; i<smsm.length; i++){
                smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                sms_address = smsm[i].getOriginatingAddress().toString();
                sms_body += smsm[i].getMessageBody().toString();
            }

            sms_body = sms_body.replace("\n", "=");
            smsBodyParts = sms_body.split("=");

            if(contact_list!=null) {
                size = contact_list.length;
                String[] arr;
                arr = new String[2];
                for (int i = 0; i < size; i++) {
                    if(!new String(contact_list[i]).equals("")) {
                        arr = contact_list[i].split(":");
                        if (new String(arr[1]).equals(sms_address)) {
                            if (smsBodyParts.length >= 12) {
                                dataProcessAndSend(context,smsBodyParts[1], smsBodyParts[3], smsBodyParts[5], smsBodyParts[7], smsBodyParts[9], smsBodyParts[11], sms_address);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void dataProcessAndSend(Context context, String vr, String vy, String vb, String cr, String cy, String cb, String tag){
        double vR, vY, vB, cR, cY, cB;
        vR = Double.parseDouble(vr);
        vY = Double.parseDouble(vy);
        vB = Double.parseDouble(vb);
        cR = Double.parseDouble(cr);
        cY = Double.parseDouble(cy);
        cB = Double.parseDouble(cb);

        double total = vR+ vY+ vB+ cR+ cY+ cB;
        double threshold = 800.0;

        if(total>threshold){
            Intent smsIntent=new Intent(context,StatusActivity.class);
            smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            smsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            smsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(smsIntent);
        }
    }

    private String[] getFavoriteList(){
        String favoriteList = getStringFromPreferences(null,"favorites");
        return convertStringToArray(favoriteList);
    }

    private String getStringFromPreferences(String defaultValue,String key){
        if(health_status.contains(key)) {
            String temp = health_status.getString(key, defaultValue);
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
