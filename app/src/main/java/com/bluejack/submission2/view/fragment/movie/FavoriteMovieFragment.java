package com.bluejack.submission2.view.fragment.movie;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluejack.submission2.R;
import com.bluejack.submission2.service.model.entity.Favorite;
import com.bluejack.submission2.view.activity.detail.DetailFavoriteActivity;
import com.bluejack.submission2.view.adapter.FavoriteAdapter;
import com.bluejack.submission2.view.interfaces.FavoriteItemClick;
import com.bluejack.submission2.viewmodel.FavoriteViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteMovieFragment extends Fragment {

    @BindView(R.id.my_movie_favorite)
    RecyclerView favoriteRecyclerView;
    @BindView(R.id.progress_bar_favorite_movie)
    ProgressBar progressBar;
    @BindView(R.id.empty_view_movie)
    TextView emptyTxt;

    private FavoriteAdapter adapter;

    public FavoriteMovieFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FavoriteViewModel favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        favoriteViewModel.getFavorites("movie").observe(this, getFavorites);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        showLoading(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        favoriteRecyclerView.setLayoutManager(layoutManager);

        adapter = new FavoriteAdapter(getContext());
        favoriteRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickCallback(new FavoriteItemClick() {
            @Override
            public void onItemClicked(Favorite favorite) {
                selectedItem(favorite);
            }
        });
    }

    private void selectedItem(Favorite favorite) {
        Intent i = new Intent(getContext(), DetailFavoriteActivity.class);
        i.putExtra(DetailFavoriteActivity.EXTRA_FAV, favorite);
        startActivity(i);
    }


    private final Observer<ArrayList<Favorite>> getFavorites = new Observer<ArrayList<Favorite>>() {
        @Override
        public void onChanged(ArrayList<Favorite> favoriteArrayList) {
            adapter.setFavorites(favoriteArrayList);
            if (adapter.getItemCount() == 0) {
                favoriteRecyclerView.setVisibility(View.GONE);
                emptyTxt.setVisibility(View.VISIBLE);
            }


            showLoading(false);
        }
    };

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
