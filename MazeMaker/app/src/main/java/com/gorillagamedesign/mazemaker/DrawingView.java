package com.gorillagamedesign.mazemaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by simon on 1/4/2018.
 */

public class DrawingView extends View {
    //drawing and canvas paint
    private Paint paintSettings;

    //vars
    private int[] gridSize = new int[2];
    private GridSquare current;
    private GridSquare[][] grid;
    public Stack<GridSquare> spotStack;
    public Stack<GridSquare> path;
    public Timer drawTimer;
    public Point offset;
    public int difficulty = 1;
    public int gridSquareSize = 100;
    public boolean drawing = true;
    public boolean creating = true;
    public int width;
    public int height;
    public int marginWidth;
    public int marginHeight;
    public Bitmap mazeBitmap;
    public Canvas bitmapCanvas;

    public DrawingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public void init()
    {
        setupDrawing();
        drawTimer = new Timer();
        if(width != 0)
        {
            gridSize = new int[]{height / gridSquareSize, width / gridSquareSize};
            offset = new Point((width % gridSquareSize) / 2 + marginWidth, (height % gridSquareSize) / 2 + marginHeight);
            grid = new GridSquare[gridSize[0]][];
            for (int i = 0; i < grid.length; i++) {
                grid[i] = new GridSquare[gridSize[1]];
                for (int j = 0; j < grid[i].length; j++)
                    grid[i][j] = new GridSquare(i, j, this);
            }
            current = grid[0][0];
            spotStack = new Stack<>();
            mazeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
            bitmapCanvas = new Canvas(mazeBitmap);
        }
    }
    private FullscreenActivity getActivity()
    {
        return (FullscreenActivity) getContext();
    }
    private void setupDrawing() {
        paintSettings = new Paint(Paint.DITHER_FLAG);
        paintSettings.setAntiAlias(false);
        this.setBackgroundColor(Color.TRANSPARENT);

    }
    public void paintInterval()
    {
        if(gridSize[0] == 0 && gridSize[1] == 0)
        {
            getActivity().defineVars();
            init();
        }
        else
        {
            if (creating)
            {
                invalidate();
                while(creating) {
                    grid[current.i][current.j].visited = true;
                    grid[current.i][current.j].current = false;
                    GridSquare randomNeighbor = current.getRandomNeighbor(grid);
                    if (randomNeighbor != null)
                    {
                        spotStack.push(current);
                        GridSquare[] cellsWithRemovedWalls = GridSquare.removeWalls(current, randomNeighbor, grid[0].length, grid.length);
                        grid[cellsWithRemovedWalls[0].i][cellsWithRemovedWalls[0].j] = cellsWithRemovedWalls[0];
                        grid[cellsWithRemovedWalls[1].i][cellsWithRemovedWalls[1].j] = cellsWithRemovedWalls[1];
                        current = grid[randomNeighbor.i][randomNeighbor.j];
                        grid[current.i][current.j].current = true;
                    }
                    else if (spotStack.size() != 0)
                    {
                        current = spotStack.pop();
                        grid[current.i][current.j].current = true;
                    }
                    if (current == grid[0][0] && current.visited)
                    {
                        creating = false;
                        drawTimer.cancel();
                    }
                }
                grid[0][0].wallsActive[3] = false;
                grid[gridSize[0] - 1][gridSize[1] - 1].wallsActive[1] = false;
                mazeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
                bitmapCanvas = new Canvas(mazeBitmap);
                if(gridSize[0] != 0 && gridSize[1] != 0 && drawing)
                    for (int i = 0; i < grid.length; i++)
                        for (int j = 0; j < grid[i].length; j++)
                            grid[i][j].Draw(bitmapCanvas);
                //bitmapCanvas = new Canvas(mazeBitmap);
            }
        }
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {

    }

    @Override
    protected void onDraw(Canvas canvasScreen)
    {
        if(mazeBitmap != null)
            canvasScreen.drawBitmap(mazeBitmap,  offset.x, offset.y, paintSettings);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        return true;
    }
}
