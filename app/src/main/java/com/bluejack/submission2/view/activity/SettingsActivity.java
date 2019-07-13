package com.bluejack.submission2.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.bluejack.submission2.R;
import com.bluejack.submission2.service.model.notifications.NewReleaseAlarmManager;
import com.bluejack.submission2.service.model.notifications.RepeatingAlarmManager;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.switch_new)
    Switch newReleaseSwitch;
    @BindView(R.id.switch_daily)
    Switch dailyReleaseSwitch;

    private final int ID_REPEATING = 101;
    private RepeatingAlarmManager alarmManager;
    private NewReleaseAlarmManager newReleaseAlarmManager;

    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);
        alarmManager = new RepeatingAlarmManager();
        newReleaseAlarmManager = new NewReleaseAlarmManager();

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);

        boolean a = mSettings.getBoolean("new", false);
        boolean b = mSettings.getBoolean("repeat", false);
        newReleaseSwitch.setChecked(a);
        dailyReleaseSwitch.setChecked(b);
        newReleaseSwitch.setOnCheckedChangeListener(this);
        dailyReleaseSwitch.setOnCheckedChangeListener(this);
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
        } else if (item.getItemId() == R.id.menu_settings_notif) {
            i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(i);
            return true;
        } else {
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(myIntent, 0);
            return true;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor;
        switch (buttonView.getId()){
            case R.id.switch_new:
                if (isChecked) {
                    editor = mSettings.edit();
                    editor.putBoolean("new", isChecked);
                    editor.apply();
                    newReleaseAlarmManager.setOneTimeAlarm(getApplicationContext());
                } else {
                    editor = mSettings.edit();
                    editor.putBoolean("new", isChecked);
                    editor.commit();
                    newReleaseAlarmManager.cancelAlarm(getApplicationContext());
                }
                break;
            case R.id.switch_daily:
                if (isChecked) {
                    editor = mSettings.edit();
                    editor.putBoolean("repeat", true);
                    editor.apply();
                    alarmManager.setRepeatingAlarm(getApplicationContext());
                } else {
                    editor = mSettings.edit();
                    editor.putBoolean("repeat", false);
                    editor.commit();
                    alarmManager.cancelAlarm(getApplicationContext());
                }
                break;
        }
    }
}
