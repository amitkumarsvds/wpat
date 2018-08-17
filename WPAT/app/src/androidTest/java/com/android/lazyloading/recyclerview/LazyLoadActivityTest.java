package com.android.lazyloading.recyclerview;

import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.android.lazyloading.recyclerview.lazyload.LazyLoadActivity;

public class LazyLoadActivityTest extends
        ActivityInstrumentationTestCase2<LazyLoadActivity> {

    private LazyLoadActivity mTestActivity;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;

    public LazyLoadActivityTest() {
        super(LazyLoadActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mTestActivity = getActivity();

        mSwipeRefreshLayout = (SwipeRefreshLayout) mTestActivity
                .findViewById(R.id.swipe_refresh);

        mRecyclerView = (RecyclerView) mTestActivity
                .findViewById(R.id.recycler_view);

    }

    /**
     * Test if your test fixture has the correct setup
     */
    public void testLazyPreconditions() {

        assertNotNull("mTestActivity is null", mTestActivity);
        assertNotNull("mSwipeRefreshLayout is null", mSwipeRefreshLayout);
        assertNotNull("mRecyclerView is null", mRecyclerView);
    }

    /**
     * Test if your swipe refresh does refresh and fetch data
     */
    public void testSwipeRefresBehaviour() {
        Espresso.onView(ViewMatchers.withId(R.id.swipe_refresh))
                .perform(ViewActions.swipeDown());
    }

    public void testTwoTimesSwipeRefresBehaviour() {
        Espresso.onView(ViewMatchers.withId(R.id.swipe_refresh))
                .perform(ViewActions.swipeDown())
                .perform(ViewActions.swipeDown());
    }


}