package com.example.archismansarkar.dynamicviewaddremove;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_page);

        pref = getApplicationContext().getSharedPreferences("Transformer", MODE_PRIVATE);

        statusholder = (LinearLayout)findViewById(R.id.status_holder);

        tname = getFavoriteList(this);

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
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getApplicationContext().startActivity(activityIntent);
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

        statusholder.addView(addView);
    }

    private String[] getFavoriteList(Activity activity){
        String favoriteList = getStringFromPreferences(activity,null,"favorites");
        return convertStringToArray(favoriteList);
    }

    private String getStringFromPreferences(Activity activity,String defaultValue,String key){
        //SharedPreferences sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
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
