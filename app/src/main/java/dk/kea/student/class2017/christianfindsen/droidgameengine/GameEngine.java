package dk.kea.student.class2017.christianfindsen.droidgameengine;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class GameEngine extends Activity implements Runnable, SensorEventListener
{
    private Thread mainLoopThread;
    private State state = State.Paused;
    private List<State> stateChanges = new ArrayList<>();
    private SurfaceView surfaceView; //updates now!
    private SurfaceHolder surfaceHolder;
    private Screen screen;
    private Canvas canvas = null;
    Rect src = new Rect(); //Firekant til kilden
    Rect dst = new Rect(); // Firekant til distination
    private Bitmap offscreenSurface;
    private TouchHandler touchHandler;
    private TouchEventPool touchEventPool = new TouchEventPool();
    private List<TouchEvent> touchEventBuffer = new ArrayList<>();
    private List<TouchEvent> touchEventsCopied = new ArrayList<>();
    private  float[] accelerometer = new float[3];
    private SoundPool soundPool;
    private int framesPerSecond = 0;



    public abstract Screen createStartScreen();

    public void onCreate(Bundle savedInstance)      // call when the system is started
    {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // doesnt show any title
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //flags is a byte, keep in fullscreen and doesnt dim the screen down to save power
        surfaceView = new SurfaceView(this);
        setContentView(surfaceView);
        surfaceHolder = surfaceView.getHolder();

        if(surfaceView.getWidth() > surfaceView.getHeight())
        {
            setOffscreenSurface(480,320); //vandret screen
        }
        else
        {
            setOffscreenSurface(320,480); //lodret screen
        }
        touchHandler = new MultiTouchHandler(surfaceView,touchEventBuffer,touchEventPool);
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0); //checks if the device has this type of Hardware
        {
            Sensor accSensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0); //only takes the first once and dont care if there is more
            sensorManager.registerListener(this, accSensor,SensorManager.SENSOR_DELAY_GAME); //game is faster then faster
        }

        setVolumeControlStream(AudioManager.STREAM_MUSIC); // Adding where we want to grab the volume from.
        this.soundPool = new SoundPool(20,AudioManager.STREAM_MUSIC, 0);

        // Supported from Android API 21 and higher
        // SoundPool.Builder builder = new SoundPool.Builder();
        // builder.setMaxStreams(20);
        // soundPool = builder.build();

        screen = createStartScreen();
    }

    //change the screen
    public void setScreen(Screen screen)
    {
        if(this.screen != null) this.screen.dispose();
        this.screen = screen;
    }

    //open files that turns into bits (jpeg, png files osv)
    public Bitmap loaderBitmap(String fileName)
    {
        InputStream in = null;
        Bitmap bitmap = null;

        try
        {
            in = getAssets().open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if(bitmap == null)
            {
                throw new RuntimeException("Could not create a bitmap from file :" + fileName);
            }
            return bitmap;
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not load the bloody file from file name: " + fileName + " !!!");
        }
        finally
        {
            if(in != null)
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    Log.e("GameEngine ","loadBitmap() failed to close the file: " + fileName);
                }
        }

    }

    public Sound loadSound(String fileName)
    {
        try
        {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(fileName);
            int soundId = soundPool.load(assetFileDescriptor,0);
            return new Sound(soundPool, soundId);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not load sound file: "+ fileName + "**********") ;
        }

    }

    public Music loadMusic(String fileName)
    {
        try
        {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(fileName);
            return new Music(assetFileDescriptor);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not load music file: " + fileName + "**********");
        }

    }


    public void clearFrameBuffer(int color)
    {
        canvas.drawColor(color);
    }


    public void setOffscreenSurface(int with, int height)
    {
        if(offscreenSurface != null) offscreenSurface.recycle();
        offscreenSurface = Bitmap.createBitmap(with, height, Bitmap.Config.RGB_565);
        canvas = new Canvas(offscreenSurface); //drawing object
    }

    public int getFrameBufferWidth()
    {
        return offscreenSurface.getWidth();
    }

    public int getFrameBufferHeight()
    {
        return offscreenSurface.getHeight();
    }

    public void drawBitmap(Bitmap bitmap, int x, int y)
    {
        if(canvas != null) canvas.drawBitmap(bitmap, x, y, null);
    }


    public void drawBitmap(Bitmap bitmap, int x, int y,
                           int srcX, int srcY,
                           int srcWitdh, int srcHeight)
    {
        if(canvas == null) return;
        src.left = srcX;
        src.top = srcY;
        src.right = srcX + srcWitdh;
        src.bottom = srcY + srcHeight;

        dst.left = x;
        dst.top = y;
        dst.right = x + srcWitdh;
        dst.bottom = y + srcHeight;

        canvas.drawBitmap(bitmap, src, dst, null);

    }

    //amount of touch
    public boolean isTouchDown(int pointer)
    {
        return touchHandler.isTouchDown(pointer);
    }

    public int getTouchX(int pointer)
    {
        return (int)(touchHandler.getTouchX(pointer) * (float) offscreenSurface.getWidth() /  (float)surfaceView.getWidth()); //   x / FysW * LogicW -- ex: 300/100 *320
    }

    public int getTouchY(int pointer)
    {
        return (int)(touchHandler.getTouchY(pointer) * (float)offscreenSurface.getHeight() / (float)surfaceView.getHeight());
    }

    private void fillEvents()
    {
        synchronized (touchEventBuffer)
        {
            for (int i=0; i<touchEventBuffer.size();i++)
            {
                touchEventsCopied.add(touchEventBuffer.get(i)); // Copy all touch events into new array
            }
            touchEventBuffer.clear(); // empty the eventbuffer after its been copied
        }
    }

    public void freeEvents()
    {
        synchronized (touchEventsCopied)
        {
            for(int i=0; i<touchEventsCopied.size(); i++)
            {
                touchEventPool.free(touchEventsCopied.get(i));
            }
        }
    }

    public float[] getAccelerometer()
    {
        return accelerometer;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }

    //same as onTouchEvent method :)
    public void onSensorChanged(SensorEvent event)
    {
        System.arraycopy(event.values, 0, accelerometer, 0,3);
    }

    public void run()
    {
        int frames = 0;
        long startTime = System.nanoTime();
        while(true) //makes a loop forever
        {
            synchronized (stateChanges)
            {
                for(int i = 0; i<stateChanges.size(); i++)
                {
                    state = stateChanges.get(i);
                    if(state == State.Disposed)
                    {
                        if(screen != null)
                        {
                            screen.dispose();
                        }
                        Log.d("GameEngine", "state changed to disposed");
                        return;
                    }
                    if (state == State.Paused)
                    {
                        if(screen != null)
                        {
                            screen.pause();
                        }
                        Log.d("GameEngine", "state changed to Paused");
                        //  return;
                    }
                    if (state == State.Resumed)
                    {
                        if(screen != null)
                        {
                            screen.resume();
                        }
                        state = State.Running;
                        Log.d("GameEngine", "state changed to Resumed /Running");
                    }
                }
                stateChanges.clear();
                if( state == State.Running)
                {
                    if(!surfaceHolder.getSurface().isValid()) continue; //get out of this loop
                    Canvas canvas = surfaceHolder.lockCanvas(); //local variable
                    // we will do all the drawing here
                    fillEvents();
                    if(screen != null) screen.update(0);
                    freeEvents();
                    src.left = 0;
                    src.top = 0;
                    src.right = offscreenSurface.getWidth() - 1; // else the last pix is out off the screen
                    src.bottom = offscreenSurface.getHeight() - 1;
                    dst.left = 0;
                    dst.top = 0;
                    dst.right = surfaceView.getWidth() - 1;
                    dst.bottom = surfaceView.getHeight() - 1;
                    canvas.drawBitmap(offscreenSurface, src, dst, null);
                    surfaceHolder.unlockCanvasAndPost(canvas); //move it from VRAM(video ram) to screen..  takes everything you have in videoram and shows it
                    canvas = null;
                    //timing test
                    frames++;
                    if ((System.nanoTime() - startTime) > 1_000_000_000)
                    {
                        framesPerSecond = frames;
                        frames = 0;
                        startTime = System.nanoTime();
                    }
                }
            }
        }
    }

    public int getFrameRate()
    {
        return framesPerSecond;
    }

    public void onPause()
    {
        super.onPause();
        synchronized (stateChanges)
        {
            if(isFinishing())
            {
                soundPool.release();
                stateChanges.add(stateChanges.size(), State.Disposed);
                ((SensorManager)getSystemService(Context.SENSOR_SERVICE)).unregisterListener(this);
            }
            else
            {
                stateChanges.add(stateChanges.size(),State.Paused);
            }
        }
    }

    public void onResume()
    {
        super.onResume();
        mainLoopThread = new Thread(this); //create the thread
        mainLoopThread.start(); // and start it
        synchronized (stateChanges)
        {
            stateChanges.add(stateChanges.size(), State.Resumed); //first free posion in the arraylist
            Log.d("GameEngine", "state Resumed added");

            SensorManager manager = ((SensorManager) getSystemService(Context.SENSOR_SERVICE));

            if (manager.getSensorList((Sensor.TYPE_ACCELEROMETER)).size() != 0)
            {
                Sensor accSensor = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
                manager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_GAME);
            }
        }
    }
}