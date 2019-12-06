package com.example.bomberman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    public BombermanView view;
    int choice;
    ArrayList<String> list = new ArrayList<>();
    private static final int MENU_FIRST = Menu.FIRST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.bombermanView);

        Bundle extras = getIntent().getExtras();
        list = extras.getStringArrayList("list");
        choice = extras.getInt("choice");
        if(choice > 0){
            setMap(choice-1);
        }
    }

    public void setMap(int id){
        BombermanView bw = findViewById(R.id.bombermanView);
        bw.setMapid(id);
        bw.restart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        list.remove(0);
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.game_menu, menu);
        menu.add(0,0,0,"Restart");
        int index = 1;
        for (String item: list){
            menu.add(0,index,0,item);
            index++;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        super.onOptionsItemSelected(item);

        BombermanView bw = findViewById(R.id.bombermanView);
        System.out.println(item.getItemId());

        switch (item.getItemId()){
            case 0:
                bw.restart();
                break;
            default:
                bw.setMapid(item.getItemId()-1);
                bw.restart();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
