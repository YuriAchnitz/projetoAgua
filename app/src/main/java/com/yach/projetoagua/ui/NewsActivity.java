package com.yach.projetoagua.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.yach.projetoagua.R;

import java.util.Calendar;

public class NewsActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private VarHolder mVarHolder = new VarHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        this.mViewHolder.insertCep = findViewById(R.id.insert_cep);
        this.mViewHolder.selectDate = findViewById(R.id.select_date);
        this.mViewHolder.refreshButton = findViewById(R.id.refresh_button);

        this.mViewHolder.selectDate.setInputType(InputType.TYPE_NULL);



    }

    @Override
    public void onClick(View v) {
        //Quando clicar no selecionar data ele abre o calendario e guarda a data na string newsDate
        //depois disso ele muda o texto da view select_date pra data selecionada
        if (v.getId() == R.id.select_date) {
            final Calendar cldr = Calendar.getInstance();
            final int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);

            DatePickerDialog picker = new DatePickerDialog(NewsActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mVarHolder.newsDate = (year + "-" + (month + 1) + "-" + day);
                }
            }, year, month, day);
            picker.show();

            mViewHolder.selectDate.setText(mVarHolder.newsDate);
        }

    }

    private static class VarHolder{
        String newsDate;
    }

    private static class ViewHolder {
        EditText insertCep;
        EditText selectDate;
        ImageButton refreshButton;
    }
}