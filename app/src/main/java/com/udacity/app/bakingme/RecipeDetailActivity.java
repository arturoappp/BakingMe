package com.udacity.app.bakingme;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.udacity.app.bakingme.databinding.ActivityRecipeDetailBinding;
import com.udacity.app.bakingme.model.Ingredient;
import com.udacity.app.bakingme.model.Recipe;
import com.udacity.app.bakingme.model.Step;
import com.udacity.app.bakingme.view.fragments.RecipeDetailFragment;
import com.udacity.app.bakingme.view.fragments.StepDetailFragment;

public class RecipeDetailActivity extends AppCompatActivity
    implements RecipeDetailFragment.OnStepFragmentInteractionListener,
        RecipeDetailFragment.OnIngredientFragmentInteractionListener,
        StepDetailFragment.OnStepDetailFragmentInteractionListener {

  public static final String RECIPE = "recipe";
  public static final String ARG_STEP = "arg_step";
  Fragment cFragment;
  Recipe recipe;
  Step step;
  private ActivityRecipeDetailBinding binding;

  private boolean mTwoPane;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);

    Toolbar toolbar = binding.toolbarRecibeDetail;
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    if (getIntent() != null) {
      recipe = getIntent().getParcelableExtra(RECIPE);
    }

    if (null != savedInstanceState) {
      step = savedInstanceState.getParcelable(ARG_STEP);
    }

    setRecipeDetailFragment();

    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
        && binding.content.fragmentStepDetail != null) {
      mTwoPane = true;
      setStepDetailFrament(recipe.getSteps().get(0));
    }
  }

  public void changeFragment(Fragment fragment) {
    cFragment = fragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager
        .beginTransaction()
        .replace(R.id.fragment_recipe_detail, fragment)
        .commitAllowingStateLoss();
  }

  public void setFragmentDetail(Fragment fragment) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager
        .beginTransaction()
        .replace(R.id.fragment_step_detail, fragment)
        .commitAllowingStateLoss();
  }

  private void setRecipeDetailFragment() {
    step = null;
    setTitle(recipe.getName());
    changeFragment(RecipeDetailFragment.newInstance(1, recipe));
  }

  private void setStepDetailFrament(Step item) {
    step = item;

    if (!mTwoPane) {
      setTitle((item.getId() == 0 ? "" : item.getId() + "-") + item.getShortDescription());
      changeFragment(StepDetailFragment.newInstance(item, recipe.getSteps().size(),recipe.getSteps().indexOf(item)));
    } else {
      setFragmentDetail(StepDetailFragment.newInstance(item, recipe.getSteps().size(),recipe.getSteps().indexOf(item)));
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  public void onStepFragmentClickItem(Step item) {
    Toast.makeText(
            getApplicationContext(), " clicked: " + item.getDescription(), Toast.LENGTH_SHORT)
        .show();
    setStepDetailFrament(item);
  }

  @Override
  public void onStepDetailFragment(Uri uri) {
    Toast.makeText(getApplicationContext(), " clicked: " + uri.toString(), Toast.LENGTH_SHORT)
        .show();
  }

  @Override
  public void onPreviousStepClicked(int index) {
    if (index < 0) {
      index = 0;
    }
    setStepDetailFrament(recipe.getSteps().get(index));
  }

  @Override
  public void onNextStepClicked(int index) {
    if (index > recipe.getSteps().size() - 1) {
      index = recipe.getSteps().size() - 1;
    }
    setStepDetailFrament(recipe.getSteps().get(index));
  }

  @Override
  public void onIngredientFragmentClickItem(Ingredient item) {}

  @Override
  public void onBackPressed() {
    if (cFragment instanceof StepDetailFragment) {
      setRecipeDetailFragment();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        if (cFragment instanceof StepDetailFragment) {
          setRecipeDetailFragment();
          return true;
        } else {
          return super.onOptionsItemSelected(item);
        }
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    savedInstanceState.putParcelable(ARG_STEP, step);
    super.onSaveInstanceState(savedInstanceState);
  }
}
