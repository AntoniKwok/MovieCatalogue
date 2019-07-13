package com.bluejack.submission2.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.bluejack.submission2.R;
import com.bluejack.submission2.view.fragment.movie.FavoriteMovieFragment;
import com.bluejack.submission2.view.fragment.tvshow.FavoriteTVShowFragment;
import com.bluejack.submission2.view.viewPageAdapter.ViewPageAdapter;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends AppCompatActivity {

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
//
//    @BindView(R.id.appBar)
//    Toolbar appBarLayout;
//
//    @BindView(R.id.back_button)
//    ImageView btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPageAdapter.addFragment(new FavoriteMovieFragment(), "Movie");
        viewPageAdapter.addFragment(new FavoriteTVShowFragment(), "TV Show");

        viewPager.setAdapter(viewPageAdapter);
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem favorite = menu.findItem(R.id.menu_favorite);
        favorite.setVisible(false);
        MenuItem search = menu.findItem(R.id.search);
        search.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;

        if (item.getItemId() == R.id.menu_settings) {
            i = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(i);
            return true;
        } else {
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(myIntent, 0);
            return true;
        }
    }
}