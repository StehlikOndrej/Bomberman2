package com.example.bomberman;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

public class BackgroundSounds extends AsyncTask<Void, Void, Void> {

    public Context context;
    BackgroundSounds(Context context){

        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        MediaPlayer player = MediaPlayer.create(context, R.raw.background);
        player.setLooping(true); // Set looping
        player.setVolume(0.3f, 0.3f);
        player.start();
        return null;
    }
}
