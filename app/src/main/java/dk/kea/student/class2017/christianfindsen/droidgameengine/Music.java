package dk.kea.student.class2017.christianfindsen.droidgameengine;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;


import java.io.IOException;


/**
 * Created by Christian on 09-03-2017.
 */


public class Music implements MediaPlayer.OnCompletionListener
{
    private MediaPlayer mediaPlayer; //MediaPlayer is doing the audio playback
    private boolean isPrepared;     //is the MediaPlayer prepared and usable?

    //construktor
    public Music(AssetFileDescriptor assetFileDescriptor)
    {
        mediaPlayer = new MediaPlayer();
        try
        {
            //get file
            //from that file tell me where to start
            //and long
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);

        }
        catch (Exception e)
        {
            throw new RuntimeException(" Could not load music! ****************************");
        }
    }

    public void dispose()
    {
        if(mediaPlayer.isPlaying()) mediaPlayer.stop();
        mediaPlayer.release();
    }

    public boolean isLooping()
    {
        return mediaPlayer.isLooping();
    }

    public boolean isPlaying()
    {
        return mediaPlayer.isPlaying();
    }

    // if not playing, its stopped
    public boolean isStopped()
    {
        return !isPrepared;
    }

    public void pause()
    {
        mediaPlayer.pause();
        //if(mediaPlayer.isPlaying()) mediaPlayer.pause(); //- maybe nescessary?
    }

    public void play()
    {
        if (mediaPlayer.isPlaying()) return;
        try
        {
            synchronized (this)
            {
                if(!isPrepared) mediaPlayer.prepare();
                mediaPlayer.start();
            }
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }

    public void setLooping(boolean isLooping)
    {
        mediaPlayer.setLooping(isLooping);
    }

    public void setVolume(float volume)
    {
        mediaPlayer.setVolume(volume, volume); //works like mono
    }

    public void stop()
    {
        synchronized (this)
        {
            if(!isPrepared) return;
            mediaPlayer.stop();
            isPrepared = false;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        synchronized (this)
        {
            isPrepared = false;
        }
    }
}