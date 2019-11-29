package com.example.bomberman;

import java.util.Random;

public class NPC extends Thread {
    int X;
    int Y;
    int direction;
    int cycle = 0;
    BombermanView view;
    boolean moving = false;
    public boolean running = true;
    int destroy = 0;

    NPC(BombermanView view, int x, int y){
        this.view = view;
        this.X = x;
        this.Y = y;
    }

    public void run() {
        move();
        if (Thread.interrupted()) {
            return;
        }
    }


    public boolean positionCheck(int n){
        switch (n) {
            case 0: {
                if (view.map[X + 1 + Y * 10] < 3 && X + 1 + Y * 10 < view.map.length) {
                    moving = true;
                    direction = 1;
                    cycle = 4;
                }
                return true;
            }
            case 1: {
                if (view.map[X- 1 + Y * 10] < 3 && X- 1 + Y * 10 > 0) {
                    moving = true;
                    direction = -1;
                    cycle = 4;
                }
                return true;
            }
            case 2: {
                if (view.map[X + (Y + 1) * 10] < 3 && X + (Y + 1) * 10 < view.map.length) {
                    moving = true;
                    direction = 10;
                    cycle = 4;
                }
                return true;

            }
            case 3: {
                if (view.map[X + (Y - 1) * 10] < 3 && X + (Y - 1) * 10 > 0) {
                    moving = true;
                    direction = -10;
                    cycle = 4;
                }
                return true;
            }
        }
        return false;
    }

    private void straightMoving(){
        switch (direction) {
            case 1: {
                if(view.map[X + 1 + Y * 10] < 3 && X + 1 + Y * 10 < view.map.length){
                    view.map[X + Y * 10] = 0;
                    X++;
                    view.map[X + Y * 10] = 3;}
                else {
                    moving = false;
                    direction = 0;
                }
                break;
            }
            case -1: {
                if(view.map[X - 1 + Y * 10] < 3 && X - 1 + Y * 10 > 0){
                    view.map[X + Y * 10] = 0;
                    X--;
                    view.map[X + Y * 10] = 3;}
                else {
                    moving = false;
                    direction = 0;
                }
                break;
            }
            case 10: {
                if(view.map[X  + (Y+1) * 10] < 3 && (X + (Y + 1) * 10) < view.map.length){
                    view.map[X + Y * 10] = 0;
                    Y++;
                    view.map[X + Y * 10] = 3;}
                else {
                    moving = false;
                    direction = 0;
                }
                break;
            }
            case -10: {
                if(view.map[X  + (Y - 1) * 10] < 3 && (X + (Y - 1) * 10) > 0){
                    view.map[X + Y * 10] = 0;
                    Y--;
                    view.map[X + Y * 10] = 3;}
                else {
                    moving = false;
                    direction = 0;
                }
                break;
            }
        }
        view.invalidate();
    }

    private void move(){
        while (running) {
            try {
                if((X+Y*10) == (view.heroX + view.heroY*10) ){
                    view.restart();
                }
                if (moving == false) {
                    Random rand = new Random();
                    int n = rand.nextInt(5);
                    cycle = 0;
                    while (cycle < 4){
                        positionCheck(n);
                        if(n < 4){
                            n++;
                        }else {
                            n=0;
                        }
                        cycle++;
                    }

                } else {
                    Thread.sleep(5000);
                    straightMoving();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /*System.out.println((X+Y*10) + "+" + (view.heroX + view.heroY*10));
            if((X+Y*10) == (view.heroX + view.heroY*10) ){
                view.restart();
            }*/

            //bug fix, some NPCs remained active after restart
            destroy = 0;
            for(int i = 0; i < view.npcs.size(); i++){
                if (view.npcs.get(i) == this){
                    destroy = 1;
                }
            }
            if (destroy == 0){
                running = false;
                view.map[X+Y*10]=0;
                this.interrupt();
            }

        }
    }
}
