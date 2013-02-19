package com.morganclaypool.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class TwitterSearchActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    final Button searchButton = (Button) findViewById(R.id.searchButton);
    searchButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(TwitterSearchActivity.this, TweetListActivity.class);
        
        Bundle bundle = new Bundle();
        final EditText keywordEditText = (EditText) findViewById(R.id.keyWordEditText);
        bundle.putString("keyword", keywordEditText.getText().toString());
        intent.putExtras(bundle);
        
        startActivity(intent);
      }
    });
    
    final Button registerButton = (Button) findViewById(R.id.registerButton);
    registerButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(TwitterSearchActivity.this, TwitterBookMarkActivity.class);
        System.out.println("Clicked register button.");

        startActivity(intent);
      }
    });
   
    final Button stopButton = (Button) findViewById(R.id.stopButton);
    stopButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
          System.out.println("Clicked stop button.");
          if(TwitterBookMarkActivity.getTwitterServiceIntent() != null)
          {
        	  Toast.makeText(TwitterSearchActivity.this, "Service Stopped.", Toast.LENGTH_SHORT).show();
        	  System.out.println("DEBUG: Calling stop service!");
        	  stopService(TwitterBookMarkActivity.getTwitterServiceIntent());
          }
          else
          {
        	  System.out.println("DEBUG: NOT Calling stop service!");
        	  Toast.makeText(TwitterSearchActivity.this, "No Service Running", Toast.LENGTH_SHORT).show();
          }
        }
      });
    
  }

}