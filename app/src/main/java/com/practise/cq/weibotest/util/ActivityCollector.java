package com.practise.cq.weibotest.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CQ on 2016/5/13 0013.
 */
public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity : activities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
