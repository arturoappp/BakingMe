package com.udacity.app.bakingme.widget;


import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by E560XT on 7/24/2018.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        return (new ListProvider(this.getApplicationContext(), intent));
    }
}
