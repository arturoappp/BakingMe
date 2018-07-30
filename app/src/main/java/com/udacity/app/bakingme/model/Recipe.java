package com.udacity.app.bakingme.model;

import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

  @SerializedName("id")
  @Expose
  private Integer id;

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("ingredients")
  @Expose
  private List<Ingredient> ingredients = new ArrayList<Ingredient>();

  @SerializedName("steps")
  @Expose
  private List<Step> steps = new ArrayList<Step>();

  @SerializedName("servings")
  @Expose
  private Integer servings;

  @SerializedName("image")
  @Expose
  private String image;

  @BindingAdapter("image")
  public static void setImage(ImageView view, String image) {
    Glide.with(view.getContext()).load(image).into(view);
  }

  public static final Parcelable.Creator<Recipe> CREATOR =
      new Creator<Recipe>() {

        @SuppressWarnings({"unchecked"})
        public Recipe createFromParcel(Parcel in) {
          return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
          return (new Recipe[size]);
        }
      };

  protected Recipe(Parcel in) {
    this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
    this.name = ((String) in.readValue((String.class.getClassLoader())));
    in.readList(
        this.ingredients, (com.udacity.app.bakingme.model.Ingredient.class.getClassLoader()));
    in.readList(this.steps, (com.udacity.app.bakingme.model.Step.class.getClassLoader()));
    this.servings = ((Integer) in.readValue((Integer.class.getClassLoader())));
    this.image = ((String) in.readValue((String.class.getClassLoader())));
  }

  public Recipe() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public void setIngredients(List<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }

  public List<Step> getSteps() {
    return steps;
  }

  public void setSteps(List<Step> steps) {
    this.steps = steps;
  }

  public Integer getServings() {
    return servings;
  }

  public void setServings(Integer servings) {
    this.servings = servings;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(id);
    dest.writeValue(name);
    dest.writeList(ingredients);
    dest.writeList(steps);
    dest.writeValue(servings);
    dest.writeValue(image);
  }

  public int describeContents() {
    return 0;
  }
}
