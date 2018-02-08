package com.gorillagamedesign.mazemaker;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;

import java.util.TimerTask;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        defineVars();
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
    }
    public static DrawingView drawingView;
    private Button settingsButton;
    private Button returnButton;
    private Button newMazeButton;
    private Button easyChoiceButton;
    private Button mediumChoiceButton;
    private Button hardChoiceButton;
    private TextView easyChoiceText;
    private TextView mediumChoiceText;
    private TextView hardChoiceText;
    private Space easy;
    private Space medium;
    private Space hard;
    private Space top;
    private Space left;
    private Space bounds;
    public void defineVars()
    {
        drawingView = findViewById(R.id.drawing);
        settingsButton = findViewById(R.id.settings);
        returnButton = findViewById(R.id.returnback);
        newMazeButton = findViewById(R.id.newmaze);
        easyChoiceButton = findViewById(R.id.easy);
        mediumChoiceButton = findViewById(R.id.medium);
        hardChoiceButton = findViewById(R.id.hard);
        easyChoiceText = findViewById(R.id.easytext);
        mediumChoiceText = findViewById(R.id.mediumtext);
        hardChoiceText = findViewById(R.id.hardtext);
        easy = findViewById(R.id.easyspace);
        medium = findViewById(R.id.mediumspace);
        hard = findViewById(R.id.hardspace);
        top = findViewById(R.id.top);
        left = findViewById(R.id.left);
        bounds = findViewById(R.id.screenbounds);
        drawingView.height = bounds.getHeight();
        drawingView.width = bounds.getWidth();
        drawingView.marginHeight = top.getHeight();
        drawingView.marginWidth = left.getWidth();
        settingsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                settingsButton.setVisibility(View.INVISIBLE);
                returnButton.setVisibility(View.VISIBLE);
                newMazeButton.setVisibility(View.INVISIBLE);
                easyChoiceButton.setVisibility(View.VISIBLE);
                mediumChoiceButton.setVisibility(View.VISIBLE);
                hardChoiceButton.setVisibility(View.VISIBLE);
                easyChoiceText.setVisibility(View.VISIBLE);
                mediumChoiceText.setVisibility(View.VISIBLE);
                hardChoiceText.setVisibility(View.VISIBLE);
                drawingView.drawing = false;
                drawingView.setVisibility(View.INVISIBLE);
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                settingsButton.setVisibility(View.VISIBLE);
                returnButton.setVisibility(View.INVISIBLE);
                newMazeButton.setVisibility(View.VISIBLE);
                easyChoiceButton.setVisibility(View.INVISIBLE);
                mediumChoiceButton.setVisibility(View.INVISIBLE);
                hardChoiceButton.setVisibility(View.INVISIBLE);
                easyChoiceText.setVisibility(View.INVISIBLE);
                mediumChoiceText.setVisibility(View.INVISIBLE);
                hardChoiceText.setVisibility(View.INVISIBLE);
                drawingView.drawing = true;
                drawingView.setVisibility(View.VISIBLE);
            }
        });
        easyChoiceButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                drawingView.difficulty = 1;
                easyChoiceButton.setBackground(getResources().getDrawable(R.drawable.button));
                mediumChoiceButton.setBackground(getResources().getDrawable(R.drawable.empty));
                hardChoiceButton.setBackground(getResources().getDrawable(R.drawable.empty));
                drawingView.gridSquareSize = easy.getWidth();
            }
        });
        mediumChoiceButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                drawingView.difficulty = 2;
                easyChoiceButton.setBackground(getResources().getDrawable(R.drawable.empty));
                mediumChoiceButton.setBackground(getResources().getDrawable(R.drawable.button));
                hardChoiceButton.setBackground(getResources().getDrawable(R.drawable.empty));
                drawingView.gridSquareSize = medium.getWidth();
            }
        });
        hardChoiceButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                drawingView.difficulty = 3;
                easyChoiceButton.setBackground(getResources().getDrawable(R.drawable.empty));
                mediumChoiceButton.setBackground(getResources().getDrawable(R.drawable.empty));
                hardChoiceButton.setBackground(getResources().getDrawable(R.drawable.button));
                drawingView.gridSquareSize = hard.getWidth();
            }
        });
        newMazeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                drawingView.init();
                drawingView.drawing = true;
                drawingView.creating = true;
                drawingView.drawTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        drawingView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                drawingView.paintInterval();
                            }
                        }, 0);
                    }
                }, 10, 1);
            }
        });
        drawingView.gridSquareSize = easy.getWidth();
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
