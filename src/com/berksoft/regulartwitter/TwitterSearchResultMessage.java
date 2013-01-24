package com.berksoft.regulartwitter;

import java.util.List;

public class TwitterSearchResultMessage {
    private List<TwitterSearchResult> mTwitterSearchResults;
    
    public TwitterSearchResultMessage(List<TwitterSearchResult> twitterSearchResults) {
        mTwitterSearchResults = twitterSearchResults;
    }
    
    public List<TwitterSearchResult> getTwitterSearchResults() {
        return mTwitterSearchResults;
    }
}
