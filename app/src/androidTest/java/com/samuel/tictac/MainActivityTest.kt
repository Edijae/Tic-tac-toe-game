package com.samuel.tictac

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.samuel.tictac.activities.MainActivity
import com.samuel.tictac.adapters.GridAdapter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun winTest() {
        onView(withId(R.id.startX)).perform(click())
        onView(withId(R.id.rv)).perform(
            RecyclerViewActions.actionOnItemAtPosition<GridAdapter.GridViewHolder>(
                0, click()
            ),
            RecyclerViewActions.actionOnItemAtPosition<GridAdapter.GridViewHolder>(
                1, click()
            ),
            RecyclerViewActions.actionOnItemAtPosition<GridAdapter.GridViewHolder>(
                4, click()
            ),
            RecyclerViewActions.actionOnItemAtPosition<GridAdapter.GridViewHolder>(
                2, click()
            ),
            RecyclerViewActions.actionOnItemAtPosition<GridAdapter.GridViewHolder>(
                8, click()
            )
        )
        onView(withId(R.id.btn))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
        onView(isRoot())
            .inRoot(isDialog())
            .noActivity()
    }

    @Test
    fun drawTest() {
        onView(withId(R.id.startO)).perform(click())
        onView(withId(R.id.rv)).perform(
            RecyclerViewActions.actionOnItemAtPosition<GridAdapter.GridViewHolder>(
                0, click()
            ),
            RecyclerViewActions.actionOnItemAtPosition<GridAdapter.GridViewHolder>(
                1, click()
            ),
            RecyclerViewActions.actionOnItemAtPosition<GridAdapter.GridViewHolder>(
                2, click()
            ),
            RecyclerViewActions.actionOnItemAtPosition<GridAdapter.GridViewHolder>(
                5, click()
            ),
            RecyclerViewActions.actionOnItemAtPosition<GridAdapter.GridViewHolder>(
                3, click()
            ),
            RecyclerViewActions.actionOnItemAtPosition<GridAdapter.GridViewHolder>(
                6, click()
            ),
            RecyclerViewActions.actionOnItemAtPosition<GridAdapter.GridViewHolder>(
                4, click()
            ),
            RecyclerViewActions.actionOnItemAtPosition<GridAdapter.GridViewHolder>(
                8, click()
            ),
            RecyclerViewActions.actionOnItemAtPosition<GridAdapter.GridViewHolder>(
                7, click()
            )
        )
        onView(withId(R.id.btn))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
        onView(isRoot())
            .inRoot(isDialog())
            .noActivity()
    }
}