package com.example.bomberman;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;

public class Bomb extends Thread {
    public int X;
    public int Y;
    public Context context;

    final int explosionTime = 2000;

    BombermanView view;

    Bomb(BombermanView view, int x, int y, Context context){
        this.view = view;
        this.X = x;
        this.Y = y;
        this.context = context;
    }

    public int getPosition(){
        return (X + Y *10 );
    }

    public void run() {
        try {
            Thread.sleep(explosionTime);
            view.deleteBomb(this);
            explode();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (Thread.interrupted()) {
            return;
        }

    }

    public void explode(){

        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.explosionwaw);
        mediaPlayer.start();

        for (int i = 0; i < 2; i++){
            if(view.map[X+i + Y*10] < view.map.length && view.map[X+i + Y*10] < 6){
                view.map[X+i + Y*10] = 0;
                view.set.add(X+i + Y*10);}

            if(view.map[X + (Y+i)*10] < view.map.length && view.map[X + (Y+i)*10] < 6){
                view.map[X + (Y+i)*10] = 0;
                view.set.add(X + (Y+i)*10);}

            if(view.map[X-i + Y*10] > 0 && view.map[X-i + Y*10] < 6){
                view.map[X-i + Y*10] = 0;
                view.set.add(X-i + Y*10);}

            if(view.map[X + (Y-i)*10] > 0 && view.map[X + (Y-i)*10] < 6){
                view.map[X + (Y-i)*10] = 0 ;
                view.set.add(X + (Y-i)*10);}
        }

        for (int i = 0; i < view.npcs.size();i++){
            int x = view.npcs.get(i).X;
            int y = view.npcs.get(i).Y;

            if(view.map[x+y*10] == 0){
                view.npcs.get(i).interrupt();
                view.npcs.remove(i);
            }
        }



        if(view.map[view.heroX + view.heroY*10] > 2 || view.map[view.heroX + view.heroY*10] == 0 ){
            view.restart();
            Toast.makeText(context,"You have lost", Toast.LENGTH_LONG).show();
        }

        this.interrupt();
    }
}
