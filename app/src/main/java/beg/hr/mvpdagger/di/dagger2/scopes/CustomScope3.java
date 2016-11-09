package beg.hr.mvpdagger.di.dagger2.scopes;

/**
 * Created by juraj on 04/11/16.
 */

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A scoping annotation to permit objects whose lifetime should
 * conform to the life of the activity to be memoized in the
 * correct component.
 */
@Scope
@Retention(RUNTIME)
public @interface CustomScope3 {
}
