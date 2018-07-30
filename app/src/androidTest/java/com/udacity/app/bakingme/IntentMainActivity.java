package com.udacity.app.bakingme;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.hamcrest.Matchers.allOf;

/** Created by E560XT on 7/30/2018. */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class IntentMainActivity {

  @Rule
  public IntentsTestRule<MainActivity> mActivityTestRule =
      new IntentsTestRule<>(MainActivity.class);

  @Test
  public void testIntentHasRecipe() {

    ViewInteraction recyclerView = onView(allOf(withId(R.id.recycler_view_tablet)));
    recyclerView.perform(actionOnItemAtPosition(0, click()));
    intended(allOf(hasComponent(hasShortClassName(".RecipeDetailActivity"))));
  }
}
