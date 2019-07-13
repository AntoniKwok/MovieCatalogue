package com.bluejack.submission2.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bluejack.submission2.R;
import com.bluejack.submission2.service.model.entity.Favorite;
import com.bluejack.submission2.view.interfaces.FavoriteItemClick;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private final Context context;
    private ArrayList<Favorite> favorites;
    private FavoriteItemClick itemClickSupport;

    public void setOnItemClickCallback(FavoriteItemClick itemClickSupport) {
        this.itemClickSupport = itemClickSupport;
    }

    public FavoriteAdapter(Context context) {
        this.context = context;
        favorites = new ArrayList<>();
    }

    private ArrayList<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<Favorite> favorites) {
        this.favorites = favorites;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_film, viewGroup, false);
        return new FavoriteAdapter.FavoriteViewHolder(view);
    }

    private Object getItem(int position) {
        return favorites.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder favoriteViewHolder, int i) {

        final Favorite favorite = (Favorite) getItem(i);
        favoriteViewHolder.bind(i);

        favoriteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickSupport.onItemClicked(favorite);
            }
        });

    }

    @Override
    public int getItemCount() {
        return getFavorites().size();
    }


    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.cover_photo)
        ImageView coverPhoto;

        @BindView(R.id.txt_rating)
        TextView txtRating;
        @BindView(R.id.rating_bar_item)
        RatingBar ratingBar;

        FavoriteViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(int i) {
            double roundValue = Math.round(getFavorites().get(i).getVote() * 10) / 10.0;
            txtTitle.setText(getFavorites().get(i).getTitle());
            txtDate.setText(getFavorites().get(i).getReleaseDate());
            Glide.with(context).load(getFavorites().get(i).getCover_photo()).into(coverPhoto);
            txtRating.setText(String.valueOf(roundValue));
            ratingBar.setStepSize(0.5f);
            ratingBar.setRating(((float) roundValue / 10f) * 5);
        }
    }
}
