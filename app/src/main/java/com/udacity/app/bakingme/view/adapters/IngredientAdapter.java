package com.udacity.app.bakingme.view.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.app.bakingme.R;
import com.udacity.app.bakingme.databinding.IngredientRowItemBinding;
import com.udacity.app.bakingme.model.Ingredient;
import com.udacity.app.bakingme.view.fragments.RecipeDetailFragment;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

  private final List<Ingredient> mValues;
  private LayoutInflater layoutInflater;
  private final RecipeDetailFragment.OnIngredientFragmentInteractionListener listener;

  public IngredientAdapter(
      List<Ingredient> items,
      RecipeDetailFragment.OnIngredientFragmentInteractionListener listener) {
    mValues = items;
    this.listener = listener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (layoutInflater == null) {
      layoutInflater = LayoutInflater.from(parent.getContext());
    }
    IngredientRowItemBinding binding =
        DataBindingUtil.inflate(layoutInflater, R.layout.ingredient_row_item, parent, false);
    return new IngredientAdapter.ViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, final int position) {
    holder.binding.setIngredient(mValues.get(position));
    holder
        .binding
        .getRoot()
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                if (listener != null) {
                  listener.onIngredientFragmentClickItem(mValues.get(position));
                }
              }
            });
  }

  @Override
  public int getItemCount() {
    return mValues.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    private final IngredientRowItemBinding binding;

    public ViewHolder(final IngredientRowItemBinding itemBinding) {
      super(itemBinding.getRoot());
      this.binding = itemBinding;
    }
  }
}
