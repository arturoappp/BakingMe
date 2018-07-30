package com.udacity.app.bakingme.view.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.app.bakingme.R;
import com.udacity.app.bakingme.databinding.RecipeRowItemBinding;
import com.udacity.app.bakingme.model.Recipe;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.MyViewHolder> {

  private List<Recipe> recipeList;
  private LayoutInflater layoutInflater;
  private RecipeAdapterListener listener;

  public RecipesAdapter(List<Recipe> recipeList, RecipeAdapterListener listener) {
    this.recipeList = recipeList;
    this.listener = listener;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (layoutInflater == null) {
      layoutInflater = LayoutInflater.from(parent.getContext());
    }
    RecipeRowItemBinding binding =
        DataBindingUtil.inflate(layoutInflater, R.layout.recipe_row_item, parent, false);
    return new MyViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, final int position) {
    holder.binding.setRecipe(recipeList.get(position));
    holder
        .binding
        .getRoot()
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                if (listener != null) {
                  listener.onRecipetClicked(recipeList.get(position));
                }
              }
            });
  }

  public void setRecipeList(List<Recipe> recipeList) {
    this.recipeList = recipeList;
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    if (recipeList == null) return 0;
    return recipeList.size();
  }

  public interface RecipeAdapterListener {
    public void onRecipetClicked(Recipe post);
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {

    private final RecipeRowItemBinding binding;

    public MyViewHolder(final RecipeRowItemBinding itemBinding) {
      super(itemBinding.getRoot());
      this.binding = itemBinding;
    }
  }
}
