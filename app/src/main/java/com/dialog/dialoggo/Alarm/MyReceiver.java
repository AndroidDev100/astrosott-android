package com.dialog.dialoggo.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dialog.dialoggo.utils.helpers.AppLevelConstants;


public class MyReceiver extends BroadcastReceiver {
    private String name = "";
    private String description = "";
    private String screen_name = "";
    private Long Id;
    private int requestcode;

    @Override
    public void onReceive(Context context, Intent intent)
    {

        Log.d("OnConditionCall","1");
        name = intent.getStringExtra(AppLevelConstants.Title);
        description = intent.getStringExtra(AppLevelConstants.DESCRIPTION);
        Id = intent.getLongExtra(AppLevelConstants.ID,0);
        screen_name = intent.getStringExtra(AppLevelConstants.SCREEN_NAME);
        requestcode = intent.getIntExtra("requestcode",0);
     //   new KsPreferenceKeys(context).setReminderId(Id.toString(),true);
        try{
            utils.generateNotification(context,name,description,Id,screen_name,requestcode);
            Log.d("OnConditionCall",name+ " "+description+ " "+Id+ " "+screen_name+ " "+requestcode);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}