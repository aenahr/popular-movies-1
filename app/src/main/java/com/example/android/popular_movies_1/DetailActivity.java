package com.example.android.popular_movies_1;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    private TextView mTitle;
    private TextView mReleaseDate;
    private ImageView mPoster;
    private TextView mAverage;
    private TextView mPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // set up all references
        mTitle = (TextView) findViewById(R.id.tv_title);
        mReleaseDate = (TextView) findViewById(R.id.tv_release);
        mPoster = (ImageView) findViewById(R.id.iv_poster);
        mAverage = (TextView) findViewById(R.id.tv_average);
        mPlot = (TextView) findViewById(R.id.tv_plot);

        Intent intentMovie = getIntent();
        String movieJSON = "";
        if(intentMovie.hasExtra("movieData")){
            movieJSON = intentMovie.getStringExtra("movieData");
        }
        JSONObject movie = null;
        try {
            movie = new JSONObject(movieJSON);
            Movie newMovie = new Movie(movie.getString("title"), movie.getString("release_date"), movie.getString("poster_path"), movie.getDouble("vote_average"), movie.getString("overview"));
            String imageURL = "http://image.tmdb.org/t/p/w780/" + newMovie.getMoviePoster();

            // link everything to the xml objects
            Picasso.with(this)
                    .load(imageURL)
                    .error(R.drawable.image_error_placeholder)
                    .placeholder(R.drawable.image_placeholder)
                    .into(mPoster);

            mTitle.setText(newMovie.getTitle());
            mReleaseDate.setText(newMovie.getReleaseDate());
            mAverage.setText(String.valueOf(newMovie.getVoteAverage()));
            mPlot.setText(newMovie.getPlot());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
