package com.yach.projetoagua.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.gson.JsonObject;
import com.yach.projetoagua.R;
import com.yach.projetoagua.data.Post;
import com.yach.projetoagua.data.ProjetoAguaApi;
import com.yach.projetoagua.data.UserPreferences;
import com.yach.projetoagua.data.UserData;

import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private UserPreferences mSharedPreferences;
    private ProjetoAguaApi projetoAguaApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.mSharedPreferences = new UserPreferences(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        projetoAguaApi = retrofit.create(ProjetoAguaApi.class);

        this.mViewHolder.switchEmergency = findViewById(R.id.switch_emergency);
        this.mViewHolder.switchWarning = findViewById(R.id.switch_warning);
        this.mViewHolder.switchNews = findViewById(R.id.switch_news);

        if (this.mSharedPreferences.getStorageString(UserData.EMERGENCY_SWITCH).equals("true")) {
            this.mViewHolder.switchEmergency.setChecked(true);
        } else {
            this.mViewHolder.switchEmergency.setChecked(false);
        }

        if (this.mSharedPreferences.getStorageString(UserData.WARNING_SWITCH).equals("true")) {
            this.mViewHolder.switchWarning.setChecked(true);
        } else {
            this.mViewHolder.switchWarning.setChecked(false);
        }

        if (this.mSharedPreferences.getStorageString(UserData.NEWS_SWITCH).equals("true")) {
            this.mViewHolder.switchNews.setChecked(true);
        } else {
            this.mViewHolder.switchNews.setChecked(false);
        }

        this.mViewHolder.gotoHome = findViewById(R.id.icon_home);
        this.mViewHolder.gotoNews = findViewById(R.id.icon_news);
        this.mViewHolder.gotoReport = findViewById(R.id.icon_report);

        this.mViewHolder.manualCep = findViewById(R.id.manual_cep);
        this.mViewHolder.addManualCep = findViewById(R.id.add_manual_cep);

        this.mViewHolder.gotoHome.setOnClickListener(this);
        this.mViewHolder.gotoNews.setOnClickListener(this);
        this.mViewHolder.gotoReport.setOnClickListener(this);

        this.mViewHolder.addManualCep.setOnClickListener(this);

        this.mViewHolder.switchEmergency.setOnClickListener(this);
        this.mViewHolder.switchWarning.setOnClickListener(this);
        this.mViewHolder.switchNews.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mViewHolder.manualCep.setText(mSharedPreferences.getStorageString(UserData.MANUAL_CEP_NUM));
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

        if (v.getId() == R.id.icon_report) {
            Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        if (v.getId() == R.id.switch_emergency) {
            if (this.mViewHolder.switchEmergency.isChecked()) {
                this.mSharedPreferences.storeString(UserData.EMERGENCY_SWITCH, UserData.TRUE);
            } else {
                this.mSharedPreferences.storeString(UserData.EMERGENCY_SWITCH, UserData.FALSE);
            }
        }

        if (v.getId() == R.id.switch_warning) {
            if (this.mViewHolder.switchWarning.isChecked()) {
                this.mSharedPreferences.storeString(UserData.WARNING_SWITCH, UserData.TRUE);
            } else {
                this.mSharedPreferences.storeString(UserData.WARNING_SWITCH, UserData.FALSE);
            }
        }

        if (v.getId() == R.id.switch_news) {
            if (this.mViewHolder.switchNews.isChecked()) {
                this.mSharedPreferences.storeString(UserData.NEWS_SWITCH, UserData.TRUE);
            } else {
                this.mSharedPreferences.storeString(UserData.NEWS_SWITCH, UserData.FALSE);
            }
        }

        if (v.getId() == R.id.add_manual_cep) {
            if (mViewHolder.manualCep.length() == 8) {
                String cep = mViewHolder.manualCep.getText().toString();
                validateCep(cep);
            } else if (mViewHolder.manualCep.length() == 0) {
                mSharedPreferences.storeString(UserData.MANUAL_CEP, "");
                mSharedPreferences.storeString(UserData.MANUAL_CEP_NUM, "");
                Toast toast = Toast.makeText(getApplicationContext(),
                        "CEP removido",
                        Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toastFailure = Toast.makeText(getApplicationContext(),
                        "CEP inválido",
                        Toast.LENGTH_SHORT);
                toastFailure.show();
            }

            //mViewHolder.manualCep.setText(mSharedPreferences.getStorageString(UserData.MANUAL_CEP));
        }
    }

    public void validateCep(String cep) {
        Call<JsonObject> call = projetoAguaApi.getCep(cep);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    Toast toastNotSuccessful = Toast.makeText(getApplicationContext(),
                            "Código: " + response.code(),
                            Toast.LENGTH_SHORT);
                    toastNotSuccessful.show();

                    /*Toast toastNotSuccessful = Toast.makeText(getApplicationContext(),
                            "Erro para validar o CEP",
                            Toast.LENGTH_SHORT);
                    toastNotSuccessful.show();*/

                    return;
                }

                assert response.body() != null;
                try {
                    String bairro = response.body().get("bairro").getAsString();
                    String localidade = response.body().get("localidade").getAsString();

                    mSharedPreferences.storeString(UserData.MANUAL_CEP, bairro);
                    mSharedPreferences.storeString(UserData.MANUAL_CEP_NUM, mViewHolder.manualCep.getText().toString());

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Sua localização agora é: " + bairro + " - " + mViewHolder.manualCep.getText().toString() + " - " + localidade,
                            Toast.LENGTH_LONG);
                    toast.show();
                } catch (Exception e) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Erro ao validar CEP",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast toastFailure = Toast.makeText(getApplicationContext(),
                        "Sem resposta: " + t.getMessage(),
                        Toast.LENGTH_LONG);
                toastFailure.show();

                //validateCep(mViewHolder.manualCep.getText().toString());
            }
        });
    }

    private static class ViewHolder {
        ImageButton gotoHome;
        ImageButton gotoNews;
        ImageButton gotoReport;

        EditText manualCep;
        Button addManualCep;

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch switchEmergency;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch switchWarning;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch switchNews;
    }
}