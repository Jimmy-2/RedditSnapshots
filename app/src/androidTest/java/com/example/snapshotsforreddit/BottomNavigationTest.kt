package com.example.snapshotsforreddit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoActivityResumedException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class BottomNavigationTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testBottomNavigationClicks() {
        assertHomeScreen()

        openSearchScreen()
        //assertSearchScreen()

        openAccountScreen()
        assertAccountScreen()

        openInboxScreen()
        assertInboxScreen()

        openHomeScreen()
        assertHomeScreen()
    }

    @Test
    fun testBackBottomNavigation() {
        openInboxScreen()
        Espresso.pressBack()
        assertHomeScreen()
    }

    @Test(expected = NoActivityResumedException::class)
    fun testExitOutOfAppFromBottomNavigation() {
        assertHomeScreen()
        Espresso.pressBack()
        Assert.fail()
    }





    private fun openHomeScreen() {
        Espresso.onView(
            CoreMatchers.allOf(
                withContentDescription(R.string.title_home),
                ViewMatchers.isDisplayed()
            )
        )
            .perform(ViewActions.click())
    }

    private fun openInboxScreen() {
        Espresso.onView(
            CoreMatchers.allOf(
                withContentDescription(R.string.title_inbox),
                ViewMatchers.isDisplayed()
            )
        )
            .perform(ViewActions.click())
    }

    private fun openAccountScreen() {
        Espresso.onView(
            CoreMatchers.allOf(
                withContentDescription(R.string.title_account),
                ViewMatchers.isDisplayed()
            )
        )
            .perform(ViewActions.click())
    }

    private fun openSearchScreen() {
        Espresso.onView(
            CoreMatchers.allOf(
                withContentDescription(R.string.title_search),
                ViewMatchers.isDisplayed()
            )
        )
            .perform(ViewActions.click())
    }

    private fun openOptionsScreen() {
        Espresso.onView(
            CoreMatchers.allOf(
                withContentDescription(R.string.title_options),
                ViewMatchers.isDisplayed()
            )
        )
            .perform(ViewActions.click())
    }


    private fun openSnapshotsScreen() {
        Espresso.onView(
            CoreMatchers.allOf(
                withContentDescription(R.string.title_snapshots),
                ViewMatchers.isDisplayed()
            )
        )
            .perform(ViewActions.click())
    }





//TODO do testing on anonymous user instead of logged in user
    private fun assertHomeScreen() {

        Espresso.onView(withText(R.string.title_subreddits))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun assertInboxScreen() {
        Espresso.onView(withText(R.string.title_boxes))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun assertAccountScreen() {
        Espresso.onView(withText(R.string.menu_accounts))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun assertSearchScreen() {
        Espresso.onView(withText(R.string.hint_search_fragment))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun assertOptionsScreen() {
        Espresso.onView(withText(R.string.title_options))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
    private fun assertSnapshotsScreen() {
        Espresso.onView(withText(R.string.title_snapshots))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


}