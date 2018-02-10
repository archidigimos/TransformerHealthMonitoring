package com.example.archismansarkar.dynamicviewaddremove;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    EditText nameTF;
    EditText numberTF;
    Button buttonAdd;
    LinearLayout container;
    String[] tname;
    int size = 0;
    private Boolean exit = false;

    SharedPreferences sharedpreferences;
    SharedPreferences pref;
    String desired_number;

    private static final String[] ALL_PERMISSIONS = {
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS
    };

    private static final int SMS_READ_RECEIVE_REQUEST_CODE = 101;
    private static final int ALL_REQUEST_CODE = 0;
    private AppPermissions mRuntimePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRuntimePermission = new AppPermissions(this);
        if (!mRuntimePermission.hasPermission(ALL_PERMISSIONS)) {
            mRuntimePermission.requestPermission(this, ALL_PERMISSIONS, ALL_REQUEST_CODE);
        }

        nameTF = (EditText)findViewById(R.id.tfname);
        numberTF = (EditText)findViewById(R.id.tfnumber);
        buttonAdd = (Button)findViewById(R.id.add);
        container = (LinearLayout)findViewById(R.id.container);

        sharedpreferences = getApplicationContext().getSharedPreferences("Transformer", MODE_PRIVATE);

        pref = getApplicationContext().getSharedPreferences("Desired_Sender_Address", MODE_PRIVATE);
        desired_number = pref.getString("desired_address", null);

        buttonAdd.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {
                addViewDynamic();
            }});

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
        final View addView = layoutInflater.inflate(R.layout.field, null);
        Button transformer = (Button)addView.findViewById(R.id.tf);
        transformer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRuntimePermission.hasPermission(ALL_PERMISSIONS)) {
                    SharedPreferences.Editor idnumber = pref.edit();
                    idnumber.putString("desired_address", data2);
                    idnumber.commit();

                    Intent activityIntent=new Intent(getApplicationContext(),DataDisplayActivity.class);
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getApplicationContext().startActivity(activityIntent);
                } else {
                    mRuntimePermission.requestPermission(MainActivity.this, ALL_PERMISSIONS, ALL_REQUEST_CODE);
                }
            }
        });
        TextView tid = (TextView)addView.findViewById(R.id.identifier);
        tid.setText("Name: "+data1+" ID: "+data2);
        Button buttonRemove = (Button)addView.findViewById(R.id.remove);
        buttonRemove.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                removeFavoriteItem(MainActivity.this, (data1+":"+data2));
                ((LinearLayout)addView.getParent()).removeView(addView);
            }});

        container.addView(addView);
    }

    private void addViewDynamic(){
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.field, null);
        Button transformer = (Button)addView.findViewById(R.id.tf);

        final String data1 = nameTF.getText().toString();
        final String data2 = numberTF.getText().toString();
        TextView tid = (TextView)addView.findViewById(R.id.identifier);
        tid.setText("Name: "+data1+" ID: "+data2);

        transformer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRuntimePermission.hasPermission(ALL_PERMISSIONS)) {
                    SharedPreferences.Editor idnumber = pref.edit();
                    idnumber.putString("desired_address", data2);
                    idnumber.commit();

                    Intent activityIntent=new Intent(getApplicationContext(),DataDisplayActivity.class);
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getApplicationContext().startActivity(activityIntent);
                } else {
                    mRuntimePermission.requestPermission(MainActivity.this, ALL_PERMISSIONS, ALL_REQUEST_CODE);
                }
            }
        });

        addFavoriteItem(MainActivity.this, (data1+":"+data2));
        Button buttonRemove = (Button)addView.findViewById(R.id.remove);
        buttonRemove.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                removeFavoriteItem(MainActivity.this, (data1+":"+data2));
                ((LinearLayout)addView.getParent()).removeView(addView);
            }});

        container.addView(addView);
    }

    private boolean addFavoriteItem(Activity activity, String favoriteItem){
        //Get previous favorite items
        String favoriteList = getStringFromPreferences(activity,null,"favorites");
        // Append new Favorite item
        if(favoriteList!=null){
            favoriteList = favoriteList+","+favoriteItem;
        }else{
            favoriteList = favoriteItem;
        }
        // Save in Shared Preferences
        return putStringInPreferences(activity,favoriteList,"favorites");
    }

    private boolean removeFavoriteItem(Activity activity, String favoriteItem){
        //Get previous favorite items
        String favoriteList = getStringFromPreferences(activity,null,"favorites");
        String newConcat = new String();
        // Append new Favorite item
        if(favoriteList!=null){
            String[] parsed = favoriteList.split(",");
            int size = parsed.length;

            for(int i = 0; i<size; i++){
                if((new String(parsed[i]).equals(favoriteItem)==false)&&(new String(parsed[i]).equals("")==false)){
                    if(new String(newConcat).equals("")==true) newConcat = parsed[i];
                    else newConcat = newConcat + "," + parsed[i];
                }
            }
        }
        // Save in Shared Preferences
        return putStringInPreferences(activity,newConcat,"favorites");
    }

    private String[] getFavoriteList(Activity activity){
        String favoriteList = getStringFromPreferences(activity,null,"favorites");
        return convertStringToArray(favoriteList);
    }
    private boolean putStringInPreferences(Activity activity,String nick,String key){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, nick);
        editor.commit();
        return true;
    }

    private String getStringFromPreferences(Activity activity,String defaultValue,String key){
        if(sharedpreferences.contains(key)) {
            String temp = sharedpreferences.getString(key, defaultValue);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ALL_REQUEST_CODE:
                List<Integer> permissionResults = new ArrayList<>();
                for (int grantResult : grantResults) {
                    permissionResults.add(grantResult);
                }
                if (permissionResults.contains(PackageManager.PERMISSION_DENIED)) {
                    Toast.makeText(this, "All Permissions not granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "All Permissions granted", Toast.LENGTH_SHORT).show();
                }
                break;
            case SMS_READ_RECEIVE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "SMS Read and Receive Permissions not granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "SMS Read and Receive Permissions granted", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
