package dk.kea.student.class2017.christianfindsen.droidgameengine.Breakout;

import android.graphics.Bitmap;

import dk.kea.student.class2017.christianfindsen.droidgameengine.GameEngine;

/**
 * Created by Christian Findsen on 20-03-2017.
 */

public class WorldRenderer
{
    GameEngine game;
    World world;
    Bitmap ballImage;
    Bitmap paddleImage;


    //contruktor
    public WorldRenderer(GameEngine game, World world)
    {
        this.game = game;
        this.world = world;
        ballImage = game.loadBitmap("ball.png");
        paddleImage = game.loadBitmap("paddle.png");
    }

    //draw the object
    public void render()
    {
        game.drawBitmap(ballImage, (int)world.ball.x, (int)world.ball.y);
        game.drawBitmap(paddleImage, (int)world.paddle.x, (int)world.paddle.y);
    }
}