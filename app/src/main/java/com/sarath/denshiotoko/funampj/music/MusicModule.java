package com.sarath.denshiotoko.funampj.music;

import com.sarath.denshiotoko.funampj.di.ActivityScoped;
import com.sarath.denshiotoko.funampj.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link MusicPresenter}.
 */
@Module
public abstract class MusicModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MusicFragment tasksFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract LyricFragment lyricFragment();

    @ActivityScoped
    @Binds
    abstract MusicContract.Presenter taskPresenter(MusicPresenter presenter);
}
