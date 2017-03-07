package dk.kea.student.class2017.christianfindsen.droidgameengine;

/**
 * Created by Christian Findsen on 04-03-2017.
 */

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.List;

public class MultiTouchHandler implements TouchHandler, View.OnTouchListener
{

    private boolean[] isTouched = new boolean[20];  // same information, but its a object
    private int[] touchX = new int[20];             // same information, but its a object
    private int[] touchY = new int[20];             // same information, but its a object
    private List<TouchEvent> touchEventBuffer;      // same information, but faster to find
    private TouchEventPool touchEventPool;

    public MultiTouchHandler(View v, List<TouchEvent> touchEventBuffer, TouchEventPool touchEventPool)
    {
        v.setOnTouchListener(this);
        this.touchEventBuffer = touchEventBuffer;
        this.touchEventPool = touchEventPool;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        TouchEvent touchEvent = null;
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT; // >> bit shift
        int pointerId = event.getPointerId(pointerIndex);

        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                touchEvent = touchEventPool.obtain();
                touchEvent.type = TouchEvent.TouchEventType.Down;
                touchEvent.pointer = pointerId;
                touchX[pointerId] = (int) event.getX(pointerIndex);
                touchEvent.x = touchX[pointerId];
                touchY[pointerId] = (int) event.getY(pointerIndex);
                touchEvent.y = touchY[pointerId];
                isTouched[pointerId] = true;
                synchronized (touchEventBuffer)
                {
                    touchEventBuffer.add(touchEvent);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                touchEvent = touchEventPool.obtain();
                touchEvent.type = TouchEvent.TouchEventType.Up;
                touchEvent.pointer = pointerId;
                touchX[pointerId] = (int) event.getX(pointerIndex);
                touchEvent.x = touchX[pointerId];
                touchY[pointerId] = (int) event.getY(pointerIndex);
                touchEvent.y = touchY[pointerId];
                isTouched[pointerId] = false;
                synchronized (touchEventBuffer)
                {
                    touchEventBuffer.add(touchEvent);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                int pointerCouter = event.getPointerCount();
                synchronized (touchEventBuffer)
                {
                    for (int i = 0; i < pointerCouter; i++)
                    {
                        touchEvent = touchEventPool.obtain();
                        touchEvent.type = TouchEvent.TouchEventType.Draggged;
                        pointerIndex = i;
                        pointerId = event.getPointerId(pointerIndex);
                        touchEvent.pointer = pointerId;
                        touchX[pointerId] = (int) event.getX(pointerIndex);
                        touchEvent.x = touchX[pointerId];
                        touchY[pointerId] = (int) event.getY(pointerIndex);
                        touchEvent.y = touchY[pointerId];
                        isTouched[pointerId] = true;
                        touchEventBuffer.add(touchEvent);
                    }
                }
                break;
        } // end of switch statement
        return true;
    }//end of onTouch() method

    @Override
    public boolean isTouchDown(int pointer)
    {
        return isTouched[pointer];
    }

    @Override
    public int getTouchX(int pointer)
    {
        return touchX[pointer];
    }

    @Override
    public int getTouchY(int pointer)
    {
        return touchY[pointer];
    }
}