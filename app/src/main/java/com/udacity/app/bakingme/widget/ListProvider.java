package com.udacity.app.bakingme.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.udacity.app.bakingme.R;
import com.udacity.app.bakingme.model.Ingredient;

import java.util.ArrayList;

public class ListProvider implements RemoteViewsFactory {
  private ArrayList<Ingredient> listItemList = new ArrayList<>();
  private Context context = null;
  private int appWidgetId;

  public ListProvider(Context context, Intent intent) {
    this.context = context;
    appWidgetId =
        intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

    populateListItem();
  }

  private void populateListItem() {
    listItemList = WidgetIntentService.ingredients;
  }

  @Override
  public int getCount() {
    if (listItemList == null) return 0;
    return listItemList.size();
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public RemoteViews getViewAt(int position) {
    final RemoteViews remoteView =
        new RemoteViews(context.getPackageName(), R.layout.widget_list_row);
    Ingredient listItem = listItemList.get(position);
    remoteView.setTextViewText(R.id.tv_ingredient, listItem.getIngredient());
    remoteView.setTextViewText(R.id.tv_quantity, String.valueOf(listItem.getQuantity()));
    remoteView.setTextViewText(R.id.tv_measure, listItem.getMeasure());

    return remoteView;
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
  public boolean hasStableIds() {
    return true;
  }

  @Override
  public void onCreate() {}

  @Override
  public void onDataSetChanged() {}

  @Override
  public void onDestroy() {}
}
