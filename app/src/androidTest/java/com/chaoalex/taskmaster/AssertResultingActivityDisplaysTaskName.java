package com.chaoalex.taskmaster;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AssertResultingActivityDisplaysTaskName {

  @Rule
  public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
          new ActivityScenarioRule<>(MainActivity.class);

  @Test
  public void assertResultingActivityDisplaysTaskName() {
    ViewInteraction materialButton = onView(
            allOf(withId(R.id.MainActivityMoveToAddTasksButton), withText("Add Tasks"),
                    childAtPosition(
                            childAtPosition(
                                    withId(android.R.id.content),
                                    0),
                            1),
                    isDisplayed()));
    materialButton.perform(click());

    ViewInteraction appCompatEditText = onView(
            allOf(withId(R.id.AddTasksActivityTaskTitleInputTextView),
                    childAtPosition(
                            childAtPosition(
                                    withId(android.R.id.content),
                                    0),
                            1),
                    isDisplayed()));
    appCompatEditText.perform(replaceText("Test Task"), closeSoftKeyboard());

    ViewInteraction appCompatMultiAutoCompleteTextView = onView(
            allOf(withId(R.id.AddTaskDescriptionTaskDescriptionMultiAutoCompleteTextView),
                    childAtPosition(
                            childAtPosition(
                                    withId(android.R.id.content),
                                    0),
                            5),
                    isDisplayed()));
    appCompatMultiAutoCompleteTextView.perform(replaceText("Test Description"), closeSoftKeyboard());

    ViewInteraction appCompatSpinner = onView(
            allOf(withId(R.id.AddTasksActivityStateSpinner),
                    childAtPosition(
                            childAtPosition(
                                    withId(android.R.id.content),
                                    0),
                            4),
                    isDisplayed()));
    appCompatSpinner.perform(click());

    DataInteraction materialTextView = onData(anything())
            .inAdapterView(childAtPosition(
                    withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                    0))
            .atPosition(3);
    materialTextView.perform(click());

    ViewInteraction materialButton2 = onView(
            allOf(withId(R.id.AddTasksActivitySaveTaskButton), withText("Add Tasks"),
                    childAtPosition(
                            childAtPosition(
                                    withId(android.R.id.content),
                                    0),
                            2),
                    isDisplayed()));
    materialButton2.perform(click());

    pressBack();

    ViewInteraction recyclerView = onView(
            allOf(withId(R.id.taskFragmentTextView),
                    childAtPosition(
                            withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                            5)));
    recyclerView.perform(actionOnItemAtPosition(0, click()));

    ViewInteraction textView = onView(
            allOf(withId(R.id.TaskDetailActivityTaskTitleTextView), withText("Test Task"),
                    withParent(withParent(withId(android.R.id.content))),
                    isDisplayed()));
    textView.check(matches(withText("Test Task")));

    ViewInteraction textView2 = onView(
            allOf(withId(R.id.TaskDetailActivityTaskDescriptionTextView), withText("Test Description"),
                    withParent(withParent(withId(android.R.id.content))),
                    isDisplayed()));
    textView2.check(matches(withText("Test Description")));
  }

  private static Matcher<View> childAtPosition(
          final Matcher<View> parentMatcher, final int position) {

    return new TypeSafeMatcher<View>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("Child at position " + position + " in parent ");
        parentMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(View view) {
        ViewParent parent = view.getParent();
        return parent instanceof ViewGroup && parentMatcher.matches(parent)
                && view.equals(((ViewGroup) parent).getChildAt(position));
      }
    };
  }
}
