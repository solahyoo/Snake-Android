package com.example.yoo.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
 * Main code for Snake
 */

class SnakeView extends SurfaceView implements Runnable {
    private Thread thread = null;

    // For tracking movement
    public enum Direction {NORTH, SOUTH, EAST, WEST};
    // Start direction - go right
    private Direction dir = Direction.WEST;

    // Display size
    private int screenWidth;
    private int screenHeight;
    // Block size in pixels
    private int blockSize;
    private final int NUM_BLOCKS = 30;


    // Game Objects
    // Length of snake
    private int length;
    // Location of snake
    private int[] x;
    private int[] y;
    private final int MAX_LENGTH = 300;
    // Wall
    private int wallX1;
    private int wallX2;
    private int wallY1;
    private int wallY2;
    // Apple
    private int appleX;
    private int appleY;

    // Canvas for painting
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Paint paint;

    // Stats and frames
    private int fps;
    private long lastFrameTime;
    private int score;


    private volatile boolean isPlaying;

    /* Constructor */
    public SnakeView(Context context, Point size) {
        super(context);
        surfaceHolder = getHolder();
        paint = new Paint();

        // Display parameters
        screenWidth = size.x;
        screenHeight = size.y;
        blockSize = screenWidth / NUM_BLOCKS;

        // Size of wall
        wallX1 = blockSize * 2;
        wallX2 = screenWidth - wallX1;
        wallY1 = blockSize * 2;
        wallY2 = screenHeight - wallY1;

        // Initialize position arrays to be length of MAX_LENGTH
        x = new int[MAX_LENGTH]; // x[0], y[0] is position of head
        y = new int[MAX_LENGTH];

        newGame();
    }

    public void newGame() {
        // Initialize snake parameters
        length = 1;
        score = 0;
        // Start snake in middle of screen
        x[0] = screenWidth / 2;
        y[0] = screenHeight / 2;

        spawnApple();
    }

    public void spawnApple() {
        Random random = new Random();
        appleX = random.nextInt(NUM_BLOCKS) + wallX1 + 1;
        appleY = random.nextInt(screenHeight / blockSize) + wallY1 + 1;

    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            controlFPS();
        }
    }

    public void update() {

    }

    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            // initialize canvas and paint
            canvas = surfaceHolder.lockCanvas();

            // Background color
            canvas.drawColor(Color.parseColor("#D27BC932"));
            // Set paint color to black for text
            paint.setColor(Color.BLACK);
            // Set text size
            paint.setTextSize(45);
            // Draw text at 20, 40 (top of screen)
            canvas.drawText("Score:" + score, 20, 40, paint);
            // Set paint color for drawing the snake
            paint.setColor(Color.parseColor("#444444"));

            // Draw snake
            for (int i = 0; i < length; i++) {
                // canvas.drawRect(x[i], y[i], )
            }





        }
    }

    public void controlFPS() {

    }

    public void pause() {
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            // error
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }
}
