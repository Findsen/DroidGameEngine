package dk.kea.student.class2017.christianfindsen.droidgameengine.Breakout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian Findsen on 20-03-2017.
 */

public class World
{
    public static final float MIN_X =0;
    public static final float MAX_X =319;
    public static final float MIN_Y =40;
    public static final float MAX_Y =479;

    boolean gameOver = false;
    Ball ball = new Ball();
    Paddle paddle = new Paddle();
    List<Block> blockArrayList = new ArrayList<>();

    //updates the ball
    public void update(float deltaTime, float accelX)
    {
        ball.x = ball.x + ball.vx * deltaTime;
        ball.y = ball.y + ball.vy * deltaTime;

        //if ball hits the sites, it have to bounce back
        if (ball.x < MIN_X)
        {
            ball.vx = -ball.vx;
            ball.x = MIN_X; // the user doesnt see it goes out of the screen
        }

        if (ball.x > MAX_X - ball.WIDTH)
        {
            ball.vx = -ball.vx;
            ball.x = MAX_X - ball.WIDTH;
        }

        if (ball.y < MIN_Y)
        {
            ball.vy = -ball.vy;
            ball.y = MIN_Y;
        }

        if (ball.y > MAX_Y - ball.HEIGHT)
        {
            gameOver = true;
            return;
        }

        //update the paddle
        //looks for the Accelerometer and speed
        paddle.x = paddle.x - accelX *deltaTime * 50;

        //check if paddle hit the wall
        if (paddle.x < MIN_X) paddle.x = MIN_X;
        if (paddle.x + Paddle.WIDTH > MAX_X) paddle.x = MAX_X - Paddle.WIDTH;

        collideBallPaddle();
    }

    private void generateBlocks()
    {
        blockArrayList.clear();                                                                     // clear the arraylist, to make sure is empty
        for (int y = 60, type = 0; y < 60 + 8 * Block.HEIGHT; y = y + (int)Block.HEIGHT, type++ )   //for the row, 7
        {
            for (int x = 20; x < MAX_X - Block.WIDTH; x = + (int)Block.WIDTH)                       // for the collums 8
            {
                blockArrayList.add(new Block(x,y,type));
            }
        }
    }

    //check for collision
    private void collideBallPaddle()
    {
        if (ball.x + Ball.WIDTH > paddle.x
                && ball.x < paddle.x +Paddle.WIDTH
                && ball.y + Ball.HEIGHT > paddle.y)
        {
            ball.vy = - ball.vy;
            ball.y = paddle.y - Ball.HEIGHT -1;

        }
    }
}