package com.yach.projetoagua.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.yach.projetoagua.R;

import java.util.Calendar;


public class NewsActivity extends AppCompatActivity implements View.OnClickListener {

    DatePickerDialog picker;
    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        this.mViewHolder.gotoHome = findViewById(R.id.icon_home);
        this.mViewHolder.gotoReport = findViewById(R.id.icon_report);
        this.mViewHolder.gotoSettings = findViewById(R.id.icon_settings);

        this.mViewHolder.gotoHome.setOnClickListener(this);
        this.mViewHolder.gotoReport.setOnClickListener(this);
        this.mViewHolder.gotoSettings.setOnClickListener(this);


        this.mViewHolder.insertCep = findViewById(R.id.insert_cep);
        this.mViewHolder.selectDate = findViewById(R.id.select_date);
        this.mViewHolder.refreshButton = findViewById(R.id.refresh_button);

        this.mViewHolder.selectDate.setInputType(InputType.TYPE_NULL);
        this.mViewHolder.selectDate.setOnClickListener(this);

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
                    String date;
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

        if (v.getId() == R.id.icon_home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
    }

    private static class ViewHolder {
        EditText insertCep;
        Button selectDate;
        ImageButton refreshButton;

        ImageButton gotoHome;
        ImageButton gotoReport;
        ImageButton gotoSettings;
    }
}