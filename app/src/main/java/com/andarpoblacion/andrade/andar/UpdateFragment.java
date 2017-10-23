package com.andarpoblacion.andrade.andar;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateFragment extends Fragment {

    private ListView tweetsLv;
    private ProgressBar pbTweet;

    public UpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_update, container, false);
        tweetsLv = (ListView) v.findViewById(R.id.lvTweets);
        pbTweet = (ProgressBar) v.findViewById(R.id.progressBar3);


        /*if(!isConnectedToInternet()){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder
                    .setMessage("No internet connection..."+"\n"+"You need internet connection to see the updates")
                    .setCancelable(true);

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setTitle("Error");
            alertDialog.show();
        }*/


        TwitterConfig config = new TwitterConfig.Builder(getActivity())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("1h3yGRCRBld8xdNPenI8aDn21", "nE6uINZxUicaHCmhwH3hmkP5dAsjlX9rMD917L7Lgi6R7YaSs6"))
                .debug(true)
                .build();
        Twitter.initialize(config);


        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("dost_pagasa")
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(userTimeline)
                .build();

        tweetsLv.setAdapter(adapter);
        tweetsLv.setEmptyView(pbTweet);

        return v;
    }

    private boolean isConnectedToInternet(){
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(null!=ni){
            return true;
        } else {
            return false;
        }

    }

}
