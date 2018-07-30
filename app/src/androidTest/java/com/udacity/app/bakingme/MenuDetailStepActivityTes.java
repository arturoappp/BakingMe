package com.udacity.app.bakingme;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MenuDetailStepActivityTes {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule =
      new ActivityTestRule<>(MainActivity.class);

  @Test
  public void mainActivityTest2() {
    ViewInteraction recyclerView = onView(allOf(withId(R.id.recycler_view_tablet)));
    recyclerView.perform(actionOnItemAtPosition(3, click()));



    ViewInteraction recyclerView2 = onView(allOf(withId(R.id.recyclerView_steps)));
    recyclerView2.perform(actionOnItemAtPosition(0, click()));

    ViewInteraction textView =
        onView(allOf(withId(R.id.tv_description), withText("Recipe Introduction"), isDisplayed()));
    textView.check(matches(withText("Recipe Introduction")));
  }
}
