package com.example.android.popular_movies_1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements RecyclerViewMovieAdapter.MovieAdapterOnClickHandler{

    // private api key key for moviedb
    private static final String MOVIE_API_KEY = "API_KEY_HERE";
    private static final int numColumns = 2;
    private static final String SORT_BY_POPULAR = "POPULAR";
    private static final String SORT_BY_RATING = "RATING";

    private RecyclerView mRecyclerView;
    private RecyclerViewMovieAdapter mMovieAdapter;

    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // reference recycler view from main activity
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        // set a grid layout
        GridLayoutManager layoutManager = new GridLayoutManager(this, numColumns);
        mRecyclerView.setLayoutManager(layoutManager);

        // set adapter to the recycler view
        mMovieAdapter = new RecyclerViewMovieAdapter(this, MainActivity.this);
        mRecyclerView.setAdapter(mMovieAdapter);

        loadMovieData(SORT_BY_POPULAR);
    }

    /**
     * Loads and determines how the movies will be sorted
     * @param sortBy how the movies will be sorted
     */
    private void loadMovieData(String sortBy){
        showMovieDataView();

        new FetchMovieData().execute(sortBy);
    }

    private void showMovieDataView(){
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(String movieData){
        Context context = this;

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("movieData", movieData);
        startActivity(intent);
//        Toast.makeText(context, movieData, Toast.LENGTH_LONG).show();
    }

    public class FetchMovieData extends AsyncTask<String, Void, String[]>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {
            // check how the end user wants the movies sorted
            String sort_by = "";
            if(params.length > 0){
                String sortType = params[0];
                if(sortType.equals(SORT_BY_POPULAR)){
                    sort_by = "popularity.desc";
                }else if(sortType.equals(SORT_BY_RATING)){
                    sort_by = "vote_average.desc";
                }
            }

            // append url with API key and desired sorting
            String urlString = "https://api.themoviedb.org/3/discover/movie?api_key=" + MOVIE_API_KEY + "&language=en-US&sort_by=" + sort_by + "&include_adult=false&include_video=false";
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            URL url = null;
            String[] results = new String[20];
            try {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream stream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {

                    String JSON = line;
                    JSONObject jsonResponse = new JSONObject(JSON);
                    JSONArray movies = jsonResponse.getJSONArray("results");
                    for (int i=0; i<movies.length(); i++) {
                        JSONObject movie = movies.getJSONObject(i);
//                        Movie newMovie = new Movie(movie.getString("title"), movie.getString("release_date"), movie.getString("poster_path"), movie.getDouble("vote_average"), movie.getString("overview"));
//                        results[i] = newMovie;
                        results[i] = movie.toString();
                    }
                }

            }  catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                urlConnection.disconnect();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String[] movieData){
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if( movieData != null){
                showMovieDataView();
                mMovieAdapter.setMovieData(movieData);
            } else{
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.display_order, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {
            mMovieAdapter.setMovieData(null);
            loadMovieData(SORT_BY_POPULAR);
            return true;
        } else if( id == R.id.action_rated){
            mMovieAdapter.setMovieData(null);
            loadMovieData(SORT_BY_RATING);
        }

        return super.onOptionsItemSelected(item);
    }
}
