package beg.hr.mvpdagger.db;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by juraj on 17/10/16.
 */
@Module
public class DbModule {

    @Provides
    @Singleton
    public UserRepository userRepository(DummyUserRepository p_repository) {
        return p_repository;
    }

}
