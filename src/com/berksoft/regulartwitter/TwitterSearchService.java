package com.berksoft.regulartwitter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

public class TwitterSearchService extends IntentService {

    public static String ACTION_QUERY_TWITTER = "com.berksoft.ottotwitter.TwitterSearchService.QUERY_TWITTER";
    public static String EXTRA_QUERY_TWITTER_QUERY = "query";
    
    private static String TWITTER_QUERY_STRING = "http://search.twitter.com/search.json?q=";
    
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ArrayList<TwitterSearchResult> twitterSearchResults = (ArrayList<TwitterSearchResult>)msg.obj;
            
            Intent intent = new Intent();
            intent.setAction(TwitterSearchActivity.TWITTER_SEARCH_RESULTS_MESSAGE);
            intent.putParcelableArrayListExtra(TwitterSearchActivity.TWITTER_SEARCH_RESULT_MESSAGE_EXTRA_RESULTS, twitterSearchResults);
            
            LocalBroadcastManager.getInstance(TwitterSearchService.this).sendBroadcast(intent);
        };
    };
    
    public TwitterSearchService() {
        super("TwitterSearchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        List<TwitterSearchResult> twitterSearchResults = new ArrayList<TwitterSearchResult>();
        
        if (!TextUtils.isEmpty(action) && action.equals(ACTION_QUERY_TWITTER)) {
            String query = null;
            
            query = intent.getExtras().getString(EXTRA_QUERY_TWITTER_QUERY);
            if (!TextUtils.isEmpty(query)) {
                
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response;
                try {
                    response = httpclient.execute(new HttpGet(TWITTER_QUERY_STRING + query));
                    StatusLine statusLine = response.getStatusLine();
                    if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        response.getEntity().writeTo(out);
                        out.close();
                        String responseString = out.toString();
                        
                        JSONObject jsonObject = new JSONObject(responseString);
                        
                        JSONArray resultObjects = jsonObject.getJSONArray("results");
                        
                        for (int i = 0; i < resultObjects.length(); i++) {
                            TwitterSearchResult currentResult = 
                                    new TwitterSearchResult(resultObjects.getJSONObject(i));
                            twitterSearchResults.add(currentResult);
                        }
                        
                        Message message = mHandler.obtainMessage();
                        message.obj = twitterSearchResults;
                        mHandler.sendMessage(message);
                        
                    } else{
                        //Closes the connection.
                        response.getEntity().getContent().close();
                        throw new IOException(statusLine.getReasonPhrase());
                    }
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
