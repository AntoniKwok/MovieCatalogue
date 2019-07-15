package com.bluejack.submission2.view.fragment.tvshow;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;

import com.bluejack.submission2.R;
import com.bluejack.submission2.service.model.Film;
import com.bluejack.submission2.service.model.notifications.NewReleaseAlarmManager;
import com.bluejack.submission2.service.model.notifications.RepeatingAlarmManager;
import com.bluejack.submission2.view.activity.detail.DetailActivity;
import com.bluejack.submission2.view.adapter.FilmAdapter;
import com.bluejack.submission2.view.interfaces.ItemClickSupport;
import com.bluejack.submission2.viewmodel.MainViewModel;
import com.bluejack.submission2.viewmodel.SearchVIewModel;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TVShowFragment extends Fragment {

    @BindView(R.id.myRecyclerView)
    RecyclerView movieRecyclerView;
    @BindView(R.id.progress_bar_tv)
    ProgressBar progressBar;
    private FilmAdapter adapter;


    public TVShowFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getFilms("tv").observe(this, getFilms);

        RepeatingAlarmManager repeatingAlarmManager = new RepeatingAlarmManager(getActivity());

        NewReleaseAlarmManager newReleaseAlarmManager = new NewReleaseAlarmManager(getActivity());


    }

    private final Observer<ArrayList<Film>> getFilms = new Observer<ArrayList<Film>>() {
        @Override
        public void onChanged(ArrayList<Film> filmItems) {
            adapter.setFilms(filmItems);
            showLoading(false);
        }
    };

    private final Observer<ArrayList<Film>> getFiltered = new Observer<ArrayList<Film>>() {
        @Override
        public void onChanged(ArrayList<Film> filmItems) {
            adapter.setFilms(filmItems);
//            movieRecyclerView.setAdapter(adapter);
            showLoading(false);
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        showLoading(true);
        setHasOptionsMenu(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        movieRecyclerView.setLayoutManager(layoutManager);

        if (getContext() != null) {
            adapter = new FilmAdapter(getContext());
            movieRecyclerView.setAdapter(adapter);

            adapter.setOnItemClickCallback(new ItemClickSupport() {
                @Override
                public void onItemClicked(Film film) {
                    selectedFilm(film);
                }
            });

        }

    }

    private void selectedFilm(Film film) {
        Intent i = new Intent(getContext(), DetailActivity.class);
        i.putExtra(DetailActivity.EXTRA_FILM, film);
        startActivity(i);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
//        SearchView searchView = new SearchView(Objects.requireNonNull(((MainActivity) context).getSupportActionBar()).getThemedContext());
        SearchView searchView = (SearchView) menuItem.getActionView();

        MenuItemCompat.setActionView(menuItem, searchView);

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                getData(s);
                return false;
            }

            void getData(String s) {
                if (s.toLowerCase().trim().equals("")) {
                    if (getActivity() != null) {
                        MainViewModel mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
                        mainViewModel.getFilms("tv").observe(TVShowFragment.this, getFilms);
                    }
                } else {
                    if (getActivity() != null) {
                        SearchVIewModel searchVIewModel;
                        searchVIewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SearchVIewModel.class);
                        searchVIewModel.getFilms("tv", s).observe(TVShowFragment.this, getFiltered);
                    }

                }

            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tvshow, container, false);
    }


    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
