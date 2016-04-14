package com.minutes111.launchmode.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.minutes111.launchmode.BaseApplication;
import com.minutes111.launchmode.R;

/**
 * Created by barikos on 07.04.16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private String[] intentFlagsText = {"CLEAR_TOP", "CLEAR_WHEN_TASK_RESET", "EXCLUDE_FROM_RECENTS",
            "FORWARD_RESULT", "MULTIPLE_TASK", "NEW_TASK", "NO_HISTORY", "NO_USER_ACTION", "PREVIOUS_IS_TOP",
            "REORDER_TO_FRONT", "RESET_TASK_IF_NEEDED", "SINGLE_TOP"};

    private int[] intentFlags = {Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET,
            Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS, Intent.FLAG_ACTIVITY_FORWARD_RESULT,
            Intent.FLAG_ACTIVITY_MULTIPLE_TASK, Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_NO_HISTORY,
            Intent.FLAG_ACTIVITY_NO_USER_ACTION, Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP,
            Intent.FLAG_ACTIVITY_REORDER_TO_FRONT, Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED,
            Intent.FLAG_ACTIVITY_SINGLE_TOP};

    private static final String LOG_TAG = "myLogs";
    private static final String LOG_TAG_M = "mLog";

    private TextView mLifeCycle;
    private BaseApplication mApp;
    private StringBuilder lifecycleActivity = new StringBuilder();
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        View activityLayout = findViewById(R.id.lay_main);
        activityLayout.setBackgroundResource(getBackgroundColor());

        mApp = (BaseApplication) getApplication();

        TextView tvHeader = (TextView) findViewById(R.id.txt_header);
        mLifeCycle = (TextView) findViewById(R.id.txt_lifecycle);
        mLifeCycle.setMovementMethod(new ScrollingMovementMethod());

        showMethodStack();

        tvHeader.setText(getLaunchMode());

        mApp = (BaseApplication) getApplication();
        mApp.pushToStack(this);
    }

    @Override
    protected void onResume() {
        showMethodStack();
        Runnable stackActivityDrawer = new StackActivityDrawer(this);
        mHandler.postDelayed(stackActivityDrawer, 500);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        showMethodStack();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        showMethodStack();
        MenuItem item = menu.findItem(R.id.menu_item_filter_mode);
        String title = "Turn IntentFilter mode " + (mApp.isIntentFilterMode() ? "OFF" : "ON");
        item.setTitle(title);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showMethodStack();
        switch (item.getItemId()) {
            case R.id.menu_item_filter_mode:
                mApp.changeIntentFilterMode();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onContentChanged() {
        showMethodStack();
        super.onContentChanged();
    }

    @Override
    protected void onPause() {
        showMethodStack();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        showMethodStack();
        super.onRestart();
    }

    @Override
    protected void onStart() {
        showMethodStack();
        super.onStart();
    }

    @Override
    protected void onStop() {
        showMethodStack();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        showMethodStack();
        mApp.removeFromStack(this);
        super.onDestroy();
    }

    private String getLaunchMode() {
        return "[" + hashCode() + "] " + getClass().getSimpleName();
    }

    public void onClickButton(View view) {
        if (mApp.isIntentFilterMode()) {
            showButtonDialog(view);
        } else {
            startActivity(getNewIntent(view));
        }
    }

    private Intent getNewIntent(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_singleTop:
                intent = new Intent(this, SingleTop.class);
                break;
            case R.id.btn_singleTask:
                intent = new Intent(this, SingleTask.class);
                break;
            case R.id.btn_singleInstance:
                intent = new Intent(this, SingleInstance.class);
                break;
            default:
                intent = new Intent(this, Standard.class);
                break;
        }
        return intent;
    }

    private void showButtonDialog(View view) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Selection mode");
        adb.setCancelable(true);
        DialogInterface.OnMultiChoiceClickListener listener = new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                Log.d(LOG_TAG, String.valueOf(which));
            }
        };
        adb.setMultiChoiceItems(intentFlagsText, null, listener);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(LOG_TAG, String.valueOf(which));
            }
        });
        adb.show();
    }

    private void showMethodStack() {
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        Log.d(LOG_TAG_M, methodName);
        lifecycleActivity.append(methodName).append("\n");
        if (mLifeCycle != null) {
            mLifeCycle.setText(lifecycleActivity);
        }
    }

    public abstract int getBackgroundColor();
}
