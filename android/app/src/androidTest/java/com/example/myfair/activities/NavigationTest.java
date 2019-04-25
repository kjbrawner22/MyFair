package com.example.myfair.activities;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.myfair.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NavigationTest {

    @Rule
    public ActivityTestRule<SplashPageActivity> mActivityTestRule = new ActivityTestRule<>(SplashPageActivity.class);

    @Test
    public void navigationTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.etSignInEmail),
                        childAtPosition(
                                allOf(withId(R.id.lytSignIn),
                                        childAtPosition(
                                                withId(R.id.lytForms),
                                                2)),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.etSignInEmail),
                        childAtPosition(
                                allOf(withId(R.id.lytSignIn),
                                        childAtPosition(
                                                withId(R.id.lytForms),
                                                2)),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("kjbrawner22@gmail.com"));

        pressBack();

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.etSignInPassword),
                        childAtPosition(
                                allOf(withId(R.id.lytSignIn),
                                        childAtPosition(
                                                withId(R.id.lytForms),
                                                2)),
                                2),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("hello123"), closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btnSignIn), withText("Sign In"),
                        childAtPosition(
                                allOf(withId(R.id.lytSignIn),
                                        childAtPosition(
                                                withId(R.id.lytForms),
                                                2)),
                                3),
                        isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_collections),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withText("Collections"),
                        isDisplayed()));
        textView.check(matches(withText("Collections")));

        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.navigation_create),
                        isDisplayed()));
        bottomNavigationItemView2.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withText("Create"),
                        isDisplayed()));
        textView2.check(matches(withText("Create")));

        ViewInteraction bottomNavigationItemView3 = onView(
                allOf(withId(R.id.navigation_analytics),
                        isDisplayed()));
        bottomNavigationItemView3.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withText("Analytics"),
                        isDisplayed()));
        textView3.check(matches(withText("Analytics")));

        ViewInteraction bottomNavigationItemView4 = onView(
                allOf(withId(R.id.navigation_profile),
                        isDisplayed()));
        bottomNavigationItemView4.perform(click());

        ViewInteraction textView4 = onView(
                allOf(withText("Profile"),
                        isDisplayed()));
        textView4.check(matches(withText("Profile")));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        // Click the item.
        onView(withText("Sign Out"))
                .perform(click());
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
