package io.lindstrom.m3u8.model;


import java.util.*;

/**
 * Builds instances of type {@link IFrameVariant IFrameVariant}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code IFrameVariantBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})

class IFrameVariantBuilder {
  private static final long INIT_BIT_URI = 0x1L;
  private static final long INIT_BIT_BANDWIDTH = 0x2L;
  private long initBits = 0x3L;

  private String uri;
  private long bandwidth;
  private Long averageBandwidth;
  private Double score;
  private List<String> codecs = new ArrayList<String>();
  private Resolution resolution;
  private String hdcpLevel;
  private List<String> allowedCpc = new ArrayList<String>();
  private String stableVariantId;
  private String video;
  private Integer programId;
  private VideoRange videoRange;
  private String name;
  private String language;

  /**
   * Creates a builder for {@link IFrameVariant IFrameVariant} instances.
   */
  IFrameVariantBuilder() {
    if (!(this instanceof IFrameVariant.Builder)) {
      throw new UnsupportedOperationException("Use: new IFrameVariant.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code IFrameVariant} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * Collection elements and entries will be added, not replaced.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder from(IFrameVariant instance) {
    Objects.requireNonNull(instance, "instance");
    uri(instance.uri());
    bandwidth(instance.bandwidth());
    Optional<Long> averageBandwidthOptional = instance.averageBandwidth();
    if (averageBandwidthOptional.isPresent()) {
      averageBandwidth(averageBandwidthOptional);
    }
    Optional<Double> scoreOptional = instance.score();
    if (scoreOptional.isPresent()) {
      score(scoreOptional);
    }
    addAllCodecs(instance.codecs());
    Optional<Resolution> resolutionOptional = instance.resolution();
    if (resolutionOptional.isPresent()) {
      resolution(resolutionOptional);
    }
    Optional<String> hdcpLevelOptional = instance.hdcpLevel();
    if (hdcpLevelOptional.isPresent()) {
      hdcpLevel(hdcpLevelOptional);
    }
    addAllAllowedCpc(instance.allowedCpc());
    Optional<String> stableVariantIdOptional = instance.stableVariantId();
    if (stableVariantIdOptional.isPresent()) {
      stableVariantId(stableVariantIdOptional);
    }
    Optional<String> videoOptional = instance.video();
    if (videoOptional.isPresent()) {
      video(videoOptional);
    }
    Optional<Integer> programIdOptional = instance.programId();
    if (programIdOptional.isPresent()) {
      programId(programIdOptional);
    }
    Optional<VideoRange> videoRangeOptional = instance.videoRange();
    if (videoRangeOptional.isPresent()) {
      videoRange(videoRangeOptional);
    }
    Optional<String> nameOptional = instance.name();
    if (nameOptional.isPresent()) {
      name(nameOptional);
    }
    Optional<String> languageOptional = instance.language();
    if (languageOptional.isPresent()) {
      language(languageOptional);
    }
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the value for the {@link IFrameVariant#uri() uri} attribute.
   * @param uri The value for uri 
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder uri(String uri) {
    this.uri = Objects.requireNonNull(uri, "uri");
    initBits &= ~INIT_BIT_URI;
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the value for the {@link IFrameVariant#bandwidth() bandwidth} attribute.
   * @param bandwidth The value for bandwidth 
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder bandwidth(long bandwidth) {
    this.bandwidth = bandwidth;
    initBits &= ~INIT_BIT_BANDWIDTH;
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#averageBandwidth() averageBandwidth} to averageBandwidth.
   * @param averageBandwidth The value for averageBandwidth
   * @return {@code this} builder for chained invocation
   */
  public final IFrameVariant.Builder averageBandwidth(long averageBandwidth) {
    this.averageBandwidth = averageBandwidth;
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#averageBandwidth() averageBandwidth} to averageBandwidth.
   * @param averageBandwidth The value for averageBandwidth
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder averageBandwidth(Optional<Long> averageBandwidth) {
    this.averageBandwidth = averageBandwidth.orElse(null);
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#score() score} to score.
   * @param score The value for score
   * @return {@code this} builder for chained invocation
   */
  public final IFrameVariant.Builder score(double score) {
    this.score = score;
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#score() score} to score.
   * @param score The value for score
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder score(Optional<Double> score) {
    this.score = score.orElse(null);
    return (IFrameVariant.Builder) this;
  }

  /**
   * Adds one element to {@link IFrameVariant#codecs() codecs} list.
   * @param element A codecs element
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder addCodecs(String element) {
    this.codecs.add(Objects.requireNonNull(element, "codecs element"));
    return (IFrameVariant.Builder) this;
  }

  /**
   * Adds elements to {@link IFrameVariant#codecs() codecs} list.
   * @param elements An array of codecs elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder addCodecs(String... elements) {
    for (String element : elements) {
      this.codecs.add(Objects.requireNonNull(element, "codecs element"));
    }
    return (IFrameVariant.Builder) this;
  }

  /**
   * Sets or replaces all elements for {@link IFrameVariant#codecs() codecs} list.
   * @param elements An iterable of codecs elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder codecs(Iterable<String> elements) {
    this.codecs.clear();
    return addAllCodecs(elements);
  }

  /**
   * Adds elements to {@link IFrameVariant#codecs() codecs} list.
   * @param elements An iterable of codecs elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder addAllCodecs(Iterable<String> elements) {
    for (String element : elements) {
      this.codecs.add(Objects.requireNonNull(element, "codecs element"));
    }
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#resolution() resolution} to resolution.
   * @param resolution The value for resolution
   * @return {@code this} builder for chained invocation
   */
  public final IFrameVariant.Builder resolution(Resolution resolution) {
    this.resolution = Objects.requireNonNull(resolution, "resolution");
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#resolution() resolution} to resolution.
   * @param resolution The value for resolution
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder resolution(Optional<? extends Resolution> resolution) {
    this.resolution = resolution.orElse(null);
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#hdcpLevel() hdcpLevel} to hdcpLevel.
   * @param hdcpLevel The value for hdcpLevel
   * @return {@code this} builder for chained invocation
   */
  public final IFrameVariant.Builder hdcpLevel(String hdcpLevel) {
    this.hdcpLevel = Objects.requireNonNull(hdcpLevel, "hdcpLevel");
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#hdcpLevel() hdcpLevel} to hdcpLevel.
   * @param hdcpLevel The value for hdcpLevel
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder hdcpLevel(Optional<String> hdcpLevel) {
    this.hdcpLevel = hdcpLevel.orElse(null);
    return (IFrameVariant.Builder) this;
  }

  /**
   * Adds one element to {@link IFrameVariant#allowedCpc() allowedCpc} list.
   * @param element A allowedCpc element
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder addAllowedCpc(String element) {
    this.allowedCpc.add(Objects.requireNonNull(element, "allowedCpc element"));
    return (IFrameVariant.Builder) this;
  }

  /**
   * Adds elements to {@link IFrameVariant#allowedCpc() allowedCpc} list.
   * @param elements An array of allowedCpc elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder addAllowedCpc(String... elements) {
    for (String element : elements) {
      this.allowedCpc.add(Objects.requireNonNull(element, "allowedCpc element"));
    }
    return (IFrameVariant.Builder) this;
  }

  /**
   * Sets or replaces all elements for {@link IFrameVariant#allowedCpc() allowedCpc} list.
   * @param elements An iterable of allowedCpc elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder allowedCpc(Iterable<String> elements) {
    this.allowedCpc.clear();
    return addAllAllowedCpc(elements);
  }

  /**
   * Adds elements to {@link IFrameVariant#allowedCpc() allowedCpc} list.
   * @param elements An iterable of allowedCpc elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder addAllAllowedCpc(Iterable<String> elements) {
    for (String element : elements) {
      this.allowedCpc.add(Objects.requireNonNull(element, "allowedCpc element"));
    }
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#stableVariantId() stableVariantId} to stableVariantId.
   * @param stableVariantId The value for stableVariantId
   * @return {@code this} builder for chained invocation
   */
  public final IFrameVariant.Builder stableVariantId(String stableVariantId) {
    this.stableVariantId = Objects.requireNonNull(stableVariantId, "stableVariantId");
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#stableVariantId() stableVariantId} to stableVariantId.
   * @param stableVariantId The value for stableVariantId
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder stableVariantId(Optional<String> stableVariantId) {
    this.stableVariantId = stableVariantId.orElse(null);
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#video() video} to video.
   * @param video The value for video
   * @return {@code this} builder for chained invocation
   */
  public final IFrameVariant.Builder video(String video) {
    this.video = Objects.requireNonNull(video, "video");
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#video() video} to video.
   * @param video The value for video
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder video(Optional<String> video) {
    this.video = video.orElse(null);
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#programId() programId} to programId.
   * @param programId The value for programId
   * @return {@code this} builder for chained invocation
   */
  public final IFrameVariant.Builder programId(int programId) {
    this.programId = programId;
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#programId() programId} to programId.
   * @param programId The value for programId
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder programId(Optional<Integer> programId) {
    this.programId = programId.orElse(null);
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#videoRange() videoRange} to videoRange.
   * @param videoRange The value for videoRange
   * @return {@code this} builder for chained invocation
   */
  public final IFrameVariant.Builder videoRange(VideoRange videoRange) {
    this.videoRange = Objects.requireNonNull(videoRange, "videoRange");
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#videoRange() videoRange} to videoRange.
   * @param videoRange The value for videoRange
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder videoRange(Optional<? extends VideoRange> videoRange) {
    this.videoRange = videoRange.orElse(null);
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#name() name} to name.
   * @param name The value for name
   * @return {@code this} builder for chained invocation
   */
  public final IFrameVariant.Builder name(String name) {
    this.name = Objects.requireNonNull(name, "name");
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#name() name} to name.
   * @param name The value for name
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder name(Optional<String> name) {
    this.name = name.orElse(null);
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#language() language} to language.
   * @param language The value for language
   * @return {@code this} builder for chained invocation
   */
  public final IFrameVariant.Builder language(String language) {
    this.language = Objects.requireNonNull(language, "language");
    return (IFrameVariant.Builder) this;
  }

  /**
   * Initializes the optional value {@link IFrameVariant#language() language} to language.
   * @param language The value for language
   * @return {@code this} builder for use in a chained invocation
   */
  public final IFrameVariant.Builder language(Optional<String> language) {
    this.language = language.orElse(null);
    return (IFrameVariant.Builder) this;
  }

  /**
   * Builds a new {@link IFrameVariant IFrameVariant}.
   * @return An immutable instance of IFrameVariant
   * @throws IllegalStateException if any required attributes are missing
   */
  public IFrameVariant build() {
    if (initBits != 0) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
    return new ImmutableIFrameVariant(this);
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if ((initBits & INIT_BIT_URI) != 0) attributes.add("uri");
    if ((initBits & INIT_BIT_BANDWIDTH) != 0) attributes.add("bandwidth");
    return "Cannot build IFrameVariant, some of required attributes are not set " + attributes;
  }

  /**
   * Immutable implementation of {@link IFrameVariant}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new IFrameVariant.Builder()}.
   */
  private static final class ImmutableIFrameVariant implements IFrameVariant {
    private final String uri;
    private final long bandwidth;
    private final Long averageBandwidth;
    private final Double score;
    private final List<String> codecs;
    private final Resolution resolution;
    private final String hdcpLevel;
    private final List<String> allowedCpc;
    private final String stableVariantId;
    private final String video;
    private final Integer programId;
    private final VideoRange videoRange;
    private final String name;
    private final String language;

    private ImmutableIFrameVariant(IFrameVariantBuilder builder) {
      this.uri = builder.uri;
      this.bandwidth = builder.bandwidth;
      this.averageBandwidth = builder.averageBandwidth;
      this.score = builder.score;
      this.codecs = createUnmodifiableList(true, builder.codecs);
      this.resolution = builder.resolution;
      this.hdcpLevel = builder.hdcpLevel;
      this.allowedCpc = createUnmodifiableList(true, builder.allowedCpc);
      this.stableVariantId = builder.stableVariantId;
      this.video = builder.video;
      this.programId = builder.programId;
      this.videoRange = builder.videoRange;
      this.name = builder.name;
      this.language = builder.language;
    }

    /**
     * @return The value of the {@code uri} attribute
     */
    @Override
    public String uri() {
      return uri;
    }

    /**
     * @return The value of the {@code bandwidth} attribute
     */
    @Override
    public long bandwidth() {
      return bandwidth;
    }

    /**
     * @return The value of the {@code averageBandwidth} attribute
     */
    @Override
    public Optional<Long> averageBandwidth() {
      return Optional.ofNullable(averageBandwidth);
    }

    /**
     * @return The value of the {@code score} attribute
     */
    @Override
    public Optional<Double> score() {
      return Optional.ofNullable(score);
    }

    /**
     * @return The value of the {@code codecs} attribute
     */
    @Override
    public List<String> codecs() {
      return codecs;
    }

    /**
     * @return The value of the {@code resolution} attribute
     */
    @Override
    public Optional<Resolution> resolution() {
      return Optional.ofNullable(resolution);
    }

    /**
     * @return The value of the {@code hdcpLevel} attribute
     */
    @Override
    public Optional<String> hdcpLevel() {
      return Optional.ofNullable(hdcpLevel);
    }

    /**
     * @return The value of the {@code allowedCpc} attribute
     */
    @Override
    public List<String> allowedCpc() {
      return allowedCpc;
    }

    /**
     * @return The value of the {@code stableVariantId} attribute
     */
    @Override
    public Optional<String> stableVariantId() {
      return Optional.ofNullable(stableVariantId);
    }

    /**
     * @return The value of the {@code video} attribute
     */
    @Override
    public Optional<String> video() {
      return Optional.ofNullable(video);
    }

    /**
     * @return The value of the {@code programId} attribute
     */
    @Override
    public Optional<Integer> programId() {
      return Optional.ofNullable(programId);
    }

    /**
     * @return The value of the {@code videoRange} attribute
     */
    @Override
    public Optional<VideoRange> videoRange() {
      return Optional.ofNullable(videoRange);
    }

    /**
     * @return The value of the {@code name} attribute
     */
    @Override
    public Optional<String> name() {
      return Optional.ofNullable(name);
    }

    /**
     * @return The value of the {@code language} attribute
     */
    @Override
    public Optional<String> language() {
      return Optional.ofNullable(language);
    }

    /**
     * This instance is equal to all instances of {@code ImmutableIFrameVariant} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutableIFrameVariant
          && equalTo((ImmutableIFrameVariant) another);
    }

    private boolean equalTo(ImmutableIFrameVariant another) {
      return uri.equals(another.uri)
          && bandwidth == another.bandwidth
          && Objects.equals(averageBandwidth, another.averageBandwidth)
          && Objects.equals(score, another.score)
          && codecs.equals(another.codecs)
          && Objects.equals(resolution, another.resolution)
          && Objects.equals(hdcpLevel, another.hdcpLevel)
          && allowedCpc.equals(another.allowedCpc)
          && Objects.equals(stableVariantId, another.stableVariantId)
          && Objects.equals(video, another.video)
          && Objects.equals(programId, another.programId)
          && Objects.equals(videoRange, another.videoRange)
          && Objects.equals(name, another.name)
          && Objects.equals(language, another.language);
    }

    /**
     * Computes a hash code from attributes: {@code uri}, {@code bandwidth}, {@code averageBandwidth}, {@code score}, {@code codecs}, {@code resolution}, {@code hdcpLevel}, {@code allowedCpc}, {@code stableVariantId}, {@code video}, {@code programId}, {@code videoRange}, {@code name}, {@code language}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + uri.hashCode();
      h += (h << 5) + Long.hashCode(bandwidth);
      h += (h << 5) + Objects.hashCode(averageBandwidth);
      h += (h << 5) + Objects.hashCode(score);
      h += (h << 5) + codecs.hashCode();
      h += (h << 5) + Objects.hashCode(resolution);
      h += (h << 5) + Objects.hashCode(hdcpLevel);
      h += (h << 5) + allowedCpc.hashCode();
      h += (h << 5) + Objects.hashCode(stableVariantId);
      h += (h << 5) + Objects.hashCode(video);
      h += (h << 5) + Objects.hashCode(programId);
      h += (h << 5) + Objects.hashCode(videoRange);
      h += (h << 5) + Objects.hashCode(name);
      h += (h << 5) + Objects.hashCode(language);
      return h;
    }

    /**
     * Prints the immutable value {@code IFrameVariant} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("IFrameVariant{");
      builder.append("uri=").append(uri);
      builder.append(", ");
      builder.append("bandwidth=").append(bandwidth);
      if (averageBandwidth != null) {
        builder.append(", ");
        builder.append("averageBandwidth=").append(averageBandwidth);
      }
      if (score != null) {
        builder.append(", ");
        builder.append("score=").append(score);
      }
      builder.append(", ");
      builder.append("codecs=").append(codecs);
      if (resolution != null) {
        builder.append(", ");
        builder.append("resolution=").append(resolution);
      }
      if (hdcpLevel != null) {
        builder.append(", ");
        builder.append("hdcpLevel=").append(hdcpLevel);
      }
      builder.append(", ");
      builder.append("allowedCpc=").append(allowedCpc);
      if (stableVariantId != null) {
        builder.append(", ");
        builder.append("stableVariantId=").append(stableVariantId);
      }
      if (video != null) {
        builder.append(", ");
        builder.append("video=").append(video);
      }
      if (programId != null) {
        builder.append(", ");
        builder.append("programId=").append(programId);
      }
      if (videoRange != null) {
        builder.append(", ");
        builder.append("videoRange=").append(videoRange);
      }
      if (name != null) {
        builder.append(", ");
        builder.append("name=").append(name);
      }
      if (language != null) {
        builder.append(", ");
        builder.append("language=").append(language);
      }
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
