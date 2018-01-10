package com.gorillagamedesign.mazemaker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by simon on 1/4/2018.
 */

public class DrawingView extends View {
    //drawing and canvas paint
    private Paint paintSettings;

    //vars
    private static Timer drawTimer;
    int size = 1;
    boolean drawing = true;
    public static int difficulty = 1;
    public static int loadedAmount = 101;
    public DrawingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setupDrawing();
        drawTimer = new Timer();
        drawTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        paintInterval();
                    }
                }, 0);
            }
        }, 1, 10);
    }

    private FullscreenActivity getActivity()
    {
        return (FullscreenActivity) getContext();
    }
    private void setupDrawing() {
        paintSettings = new Paint(Paint.DITHER_FLAG);
        paintSettings.setAntiAlias(true);
        this.setBackgroundColor(Color.BLACK);

    }
    public void paintInterval()
    {
        if(loadedAmount <= 100) {
            loadedAmount++;
            getActivity().setLoadedAmount(loadedAmount);
        }
        invalidate();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {

    }

    @Override
    protected void onDraw(Canvas canvasScreen)
    {
        paintSettings.setColor(0xFF8080FF);
        paintSettings.setStyle(Paint.Style.FILL);
        if(drawing)
            canvasScreen.drawCircle(size,size,size, paintSettings);
        else
            canvasScreen.drawRect(0,0,2*size,2*size,paintSettings);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        return true;
    }
}
