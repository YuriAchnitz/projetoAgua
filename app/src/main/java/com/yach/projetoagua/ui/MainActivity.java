package com.yach.projetoagua.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.firestore.core.OrderBy;
import com.yach.projetoagua.R;
import com.yach.projetoagua.data.UserData;
import com.yach.projetoagua.data.UserPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;


@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private UserPreferences mSharedPreferences;
    private ViewHolder mViewHolder = new ViewHolder();
    String dateOfToday;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mSharedPreferences = new UserPreferences(this);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateOfToday = sdf.format(c.getTime());

        this.mViewHolder.gotoNews = findViewById(R.id.icon_news);
        this.mViewHolder.gotoReport = findViewById(R.id.icon_report);
        this.mViewHolder.gotoSettings = findViewById(R.id.icon_settings);
        this.mViewHolder.homeRefreshButton = findViewById(R.id.icon_home);

        this.mViewHolder.gotoNews.setOnClickListener(this);
        this.mViewHolder.gotoReport.setOnClickListener(this);
        this.mViewHolder.gotoSettings.setOnClickListener(this);
        this.mViewHolder.homeRefreshButton.setOnClickListener(this);


        this.mViewHolder.errorButton = findViewById(R.id.error_button);
        this.mViewHolder.errorButton.setOnClickListener(this);

        this.mViewHolder.newsLayout = findViewById(R.id.card_news_layout);
        this.mViewHolder.emergencyLayout = findViewById(R.id.card_emergency_layout);
        this.mViewHolder.warningLayout = findViewById(R.id.card_advice_layout);

        if (mSharedPreferences.getStorageString(UserData.MANUAL_CEP).equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Adicione seu CEP na tela de configurações para receber avisos",
                    Toast.LENGTH_LONG);
            toast.show();
        }

        if (mSharedPreferences.getStorageString(UserData.USER_ID).equals("")) {
            String UserId1 = UUID.randomUUID().toString().substring(0, 9);
            String UserId2 = UUID.randomUUID().toString().substring(0, 8);
            String UserId = UserId1 + UserId2;
            mSharedPreferences.storeString(UserData.USER_ID, UserId);
        }

        /*
        Toast toast = Toast.makeText(getApplicationContext(),
                mSharedPreferences.getStorageString(UserData.USER_ID),
                Toast.LENGTH_SHORT);
        toast.show();
        */
    }

    @Override
    protected void onResume() {
        super.onResume();

        aquireData();
    }

    private void aquireData() {
        mViewHolder.emergencyLayout.removeAllViews();
        mViewHolder.warningLayout.removeAllViews();
        mViewHolder.newsLayout.removeAllViews();

        if (mSharedPreferences.getStorageString(UserData.MANUAL_CEP_NUM).length() == 8) {
            getEmergency();
            getWarning();
        }
        getNews();
    }

    @Override
    public void onClick(View v) {
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

        if (v.getId() == R.id.icon_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        if (v.getId() == R.id.icon_home) {
            aquireData();

           /*
            Intent intent = new Intent(getApplicationContext(), JsonTesterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            */
        }

        if (v.getId() == R.id.error_button) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.height = 0;
            layoutParams.width = 0;

            mViewHolder.errorButton.setLayoutParams(layoutParams);

            aquireData();
        }
    }

    private void getNews() {
        db.collection("news")
                .orderBy("date", Query.Direction.DESCENDING).limit(3)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mViewHolder.newsLayout.removeAllViews();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String text = document.getString("text");
                                //String location = document.getString("location");
                                String date = document.getString("date");
                                //String exp_date = document.getString("exp_date");
                                String ext_link = document.getString("ext_link");

                                String showDate;
                                String year, month, day;

                                year = date.substring(0, 4);
                                month = date.substring(5, 7);
                                day = date.substring(8, 10);

                                showDate = (day + "/" + month + "/" + year);

                                populateNewsCards(title, text, showDate, ext_link);
                            }
                        } else {
                            Toast toastFailure = Toast.makeText(getApplicationContext(),
                                    "Ocorreu um erro nas notícias",
                                    Toast.LENGTH_SHORT);
                            toastFailure.show();

                            callErrorButton();
                        }
                    }
                });
    }

    public void getEmergency() {
        db.collection("emergency")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mViewHolder.emergencyLayout.removeAllViews();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String text = document.getString("text");
                                String location = document.getString("location");
                                String date = document.getString("date");
                                String exp_date = document.getString("exp_date");
                                String ext_link = document.getString("ext_link");

                                if (location.equals(mSharedPreferences.getStorageString(UserData.MANUAL_CEP))) {
                                    if (dateOfToday.compareTo(date) >= 0) {
                                        if (dateOfToday.compareTo(exp_date) <= 0) {
                                            populateEmergencyCards(title, text);
                                        }
                                    }
                                }

                            }
                        } else {
                            Toast toastFailure = Toast.makeText(getApplicationContext(),
                                    "Ocorreu um erro nas emergências",
                                    Toast.LENGTH_SHORT);
                            toastFailure.show();
                        }
                    }
                });
    }

    public void getWarning() {
        db.collection("warnings")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mViewHolder.warningLayout.removeAllViews();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String text = document.getString("text");
                                String location = document.getString("location");
                                String date = document.getString("date");
                                String exp_date = document.getString("exp_date");
                                String ext_link = document.getString("ext_link");

                                if (location.equals(mSharedPreferences.getStorageString(UserData.MANUAL_CEP))) {
                                    if (dateOfToday.compareTo(date) >= 0) {
                                        if (dateOfToday.compareTo(exp_date) <= 0) {
                                            populateWarningCards(title, text);
                                        }
                                    }
                                }

                            }
                        } else {
                            Toast toastFailure = Toast.makeText(getApplicationContext(),
                                    "Ocorreu um erro nos avisos",
                                    Toast.LENGTH_SHORT);
                            toastFailure.show();
                        }
                    }
                });
    }

    public void callErrorButton() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.weight = 1.0f;
        layoutParams.gravity = Gravity.CENTER;

        mViewHolder.errorButton.setLayoutParams(layoutParams);
    }

    public void populateEmergencyCards(String title, String content) {
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        linearParams.setMargins(0, 0, 0, 50);

        CardView.LayoutParams cardParams = new CardView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 30);

        CardView emergencyCard = (CardView) getLayoutInflater().inflate(R.layout.emergency_card, null);
        emergencyCard.setLayoutParams(cardParams);

        TextView em_title = emergencyCard.findViewById(R.id.emergency_title);
        TextView em_content = emergencyCard.findViewById(R.id.emergency_content);

        em_title.setText(title);
        em_content.setText(content);

        this.mViewHolder.emergencyLayout.setLayoutParams(linearParams);
        this.mViewHolder.emergencyLayout.addView(emergencyCard);
    }

    public void populateWarningCards(String title, String content) {
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        linearParams.setMargins(0, 0, 0, 70);

        CardView.LayoutParams cardParams = new CardView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 30);

        CardView warningCard = (CardView) getLayoutInflater().inflate(R.layout.warning_card, null);
        warningCard.setLayoutParams(cardParams);

        TextView wa_title = warningCard.findViewById(R.id.warning_title);
        TextView wa_content = warningCard.findViewById(R.id.warning_content);

        wa_title.setText(title);
        wa_content.setText(content);

        this.mViewHolder.warningLayout.setLayoutParams(linearParams);
        this.mViewHolder.warningLayout.addView(warningCard);
    }

    public void populateNewsCards(String title, String content, String date, final String link) {
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        linearParams.setMargins(0, 0, 0, 35);

        CardView.LayoutParams cardParams = new CardView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 30);

        CardView newsCard = (CardView) getLayoutInflater().inflate(R.layout.news_card, null);
        newsCard.setLayoutParams(cardParams);

        newsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.news_card_body) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(link));
                    startActivity(i);
                }
            }
        });

        TextView ns_title = newsCard.findViewById(R.id.news_title);
        TextView ns_content = newsCard.findViewById(R.id.news_content);
        TextView ns_date = newsCard.findViewById(R.id.news_date);

        ns_title.setText(title);
        ns_date.setText(date);
        ns_content.setText(content);

        this.mViewHolder.newsLayout.addView(newsCard);
    }

    public void onCardClick(View v, String link) {

    }

    private static class ViewHolder {
        ImageButton gotoNews;
        ImageButton gotoReport;
        ImageButton gotoSettings;
        ImageButton homeRefreshButton;

        Button errorButton;

        LinearLayout newsLayout;

        LinearLayout emergencyLayout;

        LinearLayout warningLayout;
    }
}