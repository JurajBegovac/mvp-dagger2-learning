package beg.hr.flowlesstest.dagger2.scopes;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** Created by juraj on 30/09/16. */
@Scope
@Retention(RUNTIME)
public @interface PerScreen {}
