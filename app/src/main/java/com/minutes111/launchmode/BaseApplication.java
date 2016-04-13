package com.minutes111.launchmode;

import android.app.ActivityManager;
import android.app.Application;

import com.minutes111.launchmode.ui.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by barikos on 07.04.16.
 */
public class BaseApplication extends Application{

    private HashMap<Integer,Stack<BaseActivity>> mTasks;
    private ActivityManager mManager;
    private boolean mIntentFilterMode;

    @Override
    public void onCreate() {
        super.onCreate();
        mManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        mTasks = new HashMap<Integer,Stack<BaseActivity>>();
    }

    public void pushToStack(BaseActivity activity){
        int currentTaskId = getCurrentTaskId();
        if (!mTasks.containsKey(currentTaskId)){
            mTasks.put(currentTaskId, new Stack<BaseActivity>());
        }
        Stack<BaseActivity> stack = mTasks.get(currentTaskId);
        stack.add(activity);
    }

    public void removeFromStack(BaseActivity activity){
        Stack<BaseActivity> stack = mTasks.get(getCurrentTaskId());
        if (stack != null){
            stack.remove(activity);
        }
    }

    public Stack<BaseActivity> getCurentTask(){
        return mTasks.get(getCurrentTaskId());
    }

    public int getCurrentTaskId(){
        List<ActivityManager.RunningTaskInfo> runningTasks = mManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo runningTask = runningTasks.get(0);
        return runningTask.id;
    }

    public void changeIntentFilteMode(){
        mIntentFilterMode = !mIntentFilterMode;
    }

    public boolean isIntentFilterMode(){
        return mIntentFilterMode;
    }

}
