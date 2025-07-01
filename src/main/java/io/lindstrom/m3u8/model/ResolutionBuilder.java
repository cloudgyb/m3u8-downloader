package io.lindstrom.m3u8.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Builds instances of type {@link Resolution Resolution}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code ResolutionBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})
class ResolutionBuilder {
  private static final long INIT_BIT_WIDTH = 0x1L;
  private static final long INIT_BIT_HEIGHT = 0x2L;
  private long initBits = 0x3L;

  private int width;
  private int height;

  /**
   * Creates a builder for {@link Resolution Resolution} instances.
   */
  ResolutionBuilder() {
    if (!(this instanceof Resolution.Builder)) {
      throw new UnsupportedOperationException("Use: new Resolution.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code Resolution} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final Resolution.Builder from(Resolution instance) {
    Objects.requireNonNull(instance, "instance");
    width(instance.width());
    height(instance.height());
    return (Resolution.Builder) this;
  }

  /**
   * Initializes the value for the {@link Resolution#width() width} attribute.
   * @param width The value for width 
   * @return {@code this} builder for use in a chained invocation
   */
  public final Resolution.Builder width(int width) {
    this.width = width;
    initBits &= ~INIT_BIT_WIDTH;
    return (Resolution.Builder) this;
  }

  /**
   * Initializes the value for the {@link Resolution#height() height} attribute.
   * @param height The value for height 
   * @return {@code this} builder for use in a chained invocation
   */
  public final Resolution.Builder height(int height) {
    this.height = height;
    initBits &= ~INIT_BIT_HEIGHT;
    return (Resolution.Builder) this;
  }

  /**
   * Builds a new {@link Resolution Resolution}.
   * @return An immutable instance of Resolution
   * @throws IllegalStateException if any required attributes are missing
   */
  public Resolution build() {
    if (initBits != 0) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
    return new ImmutableResolution(this);
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if ((initBits & INIT_BIT_WIDTH) != 0) attributes.add("width");
    if ((initBits & INIT_BIT_HEIGHT) != 0) attributes.add("height");
    return "Cannot build Resolution, some of required attributes are not set " + attributes;
  }

  /**
   * Immutable implementation of {@link Resolution}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new Resolution.Builder()}.
   */
  private static final class ImmutableResolution implements Resolution {
    private final int width;
    private final int height;

    private ImmutableResolution(ResolutionBuilder builder) {
      this.width = builder.width;
      this.height = builder.height;
    }

    /**
     * @return The value of the {@code width} attribute
     */
    @Override
    public int width() {
      return width;
    }

    /**
     * @return The value of the {@code height} attribute
     */
    @Override
    public int height() {
      return height;
    }

    /**
     * This instance is equal to all instances of {@code ImmutableResolution} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutableResolution
          && equalTo((ImmutableResolution) another);
    }

    private boolean equalTo(ImmutableResolution another) {
      return width == another.width
          && height == another.height;
    }

    /**
     * Computes a hash code from attributes: {@code width}, {@code height}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + width;
      h += (h << 5) + height;
      return h;
    }

    /**
     * Prints the immutable value {@code Resolution} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      return "Resolution{"
          + "width=" + width
          + ", height=" + height
          + "}";
    }
  }
}
