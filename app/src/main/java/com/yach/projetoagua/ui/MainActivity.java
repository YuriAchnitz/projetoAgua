package com.yach.projetoagua.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yach.projetoagua.R;

import java.util.Calendar;

import static com.yach.projetoagua.R.color.BLACK;
import static com.yach.projetoagua.R.color.LIGHT_GREY;
import static com.yach.projetoagua.R.color.RED;
import static com.yach.projetoagua.R.color.WHITE;
import static com.yach.projetoagua.R.color.YELLOW;
import static com.yach.projetoagua.R.color.buttonColor;
import static com.yach.projetoagua.R.color.colorDiv;
import static com.yach.projetoagua.R.color.colorPrimaryDark;
import static com.yach.projetoagua.R.color.newsCardBodyColor;
import static com.yach.projetoagua.R.color.newsCardColor;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mViewHolder.gotoNews = findViewById(R.id.icon_news);
        this.mViewHolder.gotoReport = findViewById(R.id.icon_report);
        this.mViewHolder.gotoSettings = findViewById(R.id.icon_settings);
        this.mViewHolder.homeRefreshButton = findViewById(R.id.icon_home);

        this.mViewHolder.gotoNews.setOnClickListener(this);
        this.mViewHolder.gotoReport.setOnClickListener(this);
        this.mViewHolder.gotoSettings.setOnClickListener(this);
        this.mViewHolder.homeRefreshButton.setOnClickListener(this);

        this.mViewHolder.newsLayout = findViewById(R.id.card_news_layout);
        this.mViewHolder.emergencyLayout = findViewById(R.id.card_emergency_layout);
        this.mViewHolder.adviceLayout = findViewById(R.id.card_advice_layout);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.icon_news) {
            Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.icon_report) {
            Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.icon_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.icon_home) {
            String emergency_content = "Falta de água iminente";
            populateEmergencyCards(emergency_content);

            String advice_content = "Economize água";
            populateAdviceCards(advice_content);

            String date = "28/09/2020";
            String news_content = "Boa noite caralho boa noite caralho boa noite caralho boa noite caralho boa noite caralho boa noite caralho";
            populateNewsCards("Bom Dia", news_content, date);
            populateNewsCards("Bom Dia", news_content, date);
        }
    }

    public void populateEmergencyCards(String content) {
        LinearLayout emergencyBody = new LinearLayout(getApplicationContext());

        CardView cardView = new CardView(getApplicationContext());

        TextView attention = new TextView(getApplicationContext());
        TextView reason = new TextView(getApplicationContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(15, 15, 15, 20);

        emergencyBody.setLayoutParams(layoutParams);

        emergencyBody.setOrientation(LinearLayout.VERTICAL);

        cardView.setLayoutParams(layoutParams);

        cardView.setRadius(15);
        cardView.setBackgroundColor(getResources().getColor(RED));
        cardView.setMaxCardElevation(30);
        cardView.setMaxCardElevation(6);

        attention.setText("Atenção!");
        attention.setTextColor(getResources().getColor(WHITE));
        attention.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        attention.setTextSize(26);
        attention.setTypeface(Typeface.DEFAULT_BOLD);

        reason.setText(content);
        reason.setTextColor(getResources().getColor(WHITE));
        reason.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        reason.setTextSize(16);

        emergencyBody.addView(attention);
        emergencyBody.addView(reason);

        cardView.addView(emergencyBody);
        this.mViewHolder.emergencyLayout.addView(cardView);
    }

    public void populateAdviceCards(String content) {
        LinearLayout emergencyBody = new LinearLayout(getApplicationContext());

        CardView cardView = new CardView(getApplicationContext());

        TextView attention = new TextView(getApplicationContext());
        TextView reason = new TextView(getApplicationContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(15, 15, 15, 20);

        emergencyBody.setLayoutParams(layoutParams);

        emergencyBody.setOrientation(LinearLayout.VERTICAL);

        cardView.setLayoutParams(layoutParams);

        cardView.setRadius(15);
        cardView.setBackgroundColor(getResources().getColor(YELLOW));
        cardView.setMaxCardElevation(30);
        cardView.setMaxCardElevation(6);

        attention.setText("Aviso!");
        attention.setTextColor(getResources().getColor(BLACK));
        attention.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        attention.setTextSize(26);
        attention.setTypeface(Typeface.DEFAULT_BOLD);

        reason.setText(content);
        reason.setTextColor(getResources().getColor(BLACK));
        reason.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        reason.setTextSize(16);

        emergencyBody.addView(attention);
        emergencyBody.addView(reason);

        cardView.addView(emergencyBody);
        this.mViewHolder.adviceLayout.addView(cardView);
    }

    public void populateNewsCards(String title, String news_content, String date) {
        LinearLayout newsCardBody = new LinearLayout(getApplicationContext());
        LinearLayout newsTextBody = new LinearLayout(getApplicationContext());
        LinearLayout titleBar = new LinearLayout(getApplicationContext());
        RelativeLayout image = new RelativeLayout(getApplicationContext());

        CardView cardView = new CardView(getApplicationContext());

        TextView newsTitle = new TextView(getApplicationContext());
        TextView newsDate = new TextView(getApplicationContext());
        TextView newsText = new TextView(getApplicationContext());

        ImageView newsThumbnail = new ImageView(getApplicationContext());


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(10, 10, 10, 10);

        LinearLayout.LayoutParams titleBarParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout.LayoutParams centerImage = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        newsCardBody.setOrientation(LinearLayout.VERTICAL);
        newsCardBody.setLayoutParams(layoutParams);



        cardView.setLayoutParams(layoutParams);

        cardView.setRadius(15);
        cardView.setBackgroundColor(getResources().getColor(newsCardColor));
        cardView.setMaxCardElevation(30);
        cardView.setMaxCardElevation(6);

        //NEWS THUMBNAIL
        image.setLayoutParams(centerImage);
        image.getLayoutParams().height = 450;

        newsThumbnail.setImageResource(R.drawable.water_landscape_temp);
        newsThumbnail.setScaleType(ImageView.ScaleType.FIT_XY);
        //

        //NEWS TITLE
        titleBar.setOrientation(LinearLayout.VERTICAL);
        titleBar.setLayoutParams(titleBarParams);

        newsTitle.setPadding(10,0,10,0);
        newsTitle.setText(title);
        newsTitle.setGravity(Gravity.START);
        newsTitle.setTextColor(getResources().getColor(WHITE));
        newsTitle.setTypeface(Typeface.DEFAULT_BOLD);
        newsTitle.setTextSize(22);

        newsDate.setPadding(10,10,10,10);
        newsDate.setText(date);
        newsDate.setGravity(Gravity.END);
        newsDate.setTextColor(getResources().getColor(LIGHT_GREY));
        newsDate.setTextSize(14);
        //

        //NEWS CONTENT
        newsTextBody.setLayoutParams(layoutParams);
        newsTextBody.setBackgroundResource(R.drawable.news_content_background);

        newsText.setLayoutParams(layoutParams);
        newsText.setText(news_content);
        newsText.setTextColor(getResources().getColor(newsCardBodyColor));
        newsText.setGravity(Gravity.START);
        newsText.setTextSize(12);
        //

        image.addView(newsThumbnail);

        newsCardBody.addView(image);

        titleBar.addView(newsTitle);
        titleBar.addView(newsDate);

        newsCardBody.addView(titleBar);

        newsTextBody.addView(newsText);
        newsCardBody.addView(newsTextBody);
        cardView.addView(newsCardBody);


        this.mViewHolder.newsLayout.addView(cardView);
    }

    private static class ViewHolder {
        ImageButton gotoNews;
        ImageButton gotoReport;
        ImageButton gotoSettings;
        ImageButton homeRefreshButton;

        LinearLayout newsLayout;

        LinearLayout emergencyLayout;

        LinearLayout adviceLayout;
    }
}