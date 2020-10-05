package com.yach.projetoagua.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yach.projetoagua.R;
import com.yach.projetoagua.data.Post;
import com.yach.projetoagua.data.ProjetoAguaApi;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.entity.mime.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private ProjetoAguaApi projetoAguaApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viniferr-watermonitor.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        projetoAguaApi = retrofit.create(ProjetoAguaApi.class);

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
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }

        if (v.getId() == R.id.icon_report) {
            Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }

        if (v.getId() == R.id.icon_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }

        if (v.getId() == R.id.icon_home) {
            mViewHolder.emergencyLayout.removeAllViews();
            mViewHolder.adviceLayout.removeAllViews();
            mViewHolder.newsLayout.removeAllViews();

            getEmergency("all", "all");
            getWarning("all", "all");
            getNews("all", "all");
            /*
            Intent intent = new Intent(getApplicationContext(), JsonTesterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            */

            /*
            String emergency_title = "Atenção!";
            String emergency_content = "Falta de água iminente";
            populateEmergencyCards(emergency_title, emergency_content);

            String advice_title = "Aviso";
            String advice_content = "Economize água";
            populateAdviceCards(advice_title, advice_content);

            String date = "28/09/2020";
            String news_content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam bibendum orci ligula, in imperdiet metus hendrerit a. Nunc maximus tortor eget orci mattis, eget convallis diam sagittis.";
            populateNewsCards("Aqui vai o título da notícia", news_content, date);
            populateNewsCards("O título da notícia vai aqui", news_content, date);
            */

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewHolder.emergencyLayout.removeAllViews();
        mViewHolder.adviceLayout.removeAllViews();
        mViewHolder.newsLayout.removeAllViews();

        getEmergency("all", "all");
        getWarning("all", "all");
        getNews("all", "all");
    }

    public void getNews(String cep, String date) {
        Call<List<Post>> call = projetoAguaApi.getAllCustom(cep, date);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    Toast toastNotSuccessful = Toast.makeText(getApplicationContext(),
                            "Code: " + response.code(),
                            Toast.LENGTH_SHORT);
                    toastNotSuccessful.show();
                    return;
                }

                List<Post> posts = response.body();

                int cont = 0;
                for (Post post : posts) {
                    if (post.getType().equals("news")) {
                        if (cont == 3) {
                            return;
                        }
                        String title = post.getTitle();
                        String content = post.getText();
                        String date = post.getDate();

                        String showDate;
                        String year, month, day;

                        year = date.substring(0, 4);
                        month = date.substring(5, 7);
                        day = date.substring(8, 10);

                        showDate = (day + "/" + month + "/" + year);

                        populateNewsCards(title, content, showDate);
                        cont++;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast toastFailure = Toast.makeText(getApplicationContext(),
                        t.getMessage(),
                        Toast.LENGTH_SHORT);
                toastFailure.show();
            }
        });
    }

    public void getEmergency(String cep, String date) {
        Call<List<Post>> call = projetoAguaApi.getAllCustom(cep, date);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    Toast toastNotSuccessful = Toast.makeText(getApplicationContext(),
                            "Code: " + response.code(),
                            Toast.LENGTH_SHORT);
                    toastNotSuccessful.show();
                    return;
                }

                List<Post> posts = response.body();

                for (Post post : posts) {
                    if (post.getType().equals("emergency")) {
                        String title = post.getTitle();
                        String content = post.getText();

                        populateEmergencyCards(title, content);

                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast toastFailure = Toast.makeText(getApplicationContext(),
                        t.getMessage(),
                        Toast.LENGTH_SHORT);
                toastFailure.show();
            }
        });
    }

    public void getWarning(String cep, String date) {
        Call<List<Post>> call = projetoAguaApi.getAllCustom(cep, date);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    Toast toastNotSuccessful = Toast.makeText(getApplicationContext(),
                            "Code: " + response.code(),
                            Toast.LENGTH_SHORT);
                    toastNotSuccessful.show();
                    return;
                }

                List<Post> posts = response.body();

                for (Post post : posts) {
                    if (post.getType().equals("warning")) {
                        String title = post.getTitle();
                        String content = post.getText();

                        populateAdviceCards(title, content);

                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast toastFailure = Toast.makeText(getApplicationContext(),
                        t.getMessage(),
                        Toast.LENGTH_SHORT);
                toastFailure.show();
            }
        });
    }

    public void populateEmergencyCards(String title, String content) {
        LinearLayout emergencyBody = new LinearLayout(getApplicationContext());

        CardView cardView = new CardView(getApplicationContext());

        TextView attention = new TextView(getApplicationContext());
        TextView reason = new TextView(getApplicationContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(15, 15, 15, 20);

        LinearLayout.LayoutParams mainBody = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        mainBody.setMargins(0, 0, 0, 50);

        emergencyBody.setLayoutParams(layoutParams);

        emergencyBody.setOrientation(LinearLayout.VERTICAL);

        cardView.setLayoutParams(layoutParams);

        cardView.setRadius(15);
        cardView.setBackgroundColor(getResources().getColor(RED));
        cardView.setMaxCardElevation(30);
        cardView.setMaxCardElevation(6);

        attention.setText(title);
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

        this.mViewHolder.emergencyLayout.setLayoutParams(mainBody);
        this.mViewHolder.emergencyLayout.addView(cardView);
    }

    public void populateAdviceCards(String title, String content) {
        LinearLayout emergencyBody = new LinearLayout(getApplicationContext());

        CardView cardView = new CardView(getApplicationContext());

        TextView attention = new TextView(getApplicationContext());
        TextView reason = new TextView(getApplicationContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(15, 15, 15, 20);

        LinearLayout.LayoutParams mainBody = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        mainBody.setMargins(0, 0, 0, 120);

        emergencyBody.setLayoutParams(layoutParams);

        emergencyBody.setOrientation(LinearLayout.VERTICAL);

        cardView.setLayoutParams(layoutParams);

        cardView.setRadius(15);
        cardView.setBackgroundColor(getResources().getColor(YELLOW));
        cardView.setMaxCardElevation(30);
        cardView.setMaxCardElevation(6);

        attention.setText(title);
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

        this.mViewHolder.adviceLayout.setLayoutParams(mainBody);
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

        LinearLayout.LayoutParams mainBody = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        mainBody.setMargins(0, 0, 0, 30);

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


        cardView.setLayoutParams(mainBody);

        cardView.setRadius(15);
        cardView.setBackgroundResource(R.drawable.news_card_background);
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

        newsTitle.setPadding(10, 0, 10, 0);
        newsTitle.setText(title);
        newsTitle.setGravity(Gravity.START);
        newsTitle.setTextColor(getResources().getColor(WHITE));
        newsTitle.setTypeface(Typeface.DEFAULT_BOLD);
        newsTitle.setTextSize(22);

        newsDate.setPadding(10, 10, 10, 10);
        newsDate.setText(date);
        newsDate.setGravity(Gravity.END);
        newsDate.setTextColor(getResources().getColor(LIGHT_GREY));
        newsDate.setTextSize(14);
        //

        //NEWS CONTENT
        newsTextBody.setLayoutParams(layoutParams);
        newsText.setPadding(10, 10, 10, 10);
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