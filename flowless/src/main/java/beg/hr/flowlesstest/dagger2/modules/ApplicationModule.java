package beg.hr.flowlesstest.dagger2.modules;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import beg.hr.flowlesstest.dagger2.qualifiers.ApplicationContext;

/** Created by juraj on 27/06/16. */
@Module
public class ApplicationModule {
  private final Application m_application;

  public ApplicationModule(Application p_application) {
    m_application = p_application;
  }

  @Singleton
  @Provides
  Application application() {
    return m_application;
  }

  @Singleton
  @Provides
  @ApplicationContext
  Context applicationContext() {
    return m_application.getApplicationContext();
  }
}
