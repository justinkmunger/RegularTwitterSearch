package com.berksoft.regulartwitter;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class TwitterSearchFragment extends Fragment {
   
    public interface TwitterSearchFragmentListener {
        public void onTwitterSearchQuery(TwitterSearchQueryMessage message);
    }
    
    private EditText mQuery;
    private Button mSearchButton;
    private ListView mResults;
    
    private TwitterSearchFragmentListener mListener;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        try {
            mListener = (TwitterSearchFragmentListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement TwitterSearchFragmentListener");
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_twitter_search, container, false);
        return layout;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        
        mQuery = (EditText)activity.findViewById(R.id.fragment_twitter_search_query);
        mSearchButton = (Button)activity.findViewById(R.id.fragment_twitter_search_searchbutton);
        
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onTwitterSearchQuery(new TwitterSearchQueryMessage(mQuery.getText().toString()));
                }
            }
        });
        mResults = (ListView)activity.findViewById(R.id.fragment_twitter_search_results);
    }
    
    public void setTwitterResults(List<TwitterSearchResult> results) {
        mResults.setAdapter(new TwitterSearchResultAdapter(getActivity(), results));
    }
   
    private class TwitterSearchResultAdapter extends BaseAdapter {

        private List<TwitterSearchResult> mTwitterSearchResults;
        private LayoutInflater mLayoutInflater;
        
        public TwitterSearchResultAdapter(Context context, List<TwitterSearchResult> twitterSearchResults) {
            mTwitterSearchResults = twitterSearchResults;
            mLayoutInflater = LayoutInflater.from(context);
        }
        
        @Override
        public int getCount() {
            return mTwitterSearchResults.size();
        }

        @Override
        public Object getItem(int position) {
            return mTwitterSearchResults.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
               row = mLayoutInflater.inflate(R.layout.row_twitter_search_result, null);
            }
            
            ViewHolder holder = (ViewHolder)row.getTag();
            
            if (holder == null) {
                holder = new ViewHolder(row);
                row.setTag(holder);
            }
        
            TwitterSearchResult result = mTwitterSearchResults.get(position);
            holder.fromUser.setText(result.getFromUser());
            holder.text.setText(result.getText());
            
            return row;
        }
        
        private class ViewHolder {
            public TextView fromUser;
            public TextView text;
            
            public ViewHolder(View row) {
                fromUser = (TextView)row.findViewById(R.id.row_twitter_search_result_from_user);
                text = (TextView)row.findViewById(R.id.row_twitter_search_result_text);
            }
        }
    }
}
