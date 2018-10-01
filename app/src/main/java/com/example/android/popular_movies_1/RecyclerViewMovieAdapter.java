package com.example.android.popular_movies_1;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class RecyclerViewMovieAdapter extends RecyclerView.Adapter<RecyclerViewMovieAdapter.MovieAdapterViewHolder> {

    private String[] mData;
    private final MovieAdapterOnClickHandler mClickHandler;
    private Context mContext;

    public interface MovieAdapterOnClickHandler{
        void onClick(String movieData);
    }

    public RecyclerViewMovieAdapter(MovieAdapterOnClickHandler clickHandler, Context c){
        mClickHandler = clickHandler;
        mContext = c;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView mMoviePoster;

        public MovieAdapterViewHolder(View view){
            super(view);
            mMoviePoster = (ImageView)view.findViewById(R.id.iv_movie_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String movieInfo = mData[adapterPosition];
            mClickHandler.onClick(movieInfo);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        Context context = viewGroup.getContext();
        int layoutIdForGridItem = R.layout.movie_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttackToParentImmediately = false;

        View view = inflater.inflate(layoutIdForGridItem, viewGroup, shouldAttackToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position){
        String movieInfo = mData[position];
        JSONObject movie = null;
        try {
            movie = new JSONObject(movieInfo);
            String imageURL = "http://image.tmdb.org/t/p/w780/" + movie.getString("poster_path");
            Picasso.with(mContext)
                    .load(imageURL)
                    .error(R.drawable.image_error_placeholder)
                    .placeholder(R.drawable.image_placeholder)
                    .into(movieAdapterViewHolder.mMoviePoster);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        }

    @Override
    public int getItemCount(){
        if(null == mData) return 0;
        return mData.length;
    }

    public void setMovieData(String[] movieData){
        mData = movieData;
        notifyDataSetChanged();
    }

}
