package com.udacity.app.bakingme.view.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.app.bakingme.R;
import com.udacity.app.bakingme.databinding.StepRowItemBinding;
import com.udacity.app.bakingme.model.Step;
import com.udacity.app.bakingme.view.fragments.RecipeDetailFragment.OnStepFragmentInteractionListener;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

  private final List<Step> mValues;
  private LayoutInflater layoutInflater;
  private final OnStepFragmentInteractionListener listener;

  public StepAdapter(List<Step> items, OnStepFragmentInteractionListener listener) {
    mValues = items;
    this.listener = listener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (layoutInflater == null) {
      layoutInflater = LayoutInflater.from(parent.getContext());
    }
    StepRowItemBinding binding =
        DataBindingUtil.inflate(layoutInflater, R.layout.step_row_item, parent, false);
    return new StepAdapter.ViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, final int position) {
    holder.binding.setStep(mValues.get(position));
    holder
        .binding
        .getRoot()
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                if (listener != null) {
                  listener.onStepFragmentClickItem(mValues.get(position));
                }
              }
            });
  }

  @Override
  public int getItemCount() {
    return mValues.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    private final StepRowItemBinding binding;

    public ViewHolder(final StepRowItemBinding itemBinding) {
      super(itemBinding.getRoot());
      this.binding = itemBinding;
    }
  }
}
