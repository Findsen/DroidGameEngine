package dk.kea.student.class2017.christianfindsen.droidgameengine.Breakout;

import android.graphics.Bitmap;

import dk.kea.student.class2017.christianfindsen.droidgameengine.GameEngine;
import dk.kea.student.class2017.christianfindsen.droidgameengine.Music;
import dk.kea.student.class2017.christianfindsen.droidgameengine.Screen;

/**
 * Created by Christian Findsen on 20-03-2017.
 */

public class MainMenuScreen extends Screen
{

    Bitmap mainMenuBackground;
    Bitmap insertCoin;
    Music music;
    long startTime = System.nanoTime();
    float passedTime = 0;

    //Contruktor
    public MainMenuScreen(GameEngine game)
    {
        //load picture and music
        super(game);
        mainMenuBackground = game.loadBitmap("mainmenu.png");
        insertCoin = game.loadBitmap("insertcoin.png");
        music = game.loadMusic("music.ogg");
        music.setLooping(true);
        music.play();
    }

    //game logic fits here!
    @Override
    public void update(float deltaTime)
    {
        if (game.isTouchDown(0))
        {
            game.setScreen(new GameScreen(game)); // create a new GameScreen with the game object and then go to it
            return;
        }

        game.drawBitmap(mainMenuBackground, 0, 0);

        // make the insertcoin to blink on the screen
        passedTime += deltaTime;
        if((passedTime - (int) passedTime) > 0.5f)
        {
            game.drawBitmap(insertCoin, 160 - insertCoin.getWidth()/2, 320);
        }

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {

    }
}