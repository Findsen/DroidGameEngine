package dk.kea.student.class2017.christianfindsen.droidgameengine;

/**
 * Created by Christian Findsen on 04-03-2017.
 */

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;


public class SimpleScreen extends Screen
{
    int x = 0;
    int y = 0;
    Bitmap bitmap;
    int clearColor = Color.BLUE;
    Sound sound;
    Music music;



    //construktor
    public SimpleScreen(GameEngine game)
    {
        super(game);
        bitmap = game.loaderBitmap("bob.png");
        sound = game.loadSound("blocksplosion.wav");
        music = game.loadMusic("music.ogg");
        music.setLooping(true);
        music.play();

    }

    @Override
    public void update(float deltaTime)
    {
        Log.d("SimpleScreen","******* fps: " + game.getFrameRate());
        game.clearFrameBuffer(clearColor);

        for (int pointer = 0; pointer < 5; pointer++)
        {
            if (game.isTouchDown(pointer))
            {
                Log.d("SimpleScreen: ", " Yessssssss, we have a touch down!!!!!" );
                game.drawBitmap(bitmap,game.getTouchX(pointer),game.getTouchY(pointer));
//                sound.play(1);
                if(music.isPlaying())
                {
                    music.pause();
                }
                else
                {
                    music.play();
                }
            }

        }

        float x = -game.getAccelerometer()[0];
        float y = game.getAccelerometer()[1];
        float accKonstant = 10;
        x = (x/accKonstant) * game.getFrameBufferWidth()/2 + game.getFrameBufferWidth()/2; //if no input, bob starts in the middel
        y = (y/accKonstant) * game.getFrameBufferHeight()/2 + game.getFrameBufferHeight()/2;

        game.drawBitmap(bitmap,(int)(x-(float)bitmap.getWidth()/2), (int) (y-(float)bitmap.getHeight()/2));

    }

    @Override
    public void pause()
    {
        music.pause();
    }

    @Override
    public void resume()
    {
        if (!music.isPlaying())
            music.play();
    }

    @Override
    public void dispose()
    {
        music.dispose();
    }
}