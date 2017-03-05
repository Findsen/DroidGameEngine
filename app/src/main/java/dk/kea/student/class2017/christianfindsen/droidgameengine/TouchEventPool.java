package dk.kea.student.class2017.christianfindsen.droidgameengine;

/**
 * Created by Christian Findsen on 04-03-2017.
 */

public class TouchEventPool extends Pool<TouchEvent>
{

    @Override
    protected TouchEvent newItem()
    {
        return new TouchEvent();
    }
}