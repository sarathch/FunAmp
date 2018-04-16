package com.sarath.denshiotoko.funampj.data.source;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * This is used by Dagger to inject the required arguments into the {@link MusicRepository}.
 */
@Module
public class MusicRepositoryModule {

    @Provides
    MusicRepositoryImpl provideMusicRepository(Context context){
        return new MusicRepositoryImpl(context);
    }
}
