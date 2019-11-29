package com.example.bomberman;

public class Bomb extends Thread {
    public int X;
    public int Y;

    BombermanView view;

    Bomb(BombermanView view, int x, int y){
        this.view = view;
        this.X = x;
        this.Y = y;
    }

    public int getPosition(){
        return (X + Y *10 );
    }

    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        view.deleteBomb(this);
        explode();
        if (Thread.interrupted()) {
            // We've been interrupted: no more crunching.
            return;
        }

    }

    public void explode(){
        for (int i = 0; i < 2; i++){
            if(view.map[X+i + Y*10] < view.map.length && view.map[X+i + Y*10] < 6)
                view.map[X+i + Y*10] = 0;

            if(view.map[X + (Y+i)*10] < view.map.length && view.map[X + (Y+i)*10] < 6)
                view.map[X + (Y+i)*10] = 0;

            if(view.map[X-i + Y*10] > 0 && view.map[X-i + Y*10] < 6)
                view.map[X-i + Y*10] = 0;

            if(view.map[X + (Y-i)*10] > 0 && view.map[X + (Y-i)*10] < 6)
                view.map[X + (Y-i)*10] = 0 ;
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
        }

    }
}
