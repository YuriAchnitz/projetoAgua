package com.yach.projetoagua.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yach.projetoagua.R;
import com.yach.projetoagua.data.Post;
import com.yach.projetoagua.data.ProjetoAguaApi;
import com.yach.projetoagua.data.UserData;
import com.yach.projetoagua.data.UserPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("ALL")
public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    private ProjetoAguaApi projetoAguaApi;
    private ViewHolder mViewHolder = new ViewHolder();
    private UserPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        this.mSharedPreferences = new UserPreferences(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viniferr-watermonitor.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        projetoAguaApi = retrofit.create(ProjetoAguaApi.class);

        this.mSharedPreferences = new UserPreferences(this);

        this.mViewHolder.gotoHome = findViewById(R.id.icon_home);
        this.mViewHolder.gotoNews = findViewById(R.id.icon_news);
        this.mViewHolder.gotoSettings = findViewById(R.id.icon_settings);

        this.mViewHolder.gotoHome.setOnClickListener(this);
        this.mViewHolder.gotoNews.setOnClickListener(this);
        this.mViewHolder.gotoSettings.setOnClickListener(this);

        this.mViewHolder.reportName = findViewById(R.id.report_name);
        this.mViewHolder.reportCep = findViewById(R.id.report_cep);
        this.mViewHolder.reportDescription = findViewById(R.id.report_description);

        this.mViewHolder.postReport = findViewById(R.id.post_report);
        this.mViewHolder.postReport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.icon_home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        if (v.getId() == R.id.icon_news) {
            Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
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

        if (v.getId() == R.id.post_report) {
            String user = mSharedPreferences.getStorageString(UserData.USER_ID);
            String name = mViewHolder.reportName.getText().toString();
            String location = mViewHolder.reportCep.getText().toString();
            String desc = mViewHolder.reportDescription.getText().toString();
            String date;

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            date = df.format(c);

            if (location.equals("")) {
                Toast toastNotSuccessful = Toast.makeText(getApplicationContext(),
                        "Por favor, insira seu CEP",
                        Toast.LENGTH_SHORT);
                toastNotSuccessful.show();

            } else if (location.length() < 8) {
                Toast toastNotSuccessful = Toast.makeText(getApplicationContext(),
                        "CEP inválido",
                        Toast.LENGTH_SHORT);
                toastNotSuccessful.show();

            } else if (desc.equals("")) {
                Toast toastNotSuccessful = Toast.makeText(getApplicationContext(),
                        "Por favor, insira a descrição do problema",
                        Toast.LENGTH_SHORT);
                toastNotSuccessful.show();

            } else {
                postReport(user, name, location, desc, date);
            }

        }
    }

    private void postReport(String user, String name, String location, String desc, String date) {
        Post post = new Post(user, name, location, desc, date);

        Call<Post> call = projetoAguaApi.createPost(post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (!response.isSuccessful()) {
                    Toast toastNotSuccessful = Toast.makeText(getApplicationContext(),
                            "Code: " + response.code(),
                            Toast.LENGTH_SHORT);
                    toastNotSuccessful.show();
                    return;
                }

                Toast toastSuccessful = Toast.makeText(getApplicationContext(),
                        "Sua denúncia foi enviada com sucesso",
                        Toast.LENGTH_SHORT);
                toastSuccessful.show();

                mViewHolder.reportName.setText("");
                mViewHolder.reportCep.setText("");
                mViewHolder.reportDescription.setText("");
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast toastNotSuccessful = Toast.makeText(getApplicationContext(),
                        t.getMessage(),
                        Toast.LENGTH_SHORT);
                toastNotSuccessful.show();
            }
        });
    }


    private static class ViewHolder {
        ImageButton gotoHome;
        ImageButton gotoNews;
        ImageButton gotoSettings;

        Button postReport;

        EditText reportName;
        EditText reportCep;
        EditText reportDescription;
    }
}