package com.example.myfair.activities;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.myfair.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;

/**
 * Tests the login functionality of the app using a sample login info
 * !!!!Must be logged out at the start of the test to work properly!!!
 * Can run all other tests in a sequence -- right click androidTest folder and run all (alphabetical order)
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public ActivityTestRule<SplashPageActivity> mActivityTestRule = new ActivityTestRule<>(SplashPageActivity.class);

    @Test
    public void loginTest() {
         // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        ViewInteraction appCompatEditText = onView(
            allOf(withId(R.id.etSignInEmail), childAtPosition(
                    allOf(withId(R.id.lytSignIn), childAtPosition(
                        withId(R.id.lytForms),
                                2)), 1),
                                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
            allOf(withId(R.id.etSignInEmail), childAtPosition(
                allOf(withId(R.id.lytSignIn), childAtPosition(
                    withId(R.id.lytForms), 2)),
        1), isDisplayed()));

        appCompatEditText2.perform(replaceText("kjbrawner22@gmail.com"), closeSoftKeyboard());
        
         // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        ViewInteraction appCompatEditText9 = onView(
            allOf(withId(R.id.etSignInPassword), childAtPosition(
                allOf(withId(R.id.lytSignIn), childAtPosition(
                    withId(R.id.lytForms), 2)),
        2), isDisplayed()));
        appCompatEditText9.perform(replaceText("hello123"), closeSoftKeyboard());
        
        pressBack();
        
        ViewInteraction appCompatButton = onView(
            allOf(withId(R.id.btnSignIn), withText("Sign In"), childAtPosition(
                allOf(withId(R.id.lytSignIn), childAtPosition(
                    withId(R.id.lytForms),
        2)), 3), isDisplayed()));
        appCompatButton.perform(click());
        
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        ViewInteraction textView = onView(
            allOf(withText("Profile"), childAtPosition(
                allOf(withId(R.id.toolbar), childAtPosition(
        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class), 0)),
        0), isDisplayed()));
        textView.check(matches(withText("Profile")));

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
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
