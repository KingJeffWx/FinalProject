package com.example.jeffreywang.finalproject;

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

public class DetailsActivity extends AppCompatActivity {

    private Button submit;
    private String movie_id;
    public TextView responseView;
    public EditText movie;

    static final String SEARCH = "https://api.themoviedb.org/3/search/movie?api_key=a62a54c8895971fd4fbb1c92d2445164&language=en-US&page=1&include_adult=false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        responseView = (TextView) findViewById(R.id.detailsResponse);
        movie = (EditText) findViewById(R.id.editText6);
        submit = (Button) findViewById(R.id.button6);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new DetailsActivity.SearchJSON().execute();
                new DetailsActivity.ParseJSON().execute();
            }
        });

    }

    class SearchJSON extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            responseView.setText("");
        }

        protected String doInBackground(Void... urls) {
            String movie_title = movie.getText().toString();
            movie_title = movie_title.replaceAll("\\s", "%20");
            try {
                URL url = new URL(SEARCH + "&query=" + movie_title);
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
                for (int i = 0; i < 1; i++) {
                    JSONObject result = resultsArray.getJSONObject(i);

                    output += result.getString("id");
                }
                movie_id = output;
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        }
    }

    class ParseJSON extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            responseView.setText("");
        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/" + movie_id + "?api_key=a62a54c8895971fd4fbb1c92d2445164&language=en-US");
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
                JSONArray resultsArray = reader.optJSONArray("genres");
                output+= "Genre(s): ";
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject result = resultsArray.getJSONObject(i);
                    if (i == resultsArray.length() - 1) {
                        output += result.getString("name") + "\n";
                    }
                    else {
                        output += result.getString("name") + ", ";
                    }

                }
                output += "Overview: " + reader.getString("overview") + "\n";
                output += "Rating: " + reader.getString("vote_average") + "\n";
                output += "Release date: " + reader.getString("release_date") + "\n";
                String formattedNumber = String.format("%,d", reader.getInt("revenue"));
                output += "Revenue: $" + formattedNumber + "\n";
                output += "Runtime: " + reader.getString("runtime") + " minutes" + "\n";
                output += "Tagline: " + reader.getString("tagline") + "\n";
                responseView.setText(output);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        }
    }
}
