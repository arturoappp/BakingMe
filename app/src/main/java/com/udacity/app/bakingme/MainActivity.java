package com.udacity.app.bakingme;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.udacity.app.bakingme.IdlingResource.SimpleIdlingResource;
import com.udacity.app.bakingme.databinding.ActivityMainBinding;
import com.udacity.app.bakingme.model.Recipe;
import com.udacity.app.bakingme.utilities.NetworkUtils;
import com.udacity.app.bakingme.view.adapters.RecipesAdapter;
import com.udacity.app.bakingme.viewmodel.RecipeViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity
    implements RecipesAdapter.RecipeAdapterListener {

  private RecipesAdapter mAdapter;
  private RecyclerView recyclerView;
  private ActivityMainBinding binding;
  Recipe mRecipe;

  @Nullable private SimpleIdlingResource mIdlingResource;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // setContentView(R.layout.activity_main);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    binding.content.progress.setVisibility(View.VISIBLE);
    RecipeViewModel recipesViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
    if (NetworkUtils.isOnline(this)) {
      initRecyclerView();
      LiveData<List<Recipe>> liveData = recipesViewModel.getLiveData();

      if (mIdlingResource != null) {
        mIdlingResource.setIdleState(true);
      }

      liveData.observe(
          this,
          new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
              if (recipes != null) {
                // update the UI here with values in the snapshot
                updateUI(recipes);
                binding.content.progress.setVisibility(View.INVISIBLE);
              }
            }
          });
    } else {
      binding.content.tvNotOnline.setVisibility(View.VISIBLE);
    }
  }

  @VisibleForTesting
  @NonNull
  public IdlingResource getIdlingResource() {
    if (mIdlingResource == null) {
      mIdlingResource = new SimpleIdlingResource();
    }
    return mIdlingResource;
  }

  private void initRecyclerView() {
    int count_column = 1;
    if (binding.content.recyclerViewTablet != null) {
      recyclerView = binding.content.recyclerViewTablet;
      count_column = 2;
    } else {
      recyclerView = binding.content.recyclerView;
    }
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      count_column = 3;
    }
    recyclerView.setLayoutManager(new GridLayoutManager(this, count_column));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    mAdapter = new RecipesAdapter(null, this);
    recyclerView.setAdapter(mAdapter);
  }

  private void updateUI(List<Recipe> recipes) {
    mAdapter.setRecipeList(recipes);
    if (mIdlingResource != null) {
      mIdlingResource.setIdleState(true);
    }
  }

  @Override
  public void onRecipetClicked(Recipe recipe) {
    Toast.makeText(getApplicationContext(), "clicked: " + recipe.getName(), Toast.LENGTH_SHORT)
        .show();

    Intent intent = new Intent(this, RecipeDetailActivity.class);
    intent.putExtra(RecipeDetailActivity.RECIPE, recipe);
    startActivity(intent);
  }
}
