package com.bignerdranch.android.nerdmail;

import android.support.design.internal.NavigationMenuItemView;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;

import com.bignerdranch.android.nerdmail.controller.DrawerActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Created by SamMyxer on 4/7/16.
 */
@RunWith(AndroidJUnit4.class)
public class DrawerActivityTest {


    @Rule
    public ActivityTestRule<DrawerActivity> activityRule = new ActivityTestRule<>(
            DrawerActivity.class);

    @Test
    public void userSeesInboxFirst() {
        String inboxText = activityRule.getActivity()
                .getString(R.string.nav_drawer_inbox);
        onView(allOf(withText(inboxText), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void inboxItemSelectedFirstInNavigationDrawer() {
        String inboxText = activityRule.getActivity()
                .getString(R.string.nav_drawer_inbox);
        //open nav drawer
        DrawerActions.openDrawer(R.id.activity_drawer_layout);
        //check that the inbox item is checked
        onView(allOf(withText(inboxText),
                withParent(isAssignableFrom(NavigationMenuItemView.class))))
                .check(matches(isChecked()));
    }

    @Test
    public void selectingImportantItemShowsImportantScreen() {
        String importantText = activityRule.getActivity()
                .getString(R.string.nav_drawer_important);
        //open nav drawer
        DrawerActions.openDrawer(R.id.activity_drawer_layout);
        //click on the important item in the Navigation View
        onView(allOf(withText(importantText),
                withParent(isAssignableFrom(NavigationMenuItemView.class))))
                .perform(click());
        //verify that the important text is show in the Toolbar
        onView(allOf(withText(importantText),
                withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void selectingSpamItemShowsSpamScreen() {
        String spamText = activityRule.getActivity()
                .getString(R.string.nav_drawer_spam);
        //open nav drawer
        DrawerActions.openDrawer(R.id.activity_drawer_layout);
        //click on the spam item in the Navigation View
        onView(allOf(withText(spamText),
                withParent(isAssignableFrom(NavigationMenuItemView.class))))
                .perform(click());
        //verify that the spam text is show in the Toolbar
        onView(allOf(withText(spamText),
                withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(isDisplayed()));
    }


    @Test
    public void selectingAllItemShowsAllScreen() {
        String allText = activityRule.getActivity()
                .getString(R.string.nav_drawer_all);
        //open nav drawer
        DrawerActions.openDrawer(R.id.activity_drawer_layout);
        //click on the all item in the Navigation View
        onView(allOf(withText(allText),
                withParent(isAssignableFrom(NavigationMenuItemView.class))))
                .perform(click());
        //verify that the all text is show in the Toolbar
        onView(allOf(withText(allText),
                withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(isDisplayed()));
    }
}
