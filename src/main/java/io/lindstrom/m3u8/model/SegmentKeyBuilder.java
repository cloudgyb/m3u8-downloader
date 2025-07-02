package io.lindstrom.m3u8.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Builds instances of type {@link SegmentKey SegmentKey}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code SegmentKeyBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})

class SegmentKeyBuilder {
  private static final long INIT_BIT_METHOD = 0x1L;
  private long initBits = 0x1L;

  private KeyMethod method;
  private String uri;
  private String iv;
  private String keyFormat;
  private String keyFormatVersions;

  /**
   * Creates a builder for {@link SegmentKey SegmentKey} instances.
   */
  SegmentKeyBuilder() {
    if (!(this instanceof SegmentKey.Builder)) {
      throw new UnsupportedOperationException("Use: new SegmentKey.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code SegmentKey} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final SegmentKey.Builder from(SegmentKey instance) {
    Objects.requireNonNull(instance, "instance");
    method(instance.method());
    Optional<String> uriOptional = instance.uri();
    if (uriOptional.isPresent()) {
      uri(uriOptional);
    }
    Optional<String> ivOptional = instance.iv();
    if (ivOptional.isPresent()) {
      iv(ivOptional);
    }
    Optional<String> keyFormatOptional = instance.keyFormat();
    if (keyFormatOptional.isPresent()) {
      keyFormat(keyFormatOptional);
    }
    Optional<String> keyFormatVersionsOptional = instance.keyFormatVersions();
    if (keyFormatVersionsOptional.isPresent()) {
      keyFormatVersions(keyFormatVersionsOptional);
    }
    return (SegmentKey.Builder) this;
  }

  /**
   * Initializes the value for the {@link SegmentKey#method() method} attribute.
   * @param method The value for method 
   * @return {@code this} builder for use in a chained invocation
   */
  public final SegmentKey.Builder method(KeyMethod method) {
    this.method = Objects.requireNonNull(method, "method");
    initBits &= ~INIT_BIT_METHOD;
    return (SegmentKey.Builder) this;
  }

  /**
   * Initializes the optional value {@link SegmentKey#uri() uri} to uri.
   * @param uri The value for uri
   * @return {@code this} builder for chained invocation
   */
  public final SegmentKey.Builder uri(String uri) {
    this.uri = Objects.requireNonNull(uri, "uri");
    return (SegmentKey.Builder) this;
  }

  /**
   * Initializes the optional value {@link SegmentKey#uri() uri} to uri.
   * @param uri The value for uri
   * @return {@code this} builder for use in a chained invocation
   */
  public final SegmentKey.Builder uri(Optional<String> uri) {
    this.uri = uri.orElse(null);
    return (SegmentKey.Builder) this;
  }

  /**
   * Initializes the optional value {@link SegmentKey#iv() iv} to iv.
   * @param iv The value for iv
   * @return {@code this} builder for chained invocation
   */
  public final SegmentKey.Builder iv(String iv) {
    this.iv = Objects.requireNonNull(iv, "iv");
    return (SegmentKey.Builder) this;
  }

  /**
   * Initializes the optional value {@link SegmentKey#iv() iv} to iv.
   * @param iv The value for iv
   * @return {@code this} builder for use in a chained invocation
   */
  public final SegmentKey.Builder iv(Optional<String> iv) {
    this.iv = iv.orElse(null);
    return (SegmentKey.Builder) this;
  }

  /**
   * Initializes the optional value {@link SegmentKey#keyFormat() keyFormat} to keyFormat.
   * @param keyFormat The value for keyFormat
   * @return {@code this} builder for chained invocation
   */
  public final SegmentKey.Builder keyFormat(String keyFormat) {
    this.keyFormat = Objects.requireNonNull(keyFormat, "keyFormat");
    return (SegmentKey.Builder) this;
  }

  /**
   * Initializes the optional value {@link SegmentKey#keyFormat() keyFormat} to keyFormat.
   * @param keyFormat The value for keyFormat
   * @return {@code this} builder for use in a chained invocation
   */
  public final SegmentKey.Builder keyFormat(Optional<String> keyFormat) {
    this.keyFormat = keyFormat.orElse(null);
    return (SegmentKey.Builder) this;
  }

  /**
   * Initializes the optional value {@link SegmentKey#keyFormatVersions() keyFormatVersions} to keyFormatVersions.
   * @param keyFormatVersions The value for keyFormatVersions
   * @return {@code this} builder for chained invocation
   */
  public final SegmentKey.Builder keyFormatVersions(String keyFormatVersions) {
    this.keyFormatVersions = Objects.requireNonNull(keyFormatVersions, "keyFormatVersions");
    return (SegmentKey.Builder) this;
  }

  /**
   * Initializes the optional value {@link SegmentKey#keyFormatVersions() keyFormatVersions} to keyFormatVersions.
   * @param keyFormatVersions The value for keyFormatVersions
   * @return {@code this} builder for use in a chained invocation
   */
  public final SegmentKey.Builder keyFormatVersions(Optional<String> keyFormatVersions) {
    this.keyFormatVersions = keyFormatVersions.orElse(null);
    return (SegmentKey.Builder) this;
  }

  /**
   * Builds a new {@link SegmentKey SegmentKey}.
   * @return An immutable instance of SegmentKey
   * @throws IllegalStateException if any required attributes are missing
   */
  public SegmentKey build() {
    if (initBits != 0) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
    return new ImmutableSegmentKey(this);
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if ((initBits & INIT_BIT_METHOD) != 0) attributes.add("method");
    return "Cannot build SegmentKey, some of required attributes are not set " + attributes;
  }

  /**
   * Immutable implementation of {@link SegmentKey}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new SegmentKey.Builder()}.
   */
  private static final class ImmutableSegmentKey implements SegmentKey {
    private final KeyMethod method;
    private final String uri;
    private final String iv;
    private final String keyFormat;
    private final String keyFormatVersions;

    private ImmutableSegmentKey(SegmentKeyBuilder builder) {
      this.method = builder.method;
      this.uri = builder.uri;
      this.iv = builder.iv;
      this.keyFormat = builder.keyFormat;
      this.keyFormatVersions = builder.keyFormatVersions;
    }

    /**
     * @return The value of the {@code method} attribute
     */
    @Override
    public KeyMethod method() {
      return method;
    }

    /**
     * @return The value of the {@code uri} attribute
     */
    @Override
    public Optional<String> uri() {
      return Optional.ofNullable(uri);
    }

    /**
     * @return The value of the {@code iv} attribute
     */
    @Override
    public Optional<String> iv() {
      return Optional.ofNullable(iv);
    }

    /**
     * @return The value of the {@code keyFormat} attribute
     */
    @Override
    public Optional<String> keyFormat() {
      return Optional.ofNullable(keyFormat);
    }

    /**
     * @return The value of the {@code keyFormatVersions} attribute
     */
    @Override
    public Optional<String> keyFormatVersions() {
      return Optional.ofNullable(keyFormatVersions);
    }

    /**
     * This instance is equal to all instances of {@code ImmutableSegmentKey} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutableSegmentKey
          && equalTo((ImmutableSegmentKey) another);
    }

    private boolean equalTo(ImmutableSegmentKey another) {
      return method.equals(another.method)
          && Objects.equals(uri, another.uri)
          && Objects.equals(iv, another.iv)
          && Objects.equals(keyFormat, another.keyFormat)
          && Objects.equals(keyFormatVersions, another.keyFormatVersions);
    }

    /**
     * Computes a hash code from attributes: {@code method}, {@code uri}, {@code iv}, {@code keyFormat}, {@code keyFormatVersions}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + method.hashCode();
      h += (h << 5) + Objects.hashCode(uri);
      h += (h << 5) + Objects.hashCode(iv);
      h += (h << 5) + Objects.hashCode(keyFormat);
      h += (h << 5) + Objects.hashCode(keyFormatVersions);
      return h;
    }

    /**
     * Prints the immutable value {@code SegmentKey} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("SegmentKey{");
      builder.append("method=").append(method);
      if (uri != null) {
        builder.append(", ");
        builder.append("uri=").append(uri);
      }
      if (iv != null) {
        builder.append(", ");
        builder.append("iv=").append(iv);
      }
      if (keyFormat != null) {
        builder.append(", ");
        builder.append("keyFormat=").append(keyFormat);
      }
      if (keyFormatVersions != null) {
        builder.append(", ");
        builder.append("keyFormatVersions=").append(keyFormatVersions);
      }
      return builder.append("}").toString();
    }
  }
}
