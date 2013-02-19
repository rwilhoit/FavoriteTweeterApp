package com.morganclaypool.mobile;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.*;

import android.app.ListActivity;
import android.os.Bundle;

public class TweetListActivity extends ListActivity {

  private static final String TWITTER_SEARCH_API = "http://search.twitter.com/search.json?lang=en&q=";
  private DefaultHttpClient httpClient = new DefaultHttpClient();
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle bundle = this.getIntent().getExtras();
    String keyword = bundle.getString("keyword");
    
    TweetArrayAdaptor adapter = new TweetArrayAdaptor(this, searchTweets(keyword));
    setListAdapter(adapter);    
  }
  
  private List<Tweet> searchTweets(String keyword) {
    List<Tweet> tweetList = new ArrayList<Tweet>();
    String resultString = get(TWITTER_SEARCH_API + URLEncoder.encode(keyword));
    try {
      JSONArray resultJsonArray = (new JSONObject(resultString)).getJSONArray("results");
      JSONObject jsonObject = null;
      for (int i = 0; i < resultJsonArray.length(); i++) {
        jsonObject = resultJsonArray.getJSONObject(i);
        Tweet tweet = new Tweet();
        tweet.user = jsonObject.getString("from_user");
        tweet.text = jsonObject.getString("text");
        tweet.iconUrl = jsonObject.getString("profile_image_url");
        tweetList.add(tweet);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return tweetList;
  }
  
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