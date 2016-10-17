package beg.hr.mvpdagger.db;

import com.google.auto.value.AutoValue;

/**
 * Created by juraj on 17/10/16.
 */

@AutoValue
public abstract class User {

    public static User create(String id, String name) {
        return new AutoValue_User(id, name);
    }

    public abstract String id();
    public abstract String name();
}
