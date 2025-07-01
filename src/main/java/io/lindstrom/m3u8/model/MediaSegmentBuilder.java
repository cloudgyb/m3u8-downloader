package io.lindstrom.m3u8.model;


import java.time.OffsetDateTime;
import java.util.*;

/**
 * Builds instances of type {@link MediaSegment MediaSegment}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code MediaSegmentBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})

class MediaSegmentBuilder {
  private static final long INIT_BIT_DURATION = 0x1L;
  private static final long INIT_BIT_URI = 0x2L;
  private static final long OPT_BIT_DISCONTINUITY = 0x1L;
  private static final long OPT_BIT_CUE_IN = 0x2L;
  private static final long OPT_BIT_GAP = 0x4L;
  private long initBits = 0x3L;
  private long optBits;

  private double duration;
  private String title;
  private String uri;
  private ByteRange byteRange;
  private OffsetDateTime programDateTime;
  private DateRange dateRange;
  private SegmentMap segmentMap;
  private SegmentKey segmentKey;
  private boolean discontinuity;
  private Double cueOut;
  private boolean cueIn;
  private boolean gap;
  private Long bitrate;
  private List<PartialSegment> partialSegments = new ArrayList<PartialSegment>();

  /**
   * Creates a builder for {@link MediaSegment MediaSegment} instances.
   */
  MediaSegmentBuilder() {
    if (!(this instanceof MediaSegment.Builder)) {
      throw new UnsupportedOperationException("Use: new MediaSegment.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code MediaSegment} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * Collection elements and entries will be added, not replaced.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder from(MediaSegment instance) {
    Objects.requireNonNull(instance, "instance");
    duration(instance.duration());
    Optional<String> titleOptional = instance.title();
    if (titleOptional.isPresent()) {
      title(titleOptional);
    }
    uri(instance.uri());
    Optional<ByteRange> byteRangeOptional = instance.byteRange();
    if (byteRangeOptional.isPresent()) {
      byteRange(byteRangeOptional);
    }
    Optional<OffsetDateTime> programDateTimeOptional = instance.programDateTime();
    if (programDateTimeOptional.isPresent()) {
      programDateTime(programDateTimeOptional);
    }
    Optional<DateRange> dateRangeOptional = instance.dateRange();
    if (dateRangeOptional.isPresent()) {
      dateRange(dateRangeOptional);
    }
    Optional<SegmentMap> segmentMapOptional = instance.segmentMap();
    if (segmentMapOptional.isPresent()) {
      segmentMap(segmentMapOptional);
    }
    Optional<SegmentKey> segmentKeyOptional = instance.segmentKey();
    if (segmentKeyOptional.isPresent()) {
      segmentKey(segmentKeyOptional);
    }
    discontinuity(instance.discontinuity());
    Optional<Double> cueOutOptional = instance.cueOut();
    if (cueOutOptional.isPresent()) {
      cueOut(cueOutOptional);
    }
    cueIn(instance.cueIn());
    gap(instance.gap());
    Optional<Long> bitrateOptional = instance.bitrate();
    if (bitrateOptional.isPresent()) {
      bitrate(bitrateOptional);
    }
    addAllPartialSegments(instance.partialSegments());
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the value for the {@link MediaSegment#duration() duration} attribute.
   * @param duration The value for duration 
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder duration(double duration) {
    this.duration = duration;
    initBits &= ~INIT_BIT_DURATION;
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaSegment#title() title} to title.
   * @param title The value for title
   * @return {@code this} builder for chained invocation
   */
  public final MediaSegment.Builder title(String title) {
    this.title = Objects.requireNonNull(title, "title");
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaSegment#title() title} to title.
   * @param title The value for title
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder title(Optional<String> title) {
    this.title = title.orElse(null);
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the value for the {@link MediaSegment#uri() uri} attribute.
   * @param uri The value for uri 
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder uri(String uri) {
    this.uri = Objects.requireNonNull(uri, "uri");
    initBits &= ~INIT_BIT_URI;
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaSegment#byteRange() byteRange} to byteRange.
   * @param byteRange The value for byteRange
   * @return {@code this} builder for chained invocation
   */
  public final MediaSegment.Builder byteRange(ByteRange byteRange) {
    this.byteRange = Objects.requireNonNull(byteRange, "byteRange");
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaSegment#byteRange() byteRange} to byteRange.
   * @param byteRange The value for byteRange
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder byteRange(Optional<? extends ByteRange> byteRange) {
    this.byteRange = byteRange.orElse(null);
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaSegment#programDateTime() programDateTime} to programDateTime.
   * @param programDateTime The value for programDateTime
   * @return {@code this} builder for chained invocation
   */
  public final MediaSegment.Builder programDateTime(OffsetDateTime programDateTime) {
    this.programDateTime = Objects.requireNonNull(programDateTime, "programDateTime");
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaSegment#programDateTime() programDateTime} to programDateTime.
   * @param programDateTime The value for programDateTime
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder programDateTime(Optional<? extends OffsetDateTime> programDateTime) {
    this.programDateTime = programDateTime.orElse(null);
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaSegment#dateRange() dateRange} to dateRange.
   * @param dateRange The value for dateRange
   * @return {@code this} builder for chained invocation
   */
  public final MediaSegment.Builder dateRange(DateRange dateRange) {
    this.dateRange = Objects.requireNonNull(dateRange, "dateRange");
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaSegment#dateRange() dateRange} to dateRange.
   * @param dateRange The value for dateRange
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder dateRange(Optional<? extends DateRange> dateRange) {
    this.dateRange = dateRange.orElse(null);
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaSegment#segmentMap() segmentMap} to segmentMap.
   * @param segmentMap The value for segmentMap
   * @return {@code this} builder for chained invocation
   */
  public final MediaSegment.Builder segmentMap(SegmentMap segmentMap) {
    this.segmentMap = Objects.requireNonNull(segmentMap, "segmentMap");
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaSegment#segmentMap() segmentMap} to segmentMap.
   * @param segmentMap The value for segmentMap
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder segmentMap(Optional<? extends SegmentMap> segmentMap) {
    this.segmentMap = segmentMap.orElse(null);
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaSegment#segmentKey() segmentKey} to segmentKey.
   * @param segmentKey The value for segmentKey
   * @return {@code this} builder for chained invocation
   */
  public final MediaSegment.Builder segmentKey(SegmentKey segmentKey) {
    this.segmentKey = Objects.requireNonNull(segmentKey, "segmentKey");
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaSegment#segmentKey() segmentKey} to segmentKey.
   * @param segmentKey The value for segmentKey
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder segmentKey(Optional<? extends SegmentKey> segmentKey) {
    this.segmentKey = segmentKey.orElse(null);
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the value for the {@link MediaSegment#discontinuity() discontinuity} attribute.
   * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link MediaSegment#discontinuity() discontinuity}.</em>
   * @param discontinuity The value for discontinuity 
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder discontinuity(boolean discontinuity) {
    this.discontinuity = discontinuity;
    optBits |= OPT_BIT_DISCONTINUITY;
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaSegment#cueOut() cueOut} to cueOut.
   * @param cueOut The value for cueOut
   * @return {@code this} builder for chained invocation
   */
  public final MediaSegment.Builder cueOut(double cueOut) {
    this.cueOut = cueOut;
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaSegment#cueOut() cueOut} to cueOut.
   * @param cueOut The value for cueOut
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder cueOut(Optional<Double> cueOut) {
    this.cueOut = cueOut.orElse(null);
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the value for the {@link MediaSegment#cueIn() cueIn} attribute.
   * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link MediaSegment#cueIn() cueIn}.</em>
   * @param cueIn The value for cueIn 
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder cueIn(boolean cueIn) {
    this.cueIn = cueIn;
    optBits |= OPT_BIT_CUE_IN;
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the value for the {@link MediaSegment#gap() gap} attribute.
   * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link MediaSegment#gap() gap}.</em>
   * @param gap The value for gap 
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder gap(boolean gap) {
    this.gap = gap;
    optBits |= OPT_BIT_GAP;
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaSegment#bitrate() bitrate} to bitrate.
   * @param bitrate The value for bitrate
   * @return {@code this} builder for chained invocation
   */
  public final MediaSegment.Builder bitrate(long bitrate) {
    this.bitrate = bitrate;
    return (MediaSegment.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaSegment#bitrate() bitrate} to bitrate.
   * @param bitrate The value for bitrate
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder bitrate(Optional<Long> bitrate) {
    this.bitrate = bitrate.orElse(null);
    return (MediaSegment.Builder) this;
  }

  /**
   * Adds one element to {@link MediaSegment#partialSegments() partialSegments} list.
   * @param element A partialSegments element
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder addPartialSegments(PartialSegment element) {
    this.partialSegments.add(Objects.requireNonNull(element, "partialSegments element"));
    return (MediaSegment.Builder) this;
  }

  /**
   * Adds elements to {@link MediaSegment#partialSegments() partialSegments} list.
   * @param elements An array of partialSegments elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder addPartialSegments(PartialSegment... elements) {
    for (PartialSegment element : elements) {
      this.partialSegments.add(Objects.requireNonNull(element, "partialSegments element"));
    }
    return (MediaSegment.Builder) this;
  }

  /**
   * Sets or replaces all elements for {@link MediaSegment#partialSegments() partialSegments} list.
   * @param elements An iterable of partialSegments elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder partialSegments(Iterable<? extends PartialSegment> elements) {
    this.partialSegments.clear();
    return addAllPartialSegments(elements);
  }

  /**
   * Adds elements to {@link MediaSegment#partialSegments() partialSegments} list.
   * @param elements An iterable of partialSegments elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaSegment.Builder addAllPartialSegments(Iterable<? extends PartialSegment> elements) {
    for (PartialSegment element : elements) {
      this.partialSegments.add(Objects.requireNonNull(element, "partialSegments element"));
    }
    return (MediaSegment.Builder) this;
  }

  /**
   * Builds a new {@link MediaSegment MediaSegment}.
   * @return An immutable instance of MediaSegment
   * @throws IllegalStateException if any required attributes are missing
   */
  public MediaSegment build() {
    if (initBits != 0) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
    return new ImmutableMediaSegment(this);
  }

  private boolean discontinuityIsSet() {
    return (optBits & OPT_BIT_DISCONTINUITY) != 0;
  }

  private boolean cueInIsSet() {
    return (optBits & OPT_BIT_CUE_IN) != 0;
  }

  private boolean gapIsSet() {
    return (optBits & OPT_BIT_GAP) != 0;
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if ((initBits & INIT_BIT_DURATION) != 0) attributes.add("duration");
    if ((initBits & INIT_BIT_URI) != 0) attributes.add("uri");
    return "Cannot build MediaSegment, some of required attributes are not set " + attributes;
  }

  /**
   * Immutable implementation of {@link MediaSegment}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new MediaSegment.Builder()}.
   */
  private static final class ImmutableMediaSegment implements MediaSegment {
    private final double duration;
    private final String title;
    private final String uri;
    private final ByteRange byteRange;
    private final OffsetDateTime programDateTime;
    private final DateRange dateRange;
    private final SegmentMap segmentMap;
    private final SegmentKey segmentKey;
    private final boolean discontinuity;
    private final Double cueOut;
    private final boolean cueIn;
    private final boolean gap;
    private final Long bitrate;
    private final List<PartialSegment> partialSegments;

    private ImmutableMediaSegment(MediaSegmentBuilder builder) {
      this.duration = builder.duration;
      this.title = builder.title;
      this.uri = builder.uri;
      this.byteRange = builder.byteRange;
      this.programDateTime = builder.programDateTime;
      this.dateRange = builder.dateRange;
      this.segmentMap = builder.segmentMap;
      this.segmentKey = builder.segmentKey;
      this.cueOut = builder.cueOut;
      this.bitrate = builder.bitrate;
      this.partialSegments = createUnmodifiableList(true, builder.partialSegments);
      if (builder.discontinuityIsSet()) {
        initShim.discontinuity(builder.discontinuity);
      }
      if (builder.cueInIsSet()) {
        initShim.cueIn(builder.cueIn);
      }
      if (builder.gapIsSet()) {
        initShim.gap(builder.gap);
      }
      this.discontinuity = initShim.discontinuity();
      this.cueIn = initShim.cueIn();
      this.gap = initShim.gap();
      this.initShim = null;
    }

    private static final int STAGE_INITIALIZING = -1;
    private static final int STAGE_UNINITIALIZED = 0;
    private static final int STAGE_INITIALIZED = 1;
    private transient volatile InitShim initShim = new InitShim();

    private final class InitShim {
      private boolean discontinuity;
      private int discontinuityBuildStage;

      boolean discontinuity() {
        if (discontinuityBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
        if (discontinuityBuildStage == STAGE_UNINITIALIZED) {
          discontinuityBuildStage = STAGE_INITIALIZING;
          this.discontinuity = discontinuityInitialize();
          discontinuityBuildStage = STAGE_INITIALIZED;
        }
        return this.discontinuity;
      }

      void discontinuity(boolean discontinuity) {
        this.discontinuity = discontinuity;
        discontinuityBuildStage = STAGE_INITIALIZED;
      }
      private boolean cueIn;
      private int cueInBuildStage;

      boolean cueIn() {
        if (cueInBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
        if (cueInBuildStage == STAGE_UNINITIALIZED) {
          cueInBuildStage = STAGE_INITIALIZING;
          this.cueIn = cueInInitialize();
          cueInBuildStage = STAGE_INITIALIZED;
        }
        return this.cueIn;
      }

      void cueIn(boolean cueIn) {
        this.cueIn = cueIn;
        cueInBuildStage = STAGE_INITIALIZED;
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
        if (discontinuityBuildStage == STAGE_INITIALIZING) attributes.add("discontinuity");
        if (cueInBuildStage == STAGE_INITIALIZING) attributes.add("cueIn");
        if (gapBuildStage == STAGE_INITIALIZING) attributes.add("gap");
        return "Cannot build MediaSegment, attribute initializers form cycle" + attributes;
      }
    }

    private boolean discontinuityInitialize() {
      return MediaSegment.super.discontinuity();
    }

    private boolean cueInInitialize() {
      return MediaSegment.super.cueIn();
    }

    private boolean gapInitialize() {
      return MediaSegment.super.gap();
    }

    /**
     * @return The value of the {@code duration} attribute
     */
    @Override
    public double duration() {
      return duration;
    }

    /**
     * @return The value of the {@code title} attribute
     */
    @Override
    public Optional<String> title() {
      return Optional.ofNullable(title);
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
     * @return The value of the {@code programDateTime} attribute
     */
    @Override
    public Optional<OffsetDateTime> programDateTime() {
      return Optional.ofNullable(programDateTime);
    }

    /**
     * @return The value of the {@code dateRange} attribute
     */
    @Override
    public Optional<DateRange> dateRange() {
      return Optional.ofNullable(dateRange);
    }

    /**
     * @return The value of the {@code segmentMap} attribute
     */
    @Override
    public Optional<SegmentMap> segmentMap() {
      return Optional.ofNullable(segmentMap);
    }

    /**
     * @return The value of the {@code segmentKey} attribute
     */
    @Override
    public Optional<SegmentKey> segmentKey() {
      return Optional.ofNullable(segmentKey);
    }

    /**
     * @return The value of the {@code discontinuity} attribute
     */
    @Override
    public boolean discontinuity() {
      InitShim shim = this.initShim;
      return shim != null
          ? shim.discontinuity()
          : this.discontinuity;
    }

    /**
     * @return The value of the {@code cueOut} attribute
     */
    @Override
    public Optional<Double> cueOut() {
      return Optional.ofNullable(cueOut);
    }

    /**
     * @return The value of the {@code cueIn} attribute
     */
    @Override
    public boolean cueIn() {
      InitShim shim = this.initShim;
      return shim != null
          ? shim.cueIn()
          : this.cueIn;
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
     * @return The value of the {@code bitrate} attribute
     */
    @Override
    public Optional<Long> bitrate() {
      return Optional.ofNullable(bitrate);
    }

    /**
     * @return The value of the {@code partialSegments} attribute
     */
    @Override
    public List<PartialSegment> partialSegments() {
      return partialSegments;
    }

    /**
     * This instance is equal to all instances of {@code ImmutableMediaSegment} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutableMediaSegment
          && equalTo((ImmutableMediaSegment) another);
    }

    private boolean equalTo(ImmutableMediaSegment another) {
      return Double.doubleToLongBits(duration) == Double.doubleToLongBits(another.duration)
          && Objects.equals(title, another.title)
          && uri.equals(another.uri)
          && Objects.equals(byteRange, another.byteRange)
          && Objects.equals(programDateTime, another.programDateTime)
          && Objects.equals(dateRange, another.dateRange)
          && Objects.equals(segmentMap, another.segmentMap)
          && Objects.equals(segmentKey, another.segmentKey)
          && discontinuity == another.discontinuity
          && Objects.equals(cueOut, another.cueOut)
          && cueIn == another.cueIn
          && gap == another.gap
          && Objects.equals(bitrate, another.bitrate)
          && partialSegments.equals(another.partialSegments);
    }

    /**
     * Computes a hash code from attributes: {@code duration}, {@code title}, {@code uri}, {@code byteRange}, {@code programDateTime}, {@code dateRange}, {@code segmentMap}, {@code segmentKey}, {@code discontinuity}, {@code cueOut}, {@code cueIn}, {@code gap}, {@code bitrate}, {@code partialSegments}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + Double.hashCode(duration);
      h += (h << 5) + Objects.hashCode(title);
      h += (h << 5) + uri.hashCode();
      h += (h << 5) + Objects.hashCode(byteRange);
      h += (h << 5) + Objects.hashCode(programDateTime);
      h += (h << 5) + Objects.hashCode(dateRange);
      h += (h << 5) + Objects.hashCode(segmentMap);
      h += (h << 5) + Objects.hashCode(segmentKey);
      h += (h << 5) + Boolean.hashCode(discontinuity);
      h += (h << 5) + Objects.hashCode(cueOut);
      h += (h << 5) + Boolean.hashCode(cueIn);
      h += (h << 5) + Boolean.hashCode(gap);
      h += (h << 5) + Objects.hashCode(bitrate);
      h += (h << 5) + partialSegments.hashCode();
      return h;
    }

    /**
     * Prints the immutable value {@code MediaSegment} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("MediaSegment{");
      builder.append("duration=").append(duration);
      if (title != null) {
        builder.append(", ");
        builder.append("title=").append(title);
      }
      builder.append(", ");
      builder.append("uri=").append(uri);
      if (byteRange != null) {
        builder.append(", ");
        builder.append("byteRange=").append(byteRange);
      }
      if (programDateTime != null) {
        builder.append(", ");
        builder.append("programDateTime=").append(programDateTime);
      }
      if (dateRange != null) {
        builder.append(", ");
        builder.append("dateRange=").append(dateRange);
      }
      if (segmentMap != null) {
        builder.append(", ");
        builder.append("segmentMap=").append(segmentMap);
      }
      if (segmentKey != null) {
        builder.append(", ");
        builder.append("segmentKey=").append(segmentKey);
      }
      builder.append(", ");
      builder.append("discontinuity=").append(discontinuity);
      if (cueOut != null) {
        builder.append(", ");
        builder.append("cueOut=").append(cueOut);
      }
      builder.append(", ");
      builder.append("cueIn=").append(cueIn);
      builder.append(", ");
      builder.append("gap=").append(gap);
      if (bitrate != null) {
        builder.append(", ");
        builder.append("bitrate=").append(bitrate);
      }
      builder.append(", ");
      builder.append("partialSegments=").append(partialSegments);
      return builder.append("}").toString();
    }
  }

  private static <T> List<T> createSafeList(Iterable<? extends T> iterable, boolean checkNulls, boolean skipNulls) {
    ArrayList<T> list;
    if (iterable instanceof Collection<?>) {
      int size = ((Collection<?>) iterable).size();
      if (size == 0) return Collections.emptyList();
      list = new ArrayList<T>();
    } else {
      list = new ArrayList<T>();
    }
    for (T element : iterable) {
      if (skipNulls && element == null) continue;
      if (checkNulls) Objects.requireNonNull(element, "element");
      list.add(element);
    }
    return list;
  }

  private static <T> List<T> createUnmodifiableList(boolean clone, List<T> list) {
    switch(list.size()) {
    case 0: return Collections.emptyList();
    case 1: return Collections.singletonList(list.get(0));
    default:
      if (clone) {
        return Collections.unmodifiableList(new ArrayList<T>(list));
      } else {
        if (list instanceof ArrayList<?>) {
          ((ArrayList<?>) list).trimToSize();
        }
        return Collections.unmodifiableList(list);
      }
    }
  }
}
