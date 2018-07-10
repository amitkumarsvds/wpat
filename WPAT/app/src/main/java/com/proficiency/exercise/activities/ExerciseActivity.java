package com.proficiency.exercise.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import com.proficiency.exercise.R;
import java.util.ArrayList;
import java.util.List;
import adapters.ExerciseAdpter;
import adapters.ExerciseServiceManager;
import adapters.VerticalLineDecorator;
import alert.ExerciseAlertDialog;
import models.exercisemodels.ExerciseManagerCallBack;
import models.exercisemodels.Row;
import services.networkmanager.NetworkConnectivityManager;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

/**
 * class which will show list of item
 */
public class ExerciseActivity extends AppCompatActivity implements ExerciseManagerCallBack {

    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;
    ExerciseAdpter mAdapter;
    Context mContext;
    List mItemLoaded = new ArrayList();
    boolean mFlag;
    public  final int DELAYTIME = 4000;
    public  final int INCREMENTCOUNT = 7;
    ProgressDialog mProgressDialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                showProgressBar();
                mFlag = true;//Will not allow to set any item in list if mFlag is true
                mItemLoaded.clear();//clearing old data from list

                //asserting to verify size is 0 or not post clearing it - refer previous line
                assertEquals(0, mItemLoaded.size());

                //referesh the list item
                mAdapter.notifyDataChanged();
                //Before calling API first will check wheather internet is there or not
                if (NetworkConnectivityManager.isNetworkAvailable(this))
                    serviceCall();
                else {
                    ExerciseAlertDialog.alertDilaog(this,
                            mContext.getResources().getString(R.string.networkmessage));

                    // asserting whether network connectivity is available ot not
                    assertEquals(true, NetworkConnectivityManager.isNetworkAvailable(this));
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mContext = this;
        setUiElements();
        if (NetworkConnectivityManager.isNetworkAvailable(this)) {
            showProgressBar();
            //API call to get the list item from the server
            serviceCall();
        }
        else {
            ExerciseAlertDialog.alertDilaog(this,
                    mContext.getResources().getString(R.string.networkmessage));
        }
    }

    /**
     * Initialize the UI elements here
     */
    private void setUiElements() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
    }

    /**
     * Http call/Network call
     */
    private void serviceCall() {
        ExerciseServiceManager apiManager = new ExerciseServiceManager(ExerciseActivity.this);
        apiManager.startConfiguration(ExerciseActivity.this);
        apiManager.getServiceResponse("");
    }

    /**
     * progress bar
     */
    private void showProgressBar() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.progress_message));
        mProgressDialog.setTitle(getResources().getString(R.string.progress_message_title));
        mProgressDialog.show();
    }


    @Override
    public void UDManagerFailureCallback(String failureData) {

        // asserts failure response string is empty or not
        assertNotSame(failureData, "");

        ExerciseAlertDialog.alertDilaog(this, failureData);

    }

    @Override
    public void UDManagerSuccessCallback(final List<Row> respRows) {

        // asserts response data is null or not?
        assertNotSame(null, respRows);

        mFlag = false;
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
        updateUI(respRows);
    }

    /**
     * load the recycle view item here/upate the UI after response
     *
     * @param respRows respRows
     */
    private void updateUI(final List<Row> respRows) {
        if (ExerciseServiceManager.mGetTitle==null)
        {
            getSupportActionBar().setTitle(getResources().getString(R.string.NA));
        }
        else
        {
            getSupportActionBar().setTitle(ExerciseServiceManager.mGetTitle);
        }

        if (!mFlag) {
            for (int i = 0; i <= INCREMENTCOUNT; i++) {
                mItemLoaded.add(respRows.get(i));
            }
            mAdapter = new ExerciseAdpter(this, mItemLoaded);
            mAdapter.setLoadMoreListener(new ExerciseAdpter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mAdapter.setMoreDataAvailable(false);
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int index = mItemLoaded.size();
                            int newSize = 0;
                            if (index + INCREMENTCOUNT < respRows.size())
                                newSize = index + INCREMENTCOUNT;
                            else
                                newSize = respRows.size();
                            for (int i = index; i < newSize; i++) {
                                mItemLoaded.add(respRows.get(i));
                            }
                            mAdapter.notifyDataChanged();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setVisibility(View.GONE);
                                    if (mItemLoaded.size() < respRows.size())
                                        mAdapter.setMoreDataAvailable(true);
                                }
                            });
                        }
                    }, DELAYTIME);
                }
            });
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.addItemDecoration(new VerticalLineDecorator(2));
            mRecyclerView.setAdapter(mAdapter);

        }
    }

}
