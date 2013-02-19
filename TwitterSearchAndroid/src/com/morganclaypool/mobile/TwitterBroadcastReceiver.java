package com.morganclaypool.mobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;



public class TwitterBroadcastReceiver extends BroadcastReceiver {    
	
	@Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, TwitterCheckService.class); //Start the service
        System.out.println("Debug: TwitterBroadcastReceiver.onReceive: Starting service from broadcast receiver");
        context.startService(startServiceIntent);
    }
}