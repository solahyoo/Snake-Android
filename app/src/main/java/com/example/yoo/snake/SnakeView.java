package com.example.yoo.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
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
    private final int NUM_BLOCKS = 20;
    private int num_blocks_vert;

    // Game Objects
    // Length of snake
    private int length;
    // Location of snake
    private int[] x;
    private int[] y;
    private final int MAX_LENGTH = 300;
    // Wall
    private int wallWidth;
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
        num_blocks_vert = screenHeight / blockSize;

        // Position of wall
        wallWidth = blockSize / 4;
        wallX1 = blockSize - wallWidth;
        wallX2 = screenWidth - wallX1 - wallWidth;
        wallY1 = blockSize - wallWidth;
        wallY2 = screenHeight - wallY1 - wallWidth * 3;

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
        x[0] = NUM_BLOCKS / 2;
        y[0] = num_blocks_vert / 2;

        spawnApple();


        Log.d("x:", String.valueOf(x[0]));
        Log.d("y:", String.valueOf(y[0]));
        Log.d("Applex:", String.valueOf(appleX));
        Log.d("Appley:", String.valueOf(appleY));
        Log.d("screenHeight:", String.valueOf(screenHeight));
        Log.d("screenWidth:", String.valueOf(screenWidth));
        Log.d("blockSize:", String.valueOf(blockSize));
    }

    public void spawnApple() {
        /*
        * Spawns apple in a random location.
        * If apple's x position is too close to snake's starting position, find another place to spawn.
        * */
        Random random = new Random();
        appleX = x[0];
        while (Math.abs(appleX - x[0]) < 2) {
            appleX = random.nextInt(NUM_BLOCKS - 5) + 3;
        }
        appleY = random.nextInt(num_blocks_vert - 5) + 3;

    }

    public void eatApple() {
        if (Math.abs(appleX - x[0]) < 2 && Math.abs(appleY - y[0]) < 2) {
            spawnApple();
            score += 1;
            // Add block at end of snake
            x[length] = x[length - 1];
            y[length] = y[length - 1];
            switch (dir) {
                case WEST:
                    x[length] += 1;
                    break;
                case EAST:
                    x[length] -= 1;
                    break;
                case NORTH:
                    y[length] += 1;
                    break;
                case SOUTH:
                    y[length] -= 1;
                    break;
            }
            length += 1;
        }
    }

    public boolean checkCollision() {
        /*
        * Method to detect if snake has hit a wall or itself.
        * */
        // Check collision with itself
        for (int i = 1; i < length; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                return true;
            }
        }
        // Check collision with wall
        if (x[0] == 0 || x[0] == NUM_BLOCKS - 1 ||
                y[0] == 0 || y[0] == num_blocks_vert - 1) {
            return true;
        }
        return false;
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
        for (int i = length - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (dir) {
            case WEST:
                x[0] -= 1;
                break;
            case EAST:
                x[0] += 1;
                break;
            case NORTH:
                y[0] -= 1;
                break;
            case SOUTH:
                y[0] += 1;
                break;
        }
        eatApple();
        boolean died = checkCollision();
        if (died) newGame();
        Log.d("x:", String.valueOf(x[0]));
        Log.d("y:", String.valueOf(y[0]));
        Log.d("length:", String.valueOf(length));
        Log.d("appleX", String.valueOf(appleX));
        Log.d("appleY", String.valueOf(appleY));
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
            paint.setTextSize(25);
            // Draw text at 20, 40 (top of screen)
            canvas.drawText("Score:" + score, 10, 20, paint);

            // Draw rectangles for wall
            // Left
            canvas.drawRect(wallX1, wallY1, wallX1 + wallWidth, wallY2, paint);
            // Right
            canvas.drawRect(wallX2, wallY1, wallX2 + wallWidth, wallY2 + wallWidth, paint);
            // Top
            canvas.drawRect(wallX1, wallY1, wallX2, wallY1 + wallWidth, paint);
            // Bottom
            canvas.drawRect(wallX1, wallY2, wallX2, wallY2 + wallWidth, paint);

            // Draw snake head in different color
            paint.setColor(Color.parseColor("#555555"));
            canvas.drawRect(x[0] * blockSize, y[0] * blockSize,
                            x[0] * blockSize + blockSize,
                            y[0] * blockSize + blockSize, paint);
            // Set paint color for drawing the snake
            paint.setColor(Color.parseColor("#555555"));
            // Draw snake
            for (int i = 1; i < length; i++) {
                 canvas.drawRect(x[i] * blockSize, y[i] * blockSize,
                                x[i] * blockSize + blockSize,
                                y[i] * blockSize + blockSize, paint);
            }

            // Draw apple
            paint.setColor(Color.RED);
            canvas.drawCircle(appleX * blockSize, appleY * blockSize, blockSize / 2, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void controlFPS() {
        // time between now and time that last frame took
        long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
        // calculate how long we want to pause between frames
        // 15 ms of pause gives us around 60 fps
        // so 15 - timeThisFrame = # of ms we should pause for to make the frame last for 15 ms
        long timeToSleep = 150 - timeThisFrame;
        if (timeThisFrame > 0) {
            fps = (int) (1000 / timeThisFrame);
        }
        if (timeToSleep > 0) {
            try {
                thread.sleep(timeToSleep);
            } catch (InterruptedException e) {
            }
        }
        lastFrameTime = System.currentTimeMillis();
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

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        /* Method to handle player touching screen.
         * Snake moves clockwise if right side is touched.
         * Snake moved counter clockwise if left side is touched.
         * */
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP: // after player's finger leaves screen
                if (motionEvent.getX() >= screenWidth / 2) { // right side of screen
                    switch (dir) {
                        case WEST:
                            dir = Direction.NORTH;
                            break;
                        case EAST:
                            dir = Direction.SOUTH;
                            break;
                        case NORTH:
                            dir = Direction.EAST;
                            break;
                        case SOUTH:
                            dir = Direction.WEST;
                            break;
                    }
                } else { // left side of screen
                    switch (dir) {
                        case WEST:
                            dir = Direction.SOUTH;
                            break;
                        case EAST:
                            dir = Direction.NORTH;
                            break;
                        case NORTH:
                            dir = Direction.WEST;
                            break;
                        case SOUTH:
                            dir = Direction.EAST;
                            break;
                    }
                }
        }
        return true;
    }
}
