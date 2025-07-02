package io.lindstrom.m3u8.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Builds instances of type {@link PartialSegmentInformation PartialSegmentInformation}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code PartialSegmentInformationBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})

class PartialSegmentInformationBuilder {
  private static final long INIT_BIT_PART_TARGET_DURATION = 0x1L;
  private long initBits = 0x1L;

  private double partTargetDuration;

  /**
   * Creates a builder for {@link PartialSegmentInformation PartialSegmentInformation} instances.
   */
  PartialSegmentInformationBuilder() {
    if (!(this instanceof PartialSegmentInformation.Builder)) {
      throw new UnsupportedOperationException("Use: new PartialSegmentInformation.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code PartialSegmentInformation} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final PartialSegmentInformation.Builder from(PartialSegmentInformation instance) {
    Objects.requireNonNull(instance, "instance");
    partTargetDuration(instance.partTargetDuration());
    return (PartialSegmentInformation.Builder) this;
  }

  /**
   * Initializes the value for the {@link PartialSegmentInformation#partTargetDuration() partTargetDuration} attribute.
   * @param partTargetDuration The value for partTargetDuration 
   * @return {@code this} builder for use in a chained invocation
   */
  public final PartialSegmentInformation.Builder partTargetDuration(double partTargetDuration) {
    this.partTargetDuration = partTargetDuration;
    initBits &= ~INIT_BIT_PART_TARGET_DURATION;
    return (PartialSegmentInformation.Builder) this;
  }

  /**
   * Builds a new {@link PartialSegmentInformation PartialSegmentInformation}.
   * @return An immutable instance of PartialSegmentInformation
   * @throws IllegalStateException if any required attributes are missing
   */
  public PartialSegmentInformation build() {
    if (initBits != 0) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
    return new ImmutablePartialSegmentInformation(this);
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if ((initBits & INIT_BIT_PART_TARGET_DURATION) != 0) attributes.add("partTargetDuration");
    return "Cannot build PartialSegmentInformation, some of required attributes are not set " + attributes;
  }

  /**
   * Immutable implementation of {@link PartialSegmentInformation}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new PartialSegmentInformation.Builder()}.
   */
  private static final class ImmutablePartialSegmentInformation
      implements PartialSegmentInformation {
    private final double partTargetDuration;

    private ImmutablePartialSegmentInformation(PartialSegmentInformationBuilder builder) {
      this.partTargetDuration = builder.partTargetDuration;
    }

    /**
     * @return The value of the {@code partTargetDuration} attribute
     */
    @Override
    public double partTargetDuration() {
      return partTargetDuration;
    }

    /**
     * This instance is equal to all instances of {@code ImmutablePartialSegmentInformation} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutablePartialSegmentInformation
          && equalTo((ImmutablePartialSegmentInformation) another);
    }

    private boolean equalTo(ImmutablePartialSegmentInformation another) {
      return Double.doubleToLongBits(partTargetDuration) == Double.doubleToLongBits(another.partTargetDuration);
    }

    /**
     * Computes a hash code from attributes: {@code partTargetDuration}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + Double.hashCode(partTargetDuration);
      return h;
    }

    /**
     * Prints the immutable value {@code PartialSegmentInformation} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      return "PartialSegmentInformation{"
          + "partTargetDuration=" + partTargetDuration
          + "}";
    }
  }
}
