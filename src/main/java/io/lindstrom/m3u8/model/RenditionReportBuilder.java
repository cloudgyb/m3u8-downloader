package io.lindstrom.m3u8.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Builds instances of type {@link RenditionReport RenditionReport}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code RenditionReportBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})

class RenditionReportBuilder {
  private static final long INIT_BIT_URI = 0x1L;
  private long initBits = 0x1L;

  private String uri;
  private Long lastMediaSequenceNumber;
  private Long lastPartialSegmentIndex;

  /**
   * Creates a builder for {@link RenditionReport RenditionReport} instances.
   */
  RenditionReportBuilder() {
    if (!(this instanceof RenditionReport.Builder)) {
      throw new UnsupportedOperationException("Use: new RenditionReport.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code RenditionReport} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final RenditionReport.Builder from(RenditionReport instance) {
    Objects.requireNonNull(instance, "instance");
    uri(instance.uri());
    Optional<Long> lastMediaSequenceNumberOptional = instance.lastMediaSequenceNumber();
    if (lastMediaSequenceNumberOptional.isPresent()) {
      lastMediaSequenceNumber(lastMediaSequenceNumberOptional);
    }
    Optional<Long> lastPartialSegmentIndexOptional = instance.lastPartialSegmentIndex();
    if (lastPartialSegmentIndexOptional.isPresent()) {
      lastPartialSegmentIndex(lastPartialSegmentIndexOptional);
    }
    return (RenditionReport.Builder) this;
  }

  /**
   * Initializes the value for the {@link RenditionReport#uri() uri} attribute.
   * @param uri The value for uri 
   * @return {@code this} builder for use in a chained invocation
   */
  public final RenditionReport.Builder uri(String uri) {
    this.uri = Objects.requireNonNull(uri, "uri");
    initBits &= ~INIT_BIT_URI;
    return (RenditionReport.Builder) this;
  }

  /**
   * Initializes the optional value {@link RenditionReport#lastMediaSequenceNumber() lastMediaSequenceNumber} to lastMediaSequenceNumber.
   * @param lastMediaSequenceNumber The value for lastMediaSequenceNumber
   * @return {@code this} builder for chained invocation
   */
  public final RenditionReport.Builder lastMediaSequenceNumber(long lastMediaSequenceNumber) {
    this.lastMediaSequenceNumber = lastMediaSequenceNumber;
    return (RenditionReport.Builder) this;
  }

  /**
   * Initializes the optional value {@link RenditionReport#lastMediaSequenceNumber() lastMediaSequenceNumber} to lastMediaSequenceNumber.
   * @param lastMediaSequenceNumber The value for lastMediaSequenceNumber
   * @return {@code this} builder for use in a chained invocation
   */
  public final RenditionReport.Builder lastMediaSequenceNumber(Optional<Long> lastMediaSequenceNumber) {
    this.lastMediaSequenceNumber = lastMediaSequenceNumber.orElse(null);
    return (RenditionReport.Builder) this;
  }

  /**
   * Initializes the optional value {@link RenditionReport#lastPartialSegmentIndex() lastPartialSegmentIndex} to lastPartialSegmentIndex.
   * @param lastPartialSegmentIndex The value for lastPartialSegmentIndex
   * @return {@code this} builder for chained invocation
   */
  public final RenditionReport.Builder lastPartialSegmentIndex(long lastPartialSegmentIndex) {
    this.lastPartialSegmentIndex = lastPartialSegmentIndex;
    return (RenditionReport.Builder) this;
  }

  /**
   * Initializes the optional value {@link RenditionReport#lastPartialSegmentIndex() lastPartialSegmentIndex} to lastPartialSegmentIndex.
   * @param lastPartialSegmentIndex The value for lastPartialSegmentIndex
   * @return {@code this} builder for use in a chained invocation
   */
  public final RenditionReport.Builder lastPartialSegmentIndex(Optional<Long> lastPartialSegmentIndex) {
    this.lastPartialSegmentIndex = lastPartialSegmentIndex.orElse(null);
    return (RenditionReport.Builder) this;
  }

  /**
   * Builds a new {@link RenditionReport RenditionReport}.
   * @return An immutable instance of RenditionReport
   * @throws IllegalStateException if any required attributes are missing
   */
  public RenditionReport build() {
    if (initBits != 0) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
    return new ImmutableRenditionReport(this);
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if ((initBits & INIT_BIT_URI) != 0) attributes.add("uri");
    return "Cannot build RenditionReport, some of required attributes are not set " + attributes;
  }

  /**
   * Immutable implementation of {@link RenditionReport}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new RenditionReport.Builder()}.
   */
  private static final class ImmutableRenditionReport implements RenditionReport {
    private final String uri;
    private final Long lastMediaSequenceNumber;
    private final Long lastPartialSegmentIndex;

    private ImmutableRenditionReport(RenditionReportBuilder builder) {
      this.uri = builder.uri;
      this.lastMediaSequenceNumber = builder.lastMediaSequenceNumber;
      this.lastPartialSegmentIndex = builder.lastPartialSegmentIndex;
    }

    /**
     * @return The value of the {@code uri} attribute
     */
    @Override
    public String uri() {
      return uri;
    }

    /**
     * @return The value of the {@code lastMediaSequenceNumber} attribute
     */
    @Override
    public Optional<Long> lastMediaSequenceNumber() {
      return Optional.ofNullable(lastMediaSequenceNumber);
    }

    /**
     * @return The value of the {@code lastPartialSegmentIndex} attribute
     */
    @Override
    public Optional<Long> lastPartialSegmentIndex() {
      return Optional.ofNullable(lastPartialSegmentIndex);
    }

    /**
     * This instance is equal to all instances of {@code ImmutableRenditionReport} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutableRenditionReport
          && equalTo((ImmutableRenditionReport) another);
    }

    private boolean equalTo(ImmutableRenditionReport another) {
      return uri.equals(another.uri)
          && Objects.equals(lastMediaSequenceNumber, another.lastMediaSequenceNumber)
          && Objects.equals(lastPartialSegmentIndex, another.lastPartialSegmentIndex);
    }

    /**
     * Computes a hash code from attributes: {@code uri}, {@code lastMediaSequenceNumber}, {@code lastPartialSegmentIndex}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + uri.hashCode();
      h += (h << 5) + Objects.hashCode(lastMediaSequenceNumber);
      h += (h << 5) + Objects.hashCode(lastPartialSegmentIndex);
      return h;
    }

    /**
     * Prints the immutable value {@code RenditionReport} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("RenditionReport{");
      builder.append("uri=").append(uri);
      if (lastMediaSequenceNumber != null) {
        builder.append(", ");
        builder.append("lastMediaSequenceNumber=").append(lastMediaSequenceNumber);
      }
      if (lastPartialSegmentIndex != null) {
        builder.append(", ");
        builder.append("lastPartialSegmentIndex=").append(lastPartialSegmentIndex);
      }
      return builder.append("}").toString();
    }
  }
}
