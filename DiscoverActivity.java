package com.example.jeffreywang.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscoverActivity extends AppCompatActivity {

    private Button submit;
    public TextView responseView;
    public EditText rating;
    public EditText releasedate;
    public EditText genre;
    public EditText runtime;

    static final String API_URL = "https://api.themoviedb.org/3/discover/movie?api_key=a62a54c8895971fd4fbb1c92d2445164&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";
    static final String GENRES = "https://api.themoviedb.org/3/genre/movie/list?api_key=a62a54c8895971fd4fbb1c92d2445164&language=en-US";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        responseView = (TextView) findViewById(R.id.discoverResponse);
        rating = (EditText) findViewById(R.id.editText);
        releasedate = (EditText) findViewById(R.id.editText2);
        genre = (EditText) findViewById(R.id.editText3);
        runtime = (EditText) findViewById(R.id.editText4);
        submit = (Button) findViewById(R.id.button4);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new ParseJSON().execute();
            }
        });
    }

    protected String getGenre(String genre) {
        if(genre.equals("Action")) {
            return "28";
        }
        if(genre.equals("Adventure")) {
            return "12";
        }
        if(genre.equals("Animation")) {
            return "16";
        }
        if(genre.equals("Comedy")) {
            return "35";
        }
        if(genre.equals("Crime")) {
            return "80";
        }
        if(genre.equals("Documentary")) {
            return "99";
        }
        if(genre.equals("Drama")) {
            return "18";
        }
        if(genre.equals("Family")) {
            return "10751";
        }
        if(genre.equals("Fantasy")) {
            return "14";
        }
        if(genre.equals("History")) {
            return "36";
        }
        if(genre.equals("Horror")) {
            return "27";
        }
        if(genre.equals("Music")) {
            return "10402";
        }
        if(genre.equals("Mystery")) {
            return "9648";
        }
        if(genre.equals("Romance")) {
            return "10749";
        }
        if(genre.equals("Science Fiction")) {
            return "878";
        }
        if(genre.equals("TV Movie")) {
            return "10770";
        }
        if(genre.equals("Thriller")) {
            return "53";
        }
        if(genre.equals("War")) {
            return "10752";
        }
        if(genre.equals("Western")) {
            return "37";
        }
        return "";
    }

    class ParseJSON extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            responseView.setText("");
        }

        protected String doInBackground(Void... urls) {
            String r = rating.getText().toString();
            String d = releasedate.getText().toString();
            String g = getGenre(genre.getText().toString());
            String t = runtime.getText().toString();
            try {
                URL url = new URL(API_URL + "&vote_average.gte=" + r + "&primary_release_date.gte=" + d + "&with_genres=" + g + "&with_runtime.gte=" + t);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            try {
                String output = "";
                JSONObject reader = new JSONObject(response);
                JSONArray resultsArray = reader.optJSONArray("results");
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject result = resultsArray.getJSONObject(i);

                    output += result.getString("original_title") + ", ";
                }
                responseView.setText(output);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        }
    }
}
