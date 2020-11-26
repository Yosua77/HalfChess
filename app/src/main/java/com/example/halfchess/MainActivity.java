package com.example.halfchess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bPvP:
                Intent i = new Intent(MainActivity.this,GameActivity.class);
                startActivity(i);
                break;
            case R.id.bPvC:
                Intent j = new Intent(MainActivity.this,SelectDifficultyActivity.class);
                startActivity(j);
                break;
            case R.id.bExit:
                finish();
                break;
        }
    }
}