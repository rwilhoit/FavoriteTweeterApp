package com.morganclaypool.mobile;

import java.io.*;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.*;
import android.widget.*;

public class TweetArrayAdaptor extends ArrayAdapter<Tweet> {

  private final Context context;
  private final List<Tweet> tweets;  

  public TweetArrayAdaptor(Context context, List<Tweet> tweets) {
    super(context, R.layout.tweet, tweets);
    this.context = context;
    this.tweets = tweets;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    Tweet tweet = tweets.get(position);
    View tweetView = inflater.inflate(R.layout.tweet, parent, false);
    TextView textView = (TextView) tweetView.findViewById(R.id.text);
    textView.setText(tweet.user + ": " +tweet.text);
    ImageView imageView = (ImageView) tweetView.findViewById(R.id.icon);
    imageView.setImageDrawable(loadImageFromURL(tweet.iconUrl));
    return tweetView;
  }

  private Drawable loadImageFromURL(String url) {
    Drawable drawable = null;
    try {
      InputStream is = (InputStream) new URL(url).getContent();
      drawable = Drawable.createFromStream(is, "srcname");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return drawable;
  }

}