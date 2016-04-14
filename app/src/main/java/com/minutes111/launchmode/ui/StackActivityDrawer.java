package com.minutes111.launchmode.ui;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minutes111.launchmode.BaseApplication;
import com.minutes111.launchmode.R;

import java.util.Stack;

/**
 * Created by barikos on 13.04.16.
 */
public class StackActivityDrawer implements Runnable{

    private BaseApplication mApp;
    private TextView mTaskId;
    private LinearLayout mTaskView;

    public StackActivityDrawer(BaseActivity activity) {
        mApp = (BaseApplication)activity.getApplication();
        mTaskId = (TextView)activity.findViewById(R.id.txt_task_id);
        mTaskView = (LinearLayout)activity.findViewById(R.id.lay_task_view);
        mTaskView.removeAllViews();
    }

    @Override
    public void run() {
        mTaskId.setText("Task id: " + mApp.getCurrentTaskId());

        Stack<BaseActivity> stack = mApp.getCurrentTask();
        for (int i = stack.size()-1; i>=0; i--){
            BaseActivity activity = stack.get(i);
            ImageView imageView = getActivityStackItem(activity);
            mTaskView.addView(imageView);
        }
    }

    private ImageView getActivityStackItem(BaseActivity activity){
        ImageView imageView = new ImageView(activity);
        int color = activity.getBackgroundColor();
        imageView.setBackgroundResource(color);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,10);
        params.setMargins(2, 2, 2, 2);
        imageView.setLayoutParams(params);
        return imageView;
    }
}
