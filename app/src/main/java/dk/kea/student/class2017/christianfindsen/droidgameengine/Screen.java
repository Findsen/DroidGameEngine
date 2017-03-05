package dk.kea.student.class2017.christianfindsen.droidgameengine;

/**
 * Created by Christian Findsen on 04-03-2017.
 */

public abstract class Screen
{
    protected final GameEngine game;

    public Screen(GameEngine game)
    {
        this.game = game;
    }


    public abstract void update(float deltaTime);
    public abstract void pause();
    public abstract void resume();

    // when user swip the program away, the android system gives a chance to save or do something else
    public abstract void dispose();
}