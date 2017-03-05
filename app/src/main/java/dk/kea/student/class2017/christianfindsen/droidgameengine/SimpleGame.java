package dk.kea.student.class2017.christianfindsen.droidgameengine;

/**
 * Created by Christian Findsen on 04-03-2017.
 */

public class SimpleGame extends GameEngine
{

    @Override
    public Screen createStartScreen()
    {
        return new SimpleScreen(this);
    }
}