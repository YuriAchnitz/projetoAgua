package com.yach.projetoagua.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yach.projetoagua.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mViewHolder.gotoNews = findViewById(R.id.goto_news);

        this.mViewHolder.gotoNews.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.goto_news) {
            Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
            startActivity(intent);
        }
    }

    private static class ViewHolder {
        Button gotoNews;
    }
}