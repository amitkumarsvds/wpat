package com.proficiency.exercise.wpat;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.proficiency.exercise.R;

import alert.ExerciseAlertDialog;
import services.networkmanager.NetworkConnectivityManager;

/**
 * class which will show list of item
 */
public class ExerciseActivity extends AppCompatActivity implements ExeciseView {

    public RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private ExercisePresenter presenter;

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

                if (NetworkConnectivityManager.isNetworkAvailable(this)) {
                    showProgressDialog();
                    //delegating API call to presenter to get the list item from the server
                    presenter.refresh();

                } else {
                    //If internet is not there, it will show popup
                    ExerciseAlertDialog.alertDilaog(this,
                            mContext.getResources().getString(R.string.networkmessage));

                    // asserting whether network connectivity is available ot not
                    // it would cry/fail if we dont have network, so commenting it out
                    //assertEquals(true, NetworkConnectivityManager.isNetworkAvailable(mContext));
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

        presenter = new ExercisePresenter(this, new ExerciseServiceCommunicator());

        if (NetworkConnectivityManager.isNetworkAvailable(this)) {
            showProgressDialog();
            //delegating API call to presenter to get the list item from the server
            presenter.interactWithService();
        } else {
            //If internet is not there, it will show popup
            ExerciseAlertDialog.alertDilaog(this,
                    mContext.getResources().getString(R.string.networkmessage));
        }
    }

    /**
     * Initialize the UI elements here
     */
    @Override
    public void setUiElements() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mProgressBar = findViewById(R.id.progressBar1);
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
     * activity destroy view
     */
    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();

    }
}
