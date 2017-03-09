package dk.kea.student.class2017.christianfindsen.droidgameengine;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.Externalizable;
import java.io.IOException;


/**
 * Created by Christian on 09-03-2017.
 */

public class Music implements MediaPlayer.OnCompletionListener
{
    private MediaPlayer mediaPlayer; // MediaPlayer is doing the audio playback for us
    private boolean isPrepare;       // Is the MediaPlayer prepared and usable ?

    public Music(AssetFileDescriptor assetFileDescriptor)
    {
        mediaPlayer = new MediaPlayer();
        try
        {
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(this);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not load music! *********");
        }
    }

    public void dispose()
    {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
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

    public boolean isStopped()
    {
        return !isPrepare;
    }

    public void pause()
    {
        mediaPlayer.pause();
//        if (mediaPlayer.isPlaying())
//            mediaPlayer.pause();
    }

    public void play()
    {
        if(mediaPlayer.isPlaying()) return;
        try
        {
            synchronized (this)
            {
                if(!isPrepare)
                    mediaPlayer.prepare();
                mediaPlayer.start();
            }

        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setLooping(boolean isLooping)
    {
        mediaPlayer.setLooping(isLooping);
    }

    public void setVolume(float volume)
    {
        mediaPlayer.setVolume(volume,volume);
    }

    public void stop()
    {
        synchronized (this)
        {
            if(!isPrepare) return;
            mediaPlayer.stop();
            isPrepare = false;
        }
    }


    @Override
    public void onCompletion(MediaPlayer mp)
    {
        synchronized (this)
        {
            isPrepare = false;
        }

    }
}
