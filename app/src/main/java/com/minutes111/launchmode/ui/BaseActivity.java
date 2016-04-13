package com.minutes111.launchmode.ui;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.minutes111.launchmode.BaseApplication;
import com.minutes111.launchmode.R;

import java.util.List;

/**
 * Created by barikos on 07.04.16.
 */
public abstract class BaseActivity extends AppCompatActivity{

    private String[] intentFlagsText = { "CLEAR_TOP", "CLEAR_WHEN_TASK_RESET", "EXCLUDE_FROM_RECENTS",
            "FORWARD_RESULT", "MULTIPLE_TASK", "NEW_TASK", "NO_HISTORY", "NO_USER_ACTION", "PREVIOUS_IS_TOP",
            "REORDER_TO_FRONT", "RESET_TASK_IF_NEEDED", "SINGLE_TOP" };

    private int[] intentFlags = { Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET,
            Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS, Intent.FLAG_ACTIVITY_FORWARD_RESULT,
            Intent.FLAG_ACTIVITY_MULTIPLE_TASK, Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_NO_HISTORY,
            Intent.FLAG_ACTIVITY_NO_USER_ACTION, Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP,
            Intent.FLAG_ACTIVITY_REORDER_TO_FRONT, Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED,
            Intent.FLAG_ACTIVITY_SINGLE_TOP };

    private static final String LOG_TAG = "myLogs";

    private ActivityManager mManager;
    private BaseApplication mApp;
    private StringBuilder lifecycleActivity = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        View activityLayout = findViewById(R.id.lay_main);
        activityLayout.setBackgroundResource(getBackgroundColor());

        mManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = mManager.getRunningTasks(1);

        mApp = (BaseApplication)getApplication();

        TextView tvTaskId = (TextView)findViewById(R.id.txt_task_id);
        TextView tvHeader = (TextView)findViewById(R.id.txt_header);
        tvTaskId.setText(String.valueOf(tasks.get(0).id));
        tvHeader.setText(getLaunchMode());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_item_filter_mode);
        String title = "Turn IntentFilter mode " + (mApp.isIntentFilterMode() ? "OFF" : "ON");
        item.setTitle(title);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_filter_mode:
                mApp.changeIntentFilteMode();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getLaunchMode(){
        return "[" + hashCode() +"] " + getClass().getSimpleName();
    }

    public void onClickButton(View view){
        if (mApp.isIntentFilterMode()){
            showButtonDialog(view);
        }else {
            startActivity(getNewIntent(view));
        }
    }

    private Intent getNewIntent(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.btn_singleTop:
                intent = new Intent(this,SingleTop.class);
                break;
            case R.id.btn_singleTask:
                intent = new Intent(this,SingleTask.class);
                break;
            case R.id.btn_singleInstance:
                intent = new Intent(this,SingleInstance.class);
                break;
            default:
                intent = new Intent(this,Standard.class);
                break;
        }
        return intent;
    }

    private void showButtonDialog(View view){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Selection mode");
        adb.setCancelable(true);
        DialogInterface.OnMultiChoiceClickListener listener = new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                Log.d(LOG_TAG,String.valueOf(which));
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

    public abstract int getBackgroundColor();
}
