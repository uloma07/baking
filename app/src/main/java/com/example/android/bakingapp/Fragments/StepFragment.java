package com.example.android.bakingapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.Models.Step;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.NetworkUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

public class StepFragment extends Fragment implements ExoPlayer.EventListener {

    Step step;
    public static final String STEP = "step_obj";
    public static final String PLAYBACKPOSITION = "playpos";
    public static final String CURRENTWINDOWINDEX = "CurrentWindowIndex";
    public static final String PLAYWHENREADY = "playWhenReady";
    SimpleExoPlayer mExoplayer;
    SimpleExoPlayerView mExoPlayerview;
    View rootView;
    TextView shortDesc;
    TextView desc;
    ImageView img;
    MediaSource mediaSource;
    TrackSelector trackselector;
    int currentWindow;
    Long playbackPosition;
    boolean playWhenReady;

    private ComponentListener componentListener;

    public StepFragment(){ }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if(playbackState == ExoPlayer.STATE_READY && playWhenReady){
            Log.v("EXOPLAY", "player ready");
        }
        else{
            Log.v("EXOPLAY", "player not ready");
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(STEP,step);
        if(mExoplayer != null) {
            outState.putLong(PLAYBACKPOSITION, mExoplayer.getCurrentPosition());
            outState.putInt(CURRENTWINDOWINDEX, mExoplayer.getCurrentWindowIndex());
            outState.putBoolean(PLAYWHENREADY, mExoplayer.getPlayWhenReady());
            outState.putBoolean("EXO", true);
        }
        else{
            outState.putBoolean("EXO", false);
        }
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        if(savedInstanceState != null){
            step = savedInstanceState.getParcelable(STEP);
            boolean exosaved = savedInstanceState.getBoolean("EXO");
            if(exosaved){
                playbackPosition = savedInstanceState.getLong(PLAYBACKPOSITION);
                currentWindow = savedInstanceState.getInt(CURRENTWINDOWINDEX);
                playWhenReady = savedInstanceState.getBoolean(PLAYWHENREADY);
            }
            else{
                playbackPosition = 0L;
                currentWindow = 0;
                playWhenReady = true;
            }
        }
        else{
            playbackPosition = 0L;
            currentWindow = 0;
            playWhenReady = true;
        }

        componentListener = new ComponentListener();

        View rootView = inflater.inflate(R.layout.fragment_step,container,false);
        TextView shortDesc = (TextView) rootView.findViewById(R.id.tv_shortdescription);
        TextView desc = (TextView) rootView.findViewById(R.id.tv_description);
        ImageView img = (ImageView) rootView.findViewById(R.id.im_thumbnail);
        mExoPlayerview = (SimpleExoPlayerView) rootView.findViewById(R.id.exo_playerviewer);


        if(step != null){

            shortDesc.setText(step.getshortDescription());
            desc.setText(step.getDescription());
            String url = step.getthumbnailURL();
            String vidurl = step.getvideoURL();
            if(url != null && !url.trim().isEmpty() ) {
                Picasso.with(super.getContext())
                        .load(step.getthumbnailURL())
                        .into(img);
            }
            if(vidurl != null && !vidurl.trim().isEmpty() ) {
                initialisePlayer(Uri.parse(vidurl), rootView.getContext());
            }
            else{
                mExoPlayerview.setVisibility(View.GONE);
                rootView.findViewById(R.id.no_video_textview).setVisibility(View.VISIBLE);
            }

        }else {
            //TODO: replace this with a log
        }

        return rootView;

    }

    private void initialisePlayer(Uri videourl,Context c) {
        if(!NetworkUtils.isConnectedToInternet(c)){
            Toast toast = Toast.makeText(c, "Please check internet connection", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        if(mExoplayer == null){
            trackselector = new DefaultTrackSelector();
            LoadControl loadcontrol = new DefaultLoadControl();
            mExoplayer = ExoPlayerFactory.newSimpleInstance(c,trackselector,loadcontrol);
            mExoplayer.addListener(componentListener);
            mExoPlayerview.setPlayer(mExoplayer);
            String userAgent = Util.getUserAgent(c,getResources().getString(R.string.app_name));
            mediaSource = new ExtractorMediaSource(videourl, new DefaultDataSourceFactory(c,userAgent), new DefaultExtractorsFactory(), null, null);
            mExoplayer.prepare(mediaSource);
            mExoplayer.setPlayWhenReady(playWhenReady);
            mExoplayer.seekTo(currentWindow, playbackPosition);
            mExoplayer.addListener(this);
        }
        else{
            mExoplayer.setPlayWhenReady(playWhenReady);
            mExoplayer.seekTo(currentWindow, playbackPosition);
            mExoplayer.addListener(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void releasePlayer(){
        if(mExoplayer!=null){
            playbackPosition = mExoplayer.getCurrentPosition();
            currentWindow = mExoplayer.getCurrentWindowIndex();
            playWhenReady = mExoplayer.getPlayWhenReady();
            mExoplayer.stop();
            mExoplayer.removeListener(componentListener);
            mExoplayer.release();
        }
        mExoplayer = null;
        mediaSource = null;
        trackselector = null;

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if(step!= null && rootView!= null && step.getvideoURL() != null && !step.getvideoURL().trim().isEmpty() ) {
                initialisePlayer(Uri.parse(step.getvideoURL()), rootView.getContext());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoplayer == null) {
            if(step!= null && rootView!= null && step.getvideoURL() != null && !step.getvideoURL().trim().isEmpty() ) {
                initialisePlayer(Uri.parse(step.getvideoURL()), rootView.getContext());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }



    public void setStep(Step thestep){
        step=thestep;
    }


    private class ComponentListener implements ExoPlayer.EventListener {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            String stateString;
            switch (playbackState) {
                case ExoPlayer.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    break;
                case ExoPlayer.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY     -";
                    break;
                case ExoPlayer.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    break;
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity() {

        }
    }
}
