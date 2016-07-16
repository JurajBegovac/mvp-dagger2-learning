package beg.hr.mvpdagger.di.dagger2.qualifiers;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by juraj on 16/07/16.
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface ActivityContext {
}
