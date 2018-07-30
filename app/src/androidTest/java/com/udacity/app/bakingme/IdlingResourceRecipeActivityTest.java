/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.udacity.app.bakingme;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class IdlingResourceRecipeActivityTest {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule =
      new ActivityTestRule<>(MainActivity.class);

  private IdlingResource mIdlingResource;

  @Before
  public void registerIdlingResource() {
    mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
    Espresso.registerIdlingResources(mIdlingResource);
  }

  @Test
  public void idlingResourceTest() {
    ViewInteraction recyclerView = onView(allOf(withId(R.id.recycler_view_tablet)));
    recyclerView.perform(actionOnItemAtPosition(2, click()));
  }

  @After
  public void unregisterIdlingResource() {
    if (mIdlingResource != null) {
      Espresso.unregisterIdlingResources(mIdlingResource);
    }
  }
}
