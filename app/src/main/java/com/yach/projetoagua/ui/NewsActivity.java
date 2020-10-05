package com.yach.projetoagua.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yach.projetoagua.R;
import com.yach.projetoagua.data.Post;
import com.yach.projetoagua.data.ProjetoAguaApi;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.yach.projetoagua.R.color.LIGHT_GREY;
import static com.yach.projetoagua.R.color.WHITE;
import static com.yach.projetoagua.R.color.newsCardBodyColor;
import static com.yach.projetoagua.R.color.newsCardColor;


public class NewsActivity extends AppCompatActivity implements View.OnClickListener {

    DatePickerDialog picker;
    private ViewHolder mViewHolder = new ViewHolder();
    private ProjetoAguaApi projetoAguaApi;
    private String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viniferr-watermonitor.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        projetoAguaApi = retrofit.create(ProjetoAguaApi.class);

        this.mViewHolder.gotoHome = findViewById(R.id.icon_home);
        this.mViewHolder.gotoReport = findViewById(R.id.icon_report);
        this.mViewHolder.gotoSettings = findViewById(R.id.icon_settings);
        this.mViewHolder.newsRefresh = findViewById(R.id.icon_news);

        this.mViewHolder.gotoHome.setOnClickListener(this);
        this.mViewHolder.gotoReport.setOnClickListener(this);
        this.mViewHolder.gotoSettings.setOnClickListener(this);
        this.mViewHolder.newsRefresh.setOnClickListener(this);


        this.mViewHolder.insertCep = findViewById(R.id.insert_cep);
        this.mViewHolder.selectDate = findViewById(R.id.select_date);
        this.mViewHolder.refreshButton = findViewById(R.id.refresh_button);

        this.mViewHolder.refreshButton.setOnClickListener(this);

        this.mViewHolder.selectDate.setInputType(InputType.TYPE_NULL);
        this.mViewHolder.selectDate.setOnClickListener(this);

        this.mViewHolder.newsCards = findViewById(R.id.news_simple_cards);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mViewHolder.newsCards.removeAllViews();
        String cep = mViewHolder.insertCep.getText().toString();
        if (cep.length() > 0 && cep.length() < 8) {
            Toast toastFailure = Toast.makeText(getApplicationContext(),
                    "CEP inválido",
                    Toast.LENGTH_SHORT);
            toastFailure.show();
        } else if (cep.length() == 0) {
            if (date.equals("")) {
                getNews("all", "all");
            } else {
                getNews("all", date);
            }
        } else if (cep.length() == 8) {
            if (date.equals("")) {
                getNews(cep, "all");
            } else {
                getNews(cep, date);
            }
        }
    }

    @Override
    public void onClick(View v) {
        //Quando clicar no selecionar data ele abre o calendario e guarda a data na string newsDate
        //depois disso ele muda o texto da view select_date pra data selecionada
        if (v.getId() == R.id.select_date) {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);

            picker = new DatePickerDialog(NewsActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    if (month + 1 < 10) {
                        if (dayOfMonth < 10) {
                            date = (year + "-" + "0" + (month + 1) + "-" + "0" + dayOfMonth);
                            mViewHolder.selectDate.setText("0" + dayOfMonth + "/" + "0" + (month + 1) + "/" + year);
                        } else {
                            date = (year + "-" + "0" + (month + 1) + "-" + dayOfMonth);
                            mViewHolder.selectDate.setText(dayOfMonth + "/" + "0" + (month + 1) + "/" + year);
                        }
                    } else {
                        if (dayOfMonth < 10) {
                            date = (year + "-" + (month + 1) + "-" + "0" + dayOfMonth);
                            mViewHolder.selectDate.setText("0" + dayOfMonth + "/" + (month + 1) + "/" + year);
                        } else {
                            date = (year + "-" + (month + 1) + "-" + dayOfMonth);
                            mViewHolder.selectDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        }
                    }
                }
            }, year, month, day);
            picker.show();
        }

        if (v.getId() == R.id.refresh_button) {
            mViewHolder.newsCards.removeAllViews();
            String cep = mViewHolder.insertCep.getText().toString();
            if (cep.length() > 0 && cep.length() < 8) {
                Toast toastFailure = Toast.makeText(getApplicationContext(),
                        "CEP inválido",
                        Toast.LENGTH_SHORT);
                toastFailure.show();
            } else if (cep.length() == 0) {
                if (date.equals("")) {
                    getNews("all", "all");
                } else {
                    getNews("all", date);
                }
            } else if (cep.length() == 8) {
                if (date.equals("")) {
                    getNews(cep, "all");
                } else {
                    getNews(cep, date);
                }
            }
        }

        if (v.getId() == R.id.icon_home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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

        if (v.getId() == R.id.icon_news) {
            mViewHolder.newsCards.removeAllViews();
            mViewHolder.selectDate.setText(R.string.selecione_a_data);
            mViewHolder.insertCep.setText("");
            date="";
            getNews("all", "all");
            /*
            String news_content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam bibendum orci ligula, in imperdiet metus hendrerit a. Nunc maximus tortor eget orci mattis, eget convallis diam sagittis.";
            populateNewsCards("Título da notícia", news_content, "28/09/2020");
            */
        }
    }

    public void populateNewsCards(String title, String news_content, String date) {
        LinearLayout newsTextBody = new LinearLayout(getApplicationContext());
        LinearLayout newsCardBody = new LinearLayout(getApplicationContext());

        CardView cardView = new CardView(getApplicationContext());

        TextView newsTitle = new TextView(getApplicationContext());
        TextView newsDate = new TextView(getApplicationContext());
        TextView newsText = new TextView(getApplicationContext());


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(10, 10, 10, 10);

        LinearLayout.LayoutParams titleBarParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        cardView.setLayoutParams(layoutParams);

        cardView.setRadius(15);
        cardView.setBackgroundResource(R.drawable.news_card_background);
        cardView.setMaxCardElevation(30);
        cardView.setMaxCardElevation(6);

        //NEWS TITLE
        newsCardBody.setOrientation(LinearLayout.VERTICAL);
        newsCardBody.setLayoutParams(titleBarParams);
        newsCardBody.setLayoutParams(layoutParams);

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
        newsTextBody.setBackgroundResource(R.drawable.news_content_background);

        newsText.setLayoutParams(layoutParams);
        newsText.setText(news_content);
        newsText.setTextColor(getResources().getColor(newsCardBodyColor));
        newsText.setGravity(Gravity.START);
        newsText.setTextSize(12);
        //

        newsCardBody.addView(newsTitle);
        newsCardBody.addView(newsDate);

        newsTextBody.addView(newsText);
        newsCardBody.addView(newsTextBody);

        cardView.addView(newsCardBody);


        this.mViewHolder.newsCards.addView(cardView);
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

                for (Post post : posts) {
                    if (post.getType().equals("news")) {
                        String title = post.getTitle();
                        String content = post.getText();
                        String date = post.getDate();

                        String showDate;
                        String year, month, day;

                        year = date.substring(0,4);
                        month = date.substring(5,7);
                        day = date.substring(8,10);

                        showDate = (day + "/" + month + "/" + year);

                        populateNewsCards(title, content, showDate);
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


    private static class ViewHolder {
        EditText insertCep;
        Button selectDate;
        ImageButton refreshButton;

        ImageButton gotoHome;
        ImageButton gotoReport;
        ImageButton gotoSettings;
        ImageButton newsRefresh;

        LinearLayout newsCards;
    }
}