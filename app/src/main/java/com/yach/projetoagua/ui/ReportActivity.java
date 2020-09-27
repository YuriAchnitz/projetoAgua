package com.yach.projetoagua.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.yach.projetoagua.R;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        this.mViewHolder.gotoHome = findViewById(R.id.icon_home);
        this.mViewHolder.gotoNews = findViewById(R.id.icon_news);
        this.mViewHolder.gotoSettings = findViewById(R.id.icon_settings);

        this.mViewHolder.gotoHome.setOnClickListener(this);
        this.mViewHolder.gotoNews.setOnClickListener(this);
        this.mViewHolder.gotoSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.icon_home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.icon_news) {
            Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.icon_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }
    }

    private static class ViewHolder {
        ImageButton gotoHome;
        ImageButton gotoNews;
        ImageButton gotoSettings;
    }
}