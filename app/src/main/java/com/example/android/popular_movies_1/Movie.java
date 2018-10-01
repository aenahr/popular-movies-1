package com.example.android.popular_movies_1;

public class Movie{
    //contains title, release date, movie poster, vote average, and plot synopsis.
    String mTitle;
    String mReleaseDate;
    String mMoviePoster;
    double mVoteAverage;
    String mPlot;

    public  Movie(String title, String releaseDate, String moviePoster, double voteAverage, String plot){
        mTitle = title;
        mReleaseDate = releaseDate;
        mMoviePoster = moviePoster;
        mVoteAverage = voteAverage;
        mPlot = plot;
    }

    public String getTitle(){ return mTitle;}
    public String getReleaseDate(){ return mReleaseDate;}
    public String getMoviePoster(){ return mMoviePoster;}
    public double getVoteAverage(){ return mVoteAverage;}
    public String getPlot(){ return mPlot;}

}
