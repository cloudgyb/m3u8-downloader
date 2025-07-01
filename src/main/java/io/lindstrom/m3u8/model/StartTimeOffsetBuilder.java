package io.lindstrom.m3u8.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Builds instances of type {@link StartTimeOffset StartTimeOffset}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code StartTimeOffsetBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})

class StartTimeOffsetBuilder {
  private static final long INIT_BIT_TIME_OFFSET = 0x1L;
  private static final long OPT_BIT_PRECISE = 0x1L;
  private long initBits = 0x1L;
  private long optBits;

  private double timeOffset;
  private boolean precise;

  /**
   * Creates a builder for {@link StartTimeOffset StartTimeOffset} instances.
   */
  StartTimeOffsetBuilder() {
    if (!(this instanceof StartTimeOffset.Builder)) {
      throw new UnsupportedOperationException("Use: new StartTimeOffset.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code StartTimeOffset} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final StartTimeOffset.Builder from(StartTimeOffset instance) {
    Objects.requireNonNull(instance, "instance");
    timeOffset(instance.timeOffset());
    precise(instance.precise());
    return (StartTimeOffset.Builder) this;
  }

  /**
   * Initializes the value for the {@link StartTimeOffset#timeOffset() timeOffset} attribute.
   * @param timeOffset The value for timeOffset 
   * @return {@code this} builder for use in a chained invocation
   */
  public final StartTimeOffset.Builder timeOffset(double timeOffset) {
    this.timeOffset = timeOffset;
    initBits &= ~INIT_BIT_TIME_OFFSET;
    return (StartTimeOffset.Builder) this;
  }

  /**
   * Initializes the value for the {@link StartTimeOffset#precise() precise} attribute.
   * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link StartTimeOffset#precise() precise}.</em>
   * @param precise The value for precise 
   * @return {@code this} builder for use in a chained invocation
   */
  public final StartTimeOffset.Builder precise(boolean precise) {
    this.precise = precise;
    optBits |= OPT_BIT_PRECISE;
    return (StartTimeOffset.Builder) this;
  }

  /**
   * Builds a new {@link StartTimeOffset StartTimeOffset}.
   * @return An immutable instance of StartTimeOffset
   * @throws IllegalStateException if any required attributes are missing
   */
  public StartTimeOffset build() {
    if (initBits != 0) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
    return new ImmutableStartTimeOffset(this);
  }

  private boolean preciseIsSet() {
    return (optBits & OPT_BIT_PRECISE) != 0;
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if ((initBits & INIT_BIT_TIME_OFFSET) != 0) attributes.add("timeOffset");
    return "Cannot build StartTimeOffset, some of required attributes are not set " + attributes;
  }

  /**
   * Immutable implementation of {@link StartTimeOffset}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new StartTimeOffset.Builder()}.
   */
  private static final class ImmutableStartTimeOffset implements StartTimeOffset {
    private final double timeOffset;
    private final boolean precise;

    private ImmutableStartTimeOffset(StartTimeOffsetBuilder builder) {
      this.timeOffset = builder.timeOffset;
      this.precise = builder.preciseIsSet()
          ? builder.precise
          : StartTimeOffset.super.precise();
    }

    /**
     * @return The value of the {@code timeOffset} attribute
     */
    @Override
    public double timeOffset() {
      return timeOffset;
    }

    /**
     * @return The value of the {@code precise} attribute
     */
    @Override
    public boolean precise() {
      return precise;
    }

    /**
     * This instance is equal to all instances of {@code ImmutableStartTimeOffset} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutableStartTimeOffset
          && equalTo((ImmutableStartTimeOffset) another);
    }

    private boolean equalTo(ImmutableStartTimeOffset another) {
      return Double.doubleToLongBits(timeOffset) == Double.doubleToLongBits(another.timeOffset)
          && precise == another.precise;
    }

    /**
     * Computes a hash code from attributes: {@code timeOffset}, {@code precise}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + Double.hashCode(timeOffset);
      h += (h << 5) + Boolean.hashCode(precise);
      return h;
    }

    /**
     * Prints the immutable value {@code StartTimeOffset} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      return "StartTimeOffset{"
          + "timeOffset=" + timeOffset
          + ", precise=" + precise
          + "}";
    }
  }
}
