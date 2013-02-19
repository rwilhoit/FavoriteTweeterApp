package com.morganclaypool.mobile;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class TwitterCheckService extends Service {

	String twitterUser;
	String serviceDuration;
	String currentTweetTime;
	private static final String TWITTER_SEARCH_API = "http://search.twitter.com/search.json?lang=en&q=";
	private DefaultHttpClient httpClient = new DefaultHttpClient();
	Handler handler = new Handler();
	
	//Declare a Handler here
	//Declare the Twitter stuff here
	//TimerTask to make the service only happen in intervals
	//Possibly Handler and Runnable instead
	//***create a new runnable and in onStartCommand, just display the toast***
	
@Override
public IBinder onBind(Intent intent) {
	System.out.println("Debug: TwitterCheckService.onBind(...) Called");

//This is actually the only required,
//but it is used for binding the service to an activity, 
//which we aren’t going to do.
return null;
}

@Override
public void onCreate() {
//code to execute when the service is first created
	System.out.println("Debug: TwitterCheckService.onCreate(...) Called");
	/*The onCreate method executes only on the initial creation of the service 
	 * which happens on the first call to Context.startService(). 
	 * Once the service has been created, onCreate will not fire again regardless of 
	 * how many times Context.startService() is called. 
	 */
	
	//TwitterBookMarkActivity.favoriteTwitterFile = getBaseContext().getFileStreamPath("favoritetwitter.txt");
	//TwitterBookMarkActivity.durationFile = getBaseContext().getFileStreamPath("twitterduration.txt");
	
	System.out.println("About to check if files exist");
	if(TwitterBookMarkActivity.favoriteTwitterFile == null || TwitterBookMarkActivity.durationFile == null)
	{
		System.out.println("One or more files are null, set the files again and check if they are still null or not");
		TwitterBookMarkActivity.favoriteTwitterFile = getBaseContext().getFileStreamPath("favoritetwitter.txt");
    	TwitterBookMarkActivity.durationFile = getBaseContext().getFileStreamPath("twitterduration.txt");
    	if(TwitterBookMarkActivity.favoriteTwitterFile == null || TwitterBookMarkActivity.durationFile == null)
    	{
    		System.out.println("Reset the files but they are STILL null");
    	}
	}
    if(TwitterBookMarkActivity.favoriteTwitterFile.exists() && TwitterBookMarkActivity.durationFile.exists())
    {
    	System.out.println("Debug: Both favorite twitter and duration files exist!");
        BufferedReader in = null;
    	try 
    	{
    		
    		TwitterBookMarkActivity.favoriteTwitterFile = getBaseContext().getFileStreamPath("favoritetwitter.txt");
            in = new BufferedReader(new FileReader(TwitterBookMarkActivity.favoriteTwitterFile.getAbsolutePath()));
            twitterUser = in.readLine();
            System.out.println("DEBUG: RESULTING STRING IN TwitterCheckService: " + twitterUser);
            
            TwitterBookMarkActivity.durationFile = getBaseContext().getFileStreamPath("twitterduration.txt");
        	in = new BufferedReader(new FileReader(TwitterBookMarkActivity.durationFile.getAbsolutePath()));
        	serviceDuration = in.readLine();
        	System.out.println("DEBUG:RESULTING STRING 2 IN TwitterCheckService: " + serviceDuration);
    	} 
    	catch (FileNotFoundException e) 
    	{
    		e.printStackTrace();
    	} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    } 
    else 
    {
    	System.out.println("Debug: File(s) did not exist");
    }
	//twitterUser = "kakijun";
	//serviceDuration = "10";
	System.out.println("Service started!");	
	
}

@Override
public void onDestroy() {
	System.out.println("Debug: TwitterCheckService.onDestroy() Called");
	handler.removeCallbacks(timedTask);
	System.out.println("Debug: Removed callbacks to timedTask");
}

@Override
public int onStartCommand(Intent intent, int flags, int startId) {
//code to execute when the service is starting up
	/*The method onStart, however, is called each time Context.startService() is called. 
	 * NOTE: onStart is actually deprecated and you should use onStartCommand when using API levels 5 and above.
	 */	
	
	/*
	To read a file from internal storage:
		Call openFileInput() and pass it the name of the file to read. This returns a FileInputStream.
		Read bytes from the file with read().
		Then close the stream with close().
	*/
    //Where do I get the duration from when restarting the app?
    Toast.makeText(TwitterCheckService.this, "Service Started!", Toast.LENGTH_SHORT).show();
	System.out.println("Debug: TwitterCheckService.onStartCommand() Called");
	System.out.println("Debug: TwitterCheckService.onStartCommand() Running timedTask");
	timedTask.run();	
	
	return 0;
} //end of onstart


private Runnable timedTask = new Runnable(){

@Override
public void run() {
	//Put the service body in here.
	System.out.println("Debug: TwitterCheckService: timedTask.run() searching for twitterUser: " + twitterUser);
    String resultString = get(TWITTER_SEARCH_API + URLEncoder.encode(twitterUser));
    System.out.println("The twitter url for twitter user " + twitterUser + " is: " + resultString);
    String newTweetTime = null;
    try {
        JSONArray resultJsonArray = (new JSONObject(resultString)).getJSONArray("results");
        JSONObject jsonObject = null;
        jsonObject = resultJsonArray.getJSONObject(0);
        newTweetTime = jsonObject.getString("created_at");
        System.out.println("Printing out the string result for from_user: " + jsonObject.getString("text"));
        System.out.println("Printing out the string result for from_user: " + jsonObject.getString("created_at"));
    	System.out.println("Current Tweet Time: " + newTweetTime);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    
    if(currentTweetTime != null)
    {
    	if(!newTweetTime.equals(currentTweetTime))
    	{
    		System.out.println("newTweetTime: " + newTweetTime);
    		System.out.println("currentTweetTime: " + currentTweetTime);
    		System.out.println("Debug: TwitterCheckService: The time of the last tweet is different than the currentTweetTime, show toast and reset");
    		currentTweetTime = newTweetTime;
    		Toast.makeText(TwitterCheckService.this, "There is a new tweet!!!", Toast.LENGTH_SHORT).show();
    	}
    	else
    	{
    		System.out.println("newTweetTime: " + newTweetTime);
    		System.out.println("currentTweetTime: " + currentTweetTime);
    		System.out.println("Debug: TwitterCheckService: The tweet times are more likely the same");
    	}
    }
    else
    {
    	System.out.println("Debug: TwitterCheckService: currentTweetCount was null, set to newTweetCount");
    	currentTweetTime = newTweetTime;
    }
    		
	handler.postDelayed(timedTask, Integer.parseInt(serviceDuration) * 1000);
}};


private String get(String url) {
    String responseMessage = null;
    HttpGet httpGet = new HttpGet(url);
    try {
      HttpResponse getResponse = httpClient.execute(httpGet);
      HttpEntity getResponseEntity = getResponse.getEntity();
      if (getResponseEntity != null) {
        responseMessage = EntityUtils.toString(getResponseEntity);
      }
    } catch (IOException e) {
      httpGet.abort();
      e.printStackTrace();
    }
    return responseMessage;
  }

}


