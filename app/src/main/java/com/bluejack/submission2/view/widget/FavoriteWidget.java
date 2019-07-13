package com.bluejack.submission2.view.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.bluejack.submission2.R;
import com.bluejack.submission2.service.model.entity.Favorite;
import com.bluejack.submission2.service.model.utils.FavoriteHelper;
import com.bluejack.submission2.view.activity.detail.DetailFavoriteActivity;
import com.bluejack.submission2.view.widget.service.StackWidgetService;


public class FavoriteWidget extends AppWidgetProvider {

    private static final String TOAST_ACTION = "com.bluejack.submission2.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.bluejack.submission2.EXTRA_ITEM";
    public static final String EXTRA_DATA = "com.bluejack.submission2.EXTRA_DATA";

    private FavoriteHelper favoriteHelper;
    private Favorite favorites;

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        Intent intent1 = new Intent(context, FavoriteWidget.class);
        intent1.setAction(TOAST_ACTION);

        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, intent1, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        favoriteHelper = FavoriteHelper.getInstance(context);
        if (intent.getAction() != null) {
            if (intent.getAction().equals(TOAST_ACTION)) {
                String favoriteID = intent.getStringExtra(EXTRA_DATA);
                favorites = getFavoriteData(favoriteID);
                Intent intent1 = new Intent(context, DetailFavoriteActivity.class);
                intent1.putExtra(DetailFavoriteActivity.EXTRA_FAV, favorites);
                context.startActivity(intent1);
            }
        }
    }

    private Favorite getFavoriteData(String id) {
        favoriteHelper.open();

        favorites = favoriteHelper.getAllFavoriteById(id);

        return favorites;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

