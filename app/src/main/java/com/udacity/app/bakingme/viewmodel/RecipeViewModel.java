package com.udacity.app.bakingme.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.app.bakingme.model.Recipe;
import com.udacity.app.bakingme.utilities.NetworkUtils;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

  private MutableLiveData<List<Recipe>> liveData;

  public RecipeViewModel(@NonNull Application application) {
    super(application);
  }

  public LiveData<List<Recipe>> getLiveData() {
    if (liveData == null) {
      liveData = new MutableLiveData<List<Recipe>>();
      loadRecipes();
    }
    return liveData;
  }

  private void loadRecipes() {
    // do async operation to fetch liveData
    // Instantiate the RequestQueue.
    RequestQueue queue = Volley.newRequestQueue(this.getApplication());
    // Request a string response from the provided URL.
    StringRequest stringRequest =
        new StringRequest(
            Request.Method.GET,
            NetworkUtils.URL,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                String json = response;
                Gson gson = new Gson();

                TypeToken<List<Recipe>> token = new TypeToken<List<Recipe>>() {};
                List<Recipe> recipes = gson.fromJson(json, token.getType());
                liveData.setValue(recipes);
              }
            },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {}
            });
    // Add the request to the RequestQueue.
    queue.add(stringRequest);
  }
}
