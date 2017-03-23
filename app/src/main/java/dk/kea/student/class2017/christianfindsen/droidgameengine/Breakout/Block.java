package dk.kea.student.class2017.christianfindsen.droidgameengine.Breakout;

/**
 * Created by Christian on 23-03-2017.
 */

public class Block
{
    public static final float WIDTH = 40;
    public static final float HEIGHT = 18;
    float x;
    float y;
    int type;

    //construktor
    public Block(float x, float y, int type)
    {
        this.x = x;
        this.y = y;
        this.type = type;
    }
}