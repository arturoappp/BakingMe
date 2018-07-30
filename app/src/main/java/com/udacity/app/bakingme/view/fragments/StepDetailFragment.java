package com.udacity.app.bakingme.view.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.udacity.app.bakingme.R;
import com.udacity.app.bakingme.databinding.FragmentStepDetailBinding;
import com.udacity.app.bakingme.model.Step;

public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener {
  private static final String ARG_STEP = "STEP";
  private static final String ARG_SIZE = "SIZE";
  private static final String ARG_INDEX = "INDEX";

  private Step step;
  private int sizeListSteps;
  private int mIndex;
  private OnStepDetailFragmentInteractionListener mListener;
  private MyClickHandlers handlers;

  // video
  private SimpleExoPlayer mExoPlayer;
  private static SimpleExoPlayerView mPlayerView;
  private static MediaSessionCompat mMediaSession;
  private static final String TAG = StepDetailFragment.class.getSimpleName();
  private PlaybackStateCompat.Builder mStateBuilder;

  FragmentStepDetailBinding binding;

  public StepDetailFragment() {
    // Required empty public constructor
  }

  public static StepDetailFragment newInstance(Step step, int sizeListSteps, int index) {
    StepDetailFragment fragment = new StepDetailFragment();
    Bundle args = new Bundle();
    args.putParcelable(ARG_STEP, step);
    args.putInt(ARG_SIZE, sizeListSteps);
    args.putInt(ARG_INDEX, index);

    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      this.step = getArguments().getParcelable(ARG_STEP);
      this.sizeListSteps = getArguments().getInt(ARG_SIZE);
      this.mIndex = getArguments().getInt(ARG_INDEX);
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container, false);
    View view = binding.getRoot();
    handlers = new MyClickHandlers(getContext());
    // assign click handlers
    binding.setHandlers(handlers);
    // here data must be an instance of the class MarsDataProvider
    binding.setStep(step);

    // Initialize the player view.
    mPlayerView = binding.playerView;

    // Initialize the Media Session.
    initializeMediaSession();
    // Initialize the player.
    initializePlayer(Uri.parse(step.getVideoURL()));

    initializeFloatingActionButton();

    hideActionBarIfLandscape();
    return view;
  }

  private void initializeFloatingActionButton() {
    if (binding.floatingActionButtonPrevious != null) {
      if (mIndex == 0) {
        binding.floatingActionButtonPrevious.setVisibility(View.INVISIBLE);
      } else if (mIndex == sizeListSteps - 1) {
        binding.floatingActionButtonNext.setVisibility(View.INVISIBLE);
      }
    }
  }

  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onStepDetailFragment(uri);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnStepDetailFragmentInteractionListener) {
      mListener = (OnStepDetailFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(
          context.toString() + " must implement OnStepDetailFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
    releasePlayer();
  }

  private void initializeMediaSession() {

    // Create a MediaSessionCompat.
    mMediaSession = new MediaSessionCompat(getContext(), TAG);

    // Enable callbacks from MediaButtons and TransportControls.
    mMediaSession.setFlags(
        MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
            | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

    // Do not let MediaButtons restart the player when the app is not visible.
    mMediaSession.setMediaButtonReceiver(null);

    // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
    mStateBuilder =
        new PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY
                    | PlaybackStateCompat.ACTION_PAUSE
                    | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    | PlaybackStateCompat.ACTION_PLAY_PAUSE);

    mMediaSession.setPlaybackState(mStateBuilder.build());

    // MySessionCallback has methods that handle callbacks from a media controller.
    mMediaSession.setCallback(new MySessionCallback());

    // Start the Media Session since the activity is active.
    mMediaSession.setActive(true);
  }

  private void initializePlayer(Uri mediaUri) {
    if (mExoPlayer == null) {
      // CreateSimpleExoPlayerView an instance of the ExoPlayer.
      TrackSelector trackSelector = new DefaultTrackSelector();
      LoadControl loadControl = new DefaultLoadControl();
      mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
      mPlayerView.setPlayer(mExoPlayer);

      // Set the ExoPlayer.EventListener to this activity.
      mExoPlayer.addListener(this);

      // Prepare the MediaSource.
      String userAgent =
          Util.getUserAgent(getContext(), getContext().getResources().getString(R.string.app_name));
      MediaSource mediaSource =
          new ExtractorMediaSource(
              mediaUri,
              new DefaultDataSourceFactory(getContext(), userAgent),
              new DefaultExtractorsFactory(),
              null,
              null);
      mExoPlayer.prepare(mediaSource);
      mExoPlayer.setPlayWhenReady(true);

      if (step.getVideoURL().isEmpty())
        mPlayerView.setDefaultArtwork(
            BitmapFactory.decodeResource(getResources(), R.drawable.ic_img_notsource));
    }
  }

  private void releasePlayer() {
    if (mExoPlayer != null) {
      mExoPlayer.stop();
      mExoPlayer.release();
      mExoPlayer = null;
    }
  }

  @Override
  public void onTimelineChanged(Timeline timeline, Object manifest) {}

  @Override
  public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

  @Override
  public void onLoadingChanged(boolean isLoading) {}

  @Override
  public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {}

  @Override
  public void onPlayerError(ExoPlaybackException error) {}

  @Override
  public void onPositionDiscontinuity() {}

  private class MySessionCallback extends MediaSessionCompat.Callback {
    @Override
    public void onPlay() {
      mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onPause() {
      mExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onSkipToPrevious() {
      mExoPlayer.seekTo(0);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    releasePlayer();
    mMediaSession.setActive(false);
  }

  private void hideActionBarIfLandscape() {
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }
  }

  public class MyClickHandlers {

    Context context;

    public MyClickHandlers(Context context) {
      this.context = context;
    }

    public void onPreviousStepClicked(View view) {
      binding.floatingActionButtonNext.setVisibility(View.VISIBLE);
      Toast.makeText(context, "onPreviousStepClicked is clicked!", Toast.LENGTH_SHORT).show();
      int index = mIndex - 1;

      mListener.onPreviousStepClicked(index);
    }

    public void onNextStepClicked(View view) {
      binding.floatingActionButtonPrevious.setVisibility(View.VISIBLE);
      Toast.makeText(context, "onNextStepClicked is clicked!", Toast.LENGTH_SHORT).show();
      int index = mIndex + 1;

      mListener.onNextStepClicked(index);
    }
  }

  public interface OnStepDetailFragmentInteractionListener {
    void onStepDetailFragment(Uri uri);

    void onPreviousStepClicked(int index);

    void onNextStepClicked(int index);
  }
}
