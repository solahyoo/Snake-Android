package com.example.yoo.snake;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class SnakeActivity extends AppCompatActivity {

    SnakeView snakeView;

    // For display details
    Display display;
    Point size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        snakeView = new SnakeView(this, size);

        setContentView(snakeView);
    }

    // Methods to start and stop thread in SnakeView
    @Override
    protected void onStop() {
        super.onStop();
        while (true) {
            snakeView.pause();
            break;
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        snakeView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        snakeView.resume();
    }
}
