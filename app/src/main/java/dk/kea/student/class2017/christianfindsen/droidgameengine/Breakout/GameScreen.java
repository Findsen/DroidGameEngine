package dk.kea.student.class2017.christianfindsen.droidgameengine.Breakout;

import android.graphics.Bitmap;

import dk.kea.student.class2017.christianfindsen.droidgameengine.Screen;
import dk.kea.student.class2017.christianfindsen.droidgameengine.GameEngine;

/**
 * Created by Christian Findsen on 20-03-2017.
 */

public class GameScreen extends Screen
{
    enum  State
    {
        Paused, Running, GameOver
    }

    State state = State.Paused;
    Bitmap background;
    Bitmap resume;
    Bitmap gameOver;
    World world;
    WorldRenderer renderer;


    //contruktor
    public GameScreen(GameEngine game)
    {
        super(game);
        background = game.loadBitmap("background.png");
        resume = game.loadBitmap("resume.png");
        gameOver = game.loadBitmap("gameover.png");
        world = new World();
        renderer = new WorldRenderer(game, world);
    }

    @Override
    public void update(float deltaTime)
    {
        //if the game is paused and has a touch, we change the state to running
        if(state ==  State.Paused && game.isTouchDown(0))
        {
            state = State.Running;
        }

        //if game over, go back to the mainMenu
        if (state ==State.GameOver && game.isTouchDown(0))
        {
            game.setScreen(new MainMenuScreen(game)); //set the screen to mainMenu
            return;
        }

        //if the game is running and has a touch down at the corner
        if (state == State.Running && game.isTouchDown(0) && game.getTouchX(0) > 320 -40 && game.getTouchY(0) < 40)
        {
            //call the pause method
            pause();
        }

        game.drawBitmap(background, 0, 0); //setting the background to the screen

        if(state == State.Running)
        {
            world.update(deltaTime, game.getAccelerometer()[0]);
            renderer.render();
        }

        if (world.gameOver) state = State.GameOver;

        //if paused, draw the Resume.png in the middel of the screen
        if (state == State.Paused)
        {
            game.drawBitmap(resume, 160 - resume.getWidth()/2, 240 - resume.getHeight()/2);
        }

        //if gameover, draw the gameover.png in the middel of the screen
        if(state == State.GameOver)
        {
            game.drawBitmap(gameOver, 160 - gameOver.getWidth()/2, 240 - gameOver.getHeight()/2);
        }

    }

    @Override
    public void pause()
    {
        if(state == State.Running ) state = State.Paused;
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