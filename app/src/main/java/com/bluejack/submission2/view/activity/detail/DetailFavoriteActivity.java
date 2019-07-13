package com.bluejack.submission2.view.activity.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bluejack.submission2.R;
import com.bluejack.submission2.service.model.entity.Favorite;
import com.bluejack.submission2.service.model.utils.FavoriteHelper;
import com.bluejack.submission2.view.activity.FavoriteActivity;
import com.bluejack.submission2.view.activity.SettingsActivity;
import com.bumptech.glide.Glide;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFavoriteActivity extends AppCompatActivity {
    @BindView(R.id.cover_photo)
    ImageView coverPhoto;
    @BindView(R.id.backdrop_photo)
    ImageView backdropPhoto;

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_date)
    TextView txtDate;
    @BindView(R.id.txt_desc)
    TextView txtDesc;
    @BindView(R.id.rating_value)
    TextView txtRating;

    @BindView(R.id.progress_bar_detail)
    ProgressBar progressBar;

    @BindView(R.id.rating_bar)
    RatingBar ratingBar;

    @BindView(R.id.fav_button)
    Button btnFavorite;

    private FavoriteHelper favoriteHelper;
    private Favorite favorite;

    public static final String EXTRA_FAV = "extra_fav";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_favorite);

        ButterKnife.bind(this);
        showLoading(true);

        final Favorite favoriteData = getIntent().getParcelableExtra(EXTRA_FAV);
        bind(favoriteData);

        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (favoriteHelper.checkFavorite(favoriteData.getUid())) {
            changeButton(true);
        }

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite = new Favorite();
                favorite.setUid(favoriteData.getUid());
                favorite.setTitle(favoriteData.getTitle());
                favorite.setDesc(favoriteData.getDesc());
                favorite.setReleaseDate(favoriteData.getReleaseDate());
                favorite.setVote(favoriteData.getVote());
                favorite.setCover_photo(favoriteData.getCover_photo());
                favorite.setBackdrop_photo(favoriteData.getBackdrop_photo());
                favorite.setType(favoriteData.getType());

                if (favoriteHelper.checkFavorite(favoriteData.getUid())) {
                    favoriteHelper.removeFavorite(favoriteData.getUid());
                    changeButton(false);
                    Toast.makeText(DetailFavoriteActivity.this, R.string.message_delete, Toast.LENGTH_SHORT).show();
                } else {
                    favoriteHelper.addFavorite(favorite);
                    changeButton(true);
                    Toast.makeText(DetailFavoriteActivity.this, R.string.message_save, Toast.LENGTH_SHORT).show();
                }


                Toast.makeText(DetailFavoriteActivity.this, R.string.message_save, Toast.LENGTH_SHORT).show();
            }
        });
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

    private void changeButton(Boolean state) {
        if (state) {
            btnFavorite.setBackgroundResource(R.drawable.unfavorite_button_shape);
            btnFavorite.setTextColor(Color.WHITE);
            btnFavorite.setText(R.string.unfavorite_button);
        } else {
            btnFavorite.setBackgroundResource(R.drawable.favorite_button_shape);
            btnFavorite.setTextColor(Color.parseColor("#3f51b5"));
            btnFavorite.setText(R.string.favorite_button);
        }

    }

    private void bind(Favorite favorite) {
        double roundValue = Math.round(favorite.getVote() * 10) / 10.0;
        txtTitle.setText(favorite.getTitle());
        txtDate.setText(favorite.getReleaseDate());
        txtDesc.setText(favorite.getDesc());
        txtRating.setText(String.valueOf(favorite.getVote()));
        ratingBar.setStepSize(0.5f);
        ratingBar.setRating(((float) roundValue / 10f) * 5);
        Glide.with(getApplicationContext()).load(favorite.getCover_photo()).into(coverPhoto);
        Glide.with(getApplicationContext()).load(favorite.getBackdrop_photo()).into(backdropPhoto);


        showLoading(false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        if (item.getItemId() == R.id.menu_settings) {
            i = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(i);
            return true;
        } else if (item.getItemId() == R.id.menu_favorite) {
            i = new Intent(getApplicationContext(), FavoriteActivity.class);
            startActivity(i);
            return true;
        } else if (item.getItemId() == R.id.menu_settings_notif) {
            i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(i);
            return true;
        } else {
            Intent myIntent = new Intent(getApplicationContext(), FavoriteActivity.class);
            startActivityForResult(myIntent, 0);
            return true;
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
