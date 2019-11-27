package com.example.bomberman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class BombermanView extends View {

    Bitmap[] bmp;
    final Context context = getContext();

    int lx = 10;
    int ly = 10;

    int width;
    int height;

    int heroX = 7;
    int heroY = 3;

    float xDown = 0.0f;
    float yDown = 0.0f;
    float xUp = 0.0f;
    float yUp = 0.0f;
    float xDiff,yDiff;

    public static int map[] = {
            1,1,1,1,1,1,1,1,1,1,
            1,0,0,0,0,0,0,0,1,0,
            1,0,2,0,0,2,1,0,1,0,
            1,0,1,0,2,0,2,3,1,0,
            1,0,2,0,0,2,2,0,1,0,
            1,0,1,0,2,2,2,0,1,0,
            1,0,2,0,0,2,1,0,1,0,
            1,0,0,0,0,0,0,0,1,0,
            1,1,1,1,1,1,1,1,1,0,
            0,0,0,0,0,0,0,0,0,0
    };



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
        bmp = new Bitmap[7];

        bmp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.tile);
        bmp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.obstacle);
        bmp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_bomberman);
        bmp[4] = BitmapFactory.decodeResource(getResources(), R.drawable.bomb_tile);
        bmp[5] = BitmapFactory.decodeResource(getResources(), R.drawable.enemy);
        bmp[6] = BitmapFactory.decodeResource(getResources(), R.drawable.tile_bomberman_bomb);

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
                break;
            }

            case MotionEvent.ACTION_UP:{
                xUp = event.getX();
                yUp = event.getY();
                xDiff = xDown - xUp;
                yDiff = yDown - yUp;

                if(Math.abs(yDiff) > Math.abs(xDiff)){
                    if(yUp>yDown){

                        map[heroX + heroY *10 ] = 0;
                        heroY++;
                        map[heroX + heroY *10 ] = 3;
                        invalidate();
                        return true;

                    }else {
                        map[heroX + heroY *10 ] = 0;
                        heroY--;
                        map[heroX + heroY *10 ] = 3;
                        invalidate();
                        return true;
                    }
                }else {
                    if(xUp>xDown){
                        map[heroX + heroY *10 ] = 0;
                        heroX++;
                        map[heroX + heroY *10 ] = 3;
                        invalidate();
                        return true;

                    }
                    else {
                        map[heroX + heroY *10 ] = 0;
                        heroX--;
                        map[heroX + heroY *10 ] = 3;
                        invalidate();
                        return true;
                    }
                }
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int index;
        int position = 0;
        for (int i = 0; i < lx; i++) {
            for (int j = 0; j < ly; j++) {

                canvas.drawBitmap(bmp[map[i*10+j]], null,
                        new Rect(j*width, i*height,(j+1)*width, (i+1)*height), null);
            }
        }

    }
}
