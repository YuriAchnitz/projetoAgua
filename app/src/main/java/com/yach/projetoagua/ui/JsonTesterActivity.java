package com.yach.projetoagua.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
    private ProjetoAguaApi projetoAguaApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_tester);

        jsonResult = findViewById(R.id.get_json);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viniferr-watermonitor.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        projetoAguaApi = retrofit.create(ProjetoAguaApi.class);

        //getApi();
        //getAllDates();
        //getAllCep();
        getNews();
    }

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

        public void getNews(){
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

                    for (Post post : posts){
                        if(post.getType().equals("news"))
                        {
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
    }
}