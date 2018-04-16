package com.sarath.denshiotoko.funampj.music;


import com.google.common.collect.Lists;
import com.sarath.denshiotoko.funampj.data.Song;
import com.sarath.denshiotoko.funampj.data.source.MusicRepository;
import com.sarath.denshiotoko.funampj.data.source.MusicRepositoryImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MusicPresenterTest {

    private static List<Song> SONGS;

    @Mock
    private MusicRepositoryImpl musicRepository;

    @Mock
    private MusicContract.MusicView musicView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<MusicRepository.LoadMusicCallback> mLoadMusicCallbackCaptor;

    private MusicPresenter musicPresenter;

    @Before
    public void setupMusicPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        musicPresenter = new MusicPresenter(musicRepository);
        musicPresenter.takeView(musicView);

        // The presenter won't update the view unless it's active.
        when(musicView.isActive()).thenReturn(true);

        // We start the tasks to 3, with one active and two completed
        // We start the tasks to 3, with one active and two completed
        SONGS = Lists.newArrayList(new Song(1L, "Description1","Data1"),
                new Song(2L, "Description2", "Data2"), new Song(3L, "Description3", "Data3"));
    }

    @Test
    public void loadAllMusicFromRepositoryAndLoadIntoView() {

        assertTrue(SONGS.size() == 3);
        musicPresenter.loadMusic();

        // Callback is captured and invoked with stubbed tasks twice
        //First time is when the fragment is bound to the view and a second time when we force another load
        verify(musicRepository,times(2)).getSongs(mLoadMusicCallbackCaptor.capture());
        mLoadMusicCallbackCaptor.getValue().onMusicLoaded(SONGS);

        // Given an initialized TasksPresenter with initialized tasks
        ArgumentCaptor<List> showMusicArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(musicView).showMusic(showMusicArgumentCaptor.capture());
        assertTrue(showMusicArgumentCaptor.getValue().size() == 3);
    }

    @Test
    public void loadNoMusicAndLoadIntoView() {

        musicPresenter.loadMusic();

        // Callback is captured and invoked with stubbed tasks twice
        //First time is when the fragment is bound to the view and a second time when we force another load
        verify(musicRepository,times(2)).getSongs(mLoadMusicCallbackCaptor.capture());
        mLoadMusicCallbackCaptor.getValue().onDataNotAvailable(); // Empty

        // Given an initialized TasksPresenter with initialized tasks
        verify(musicView).showNoMusic();
    }

    @Test
    public void clickOnSong_showSongMessage_Error() {
        // Given a stubbed active task
        Song requestedSong = new Song(1L,"Details Requested", "For this song");

        // When open task details is requested
        musicPresenter.playSong(requestedSong);

        // Then task detail UI is shown
        ArgumentCaptor<String> showMusicArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> showMusicArgumentCaptor2 = ArgumentCaptor.forClass(String.class);
        verify(musicView).showSongMessage(showMusicArgumentCaptor.capture(),showMusicArgumentCaptor2.capture());
        assertTrue(showMusicArgumentCaptor.getValue().equals(requestedSong.getSongTitle()));
        // sends "error" because music player cannot play this temporary song data
        assertTrue(showMusicArgumentCaptor2.getValue().equals("error"));
    }

}
