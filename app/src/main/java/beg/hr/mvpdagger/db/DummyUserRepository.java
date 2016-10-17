package beg.hr.mvpdagger.db;

import javax.inject.Inject;

/**
 * Created by juraj on 17/10/16.
 */

public class DummyUserRepository implements UserRepository {

    @Inject
    public DummyUserRepository() {
    }

    @Override
    public User getUser(String p_id) {
        return User.create(p_id, "Juraj");
    }
}
