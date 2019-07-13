package com.bluejack.submission2.view.widget.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.bluejack.submission2.view.widget.factory.StackRemoteViewsFactory;

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }

}
