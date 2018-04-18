package com.sarath.denshiotoko.funampj.music;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sarath.denshiotoko.funampj.R;
import com.sarath.denshiotoko.funampj.data.Song;
import com.sarath.denshiotoko.funampj.di.ActivityScoped;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MusicFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */

@ActivityScoped
public class MusicFragment extends DaggerFragment implements MusicContract.MusicView{

    @Inject
    MusicContract.Presenter mPresenter;

    private OnFragmentInteractionListener mListener;

    private MusicAdapter mListAdapter;

    private Context context;

    private View lastPlayedView = null;

    private View mNoMusicView;

    private LinearLayout mMusicView;

    @Inject
    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getContext();
        mListAdapter = new MusicAdapter(new ArrayList<Song>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeMusicView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropMusicView();//prevent leaking activity in
        // case presenter is orchestrating a long running task
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_music, container, false);

        // Set up tasks view
        mMusicView = root.findViewById(R.id.musicList);
        ListView listView = root.findViewById(R.id.songs_list);
        listView.setAdapter(mListAdapter);

        // Setup no music view
        mNoMusicView = root.findViewById(R.id.noMusic);

        return root;
    }

    public void sendStateToActivity(Song song) {
        if (mListener != null) {
            mListener.onFragmentInteraction(song);
        }
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
    public void showMusic(List<Song> songs) {
        mListAdapter.replaceData(songs);

        mMusicView.setVisibility(View.VISIBLE);
        mNoMusicView.setVisibility(View.GONE);
    }

    @Override
    public void showNoMusic() {
        mMusicView.setVisibility(View.GONE);
        mNoMusicView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSongMessage(String sTitle, String status) {
        if(status.equals("play"))
            showMessage(sTitle + " " +getString(R.string.success_song_play_message));
        else if(status.equals("stop"))
            showMessage(getString(R.string.success_song_stop_message));
        else
            showMessage(getString(R.string.failure_song_message));
    }

    public void showMessage(String msg){
        Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG).show();
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

    public interface SongItemListener {

        void onSongPlayClick(Song clickedSong);

        void onSongStopClick();

    }

    /**
     * Listener for clicks on tasks in the ListView.
     */
    SongItemListener mItemListener = new SongItemListener() {
        @Override
        public void onSongPlayClick(Song clickedSong) {
            sendStateToActivity(clickedSong);
            mPresenter.playSong(clickedSong);
        }

        @Override
        public void onSongStopClick() {
            mPresenter.stopSong();
        }
    };

    private class MusicAdapter extends BaseAdapter {

        private List<Song> mSongs;
        private SongItemListener mItemListener;

        private MusicAdapter(List<Song> songs, SongItemListener itemListener) {
            setList(songs);
            mItemListener = itemListener;
        }

        private void replaceData(List<Song> songs) {
            setList(songs);
            notifyDataSetChanged();
        }

        private void setList(List<Song> songs) {
            mSongs = checkNotNull(songs);
        }

        @Override
        public int getCount() {
            return mSongs.size();
        }

        @Override
        public Song getItem(int i) {
            return mSongs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.song_item, viewGroup, false);
            }

            final Song song = getItem(i);

            final TextView titleTV = rowView.findViewById(R.id.title);
            titleTV.setText(song.getSongTitle());

            if(lastPlayedView != rowView) {
                Drawable rightDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_play_circle_filled_black_24dp);
                titleTV.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, rightDrawable, null);
            }

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(lastPlayedView == view){
                        resetPlayView();
                        mItemListener.onSongStopClick();
                        return;
                    }

                    if(lastPlayedView != null){
                        resetPlayView();
                    }

                    view.setBackgroundColor(getResources().getColor(R.color.colorSelectItem));
                    Drawable rightDrawable1 = AppCompatResources.getDrawable(context, R.drawable.ic_pause_circle_filled_black_24dp);
                    titleTV.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,rightDrawable1,null);

                    lastPlayedView = view;

                    mItemListener.onSongPlayClick(song);
                }
            });

            return rowView;
        }
    }

    private void resetPlayView(){

        lastPlayedView.setBackgroundColor(Color.WHITE);

        TextView titleTV = lastPlayedView.findViewById(R.id.title);
        Drawable rightDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_play_circle_filled_black_24dp);
        titleTV.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,rightDrawable,null);

        lastPlayedView = null;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
