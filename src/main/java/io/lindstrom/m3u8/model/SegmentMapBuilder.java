package io.lindstrom.m3u8.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Builds instances of type {@link SegmentMap SegmentMap}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code SegmentMapBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})

class SegmentMapBuilder {
  private static final long INIT_BIT_URI = 0x1L;
  private long initBits = 0x1L;

  private String uri;
  private ByteRange byteRange;

  /**
   * Creates a builder for {@link SegmentMap SegmentMap} instances.
   */
  SegmentMapBuilder() {
    if (!(this instanceof SegmentMap.Builder)) {
      throw new UnsupportedOperationException("Use: new SegmentMap.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code SegmentMap} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final SegmentMap.Builder from(SegmentMap instance) {
    Objects.requireNonNull(instance, "instance");
    uri(instance.uri());
    Optional<ByteRange> byteRangeOptional = instance.byteRange();
    if (byteRangeOptional.isPresent()) {
      byteRange(byteRangeOptional);
    }
    return (SegmentMap.Builder) this;
  }

  /**
   * Initializes the value for the {@link SegmentMap#uri() uri} attribute.
   * @param uri The value for uri 
   * @return {@code this} builder for use in a chained invocation
   */
  public final SegmentMap.Builder uri(String uri) {
    this.uri = Objects.requireNonNull(uri, "uri");
    initBits &= ~INIT_BIT_URI;
    return (SegmentMap.Builder) this;
  }

  /**
   * Initializes the optional value {@link SegmentMap#byteRange() byteRange} to byteRange.
   * @param byteRange The value for byteRange
   * @return {@code this} builder for chained invocation
   */
  public final SegmentMap.Builder byteRange(ByteRange byteRange) {
    this.byteRange = Objects.requireNonNull(byteRange, "byteRange");
    return (SegmentMap.Builder) this;
  }

  /**
   * Initializes the optional value {@link SegmentMap#byteRange() byteRange} to byteRange.
   * @param byteRange The value for byteRange
   * @return {@code this} builder for use in a chained invocation
   */
  public final SegmentMap.Builder byteRange(Optional<? extends ByteRange> byteRange) {
    this.byteRange = byteRange.orElse(null);
    return (SegmentMap.Builder) this;
  }

  /**
   * Builds a new {@link SegmentMap SegmentMap}.
   * @return An immutable instance of SegmentMap
   * @throws IllegalStateException if any required attributes are missing
   */
  public SegmentMap build() {
    if (initBits != 0) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
    return new ImmutableSegmentMap(this);
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if ((initBits & INIT_BIT_URI) != 0) attributes.add("uri");
    return "Cannot build SegmentMap, some of required attributes are not set " + attributes;
  }

  /**
   * Immutable implementation of {@link SegmentMap}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new SegmentMap.Builder()}.
   */
  private static final class ImmutableSegmentMap implements SegmentMap {
    private final String uri;
    private final ByteRange byteRange;

    private ImmutableSegmentMap(SegmentMapBuilder builder) {
      this.uri = builder.uri;
      this.byteRange = builder.byteRange;
    }

    /**
     * @return The value of the {@code uri} attribute
     */
    @Override
    public String uri() {
      return uri;
    }

    /**
     * @return The value of the {@code byteRange} attribute
     */
    @Override
    public Optional<ByteRange> byteRange() {
      return Optional.ofNullable(byteRange);
    }

    /**
     * This instance is equal to all instances of {@code ImmutableSegmentMap} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutableSegmentMap
          && equalTo((ImmutableSegmentMap) another);
    }

    private boolean equalTo(ImmutableSegmentMap another) {
      return uri.equals(another.uri)
          && Objects.equals(byteRange, another.byteRange);
    }

    /**
     * Computes a hash code from attributes: {@code uri}, {@code byteRange}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + uri.hashCode();
      h += (h << 5) + Objects.hashCode(byteRange);
      return h;
    }

    /**
     * Prints the immutable value {@code SegmentMap} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("SegmentMap{");
      builder.append("uri=").append(uri);
      if (byteRange != null) {
        builder.append(", ");
        builder.append("byteRange=").append(byteRange);
      }
      return builder.append("}").toString();
    }
  }
}
