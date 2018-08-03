package com.android.lazyloading.recyclerview.lazyload;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.android.lazyloading.recyclerview.R;
import com.android.lazyloading.recyclerview.alert.LazyLoadAlertDialog;
import com.android.lazyloading.recyclerview.services.networkmanager.NetworkConnectivityManager;


/**
 * class which will show list of item
 */
public class LazyLoadActivity extends AppCompatActivity implements LazyLoadView {

    public RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private LazyLoadPresenter presenter;
    private SwipeRefreshLayout swipeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mContext = this;

        setUiElements();

        presenter = new LazyLoadPresenter(this, new LazyLoadViewServiceCommunicator());

        if (NetworkConnectivityManager.isNetworkAvailable(this)) {
            showProgressDialog();
            //delegating API call to presenter to get the list item from the server
            presenter.interactWithService();
        } else {
            //If internet is not there, it will show popup
            LazyLoadAlertDialog.alertDilaog(this,
                    mContext.getResources().getString(R.string.networkmessage));
        }

        // Adding  Swipe view Listener
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectivityManager.isNetworkAvailable(LazyLoadActivity.this)) {
                    //delegating API call to presenter to get the list item from the server
                    presenter.refresh();

                } else {
                    hideSwipeRefresh();
                    //If internet is not there, it will show popup
                    LazyLoadAlertDialog.alertDilaog(LazyLoadActivity.this,
                            mContext.getResources().getString(R.string.networkmessage));

                    // asserting whether network connectivity is available ot not
                    // it would cry/fail if we dont have network, so commenting it out
                    //assertEquals(true, NetworkConnectivityManager.isNetworkAvailable(mContext));
                }
            }
        });

        // Scheme colors for animation
        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

    }

    /**
     * Initialize the UI elements here
     */
    @Override
    public void setUiElements() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mProgressBar = findViewById(R.id.progress_bar);
        swipeLayout = findViewById(R.id.swipe_refresh);

    }

    /**
     * show progress dialog
     */
    @Override
    public void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.progress_message));
        mProgressDialog.setTitle(getResources().getString(R.string.progress_message_title));
        mProgressDialog.show();
    }

    /**
     * dismiss progress dialog
     */
    @Override
    public void dismissProgressDialog() {

        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    /**
     * show progress bar
     */
    @Override
    public void showProgressBar() {

        if (mProgressBar != null)
            mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * hide progress bar
     */
    @Override
    public void hideProgressBar() {

        if (mProgressBar != null)
            mProgressBar.setVisibility(View.GONE);
    }

    /**
     * hide swipe refresh view
     */
    @Override
    public void hideSwipeRefresh() {
        swipeLayout.setRefreshing(false);
    }

    /**
     * activity destroy view
     */
    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();

    }
}
