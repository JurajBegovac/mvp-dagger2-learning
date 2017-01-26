package beg.hr.mvpdagger.mvi.atom;

import com.google.common.base.Function;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/** TODO: Add a class header comment! */
public class Transformations {

  public static <T> Transform<T> compose(Transform<T> g, Transform<T> f) {
    return new TransformComposition<>(g, f);
  }

  /** Function to transform data */
  public interface Transform<T> extends Function<T, T> {}

  private static class TransformComposition<T> implements Transform<T> {
    private final Transform<T> g;
    private final Transform<T> f;

    public TransformComposition(Transform<T> g, Transform<T> f) {
      this.g = checkNotNull(g);
      this.f = checkNotNull(f);
    }

    @Nullable
    @Override
    public T apply(@Nullable T input) {
      return g.apply(f.apply(input));
    }

    @Override
    public int hashCode() {
      return f.hashCode() ^ g.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
      if (obj instanceof TransformComposition) {
        TransformComposition<?> that = (TransformComposition<?>) obj;
        return f.equals(that.f) && g.equals(that.g);
      }
      return false;
    }

    @Override
    public String toString() {
      return g + "(" + f + ")";
    }
  }
}
