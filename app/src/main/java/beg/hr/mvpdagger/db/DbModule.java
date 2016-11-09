package beg.hr.mvpdagger.db;

import javax.inject.Singleton;

import beg.hr.mvpdagger.di.dagger2.scopes.PerActivity;
import dagger.Module;
import dagger.Provides;

/**
 * Created by juraj on 17/10/16.
 */
@Module
public class DbModule {

    @Provides
    @PerActivity
    public UserRepository userRepository(DummyUserRepository p_repository) {
        return p_repository;
    }

}
