package io.lindstrom.m3u8.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Builds instances of type {@link PreloadHint PreloadHint}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code PreloadHintBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})

class PreloadHintBuilder {
  private static final long INIT_BIT_TYPE = 0x1L;
  private static final long INIT_BIT_URI = 0x2L;
  private long initBits = 0x3L;

  private PreloadHintType type;
  private String uri;
  private Long byteRangeStart;
  private Long byteRangeLength;

  /**
   * Creates a builder for {@link PreloadHint PreloadHint} instances.
   */
  PreloadHintBuilder() {
    if (!(this instanceof PreloadHint.Builder)) {
      throw new UnsupportedOperationException("Use: new PreloadHint.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code PreloadHint} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final PreloadHint.Builder from(PreloadHint instance) {
    Objects.requireNonNull(instance, "instance");
    type(instance.type());
    uri(instance.uri());
    Optional<Long> byteRangeStartOptional = instance.byteRangeStart();
    if (byteRangeStartOptional.isPresent()) {
      byteRangeStart(byteRangeStartOptional);
    }
    Optional<Long> byteRangeLengthOptional = instance.byteRangeLength();
    if (byteRangeLengthOptional.isPresent()) {
      byteRangeLength(byteRangeLengthOptional);
    }
    return (PreloadHint.Builder) this;
  }

  /**
   * Initializes the value for the {@link PreloadHint#type() type} attribute.
   * @param type The value for type 
   * @return {@code this} builder for use in a chained invocation
   */
  public final PreloadHint.Builder type(PreloadHintType type) {
    this.type = Objects.requireNonNull(type, "type");
    initBits &= ~INIT_BIT_TYPE;
    return (PreloadHint.Builder) this;
  }

  /**
   * Initializes the value for the {@link PreloadHint#uri() uri} attribute.
   * @param uri The value for uri 
   * @return {@code this} builder for use in a chained invocation
   */
  public final PreloadHint.Builder uri(String uri) {
    this.uri = Objects.requireNonNull(uri, "uri");
    initBits &= ~INIT_BIT_URI;
    return (PreloadHint.Builder) this;
  }

  /**
   * Initializes the optional value {@link PreloadHint#byteRangeStart() byteRangeStart} to byteRangeStart.
   * @param byteRangeStart The value for byteRangeStart
   * @return {@code this} builder for chained invocation
   */
  public final PreloadHint.Builder byteRangeStart(long byteRangeStart) {
    this.byteRangeStart = byteRangeStart;
    return (PreloadHint.Builder) this;
  }

  /**
   * Initializes the optional value {@link PreloadHint#byteRangeStart() byteRangeStart} to byteRangeStart.
   * @param byteRangeStart The value for byteRangeStart
   * @return {@code this} builder for use in a chained invocation
   */
  public final PreloadHint.Builder byteRangeStart(Optional<Long> byteRangeStart) {
    this.byteRangeStart = byteRangeStart.orElse(null);
    return (PreloadHint.Builder) this;
  }

  /**
   * Initializes the optional value {@link PreloadHint#byteRangeLength() byteRangeLength} to byteRangeLength.
   * @param byteRangeLength The value for byteRangeLength
   * @return {@code this} builder for chained invocation
   */
  public final PreloadHint.Builder byteRangeLength(long byteRangeLength) {
    this.byteRangeLength = byteRangeLength;
    return (PreloadHint.Builder) this;
  }

  /**
   * Initializes the optional value {@link PreloadHint#byteRangeLength() byteRangeLength} to byteRangeLength.
   * @param byteRangeLength The value for byteRangeLength
   * @return {@code this} builder for use in a chained invocation
   */
  public final PreloadHint.Builder byteRangeLength(Optional<Long> byteRangeLength) {
    this.byteRangeLength = byteRangeLength.orElse(null);
    return (PreloadHint.Builder) this;
  }

  /**
   * Builds a new {@link PreloadHint PreloadHint}.
   * @return An immutable instance of PreloadHint
   * @throws IllegalStateException if any required attributes are missing
   */
  public PreloadHint build() {
    if (initBits != 0) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
    return new ImmutablePreloadHint(this);
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if ((initBits & INIT_BIT_TYPE) != 0) attributes.add("type");
    if ((initBits & INIT_BIT_URI) != 0) attributes.add("uri");
    return "Cannot build PreloadHint, some of required attributes are not set " + attributes;
  }

  /**
   * Immutable implementation of {@link PreloadHint}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new PreloadHint.Builder()}.
   */
  private static final class ImmutablePreloadHint implements PreloadHint {
    private final PreloadHintType type;
    private final String uri;
    private final Long byteRangeStart;
    private final Long byteRangeLength;

    private ImmutablePreloadHint(PreloadHintBuilder builder) {
      this.type = builder.type;
      this.uri = builder.uri;
      this.byteRangeStart = builder.byteRangeStart;
      this.byteRangeLength = builder.byteRangeLength;
    }

    /**
     * @return The value of the {@code type} attribute
     */
    @Override
    public PreloadHintType type() {
      return type;
    }

    /**
     * @return The value of the {@code uri} attribute
     */
    @Override
    public String uri() {
      return uri;
    }

    /**
     * @return The value of the {@code byteRangeStart} attribute
     */
    @Override
    public Optional<Long> byteRangeStart() {
      return Optional.ofNullable(byteRangeStart);
    }

    /**
     * @return The value of the {@code byteRangeLength} attribute
     */
    @Override
    public Optional<Long> byteRangeLength() {
      return Optional.ofNullable(byteRangeLength);
    }

    /**
     * This instance is equal to all instances of {@code ImmutablePreloadHint} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutablePreloadHint
          && equalTo((ImmutablePreloadHint) another);
    }

    private boolean equalTo(ImmutablePreloadHint another) {
      return type.equals(another.type)
          && uri.equals(another.uri)
          && Objects.equals(byteRangeStart, another.byteRangeStart)
          && Objects.equals(byteRangeLength, another.byteRangeLength);
    }

    /**
     * Computes a hash code from attributes: {@code type}, {@code uri}, {@code byteRangeStart}, {@code byteRangeLength}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + type.hashCode();
      h += (h << 5) + uri.hashCode();
      h += (h << 5) + Objects.hashCode(byteRangeStart);
      h += (h << 5) + Objects.hashCode(byteRangeLength);
      return h;
    }

    /**
     * Prints the immutable value {@code PreloadHint} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("PreloadHint{");
      builder.append("type=").append(type);
      builder.append(", ");
      builder.append("uri=").append(uri);
      if (byteRangeStart != null) {
        builder.append(", ");
        builder.append("byteRangeStart=").append(byteRangeStart);
      }
      if (byteRangeLength != null) {
        builder.append(", ");
        builder.append("byteRangeLength=").append(byteRangeLength);
      }
      return builder.append("}").toString();
    }
  }
}
