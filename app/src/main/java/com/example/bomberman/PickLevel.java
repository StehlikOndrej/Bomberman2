package com.example.bomberman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PickLevel extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};
    String[] listArray;
    ArrayList<String> list = new ArrayList<>();
    int numberOfMaps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_level);

        try {
            readMap("map.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, list);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id){
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("choice",position);
        intent.putExtra("list",list);
        startActivity(intent);
    }

    private void readMap (String map) throws IOException {
        String str;
        InputStream is = getAssets().open(map);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            if (is != null){
                while ((str = reader.readLine()) != null){
                    if(str.contains("*")){
                        numberOfMaps++;
                    }
                }
            }
        }catch (Exception e){
            System.out.println("nenacetly se mapy");
        }

        list.add("Continue");

        for(int i = 0; i < numberOfMaps;i++){
            list.add("Mapa " + (i+1));
        }
        return;
    }
}
