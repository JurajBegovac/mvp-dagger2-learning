package beg.hr.mvpdagger.di.dagger2.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by juraj on 28/09/16.
 */
@Module
public class NetModule {

    @Provides
    @Singleton
    public Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .build();
    }
}
