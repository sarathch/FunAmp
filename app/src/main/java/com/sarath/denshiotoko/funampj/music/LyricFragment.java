package com.sarath.denshiotoko.funampj.music;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sarath.denshiotoko.funampj.R;
import com.sarath.denshiotoko.funampj.data.Song;
import com.sarath.denshiotoko.funampj.di.ActivityScoped;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LyricFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
@ActivityScoped
public class LyricFragment extends DaggerFragment implements MusicContract.LyricView{

    @Inject
    MusicContract.Presenter mPresenter;

    private OnFragmentInteractionListener mListener;

    private Song currentSongState = null;

    private TextView tvLyric;

    private View mLyricView;

    private LinearLayout mNoLyricView;

    private EditText etArtist, etSong;

    private TextView tvNoLyric;

    private Button btGetLyric;

    private ProgressBar progressBar;

    @Inject
    public LyricFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String currentSongStr = bundle.getString(getResources().getString(R.string.key_curr_song), "");
            if(!currentSongStr.isEmpty()){
                currentSongState = new Gson().fromJson(currentSongStr, Song.class);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_lyric, container, false);

        //set up lyric view
        mLyricView = root.findViewById(R.id.lyric_view);
        tvLyric = root.findViewById(R.id.tv_lyric);

        //setup no lyric view
        mNoLyricView = root.findViewById(R.id.no_lyric_view);
        etArtist = root.findViewById(R.id.et_enter_artist_name);
        etSong = root.findViewById(R.id.et_enter_song_name);
        btGetLyric = root.findViewById(R.id.bt_get_lyric);

        //setup progress bar view
        progressBar = root.findViewById(R.id.indeterminateProgressBar);

        // Initial load
        showNoLyrics();

        // set button click listener
        btGetLyric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadSongLyrics(etArtist.getText().toString(),etSong.getText().toString());
            }
        });

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Song song) {
        if (mListener != null) {
            mListener.onFragmentInteraction(song);
        }
    }

    private void showSongText(String title){
        tvLyric.setText(title);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("LyricFragment","onResume");
        mPresenter.takeLyricView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropLyricView();//prevent leaking activity in
        // case presenter is orchestrating a long running task
    }

    @Override
    public void setCurrentSongDetails(Song song) {
        currentSongState = song;
    }

    @Override
    public void showLyrics(String lyrics) {
        mLyricView.setVisibility(View.VISIBLE);
        mNoLyricView.setVisibility(View.GONE);
        showSongText(lyrics);
    }

    @Override
    public void showNoLyrics(){
        mNoLyricView.setVisibility(View.VISIBLE);
        mLyricView.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Song song);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
/*            currentSongState = mPresenter.fetchCurrentSongState();
            // Refresh your fragment here
            if(currentSongState!=null){
                showSongText(currentSongState.getSongTitle() +" "+ currentSongState.getSongArtist());
            }*/
            mPresenter.loadSongLyrics();
        }
    }
}
