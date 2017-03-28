package dk.kea.student.class2017.christianfindsen.droidgameengine.Breakout;

import java.util.ArrayList;
import java.util.List;

import dk.kea.student.class2017.christianfindsen.droidgameengine.GameEngine;

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


    //delete it later, only for testing (the touch)
    GameEngine game;

    //construktor
    public World(GameEngine game)
    {   this.game = game;
        generateBlocks();
    }

    //updates the ball
    public void update(float deltaTime, float accelX)
    {

        if (blockArrayList.size() == 0)
        {
            generateBlocks();
        }
        ball.x = ball.x + ball.vx * deltaTime;
        ball.y = ball.y + ball.vy * deltaTime;

        //if ball hits the sites, it have to bounce back
        if (ball.x < MIN_X)
        {
            ball.vx = -ball.vx;
            ball.x = MIN_X; // the user doesnt see it goes out of the screen
        }

        if (ball.x > MAX_X - Ball.WIDTH)
        {
            ball.vx = -ball.vx;
            ball.x = MAX_X - Ball.WIDTH;
        }

        if (ball.y < MIN_Y)
        {
            ball.vy = -ball.vy;
            ball.y = MIN_Y;
        }

        //Did the
        if (ball.y > MAX_Y - Ball.HEIGHT)
        {
            gameOver = true;
            return;
        }

        //touch, only for testing, well be deleting later
        if (game.isTouchDown(0))
        {
            if (game.getTouchY(0) > 410)
            {
                paddle.x = game.getTouchX(0) - paddle.WIDTH / 2;
            }
        }

        //update the paddle
        //looks for the Accelerometer and speed
        paddle.x = paddle.x - accelX *deltaTime * 50;

        //check if paddle hit the wall
        if (paddle.x < MIN_X) paddle.x = MIN_X;
        if (paddle.x + Paddle.WIDTH > MAX_X) paddle.x = MAX_X - Paddle.WIDTH;

        collideBallPaddle();
        collideBallBlocks(deltaTime);
    }

    private void generateBlocks()
    {
        blockArrayList.clear();                                                                     // clear the arraylist, to make sure is empty
        // from the top of the screen
        for (int y = 60, type = 0; y < 60 + 8 * (Block.HEIGHT + 2); y = y + (int)Block.HEIGHT + 2, type++ )   //for the row, 7.... 2 is the padding around the blocks
        {
            for (int x = 14; x < MAX_X - Block.WIDTH; x = x + (int)Block.WIDTH + 2)                       // for the collums 8
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

    //check how the ball hits a block and change the way the ball is moving
    private boolean collideRects(float x, float y, float width, float height,
                                 float x2, float y2, float width2, float height2)
    {
        if ((x < x2 + width2) && (x + width > x2) && (y < y2 + height2) && (y+height > y2))
        {
            return true;
        }
        return false;
    }

    private void collideBallBlocks(float deltaTime)
    {
        for (int i = 0; i < blockArrayList.size(); i++)
        {
            Block block = blockArrayList.get(i);
            if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                    block.x, block.y, Block.WIDTH, Block.HEIGHT))
            {
                blockArrayList.remove(i);
                i = i-1;
                float oldvx = ball.vx;
                float oldvy = ball.vy;
                reflectBall(ball, block);
                ball.x = ball.x - oldvx * deltaTime * 1.01f;
                ball.y = ball.y - oldvy * deltaTime * 1.01f;
            }
        }
    }


    private void reflectBall(Ball ball, Block block)
    {
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.WIDTH,
                block.x, block.y, 1, 1)) //check the top left corner of the block
        {
            if (ball.vx > 0) ball.vx = -ball.vx;
            if (ball.vy > 0) ball.vy = -ball.vy;
            return;
        }
        if (collideRects(ball.x, ball.y,Ball.WIDTH,Ball.HEIGHT,
                block.x + Block.WIDTH, block.y,1,1))  //Check the top right corner of the block
        {
            if (ball.vx < 0) ball.vx = -ball.vx;
            if (ball.vy > 0) ball.vy = -ball.vy;
            return;
        }
        if (collideRects(ball.x, ball.y, Ball.WIDTH,Ball.HEIGHT,
                        block.x,block.y+Block.HEIGHT,1,1)) //Check the bottom left corner of the block
        {
            if (ball.vx > 0) ball.vx = -ball.vx;
            if (ball.vy < 0) ball.vy = -ball.vy;
            return;
        }
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                block.x + Block.WIDTH, block.y + Block.HEIGHT,1,1)) // Check the right bottom corner of the block
        {
            if (ball.vx < 0) ball.vx = -ball.vx;
            if (ball.vy < 0) ball.vy = -ball.vy;
            return;
        }
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                block.x, block.y, Block.WIDTH, 1)) //check the top egde of the block
        {
            if (ball.vy > 0) ball.vy = -ball.vy; // should not be possible to get here from negative vy, so no if() is needed
            return;
        }
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                block.x, block.y + Block.HEIGHT, Block.WIDTH,1)) // Check the bottom egde of the block
        {
            ball.vy = -ball.vy;
            return;
        }
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                block.x, block.y,1,Block.HEIGHT))// check the left egde of the corner
        {
            ball.vx = -ball.vx;
            return;
        }
        if (collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                block.x + Block.WIDTH, block.y, 1, Block.HEIGHT)) //Check the right egde of the block
        {
            ball.vx = - ball.vx;
            return;
        }

    }
}