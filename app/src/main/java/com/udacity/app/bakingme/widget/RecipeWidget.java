package com.udacity.app.bakingme.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.udacity.app.bakingme.R;

public class RecipeWidget extends AppWidgetProvider {

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
    RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
    appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    WidgetIntentService.startActionUpdateWigget(context);
  }

  private static RemoteViews updateWidgetListView(Context context, int appWidgetId) {
    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
    Intent svcIntent = new Intent(context, WidgetService.class);
    svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
    remoteViews.setRemoteAdapter(R.id.listViewWidget, svcIntent);
    remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
    return remoteViews;
  }

  public static void updateVodWidgets(
      Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
  }
}
