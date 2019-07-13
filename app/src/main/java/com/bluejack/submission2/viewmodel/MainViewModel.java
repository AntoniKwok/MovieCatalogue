package com.bluejack.submission2.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.bluejack.submission2.BuildConfig;
import com.bluejack.submission2.service.model.Film;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

//viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
public class MainViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Film>> listFilms = new MutableLiveData<>();

    private void setFilms(final String type) {

        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Film> films = new ArrayList<>();
        films.clear();
        String url = "https://api.themoviedb.org/3/discover/"+type+"?api_key="+ BuildConfig.API_KEY+"&language=en-US";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject obj = new JSONObject(result);
                    JSONArray arr = obj.getJSONArray("results");

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject filmObj = arr.getJSONObject(i);
                        Film film = new Film();

                        film.setPhoto("https://image.tmdb.org/t/p/original" + filmObj.getString("poster_path"));
                        film.setBack_photo("https://image.tmdb.org/t/p/original" + filmObj.getString("backdrop_path"));
                        film.setVote(Double.parseDouble(filmObj.getString("vote_average")));
                        film.setDesc(filmObj.getString("overview"));
                        film.setType(type);
                        film.setId(type + "_" + filmObj.getString("id"));
                        if(type.equalsIgnoreCase("tv")){
                            film.setTitle(filmObj.getString("name"));
                            film.setDate(filmObj.getString("first_air_date"));
                        }else{
                            film.setTitle(filmObj.getString("title"));
                            film.setDate(filmObj.getString("release_date"));
                        }

                        films.add(film);

                    }
                    listFilms.postValue(films);


                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    public LiveData<ArrayList<Film>> getFilms(String type) {

        setFilms(type);
        return listFilms;
    }


}
