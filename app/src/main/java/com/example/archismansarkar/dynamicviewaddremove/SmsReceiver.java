package com.example.archismansarkar.dynamicviewaddremove;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by Archisman Sarkar on 9/8/2017.
 */

public class SmsReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Get Bundle object contained in the SMS intent passed in
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm = null;
        String sms_address ="";
        String sms_body ="";
        String sms_date = "";
        if (bundle != null)
        {
            // Get the SMS message
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsm = new SmsMessage[pdus.length];
            for (int i=0; i<smsm.length; i++){
                smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                sms_address = smsm[i].getOriginatingAddress().toString();
                sms_body += smsm[i].getMessageBody().toString();
                //sms_body+= "\n";
                sms_date = String.valueOf(smsm[i].getTimestampMillis());
            }

            //Toast.makeText(context, "Message: "+ sms_str, Toast.LENGTH_LONG).show();

            // Start Application's  MainActivty activity
            Intent smsIntent=new Intent(context,MainActivity.class);
            smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            smsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            smsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            smsIntent.putExtra("sms_address", sms_address);
            smsIntent.putExtra("sms_body", sms_body);
            smsIntent.putExtra("sms_date", sms_date);
            context.startActivity(smsIntent);
        }
    }
}
