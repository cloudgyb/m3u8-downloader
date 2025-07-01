package io.lindstrom.m3u8.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Builds instances of type {@link ByteRange ByteRange}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code ByteRangeBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})
class ByteRangeBuilder {
  private static final long INIT_BIT_LENGTH = 0x1L;
  private long initBits = 0x1L;

  private long length;
  private Long offset;

  /**
   * Creates a builder for {@link ByteRange ByteRange} instances.
   */
  ByteRangeBuilder() {
    if (!(this instanceof ByteRange.Builder)) {
      throw new UnsupportedOperationException("Use: new ByteRange.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code ByteRange} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final ByteRange.Builder from(ByteRange instance) {
    Objects.requireNonNull(instance, "instance");
    length(instance.length());
    Optional<Long> offsetOptional = instance.offset();
    if (offsetOptional.isPresent()) {
      offset(offsetOptional);
    }
    return (ByteRange.Builder) this;
  }

  /**
   * Initializes the value for the {@link ByteRange#length() length} attribute.
   * @param length The value for length 
   * @return {@code this} builder for use in a chained invocation
   */
  public final ByteRange.Builder length(long length) {
    this.length = length;
    initBits &= ~INIT_BIT_LENGTH;
    return (ByteRange.Builder) this;
  }

  /**
   * Initializes the optional value {@link ByteRange#offset() offset} to offset.
   * @param offset The value for offset
   * @return {@code this} builder for chained invocation
   */
  public final ByteRange.Builder offset(long offset) {
    this.offset = offset;
    return (ByteRange.Builder) this;
  }

  /**
   * Initializes the optional value {@link ByteRange#offset() offset} to offset.
   * @param offset The value for offset
   * @return {@code this} builder for use in a chained invocation
   */
  public final ByteRange.Builder offset(Optional<Long> offset) {
    this.offset = offset.orElse(null);
    return (ByteRange.Builder) this;
  }

  /**
   * Builds a new {@link ByteRange ByteRange}.
   * @return An immutable instance of ByteRange
   * @throws IllegalStateException if any required attributes are missing
   */
  public ByteRange build() {
    if (initBits != 0) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
    return new ImmutableByteRange(this);
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if ((initBits & INIT_BIT_LENGTH) != 0) attributes.add("length");
    return "Cannot build ByteRange, some of required attributes are not set " + attributes;
  }

  /**
   * Immutable implementation of {@link ByteRange}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new ByteRange.Builder()}.
   */
  private static final class ImmutableByteRange implements ByteRange {
    private final long length;
    private final Long offset;

    private ImmutableByteRange(ByteRangeBuilder builder) {
      this.length = builder.length;
      this.offset = builder.offset;
    }

    /**
     * @return The value of the {@code length} attribute
     */
    @Override
    public long length() {
      return length;
    }

    /**
     * @return The value of the {@code offset} attribute
     */
    @Override
    public Optional<Long> offset() {
      return Optional.ofNullable(offset);
    }

    /**
     * This instance is equal to all instances of {@code ImmutableByteRange} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutableByteRange
          && equalTo((ImmutableByteRange) another);
    }

    private boolean equalTo(ImmutableByteRange another) {
      return length == another.length
          && Objects.equals(offset, another.offset);
    }

    /**
     * Computes a hash code from attributes: {@code length}, {@code offset}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + Long.hashCode(length);
      h += (h << 5) + Objects.hashCode(offset);
      return h;
    }

    /**
     * Prints the immutable value {@code ByteRange} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("ByteRange{");
      builder.append("length=").append(length);
      if (offset != null) {
        builder.append(", ");
        builder.append("offset=").append(offset);
      }
      return builder.append("}").toString();
    }
  }
}
