package com.yach.projetoagua.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;
import com.yach.projetoagua.R;
import com.yach.projetoagua.data.ProjetoAguaApi;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("ALL")
public class NewsActivity extends AppCompatActivity implements View.OnClickListener {

    DatePickerDialog picker;
    private ViewHolder mViewHolder = new ViewHolder();
    private ProjetoAguaApi projetoAguaApi;
    private String userLocation = "";
    private String limDate = "";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/")
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

        aquireData();
    }

    private void aquireData() {
        mViewHolder.newsCards.removeAllViews();
        String cep = mViewHolder.insertCep.getText().toString();
        if (cep.length() > 0 && cep.length() < 8) {
            Toast toastFailure = Toast.makeText(getApplicationContext(),
                    "CEP inválido",
                    Toast.LENGTH_SHORT);
            toastFailure.show();
        } else if (cep.length() == 0) {
            if (limDate.equals("")) {
                getAllNews();
            } else {
                getDateNews();
            }
        } else if (cep.length() == 8) {
            if (limDate.equals("")) {
                validateCep(cep, 0);
            } else {
                validateCep(cep, 1);
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
                @SuppressLint("SetTextI18n")
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    if (month + 1 < 10) {
                        if (dayOfMonth < 10) {
                            limDate = (year + "-" + "0" + (month + 1) + "-" + "0" + dayOfMonth);
                            mViewHolder.selectDate.setText("0" + dayOfMonth + "/" + "0" + (month + 1) + "/" + year);
                        } else {
                            limDate = (year + "-" + "0" + (month + 1) + "-" + dayOfMonth);
                            mViewHolder.selectDate.setText(dayOfMonth + "/" + "0" + (month + 1) + "/" + year);
                        }
                    } else {
                        if (dayOfMonth < 10) {
                            limDate = (year + "-" + (month + 1) + "-" + "0" + dayOfMonth);
                            mViewHolder.selectDate.setText("0" + dayOfMonth + "/" + (month + 1) + "/" + year);
                        } else {
                            limDate = (year + "-" + (month + 1) + "-" + dayOfMonth);
                            mViewHolder.selectDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        }
                    }
                }
            }, year, month, day);
            picker.show();
        }

        if (v.getId() == R.id.refresh_button) {
            aquireData();
        }

        if (v.getId() == R.id.icon_home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        if (v.getId() == R.id.icon_report) {
            Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        if (v.getId() == R.id.icon_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        if (v.getId() == R.id.icon_news) {
            mViewHolder.newsCards.removeAllViews();
            mViewHolder.selectDate.setText(R.string.selecione_a_data);
            mViewHolder.insertCep.setText("");
            limDate = "";
            getAllNews();
            /*
            String news_content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam bibendum orci ligula, in imperdiet metus hendrerit a. Nunc maximus tortor eget orci mattis, eget convallis diam sagittis.";
            populateNewsCards("Título da notícia", news_content, "28/09/2020");
            */
        }

    }

    private void getAllNews() {
        db.collection("news")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mViewHolder.newsCards.removeAllViews();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String text = document.getString("text");
                                String location = document.getString("location");
                                String ndate = document.getString("date");
                                //String exp_date = document.getString("exp_date");
                                String ext_link = document.getString("ext_link");

                                String showDate;
                                String year, month, day;

                                year = ndate.substring(0, 4);
                                month = ndate.substring(5, 7);
                                day = ndate.substring(8, 10);

                                showDate = (day + "/" + month + "/" + year);

                                populateNewsCards(title, text, showDate, ext_link);
                            }
                        } else {
                            Toast toastFailure = Toast.makeText(getApplicationContext(),
                                    "Ocorreu um erro nas notícias, tente novamente",
                                    Toast.LENGTH_SHORT);
                            toastFailure.show();
                        }
                    }
                });
    }

    private void getLocationNews(final String limLocation) {
        db.collection("news")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mViewHolder.newsCards.removeAllViews();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String text = document.getString("text");
                                String location = document.getString("location");
                                String ndate = document.getString("date");
                                //String exp_date = document.getString("exp_date");
                                String ext_link = document.getString("ext_link");

                                String showDate;
                                String year, month, day;

                                year = ndate.substring(0, 4);
                                month = ndate.substring(5, 7);
                                day = ndate.substring(8, 10);

                                showDate = (day + "/" + month + "/" + year);

                                if (location.equals(limLocation)) {
                                    populateNewsCards(title, text, showDate, ext_link);
                                }

                            }
                        } else {
                            Toast toastFailure = Toast.makeText(getApplicationContext(),
                                    "Ocorreu um erro nas notícias, tente novamente",
                                    Toast.LENGTH_SHORT);
                            toastFailure.show();
                        }
                    }
                });
    }

    private void getDateNews() {
        db.collection("news")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mViewHolder.newsCards.removeAllViews();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String text = document.getString("text");
                                String location = document.getString("location");
                                String ndate = document.getString("date");
                                //String exp_date = document.getString("exp_date");
                                String ext_link = document.getString("ext_link");

                                String showDate;
                                String year, month, day;

                                year = ndate.substring(0, 4);
                                month = ndate.substring(5, 7);
                                day = ndate.substring(8, 10);

                                showDate = (day + "/" + month + "/" + year);

                                if (limDate.compareTo(ndate) >= 0) {
                                    populateNewsCards(title, text, showDate, ext_link);
                                }
                            }
                        } else {
                            Toast toastFailure = Toast.makeText(getApplicationContext(),
                                    "Ocorreu um erro nas notícias, tente novamente",
                                    Toast.LENGTH_SHORT);
                            toastFailure.show();
                        }
                    }
                });
    }

    private void getDateLocationNews(final String limLocation) {
        db.collection("news")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mViewHolder.newsCards.removeAllViews();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String text = document.getString("text");
                                String location = document.getString("location");
                                String ndate = document.getString("date");
                                //String exp_date = document.getString("exp_date");
                                String ext_link = document.getString("ext_link");

                                String showDate;
                                String year, month, day;

                                year = ndate.substring(0, 4);
                                month = ndate.substring(5, 7);
                                day = ndate.substring(8, 10);

                                showDate = (day + "/" + month + "/" + year);

                                if (location.equals(limLocation)) {
                                    if (limDate.compareTo(ndate) >= 0) {
                                        populateNewsCards(title, text, showDate, ext_link);
                                    }
                                }
                            }
                        } else {
                            Toast toastFailure = Toast.makeText(getApplicationContext(),
                                    "Ocorreu um erro nas notícias, tente novamente",
                                    Toast.LENGTH_SHORT);
                            toastFailure.show();
                        }
                    }
                });
    }

    public void validateCep(String cep, final int type) {
        Call<JsonObject> call = projetoAguaApi.getCep(cep);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    /*Toast toastNotSuccessful = Toast.makeText(getApplicationContext(),
                            "Código: " + response.code(),
                            Toast.LENGTH_SHORT);
                    toastNotSuccessful.show();*/

                    Toast toastNotSuccessful = Toast.makeText(getApplicationContext(),
                            "CEP inválido",
                            Toast.LENGTH_SHORT);
                    toastNotSuccessful.show();

                    return;
                }

                assert response.body() != null;
                try {
                    String bairro = response.body().get("bairro").getAsString();
                    String localidade = response.body().get("localidade").getAsString();

                    userLocation = bairro;

                    if (type == 0) {
                        getLocationNews(userLocation);
                    } else {
                        getDateLocationNews(userLocation);
                    }

                } catch (Exception e) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "CEP inválido",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast toastFailure = Toast.makeText(getApplicationContext(),
                        "Erro ao validar o CEP",
                        Toast.LENGTH_SHORT);
                toastFailure.show();

                //validateCep(mViewHolder.manualCep.getText().toString());
            }
        });
    }


    public void populateNewsCards(String title, String ns_content, String date, final String link) {
        CardView.LayoutParams cardParams = new CardView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 30);

        final CardView newsSimpleCard = (CardView) getLayoutInflater().inflate(R.layout.news_simple_card, null);
        newsSimpleCard.setLayoutParams(cardParams);

        newsSimpleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.news_simple_card_body) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(link));
                    startActivity(i);
                }
            }
        });

        TextView nss_title=newsSimpleCard.findViewById(R.id.news_simple_title);
        TextView nss_date=newsSimpleCard.findViewById(R.id.news_simple_date);
        TextView nss_content=newsSimpleCard.findViewById(R.id.news_simple_content);

        nss_title.setText(title);
        nss_date.setText(date);
        nss_content.setText(ns_content);

        this.mViewHolder.newsCards.addView(newsSimpleCard);
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