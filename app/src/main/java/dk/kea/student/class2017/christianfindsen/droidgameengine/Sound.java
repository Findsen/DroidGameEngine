package dk.kea.student.class2017.christianfindsen.droidgameengine;

import android.media.SoundPool;

/**
 * Created by Christian on 09-03-2017.
 */

public class Sound
{
    int soundId;
    SoundPool soundPool;

    public Sound(SoundPool soundPool, int soundId)
    {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }

    public void play(float volume)
    {
        soundPool.play(soundId,volume,volume,0,0,1);
    }

    public void dispose()
    {
        soundPool.unload(soundId);
    }
}
