package com.bluejack.submission2.view.widget.factory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bluejack.submission2.R;
import com.bluejack.submission2.service.model.entity.Favorite;
import com.bluejack.submission2.service.model.utils.FavoriteHelper;
import com.bluejack.submission2.view.widget.FavoriteWidget;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private FavoriteHelper favoriteHelper;
    private ArrayList<Favorite> temp = new ArrayList<>();

    public StackRemoteViewsFactory(Context mContext, Intent intent) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(mContext);
        favoriteHelper.open();
    }

    @Override
    public void onDataSetChanged() {
        temp.clear();
        temp = favoriteHelper.getAllFavorite();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return temp.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.wtf("testt", temp.size() + "");
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        Bitmap preview = null;
        try {
            preview = Glide.with(mContext)
                    .asBitmap()
                    .load(temp.get(position).getCover_photo())
                    .apply(new RequestOptions().fitCenter())
                    .submit()
                    .get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        rv.setImageViewBitmap(R.id.imageView, preview);
        Bundle extras = new Bundle();
        extras.putInt(FavoriteWidget.EXTRA_ITEM, position);
        extras.putString(FavoriteWidget.EXTRA_DATA, temp.get(position).getUid());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
