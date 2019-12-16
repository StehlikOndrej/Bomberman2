package com.example.bomberman;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.core.view.GestureDetectorCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static android.content.Context.MODE_PRIVATE;

public class BombermanView extends View{

    Bitmap[] bmp;
    final Context context = getContext();

    private static final long DOUBLE_CLICK_TIME_DELTA = 200;//milliseconds
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;

    long lastClickTime = 0;
    long clickTime = 0;

    int lx = 10;
    int ly = 10;

    int width;
    int height;

    private int heroXSave = 7;
    private int heroYSave = 3;
    public int heroX = 7;
    public int heroY = 3;

    float xDown = 0.0f;
    float yDown = 0.0f;
    float xUp = 0.0f;
    float yUp = 0.0f;
    float xDiff,yDiff;
    float threshold = 100;


    BackgroundSounds mBackgroundSound = new BackgroundSounds(context);
    ArrayList<Bomb> bombs = new ArrayList<>();
    ArrayList<NPC> npcs = new ArrayList<>();
    ArrayList<Integer> explosions = new ArrayList<Integer>();
    Set<Integer> set;
    ArrayList<int[]> mapList = new ArrayList<>();


    int[] map = new int[100];
    int mapid = 0;


    public BombermanView(Context context) {
        super(context);
        init(context);
    }

    public BombermanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BombermanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mBackgroundSound.execute();
        bmp = new Bitmap[8];
        set = new LinkedHashSet<>(explosions);

        bmp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.tile);
        bmp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_bomberman);
        bmp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_bomberman_bomb);
        bmp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_enemy);
        bmp[4] = BitmapFactory.decodeResource(getResources(), R.drawable.bomb_tile);
        bmp[5] = BitmapFactory.decodeResource(getResources(), R.drawable.obstacle);
        bmp[6] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp[7] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_explosion);

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        heroY = prefs.getInt("heroY", 3);
        heroX = prefs.getInt("heroX", 7);
        map = loadMap("mapPrefrence",context);
        mapid = prefs.getInt("mapid",0);

        try {
            readMap("map.txt");
            if(map.length < 6){
                map = mapList.get(0).clone();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,"Mapa se nenacetla",Toast.LENGTH_LONG).show();
        }


        for(int i = 0; i < map.length; i++){
            if(map[i] == 3){

                int y = i / 10;
                int x = i % 10;

                NPC npc = new NPC(this,x,y);
                npc.start();
                npcs.add(npc);
            }
        }
    }

    public void move(int X, int Y){
        if(map[(heroX+X) + (heroY+Y) *10 ] == 0 || map[(heroX+X) + (heroY+Y) *10 ] == 4 || map[(heroX+X) + (heroY+Y) *10 ] == 3 ){
            map[heroX + heroY *10 ] = 0;
            heroX += X;
            heroY += Y;
            map[heroX + heroY *10 ] = 1;
            invalidate();
        }
    }

    public void dropBomb() {
        if (bombs.size() < 3) {
            Bomb bomb = new Bomb(this, heroX, heroY, context);
            bomb.start();
            bombs.add(bomb);
            invalidate();
        }
    }

    public void deleteBomb(Bomb bomb){
        map[bomb.getPosition()] = 0;

        bomb.interrupt();
        bombs.remove(bomb);
        invalidate();
    }

    public void setMapid(int id){
        mapid = (id);
    }

    public void restart(){
        heroX = heroXSave;
        heroY = heroYSave;

        for (int i = 0; i < bombs.size();i++){
            bombs.get(i).interrupt();
            bombs.remove(i);
        }

        for (int i = 0; i < npcs.size();i++){
            npcs.get(i).running = false;
            npcs.get(i).interrupt();
            npcs.remove(i);
        }
        npcs.clear();
        bombs.clear();

        map = mapList.get(mapid).clone();

        for(int i = 0; i < map.length; i++){
            if(map[i] == 3){

                int y = i / 10;
                int x = i % 10;

                NPC npc = new NPC(this,x,y);
                npc.start();
                npcs.add(npc);
            }
        }
        invalidate();
    }

    private void readMap (String map) throws IOException {
        int[] result = new int[100];
        int j = 0;
        String str;
        InputStream is = getContext().getAssets().open(map);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            if (is != null){
                while ((str = reader.readLine()) != null){
                    str = str.replace(",","");

                    if(str.contains("*")){
                        j = 0;
                        mapList.add(result);
                        System.out.println("nactena jedna mapa");
                        result = new int[100];

                    }else {
                        for (int i = 0; i < str.length(); i++) {
                            result[j] = Character.getNumericValue(str.charAt(i));
                            j++;
                        }
                    }
                }
            }
        }catch (Exception e){
            System.out.println("mapa se nenacetla");
            Toast.makeText(context,"Mapa se nenacetla",Toast.LENGTH_LONG).show();
        }
        return;
    }

    public boolean saveMap(int[] array, String arrayName, Context mContext) {
        editor.putInt(arrayName +"_size", array.length);

        for(int i=0;i<array.length;i++)
            editor.putInt(arrayName + "_" + i, array[i]);
        return editor.commit();
    }

    public int[] loadMap(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PREFS_NAME, 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        int array[] = new int[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getInt(arrayName + "_" + i, 0);
        return array;
    }

    public void toast(){
        Toast.makeText(context,"You have lost", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w / ly;
        height = h / lx;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                xDown = event.getX();
                yDown = event.getY();
                clickTime = System.currentTimeMillis();
                break;
            }

            case MotionEvent.ACTION_UP: {
                xUp = event.getX();
                yUp = event.getY();
                xDiff = xDown - xUp;
                yDiff = yDown - yUp;

                //doubleClickchecker
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                    //doubleclick
                    dropBomb();
                } else {
                    //singleclick
                }
                lastClickTime = clickTime;

                if (Math.abs(xUp - xDown) > threshold || Math.abs(yUp - yDown) > threshold){
                    if (Math.abs(yDiff) > Math.abs(xDiff)) {
                        if (yUp > yDown) {
                            move(0, 1);
                            return true;

                        } else {
                            move(0, -1);
                            return true;
                        }
                    } else {
                        if (xUp > xDown) {
                            move(1, 0);
                            return true;
                        } else {
                            move(-1, 0);
                            return true;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int iterator = 0;
        explosions.addAll(set);
        Collections.sort(explosions);

        for (int i = 0; i < lx; i++) {
            for (int j = 0; j < ly; j++) {

                for(int index = 0; index < npcs.size(); index++){
                    if((npcs.get(index).X + npcs.get(index).Y*10) == (heroX + heroY*10) ){
                        restart();
                    }
                }

                for(int b = 0; b < bombs.size(); b++){
                    if((i*10+j) == bombs.get(b).getPosition()){
                        if(map[i*10+j] != 1) map[i*10+j] = 4;
                        else map[i*10+j] = 2;
                    }
                }

                //draw of explosion or normal draw
                if( explosions.size() > 0 ){
                    if(explosions.get(iterator) == (i*10+j)){
                        canvas.drawBitmap(bmp[7], null,
                                new Rect(j*width, i*height,(j+1)*width, (i+1)*height), null);
                        if(iterator < explosions.size() - 1){
                            iterator++;} }

                    else {
                        canvas.drawBitmap(bmp[map[i*10+j]], null,
                                new Rect(j*width, i*height,(j+1)*width, (i+1)*height), null);
                    }
                }else{
                    canvas.drawBitmap(bmp[map[i*10+j]], null,
                            new Rect(j*width, i*height,(j+1)*width, (i+1)*height), null);
                }
            }
        }

        if(map[heroX+heroY*10]==4){
            canvas.drawBitmap(bmp[2], null,
                    new Rect(heroX*width, heroY*height,(heroX+1)*width, (heroY+1)*height), null);
        }

        if(npcs.size() < 1){
            if(mapid+1 < mapList.size()){
                mapid++;
            }
            else{
                mapid = 0;
            }
            restart();
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.levelup);
            mediaPlayer.setVolume(0.5f, 0.5f);
            mediaPlayer.start();
            Toast.makeText(context,"Moving to another level", Toast.LENGTH_LONG).show();
        }

        editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("heroX", heroX);
        editor.putInt("heroY", heroY);
        editor.putInt("mapid", mapid);
        saveMap(map,"mapPrefrence", context);

        explosions.clear();
        set.clear();
    }
}
