package com.gorillagamedesign.mazemaker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Simon MacLean on 1/13/2018.
 */

public class GridSquare {
    private Paint paintSettings;
    public static Random r = new Random();
    private int sideLength = FullscreenActivity.drawingView.gridSquareSize;
    public boolean visited = false;
    public boolean current = false;
    public boolean[] wallsActive = new boolean[4];
    public int i, j;
    private Point[] drawVerticies;
    private Point[] fillVerticies;
    private ArrayList<GridSquare> neighbors;
    private DrawingView parent;
    public GridSquare(int i, int j, DrawingView parent)
    {
        wallsActive = new boolean[] {true, true, true, true};
        paintSettings = new Paint();
        this.i = i;
        this.j = j;
        drawVerticies = new Point[]
                {
                        new Point(j * sideLength, i * sideLength),
                        new Point(j * sideLength, (i + 1) * sideLength),
                        new Point((j + 1) * sideLength, (i + 1) * sideLength),
                        new Point((j + 1) * sideLength, i * sideLength)
                };
        fillVerticies = new Point[]
                {
                        new Point(j * sideLength + 1, i * sideLength + 1),
                        new Point(j * sideLength + 1, (i + 1) * sideLength),
                        new Point((j + 1) * sideLength, (i + 1) * sideLength),
                        new Point((j + 1) * sideLength, i * sideLength + 1)
                };
        this.parent = parent;
    }
    public GridSquare getRandomNeighbor(GridSquare[][] grid)
    {
        neighbors = new ArrayList<>();
        if (j > 0)
            if(!grid[i][j - 1].visited)
                neighbors.add(grid[i][j - 1]);
        if (i < grid.length - 1 && !grid[i + 1][j].visited)
            neighbors.add(grid[i + 1][j]);
        if (j < grid[i].length - 1 && !grid[i][j + 1].visited)
            neighbors.add(grid[i][j + 1]);
        if (i > 0 && !grid[i - 1][j].visited)
            neighbors.add(grid[i - 1][j]);
        if (neighbors.size() > 0)
            return neighbors.get(r.nextInt(neighbors.size()));
        else
            return null;
    }
    public void Draw(Canvas canvas)
    {
        for(int i = 0; i < wallsActive.length; i++)
        {
            paintSettings.setColor(wallsActive[i] ? Color.WHITE : Color.TRANSPARENT);
            //paintSettings.setColor(Color.WHITE);
            paintSettings.setStrokeWidth(5);
            canvas.drawLine(drawVerticies[i].x, drawVerticies[i].y,
                    drawVerticies[(i + 1) % drawVerticies.length].x,
                    drawVerticies[(i + 1) % drawVerticies.length].y , paintSettings);
        }
        paintSettings.setColor(Color.WHITE);
        for(int i = 0; i < drawVerticies.length; i++)
            canvas.drawPoint(drawVerticies[i].x, drawVerticies[i].y, paintSettings);
    }
    public static GridSquare[] removeWalls(GridSquare a, GridSquare b, int gridSizeY, int gridSizeX)
    {
        GridSquare[] result;
        int xDif = a.j - b.j;
        int yDif = a.i - b.i;
        if (yDif != 0)
        {
            if (yDif < 2 && yDif > -2)
            {
                a.wallsActive[2 + yDif] = false;
                b.wallsActive[2 - yDif] = false;
            }
            else if (yDif == -1 * gridSizeY + 1)
            {
                a.wallsActive[3] = false;
                b.wallsActive[1] = false;
            }
            else if (yDif == gridSizeY - 1)
            {
                a.wallsActive[1] = false;
                b.wallsActive[3] = false;
            }
        }
        if (xDif != 0)
        {
            if (xDif < 2 && xDif > -2)
            {
                a.wallsActive[1 - xDif] = false;
                b.wallsActive[1 + xDif] = false;
            }
            else if (xDif == -1 * gridSizeX + 1)
            {
                a.wallsActive[0] = false;
                b.wallsActive[2] = false;
            }
            else if (xDif == gridSizeX - 1)
            {
                a.wallsActive[2] = false;
                b.wallsActive[0] = false;
            }
        }
        result = new GridSquare[] { a, b };
        return result;
    }
}
