package com.proficiency.exercise.wpat;

import android.support.v7.widget.LinearLayoutManager;

import com.proficiency.exercise.R;

import java.util.ArrayList;
import java.util.List;

import adapters.ExerciseAdpter;
import adapters.VerticalLineDecorator;
import alert.ExerciseAlertDialog;
import models.exercisemodels.Row;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

public class ExercisePresenter implements ExerciseServiceCommunicator.OnServiceCallFinishedListener {

    private final int DELAYTIME = 2000;
    private final int INCREMENTCOUNT = 7;
    private ExeciseView mExeciseView;
    private ExerciseServiceCommunicator mExerciseServiceCommunicator;
    private List mItemLoaded = new ArrayList();
    private boolean mFlag;
    private ExerciseAdpter mAdapter;
    private ExerciseActivity mContext;


    ExercisePresenter(ExeciseView execiseView, ExerciseServiceCommunicator exerciseServiceCommunicator) {
        this.mExeciseView = execiseView;
        this.mExerciseServiceCommunicator = exerciseServiceCommunicator;
        mContext = ((ExerciseActivity) mExeciseView);

    }

    /**
     * releasing view (exercise activity context)
     */
    public void onDestroy() {
        mExeciseView = null;
    }

    /**
     * releasing view (exercise activity context)
     */
    void interactWithService() {
        mExerciseServiceCommunicator.serviceFacts(this);
    }

    /**
     * load the recycle view item here/upate the UI after response
     *
     * @param respRows respRows
     */
    private void updateUI(final List<Row> respRows, String title) {
        if (title == null) {
            mContext.getSupportActionBar().
                    setTitle(mContext.getResources().getString(R.string.NA));
        } else {
            mContext.getSupportActionBar().setTitle(title);
        }

        if (!mFlag) {
            for (int i = 0; i <= INCREMENTCOUNT; i++) {
                mItemLoaded.add(respRows.get(i));
            }
            mAdapter = new ExerciseAdpter((ExerciseActivity) mExeciseView, mItemLoaded);
            mAdapter.setLoadMoreListener(new ExerciseAdpter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                    mExeciseView.showProgressBar();

                    mAdapter.setMoreDataAvailable(false);

                    mContext.mRecyclerView.postDelayed(new Runnable() {
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
                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mExeciseView.hideProgressBar();

                                    if (mItemLoaded.size() < respRows.size())
                                        mAdapter.setMoreDataAvailable(true);
                                }
                            });
                        }
                    }, DELAYTIME);
                }
            });
            mContext.mRecyclerView.setHasFixedSize(true);
            mContext.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mContext.mRecyclerView.addItemDecoration(new VerticalLineDecorator(2));
            mContext.mRecyclerView.setAdapter(mAdapter);

        }
    }

    @Override
    public void onSuccess(final List<Row> respRows, String title) {

        assertNotSame(null, respRows);

        mFlag = false;

        mExeciseView.dismissProgressDialog();

        updateUI(respRows, title);
    }

    @Override
    public void onFailure(String failureData) {

        // asserts failure response string is empty or not
        assertNotSame(failureData, "");

        ExerciseAlertDialog.alertDilaog((ExerciseActivity) mExeciseView, failureData);

    }

    public void refresh() {

        mFlag = true;//Will not allow to set any item in list if mFlag is true
        mItemLoaded.clear();//clearing old data from list

        //asserting to verify size is 0 or not post clearing it - refer previous line
        assertEquals(0, mItemLoaded.size());

        //referesh the list item
        if (mAdapter != null) {
            mAdapter.notifyDataChanged();
        }

        interactWithService();

    }
}