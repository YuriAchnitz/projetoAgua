package com.yach.projetoagua.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yach.projetoagua.data.Post;
import com.yach.projetoagua.data.ProjetoAguaApi;
import com.yach.projetoagua.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JsonTesterActivity extends AppCompatActivity {

    private TextView jsonResult;
    //private ProjetoAguaApi projetoAguaApi;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_tester);

        /*
        jsonResult = findViewById(R.id.get_json);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viniferr-watermonitor.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        projetoAguaApi = retrofit.create(ProjetoAguaApi.class);
         */

        //getApi();
        //getAllDates();
        //getAllCep();
        //getNews();
        //getFNews();
        //getFEmergency();
        //getFWarning();
    }

    public void getFNews() {
        db.collection("news")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String content = "NEWS\n";
                                String title = document.getString("title");
                                String text = document.getString("text");
                                String location = document.getString("location");
                                String date = document.getString("date");
                                String exp_date = document.getString("exp_date");
                                String ext_link = document.getString("ext_link");

                                content += "title = " + title + "\n";
                                content += "text = " + text + "\n";
                                content += "location = " + location + "\n";
                                content += "date = " + date + "\n";
                                content += "exp_date = " + exp_date + "\n";
                                content += "ext_link = " + ext_link + "\n\n";

                                //content = document.getId() + " => " + document.getData() + "\n\n";
                                jsonResult.append(content);
                            }
                        } else {
                            String content = "";
                            content = "ih serjão sujou";
                            jsonResult.append(content);
                        }
                    }
                });
    }

    public void getFEmergency() {
        db.collection("emergency")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String content = "EMERGENCY\n";
                                String title = document.getString("title");
                                String text = document.getString("text");
                                String location = document.getString("location");
                                String date = document.getString("date");
                                String exp_date = document.getString("exp_date");
                                String ext_link = document.getString("ext_link");

                                content += "title = " + title + "\n";
                                content += "text = " + text + "\n";
                                content += "location = " + location + "\n";
                                content += "date = " + date + "\n";
                                content += "exp_date = " + exp_date + "\n";
                                content += "ext_link = " + ext_link + "\n\n";
                                //content = document.getId() + " => " + document.getData() + "\n\n";
                                jsonResult.append(content);
                            }
                        } else {
                            String content = "";
                            content = "ih serjão sujou";
                            jsonResult.append(content);
                        }
                    }
                });
    }


    public void getFWarning() {
        db.collection("warnings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String content = "WARNINGS\n";
                                String title = document.getString("title");
                                String text = document.getString("text");
                                String location = document.getString("location");
                                String date = document.getString("date");
                                String exp_date = document.getString("exp_date");
                                String ext_link = document.getString("ext_link");

                                content += "title = " + title + "\n";
                                content += "text = " + text + "\n";
                                content += "location = " + location + "\n";
                                content += "date = " + date + "\n";
                                content += "exp_date = " + exp_date + "\n";
                                content += "ext_link = " + ext_link + "\n\n";
                                //content = document.getId() + " => " + document.getData() + "\n\n";
                                jsonResult.append(content);
                            }
                        } else {
                            String content = "";
                            content = "ih serjão sujou";
                            jsonResult.append(content);
                        }
                    }
                });
    }

    /*
    public void getAllCep() {
        Call<List<Post>> call = projetoAguaApi.getAllCep("2020-09-25");

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    Toast toastNotSuccessful = Toast.makeText(getApplicationContext(),
                            "Código: " + response.code(),
                            Toast.LENGTH_SHORT);
                    toastNotSuccessful.show();
                    return;
                }

                List<Post> posts = response.body();

                for (Post post : posts) {
                    String content = "ALL CEP\n";
                    content += "ID: " + post.getId() + "\n";
                    content += "Name: " + post.getName() + "\n";
                    content += "Type: " + post.getType() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n";
                    content += "Location: " + post.getLocation() + "\n";
                    content += "Date: " + post.getDate() + "\n\n";

                    jsonResult.append(content);
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

    public void getAllDates() {
        Call<List<Post>> call = projetoAguaApi.getAllDate("18060670");

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    Toast toastNotSuccessful = Toast.makeText(getApplicationContext(),
                            "Código: " + response.code(),
                            Toast.LENGTH_SHORT);
                    toastNotSuccessful.show();
                    return;
                }

                List<Post> posts = response.body();

                for (Post post : posts) {
                    String content = "ALL DATES\n";
                    content += "ID: " + post.getId() + "\n";
                    content += "Name: " + post.getName() + "\n";
                    content += "Type: " + post.getType() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n";
                    content += "Location: " + post.getLocation() + "\n";
                    content += "Date: " + post.getDate() + "\n\n";

                    jsonResult.append(content);
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

    public void getApi() {
        Call<List<Post>> call = projetoAguaApi.getPost();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    Toast toastNotSuccessful = Toast.makeText(getApplicationContext(),
                            "Código: " + response.code(),
                            Toast.LENGTH_SHORT);
                    toastNotSuccessful.show();
                    return;
                }

                List<Post> posts = response.body();

                for (Post post : posts) {
                    String content = "ALL EVERYTHING\n";
                    content += "ID: " + post.getId() + "\n";
                    content += "Name: " + post.getName() + "\n";
                    content += "Type: " + post.getType() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n";
                    content += "Location: " + post.getLocation() + "\n";
                    content += "Date: " + post.getDate() + "\n\n";

                    jsonResult.append(content);
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

    public void getNews() {
        Call<List<Post>> call = projetoAguaApi.getPost();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    Toast toastNotSuccessful = Toast.makeText(getApplicationContext(),
                            "Código: " + response.code(),
                            Toast.LENGTH_SHORT);
                    toastNotSuccessful.show();
                    return;
                }

                List<Post> posts = response.body();

                for (Post post : posts) {
                    if (post.getType().equals("news")) {
                        String content = "";
                        content += "ID: " + post.getId() + "\n";
                        content += "Name: " + post.getName() + "\n";
                        content += "Type: " + post.getType() + "\n";
                        content += "Title: " + post.getTitle() + "\n";
                        content += "Text: " + post.getText() + "\n";
                        content += "Location: " + post.getLocation() + "\n";
                        content += "Date: " + post.getDate() + "\n\n";

                        jsonResult.append(content);
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
    } */
}