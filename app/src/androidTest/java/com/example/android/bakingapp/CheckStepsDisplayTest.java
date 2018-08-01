package com.example.android.bakingapp;

import android.content.Intent;
import android.support.test.espresso.ViewFinder;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class CheckStepsDisplayTest {
    @Rule public ActivityTestRule<MainActivity> recipeRetailsActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void show_steps(){

        onView(withId(R.id.rvRecipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));


        onView(withId(R.id.frv_steps))
                .check(matches(isDisplayed()));

    }
}
