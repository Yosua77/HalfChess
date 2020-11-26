package com.example.halfchess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SelectDifficultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_difficulty);
    }
    public void onClick(View v){
        Intent i;
        switch (v.getId()){
            case R.id.bEasy:
                i = new Intent(SelectDifficultyActivity.this,GameActivity.class);
                i.putExtra("diff","Easy");
                startActivity(i);
                break;
            case R.id.bNormal:
                i = new Intent(SelectDifficultyActivity.this,GameActivity.class);
                i.putExtra("diff","Normal");
                startActivity(i);
                break;
            case R.id.bHard:
                i = new Intent(SelectDifficultyActivity.this,GameActivity.class);
                i.putExtra("diff","Hard");
                startActivity(i);
                break;
            case R.id.bBack:
                finish();
                break;
        }
    }
}