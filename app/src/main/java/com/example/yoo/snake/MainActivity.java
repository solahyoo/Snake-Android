package com.example.yoo.snake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(this);

        // TODO:
        // Add high scores
        // Add quit
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonPlay:
                Intent i = new Intent(this, SnakeActivity.class);
                startActivity(i);
                break;
        }
    }
}
