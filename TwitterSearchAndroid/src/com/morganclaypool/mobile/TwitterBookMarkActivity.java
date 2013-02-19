package com.morganclaypool.mobile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TwitterBookMarkActivity extends Activity {

  private static Intent twitterServiceIntent;
  static File favoriteTwitterFile;
  static File durationFile;
	
  @Override
  public void onCreate(Bundle savedInstanceState) {
	System.out.println("Created TwitterBookMarkActivity");
	super.onCreate(savedInstanceState);
	setContentView(R.layout.tweetbookmark);
		
	final Button saveButton = (Button) findViewById(R.id.saveButton);	//Save Button Code
    saveButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        	
       		//Save the user's favorite Twitter user
            final EditText twitterUserText = (EditText) findViewById(R.id.favoriteTwitterUser);
            //Save the duration
    		final EditText twitterCheckDuration = (EditText) findViewById(R.id.serviceDuration);        
           
            String FAVORITETWITTERFILENAME = "favoritetwitter.txt";
            String SERVICEDURATIONFILENAME = "twitterduration.txt";
            
            String favoriteTwitterUser = twitterUserText.getText().toString();
            String twitterServiceDuration = twitterCheckDuration.getText().toString();
            BufferedReader in = null;
            FileOutputStream fos = null;
            System.out.println("DEBUG: About to write to files");
            try 
    		{
                fos = openFileOutput(FAVORITETWITTERFILENAME, Context.MODE_PRIVATE);
                fos.write(favoriteTwitterUser.getBytes());
                fos.close();
                System.out.println("DEBUG: Wrote to first file");
                
            	favoriteTwitterFile = getBaseContext().getFileStreamPath("favoritetwitter.txt");
                in = new BufferedReader(new FileReader(favoriteTwitterFile.getAbsolutePath()));
                String favoriteUser = in.readLine();
                System.out.println("DEBUG: Result 1: " + favoriteUser);
                
                fos = openFileOutput(SERVICEDURATIONFILENAME, Context.MODE_PRIVATE);
                fos.write(twitterServiceDuration.getBytes());
                fos.close();
                System.out.println("DEBUG: Wrote to second file");
                
            	durationFile = getBaseContext().getFileStreamPath("twitterduration.txt");
            	in = new BufferedReader(new FileReader(durationFile.getAbsolutePath()));
            	String serviceDuration = in.readLine();
            	System.out.println("DEBUG: Result 2: = " + serviceDuration);
    		} 
    		catch (FileNotFoundException e) 
    		{
    			e.printStackTrace();
    		} 
    		catch (IOException e) 
    		{
    			e.printStackTrace();
    		}
            finally {
                if (in != null) 
                {
                  try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                }
            }
            System.out.println("DEBUG: Completely finished reading and writing to files.");
		
        setTwitterServiceIntent(new Intent(TwitterBookMarkActivity.this, TwitterCheckService.class));
    	if(getTwitterServiceIntent() != null)
    	{
    		System.out.println("Debug: Starting service");
    		startService(getTwitterServiceIntent()); //Start the service
    	}
    	else
    	{
    		System.out.println("Debug: getTwitterServiceIntent() == null, Service not started.");
    	}
        
        }
      });	
	
	//back button code
    final Button backButton = (Button) findViewById(R.id.backButton);
    backButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
    	finish(); 
      }
    });

  }
  
  public void setTwitterServiceIntent(Intent intent)
  {
	  System.out.println("Debug: called setTwitterServiceIntent()");
	  twitterServiceIntent = intent;
  }
  
  public static Intent getTwitterServiceIntent()
  {
	  System.out.println("Debug: called getTwitterServiceIntent()");
	  return twitterServiceIntent;
  }
  
}