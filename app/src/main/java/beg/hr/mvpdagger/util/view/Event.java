package beg.hr.mvpdagger.util.view;

import com.google.auto.value.AutoValue;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@AutoValue
public abstract class Event {

  interface View {
    Event raw();
  }

  public abstract String id();

  public abstract String type();

  public abstract long timestamp();

  public abstract String origin();

  public abstract Map<String, Object> data();

  public static Builder builder() {
    return new AutoValue_Event.Builder()
        .id(UUID.randomUUID().toString())
        .timestamp(System.currentTimeMillis())
        .data(Collections.emptyMap());
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder id(String id);

    public abstract Builder type(String type);

    public abstract Builder timestamp(long timestamp);

    public abstract Builder origin(String origin);

    public abstract Builder data(Map<String, Object> data);

    public abstract Event build();
  }
}
