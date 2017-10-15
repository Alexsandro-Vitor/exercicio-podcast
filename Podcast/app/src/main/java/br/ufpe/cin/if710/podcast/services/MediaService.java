package br.ufpe.cin.if710.podcast.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Alexsandro on 15/10/2017.
 */

public class MediaService extends Service {
    private final String TAG = "MediaService";

    private MediaPlayer mPlayer;
    private int mStartID;
    private boolean prepared = false;   //Checa se o MediaPlayer já foi preparado

    @Override
    public void onCreate() {
        super.onCreate();

        // configurar media player
        //Nine Inch Nails Ghosts I-IV is licensed under a Creative Commons Attribution Non-Commercial Share Alike license.
        mPlayer = new MediaPlayer();

        //nao deixa entrar em loop
        mPlayer.setLooping(false);

        // encerrar o service quando terminar a musica
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // encerra se foi iniciado com o mesmo ID
                stopSelf(mStartID);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != mPlayer) {
            // ID para o comando de start especifico
            mStartID = startId;

            //Se ja esta tocando, pausa
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                //Pega a posição do áudio (ainda não sei onde usar isso)
                mPlayer.getCurrentPosition();
            } else {
                //Se não estiver preparado, prepara
                if (!prepared) {
                    try {
                        Log.d(TAG, intent.getData().getPath());
                        mPlayer.setDataSource(this, intent.getData());
                        mPlayer.prepare();
                        prepared = true;
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
                // inicia musica
                mPlayer.start();
            }
        }
        // nao reinicia service automaticamente se for eliminado
        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mPlayer) {
            mPlayer.stop();
            mPlayer.release();
        }

    }

    //nao eh possivel fazer binding com este service
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
        //return null;
    }
}