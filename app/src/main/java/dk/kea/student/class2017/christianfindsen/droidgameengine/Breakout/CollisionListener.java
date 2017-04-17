package dk.kea.student.class2017.christianfindsen.droidgameengine.Breakout;

/**
 * Created by Christian Findsen on 17-04-2017.
 */

public interface CollisionListener
{
    public void collisionWall();
    public void collisionPaddle();
    public void collisionBlock();
    public void collisionOutOfScreen();
}