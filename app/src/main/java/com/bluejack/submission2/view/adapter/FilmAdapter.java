package com.bluejack.submission2.view.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bluejack.submission2.R;
import com.bluejack.submission2.service.model.Film;
import com.bluejack.submission2.view.interfaces.ItemClickSupport;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> implements Filterable {

    private final Context context;
    private ArrayList<Film> films;
    private ArrayList<Film> fullFilms;

    private ItemClickSupport itemClickSupport;

    public void setOnItemClickCallback(ItemClickSupport itemClickSupport) {
        this.itemClickSupport = itemClickSupport;
    }

    public FilmAdapter(Context context) {
        this.context = context;
        films = new ArrayList<>();
    }

    private ArrayList<Film> getFilms() {
        return films;
    }

    public void setFilms(ArrayList<Film> films) {
        this.films = films;
        fullFilms = new ArrayList<>(films);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_film, viewGroup, false);
        return new FilmViewHolder(view);
    }

    private Object getItem(int position) {
        return films.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final FilmViewHolder filmViewHolder, int i) {
        final Film film = (Film) getItem(i);


        filmViewHolder.bind(i);
        filmViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickSupport.onItemClicked(film);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getFilms().size();
    }

    @Override
    public Filter getFilter() {
        return filterFilm;
    }

    private final Filter filterFilm = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Film> filteredFilm = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredFilm.addAll(fullFilms);
            } else {
                String query = constraint.toString().toLowerCase().trim();

                for (Film film : fullFilms) {
                    if (film.getTitle().toLowerCase().contains(query)) {
                        filteredFilm.add(film);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredFilm;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            films.clear();
            films.addAll((ArrayList<Film>) results.values);
            notifyDataSetChanged();
        }
    };


    class FilmViewHolder extends RecyclerView.ViewHolder {
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

        FilmViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(int i) {
            txtTitle.setText(getFilms().get(i).getTitle());
            txtDate.setText(getFilms().get(i).getDate());
            Glide.with(context).load(getFilms().get(i).getPhoto()).into(coverPhoto);
            txtRating.setText(String.valueOf(getFilms().get(i).getVote()));
            ratingBar.setStepSize(0.5f);
            ratingBar.setRating(((float) getFilms().get(i).getVote() / 10f) * 5);
        }
    }
}
