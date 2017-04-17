package dk.kea.student.class2017.christianfindsen.droidgameengine.Breakout;


import dk.kea.student.class2017.christianfindsen.droidgameengine.Screen;
import dk.kea.student.class2017.christianfindsen.droidgameengine.GameEngine;


/**
 * Created by Christian Findsen on 20-03-2017.
 */

public class MainMenu extends GameEngine
{

    @Override
    public Screen createStartScreen()
    {
        music = this.loadMusic("music.ogg");
        music.setLooping(true);
        return new MainMenuScreen(this);
    }

    public void onPause()
    {
        super.onPause();
        music.pause();
    }

    public void onResume()
    {
        super.onResume();
        music.play();
    }

}