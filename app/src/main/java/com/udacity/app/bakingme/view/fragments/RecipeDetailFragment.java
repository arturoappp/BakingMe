package com.udacity.app.bakingme.view.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.app.bakingme.R;
import com.udacity.app.bakingme.databinding.FragmentRecipeDetailBinding;
import com.udacity.app.bakingme.model.Ingredient;
import com.udacity.app.bakingme.model.Recipe;
import com.udacity.app.bakingme.model.Step;
import com.udacity.app.bakingme.view.adapters.IngredientAdapter;
import com.udacity.app.bakingme.view.adapters.StepAdapter;

/**
 * A fragment representing a list of Items.
 *
 * <p>Activities containing this fragment MUST implement the {@link
 * OnStepFragmentInteractionListener} interface.
 */
public class RecipeDetailFragment extends Fragment {

  public static final String ARG_RECIPE = "recipe";
  private static final String ARG_COLUMN_COUNT = "column-count";
  private int mColumnCount = 1;
  private OnStepFragmentInteractionListener mListenerOnStep;
  private OnIngredientFragmentInteractionListener mListenerOnIngredient;
  private Recipe recipe;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
   * screen orientation changes).
   */
  public RecipeDetailFragment() {}

  // TODO: Customize parameter initialization
  @SuppressWarnings("unused")
  public static RecipeDetailFragment newInstance(int columnCount, Recipe recipe) {
    RecipeDetailFragment fragment = new RecipeDetailFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_COLUMN_COUNT, columnCount);
    args.putParcelable(ARG_RECIPE, recipe);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
      recipe = getArguments().getParcelable(ARG_RECIPE);
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    FragmentRecipeDetailBinding binding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_detail, container, false);
    View view = binding.getRoot();

    // Set recyclers and adapters
    setRecyclerViewSteps(binding, view);
    setRecyclerViewIngredients(binding, view);
    return view;
  }

  private void setRecyclerViewIngredients(FragmentRecipeDetailBinding binding, View view) {
    Context context = view.getContext();
    RecyclerView recyclerView = binding.recyclerViewIngredients;

    if (mColumnCount <= 1) {
      recyclerView.setLayoutManager(new LinearLayoutManager(context));
    } else {
      recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
    }
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setAdapter(new IngredientAdapter(recipe.getIngredients(), mListenerOnIngredient));
  }

  private void setRecyclerViewSteps(FragmentRecipeDetailBinding binding, View view) {
    Context context = view.getContext();
    RecyclerView recyclerView = binding.recyclerViewSteps;
    ;
    if (mColumnCount <= 1) {
      recyclerView.setLayoutManager(new LinearLayoutManager(context));
    } else {
      recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
    }
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setAdapter(new StepAdapter(recipe.getSteps(), mListenerOnStep));
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnStepFragmentInteractionListener) {
      mListenerOnStep = (OnStepFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(
          context.toString() + " must implement OnStepFragmentInteractionListener");
    }

    if (context instanceof OnIngredientFragmentInteractionListener) {
      mListenerOnIngredient = (OnIngredientFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(
          context.toString() + " must implement OnIngredientFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListenerOnStep = null;
    mListenerOnIngredient = null;
  }

  /**
   * This interface must be implemented by activities that contain this fragment to allow an
   * interaction in this fragment to be communicated to the activity and potentially other fragments
   * contained in that activity.
   *
   * <p>See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html" >Communicating with
   * Other Fragments</a> for more information.
   */
  public interface OnStepFragmentInteractionListener {
    // TODO: Update argument type and name
    void onStepFragmentClickItem(Step item);
  }

  public interface OnIngredientFragmentInteractionListener {
    // TODO: Update argument type and name
    void onIngredientFragmentClickItem(Ingredient item);
  }
}
