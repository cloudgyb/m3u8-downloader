package io.lindstrom.m3u8.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Builds instances of type {@link PartialSegment PartialSegment}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code PartialSegmentBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})
class PartialSegmentBuilder {
  private static final long INIT_BIT_URI = 0x1L;
  private static final long INIT_BIT_DURATION = 0x2L;
  private static final long OPT_BIT_INDEPENDENT = 0x1L;
  private static final long OPT_BIT_GAP = 0x2L;
  private long initBits = 0x3L;
  private long optBits;

  private String uri;
  private double duration;
  private boolean independent;
  private ByteRange byterange;
  private boolean gap;

  /**
   * Creates a builder for {@link PartialSegment PartialSegment} instances.
   */
  PartialSegmentBuilder() {
    if (!(this instanceof PartialSegment.Builder)) {
      throw new UnsupportedOperationException("Use: new PartialSegment.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code PartialSegment} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final PartialSegment.Builder from(PartialSegment instance) {
    Objects.requireNonNull(instance, "instance");
    uri(instance.uri());
    duration(instance.duration());
    independent(instance.independent());
    Optional<ByteRange> byterangeOptional = instance.byterange();
    if (byterangeOptional.isPresent()) {
      byterange(byterangeOptional);
    }
    gap(instance.gap());
    return (PartialSegment.Builder) this;
  }

  /**
   * Initializes the value for the {@link PartialSegment#uri() uri} attribute.
   * @param uri The value for uri 
   * @return {@code this} builder for use in a chained invocation
   */
  public final PartialSegment.Builder uri(String uri) {
    this.uri = Objects.requireNonNull(uri, "uri");
    initBits &= ~INIT_BIT_URI;
    return (PartialSegment.Builder) this;
  }

  /**
   * Initializes the value for the {@link PartialSegment#duration() duration} attribute.
   * @param duration The value for duration 
   * @return {@code this} builder for use in a chained invocation
   */
  public final PartialSegment.Builder duration(double duration) {
    this.duration = duration;
    initBits &= ~INIT_BIT_DURATION;
    return (PartialSegment.Builder) this;
  }

  /**
   * Initializes the value for the {@link PartialSegment#independent() independent} attribute.
   * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link PartialSegment#independent() independent}.</em>
   * @param independent The value for independent 
   * @return {@code this} builder for use in a chained invocation
   */
  public final PartialSegment.Builder independent(boolean independent) {
    this.independent = independent;
    optBits |= OPT_BIT_INDEPENDENT;
    return (PartialSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link PartialSegment#byterange() byterange} to byterange.
   * @param byterange The value for byterange
   * @return {@code this} builder for chained invocation
   */
  public final PartialSegment.Builder byterange(ByteRange byterange) {
    this.byterange = Objects.requireNonNull(byterange, "byterange");
    return (PartialSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link PartialSegment#byterange() byterange} to byterange.
   * @param byterange The value for byterange
   * @return {@code this} builder for use in a chained invocation
   */
  public final PartialSegment.Builder byterange(Optional<? extends ByteRange> byterange) {
    this.byterange = byterange.orElse(null);
    return (PartialSegment.Builder) this;
  }

  /**
   * Initializes the value for the {@link PartialSegment#gap() gap} attribute.
   * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link PartialSegment#gap() gap}.</em>
   * @param gap The value for gap 
   * @return {@code this} builder for use in a chained invocation
   */
  public final PartialSegment.Builder gap(boolean gap) {
    this.gap = gap;
    optBits |= OPT_BIT_GAP;
    return (PartialSegment.Builder) this;
  }

  /**
   * Builds a new {@link PartialSegment PartialSegment}.
   * @return An immutable instance of PartialSegment
   * @throws IllegalStateException if any required attributes are missing
   */
  public PartialSegment build() {
    if (initBits != 0) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
    return new ImmutablePartialSegment(this);
  }

  private boolean independentIsSet() {
    return (optBits & OPT_BIT_INDEPENDENT) != 0;
  }

  private boolean gapIsSet() {
    return (optBits & OPT_BIT_GAP) != 0;
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if ((initBits & INIT_BIT_URI) != 0) attributes.add("uri");
    if ((initBits & INIT_BIT_DURATION) != 0) attributes.add("duration");
    return "Cannot build PartialSegment, some of required attributes are not set " + attributes;
  }

  /**
   * Immutable implementation of {@link PartialSegment}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new PartialSegment.Builder()}.
   */
  private static final class ImmutablePartialSegment implements PartialSegment {
    private final String uri;
    private final double duration;
    private final boolean independent;
    private final ByteRange byterange;
    private final boolean gap;

    private ImmutablePartialSegment(PartialSegmentBuilder builder) {
      this.uri = builder.uri;
      this.duration = builder.duration;
      this.byterange = builder.byterange;
      if (builder.independentIsSet()) {
        initShim.independent(builder.independent);
      }
      if (builder.gapIsSet()) {
        initShim.gap(builder.gap);
      }
      this.independent = initShim.independent();
      this.gap = initShim.gap();
      this.initShim = null;
    }

    private static final int STAGE_INITIALIZING = -1;
    private static final int STAGE_UNINITIALIZED = 0;
    private static final int STAGE_INITIALIZED = 1;
    private transient volatile InitShim initShim = new InitShim();

    private final class InitShim {
      private boolean independent;
      private int independentBuildStage;

      boolean independent() {
        if (independentBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
        if (independentBuildStage == STAGE_UNINITIALIZED) {
          independentBuildStage = STAGE_INITIALIZING;
          this.independent = independentInitialize();
          independentBuildStage = STAGE_INITIALIZED;
        }
        return this.independent;
      }

      void independent(boolean independent) {
        this.independent = independent;
        independentBuildStage = STAGE_INITIALIZED;
      }
      private boolean gap;
      private int gapBuildStage;

      boolean gap() {
        if (gapBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
        if (gapBuildStage == STAGE_UNINITIALIZED) {
          gapBuildStage = STAGE_INITIALIZING;
          this.gap = gapInitialize();
          gapBuildStage = STAGE_INITIALIZED;
        }
        return this.gap;
      }

      void gap(boolean gap) {
        this.gap = gap;
        gapBuildStage = STAGE_INITIALIZED;
      }

      private String formatInitCycleMessage() {
        ArrayList<String> attributes = new ArrayList<String>();
        if (independentBuildStage == STAGE_INITIALIZING) attributes.add("independent");
        if (gapBuildStage == STAGE_INITIALIZING) attributes.add("gap");
        return "Cannot build PartialSegment, attribute initializers form cycle" + attributes;
      }
    }

    private boolean independentInitialize() {
      return PartialSegment.super.independent();
    }

    private boolean gapInitialize() {
      return PartialSegment.super.gap();
    }

    /**
     * @return The value of the {@code uri} attribute
     */
    @Override
    public String uri() {
      return uri;
    }

    /**
     * @return The value of the {@code duration} attribute
     */
    @Override
    public double duration() {
      return duration;
    }

    /**
     * @return The value of the {@code independent} attribute
     */
    @Override
    public boolean independent() {
      InitShim shim = this.initShim;
      return shim != null
          ? shim.independent()
          : this.independent;
    }

    /**
     * @return The value of the {@code byterange} attribute
     */
    @Override
    public Optional<ByteRange> byterange() {
      return Optional.ofNullable(byterange);
    }

    /**
     * @return The value of the {@code gap} attribute
     */
    @Override
    public boolean gap() {
      InitShim shim = this.initShim;
      return shim != null
          ? shim.gap()
          : this.gap;
    }

    /**
     * This instance is equal to all instances of {@code ImmutablePartialSegment} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutablePartialSegment
          && equalTo((ImmutablePartialSegment) another);
    }

    private boolean equalTo(ImmutablePartialSegment another) {
      return uri.equals(another.uri)
          && Double.doubleToLongBits(duration) == Double.doubleToLongBits(another.duration)
          && independent == another.independent
          && Objects.equals(byterange, another.byterange)
          && gap == another.gap;
    }

    /**
     * Computes a hash code from attributes: {@code uri}, {@code duration}, {@code independent}, {@code byterange}, {@code gap}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + uri.hashCode();
      h += (h << 5) + Double.hashCode(duration);
      h += (h << 5) + Boolean.hashCode(independent);
      h += (h << 5) + Objects.hashCode(byterange);
      h += (h << 5) + Boolean.hashCode(gap);
      return h;
    }

    /**
     * Prints the immutable value {@code PartialSegment} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("PartialSegment{");
      builder.append("uri=").append(uri);
      builder.append(", ");
      builder.append("duration=").append(duration);
      builder.append(", ");
      builder.append("independent=").append(independent);
      if (byterange != null) {
        builder.append(", ");
        builder.append("byterange=").append(byterange);
      }
      builder.append(", ");
      builder.append("gap=").append(gap);
      return builder.append("}").toString();
    }
  }
}
