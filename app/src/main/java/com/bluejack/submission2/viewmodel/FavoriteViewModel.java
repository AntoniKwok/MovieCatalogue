package com.bluejack.submission2.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.bluejack.submission2.service.model.entity.Favorite;
import com.bluejack.submission2.service.model.utils.FavoriteHelper;

import java.util.ArrayList;

public class FavoriteViewModel extends AndroidViewModel {

    private final FavoriteHelper favoriteHelper;

    private final MutableLiveData<ArrayList<Favorite>> listFavorites = new MutableLiveData<>();

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        favoriteHelper = FavoriteHelper.getInstance(application);
    }

    private void setListFavorites(final String type) {
        ArrayList<Favorite> favorites = favoriteHelper.getAllFavoritebyType(type);
        listFavorites.postValue(favorites);
    }

    public LiveData<ArrayList<Favorite>> getFavorites(String type) {

        setListFavorites(type);
        return listFavorites;
    }
}
