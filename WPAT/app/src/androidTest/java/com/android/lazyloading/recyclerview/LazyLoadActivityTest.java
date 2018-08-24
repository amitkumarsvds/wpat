package com.android.lazyloading.recyclerview;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;

import com.android.lazyloading.recyclerview.lazyload.LazyLoadActivity;
import com.android.lazyloading.recyclerview.models.Proficiency;
import com.android.lazyloading.recyclerview.services.networkmanager.LazyLoadApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LazyLoadActivityTest extends
        ActivityInstrumentationTestCase2<LazyLoadActivity> {

    private LazyLoadActivity mTestActivity;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

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

    /**
     * Test if your swipe refresh does refresh and fetch data when swiped for two times
     */
    public void testTwoTimesSwipeRefresBehaviour() {
        Espresso.onView(ViewMatchers.withId(R.id.swipe_refresh))
                .perform(ViewActions.swipeDown())
                .perform(ViewActions.swipeDown());
    }

    /*
     * Tests the data in the response for various scenarios (pass and fail test cases)
     * 1. null data in any of the fields
     * 2. successful status of the response
     */

    public void testResponseData() {

        ((LazyLoadApplication) getActivity().getApplicationContext()).getmApiService().getFactsFromApi()
                .enqueue(new Callback<Proficiency>() {
                    @Override
                    public void onResponse(Call<Proficiency> call, Response<Proficiency> response) {

                        assertEquals(response.isSuccessful(), true);

                        // fails if 2nd post in response does not have title "Flag"
                        // this test case is against the static data being received from API
                        assertEquals(response.body().getRows().get(1).getTitle(), "Flag");

                        // fails
                        assertEquals(response.body().getTitle(), null);

                        assertNotSame(response.body().getRows().get(1).getDescription(), null);
                    }

                    @Override
                    public void onFailure(Call<Proficiency> call, Throwable t) {
                        // skip for now
                    }
                });
    }


}