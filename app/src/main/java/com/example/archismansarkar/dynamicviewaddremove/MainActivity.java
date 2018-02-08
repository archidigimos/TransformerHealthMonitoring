package com.example.archismansarkar.dynamicviewaddremove;

import android.content.SharedPreferences;
import android.os.Bundle;
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


public class MainActivity extends Activity {

    EditText nameTF;
    EditText numberTF;
    Button buttonAdd;
    LinearLayout container;
    String[] tname;
    int size = 0;
    Boolean shouldAllowBack = false;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameTF = (EditText)findViewById(R.id.tfname);
        numberTF = (EditText)findViewById(R.id.tfnumber);
        buttonAdd = (Button)findViewById(R.id.add);
        container = (LinearLayout)findViewById(R.id.container);

        sharedpreferences = getSharedPreferences("Transformer", Context.MODE_PRIVATE);

        buttonAdd.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {
                addViewDynamic();
            }});

        tname = getFavoriteList(this);
        String[] arr = new String[2];
        if(tname!=null) {
            size = tname.length;
            for (int i = 0; i < size; i++) {
                arr = tname[i].split("-");
                addViewStatic(arr[0], arr[1]);
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (shouldAllowBack==false) {
            Toast.makeText(this, "Back press exit not allowed!", Toast.LENGTH_LONG).show();
        } else {
            super.onBackPressed();
        }
    }

    public void addViewStatic(final String data1, final String data2){
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.field, null);
        Button transformer = (Button)addView.findViewById(R.id.tf);
        TextView tid = (TextView)addView.findViewById(R.id.identifier);
        tid.setText("Name: "+data1+" ID: "+data2);
        Button buttonRemove = (Button)addView.findViewById(R.id.remove);
        buttonRemove.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                //removeStringInPreferences(MainActivity.this,data1);
                removeFavoriteItem(MainActivity.this, (data1+"-"+data2));
                ((LinearLayout)addView.getParent()).removeView(addView);
            }});

        container.addView(addView);
    }

    public void addViewDynamic(){
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.field, null);
        Button transformer = (Button)addView.findViewById(R.id.tf);
        final String data1 = nameTF.getText().toString();
        final String data2 = numberTF.getText().toString();
        TextView tid = (TextView)addView.findViewById(R.id.identifier);
        tid.setText("Name: "+data1+" ID: "+data2);

        addFavoriteItem(MainActivity.this, (data1+"-"+data2));
        //putStringInPreferences(this,data2,data1);
        Button buttonRemove = (Button)addView.findViewById(R.id.remove);
        buttonRemove.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                //removeStringInPreferences(MainActivity.this,data1);
                removeFavoriteItem(MainActivity.this, (data1+"-"+data2));
                ((LinearLayout)addView.getParent()).removeView(addView);
            }});

        container.addView(addView);
    }

    public boolean addFavoriteItem(Activity activity, String favoriteItem){
        //Get previous favorite items
        //SharedPreferences.Editor editor = sharedpreferences.edit();
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

    public boolean removeFavoriteItem(Activity activity, String favoriteItem){
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

    public String[] getFavoriteList(Activity activity){
        String favoriteList = getStringFromPreferences(activity,null,"favorites");
        return convertStringToArray(favoriteList);
    }
    private boolean putStringInPreferences(Activity activity,String nick,String key){
        SharedPreferences sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, nick);
        //editor.apply();
        editor.commit();
        return true;
    }
    private boolean removeStringInPreferences(Activity activity,String key){
        SharedPreferences sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        //editor.apply();
        editor.commit();
        return true;
    }
    private String getStringFromPreferences(Activity activity,String defaultValue,String key){
        SharedPreferences sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
        if(sharedPreferences.contains(key)) {
            String temp = sharedPreferences.getString(key, defaultValue);
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
