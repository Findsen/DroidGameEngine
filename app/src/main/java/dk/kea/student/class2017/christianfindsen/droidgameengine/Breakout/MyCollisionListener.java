package dk.kea.student.class2017.christianfindsen.droidgameengine.Breakout;

import dk.kea.student.class2017.christianfindsen.droidgameengine.Sound;

/**
 * Created by Christian Findsen on 17-04-2017.
 */

public class MyCollisionListener implements CollisionListener
{
    Sound wallSound;
    Sound paddleSound;
    Sound blockSound;
    Sound gameOverSound;

    //construktor
    public MyCollisionListener(Sound wallSound, Sound paddleSound, Sound blockSound, Sound gameOverSound)
    {
        this.wallSound = wallSound;
        this.paddleSound = paddleSound;
        this.blockSound = blockSound;
        this.gameOverSound = gameOverSound;
    }

    @Override
    public void collisionWall()
    {
        wallSound.play(1);                                                                          //full sound, it goes from 0-1
    }

    @Override
    public void collisionPaddle()
    {
        paddleSound.play(1);

    }

    @Override
    public void collisionBlock()
    {
        blockSound.play(1);
    }

    @Override
    public void collisionOutOfScreen()
    {
        gameOverSound.play(1);
    }
}