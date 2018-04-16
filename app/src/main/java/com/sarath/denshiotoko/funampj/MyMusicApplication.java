package com.sarath.denshiotoko.funampj;

import android.support.annotation.VisibleForTesting;

import com.sarath.denshiotoko.funampj.data.source.MusicRepositoryImpl;
import com.sarath.denshiotoko.funampj.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class MyMusicApplication extends DaggerApplication {

    @Inject
    MusicRepositoryImpl musicRepository;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    /**
     * Our Espresso tests need to be able to get an instance of the {@link MusicRepositoryImpl}
     * so that we can delete all tasks before running each test
     */
    @VisibleForTesting
    public MusicRepositoryImpl getTasksRepository() {
        return musicRepository;
    }
}
