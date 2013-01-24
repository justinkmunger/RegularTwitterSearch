package com.berksoft.regulartwitter;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

public class TwitterSearchActivity extends Activity 
                                   implements TwitterSearchFragment.TwitterSearchFragmentListener {

    public static final String TWITTER_SEARCH_RESULTS_MESSAGE = 
            "com.berksoft.regulartwittersearch.TwitterSearchActivity.TWITTER_SEARCH_RESULTS_MESSAGE";
    
    public static final String TWITTER_SEARCH_RESULT_MESSAGE_EXTRA_RESULTS = "results";
    
    private TwitterSearchFragment mTwitterSearchFragment;

    private BroadcastReceiver mTwitterSearchResultReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), TWITTER_SEARCH_RESULTS_MESSAGE)) {
                
                List<TwitterSearchResult> twitterSearchResults = 
                        intent.getExtras().getParcelableArrayList(TWITTER_SEARCH_RESULT_MESSAGE_EXTRA_RESULTS);
                
                TwitterSearchResultMessage message = new TwitterSearchResultMessage(twitterSearchResults);
                mTwitterSearchFragment.setTwitterResults(message.getTwitterSearchResults());
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_search);
        
        mTwitterSearchFragment = 
                (TwitterSearchFragment)getFragmentManager().findFragmentById(R.id.activity_twitter_search_fragment);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        LocalBroadcastManager
            .getInstance(this)
            .registerReceiver(mTwitterSearchResultReceiver, new IntentFilter(TWITTER_SEARCH_RESULTS_MESSAGE));
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        LocalBroadcastManager
            .getInstance(this)
            .unregisterReceiver(mTwitterSearchResultReceiver);
    }
    
    @Override
    public void onTwitterSearchQuery(TwitterSearchQueryMessage message) {
        String query = message.getQuery();
        
        Intent intent = new Intent(this, TwitterSearchService.class);
        intent.setAction(TwitterSearchService.ACTION_QUERY_TWITTER);
        intent.putExtra(TwitterSearchService.EXTRA_QUERY_TWITTER_QUERY, query);
        
        startService(intent);
    }
}
