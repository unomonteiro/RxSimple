package com.vogella.android.rxjava.simple;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        Intent i = null;
        switch (view.getId()) {
            case R.id.first:
                i = new Intent(this, RxJavaSimpleActivity.class);
                break;
            case R.id.second:
                i = new Intent(this, ColorsActivity.class);
                break;
            case R.id.third:
                i = new Intent(this, BooksActivity.class);
                break;
            case R.id.fourth:
                i = new Intent(this, SchedulerActivity.class);
                break;
        }
        startActivity(i);
    }
}
