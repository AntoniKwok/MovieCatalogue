package com.bluejack.submission2.view.activity;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.bluejack.submission2.R;
import com.bluejack.submission2.service.model.notifications.NewReleaseAlarmManager;
import com.bluejack.submission2.service.model.notifications.RepeatingAlarmManager;
import com.bluejack.submission2.view.fragment.movie.MovieFragment;
import com.bluejack.submission2.view.fragment.tvshow.TVShowFragment;
import com.bluejack.submission2.view.interfaces.LoadFavoriteCallback;

import java.lang.ref.WeakReference;

import static com.bluejack.submission2.service.model.entity.DatabaseContract.FavColumn.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Bottom Navigation
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.navigation_movie);
        }

        RepeatingAlarmManager repeatingAlarmManager = new RepeatingAlarmManager(getApplicationContext());

        NewReleaseAlarmManager newReleaseAlarmManager = new NewReleaseAlarmManager(getApplicationContext());

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment;
        switch (menuItem.getItemId()) {
            case R.id.navigation_movie:
                fragment = new MovieFragment();
                switchFragment(fragment);
                return true;
            case R.id.navigation_tv_show:
                fragment = new TVShowFragment();
                switchFragment(fragment);
                return true;
        }
        return false;
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
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
            return super.onOptionsItemSelected(item);
        }
    }

    private static class LoadFavAsync extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavoriteCallback> weakCallback;

        private LoadFavAsync(Context context, LoadFavoriteCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor notes) {
            super.onPostExecute(notes);
            weakCallback.get().postExecute(notes);
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadFavAsync(context, (LoadFavoriteCallback) context).execute();
        }
    }
}
