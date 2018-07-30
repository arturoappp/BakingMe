package com.udacity.app.bakingme.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.app.bakingme.model.Ingredient;
import com.udacity.app.bakingme.model.Recipe;
import com.udacity.app.bakingme.utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class WidgetIntentService extends IntentService {

  public static ArrayList<Ingredient> ingredients;
  private static final String ACTION_UPDATE_WIDGETS = "com.udacity.app.bakingme.widget.action.BAZ";

  public WidgetIntentService() {
    super("WidgetIntentService");
  }

  public static void startActionUpdateWigget(Context context) {
    Intent intent = new Intent(context, WidgetIntentService.class);
    intent.setAction(ACTION_UPDATE_WIDGETS);
    context.startService(intent);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent != null) {
      final String action = intent.getAction();
      if (ACTION_UPDATE_WIDGETS.equals(action)) {
        handleActionUpdateWidget();
      }
    }
  }

  private void handleActionUpdateWidget() {
    RequestQueue queue = Volley.newRequestQueue(this.getApplication());
    StringRequest stringRequest =
        new StringRequest(
            Request.Method.GET,
            NetworkUtils.URL,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                String json = response;
                Gson gson = new Gson();
                TypeToken<List<Recipe>> token = new TypeToken<List<Recipe>>() {};
                List<Recipe> recipes = gson.fromJson(json, token.getType());
                ingredients = new ArrayList<>(recipes.get(0).getIngredients());

                AppWidgetManager appWidgetManager =
                    AppWidgetManager.getInstance(WidgetIntentService.this);
                int[] appWidgetIds =
                    appWidgetManager.getAppWidgetIds(
                        new ComponentName(WidgetIntentService.this, RecipeWidget.class));

                RecipeWidget.updateVodWidgets(
                    WidgetIntentService.this, appWidgetManager, appWidgetIds);
              }
            },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {}
            });
    queue.add(stringRequest);
  }
}
