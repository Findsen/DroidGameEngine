package dk.kea.student.class2017.christianfindsen.droidgameengine;

/**
 * Created by Christian Findsen on 04-03-2017.
 */

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class SimpleScreen extends Screen
{
    float x = 0;
    float y = 0;
    Bitmap bitmap;
    int clearColor = Color.BLUE;
    Sound sound;
    Music music;


    //construktor
    public SimpleScreen(GameEngine game)
    {
        super(game);
        bitmap = game.loadBitmap("bob.png");
        sound = game.loadSound("blocksplosion.wav");
        music = game.loadMusic("music.ogg");
        music.setLooping(true); // never ends
        music.play();
    }

    @Override
    public void update(float deltaTime)
    {
        game.clearFrameBuffer(clearColor);

        for (int pointer = 0; pointer < 5; pointer++)
        {
            if (game.isTouchDown(pointer))
            {
                Log.d("SimpleScreen: ", " Yessssssss, we have a touch down!!!!!" );
                game.drawBitmap(bitmap,game.getTouchX(pointer),game.getTouchY(pointer));
                //sound.play(1); // goes from 0-1 (1 is max)
                if (music.isPlaying())
                {
                    music.pause();
                }
                else
                {
                    music.play();
                }
            }
        }

        /*
        float x = -game.getAccelerometer()[0];
        float y = game.getAccelerometer()[1];
        float accKonstant = 10;
        x = (x/accKonstant) * game.getFrameBufferWidth()/2 + game.getFrameBufferWidth()/2; //if no input, bob starts in the middel when phone is laying down at table
        y = (y/accKonstant) * game.getFrameBufferHeight()/2 + game.getFrameBufferHeight()/2;
        game.drawBitmap(bitmap,(int)(x-(float)bitmap.getWidth()/2), (int) (y-(float)bitmap.getHeight()/2));
        */

        //movement over the screen
        x = x + 150 * deltaTime; //150 = speed, deltatime = hardware
        if (x >320) x = 0;
        game.drawBitmap(bitmap,(int)x,50);

    }

    @Override
    public void pause()
    {
        music.pause();
    }

    @Override
    public void resume()
    {
        if(!music.isPlaying()) music.play();
    }

    @Override
    public void dispose()
    {
        music.dispose();
    }
}