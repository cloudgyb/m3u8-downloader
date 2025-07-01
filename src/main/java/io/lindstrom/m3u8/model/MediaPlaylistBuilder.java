package io.lindstrom.m3u8.model;


import java.util.*;

/**
 * Builds instances of type {@link MediaPlaylist MediaPlaylist}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code MediaPlaylistBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})

class MediaPlaylistBuilder {
  private static final long INIT_BIT_TARGET_DURATION = 0x1L;
  private static final long OPT_BIT_MEDIA_SEQUENCE = 0x1L;
  private static final long OPT_BIT_DISCONTINUITY_SEQUENCE = 0x2L;
  private static final long OPT_BIT_ONGOING = 0x4L;
  private static final long OPT_BIT_I_FRAMES_ONLY = 0x8L;
  private static final long OPT_BIT_INDEPENDENT_SEGMENTS = 0x10L;
  private long initBits = 0x1L;
  private long optBits;

  private int targetDuration;
  private long mediaSequence;
  private long discontinuitySequence;
  private boolean ongoing;
  private Boolean allowCache;
  private PlaylistType playlistType;
  private boolean iFramesOnly;
  private List<MediaSegment> mediaSegments = new ArrayList<MediaSegment>();
  private ServerControl serverControl;
  private PartialSegmentInformation partialSegmentInformation;
  private List<PartialSegment> partialSegments = new ArrayList<PartialSegment>();
  private Skip skip;
  private PreloadHint preloadHint;
  private List<RenditionReport> renditionReports = new ArrayList<RenditionReport>();
  private Integer version;
  private boolean independentSegments;
  private StartTimeOffset startTimeOffset;
  private List<PlaylistVariable> variables = new ArrayList<PlaylistVariable>();
  private List<String> comments = new ArrayList<String>();

  /**
   * Creates a builder for {@link MediaPlaylist MediaPlaylist} instances.
   */
  MediaPlaylistBuilder() {
    if (!(this instanceof MediaPlaylist.Builder)) {
      throw new UnsupportedOperationException("Use: new MediaPlaylist.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code io.lindstrom.m3u8.model.Playlist} instance.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder from(Playlist instance) {
    Objects.requireNonNull(instance, "instance");
    from((Object) instance);
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Fill a builder with attribute values from the provided {@code io.lindstrom.m3u8.model.MediaPlaylist} instance.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder from(MediaPlaylist instance) {
    Objects.requireNonNull(instance, "instance");
    from((Object) instance);
    return (MediaPlaylist.Builder) this;
  }

  private void from(Object object) {
    if (object instanceof Playlist) {
      Playlist instance = (Playlist) object;
      addAllVariables(instance.variables());
      addAllComments(instance.comments());
      independentSegments(instance.independentSegments());
      Optional<StartTimeOffset> startTimeOffsetOptional = instance.startTimeOffset();
      if (startTimeOffsetOptional.isPresent()) {
        startTimeOffset(startTimeOffsetOptional);
      }
      Optional<Integer> versionOptional = instance.version();
      if (versionOptional.isPresent()) {
        version(versionOptional);
      }
    }
    if (object instanceof MediaPlaylist) {
      MediaPlaylist instance = (MediaPlaylist) object;
      Optional<ServerControl> serverControlOptional = instance.serverControl();
      if (serverControlOptional.isPresent()) {
        serverControl(serverControlOptional);
      }
      addAllMediaSegments(instance.mediaSegments());
      addAllPartialSegments(instance.partialSegments());
      addAllRenditionReports(instance.renditionReports());
      Optional<Boolean> allowCacheOptional = instance.allowCache();
      if (allowCacheOptional.isPresent()) {
        allowCache(allowCacheOptional);
      }
      Optional<Skip> skipOptional = instance.skip();
      if (skipOptional.isPresent()) {
        skip(skipOptional);
      }
      mediaSequence(instance.mediaSequence());
      Optional<PartialSegmentInformation> partialSegmentInformationOptional = instance.partialSegmentInformation();
      if (partialSegmentInformationOptional.isPresent()) {
        partialSegmentInformation(partialSegmentInformationOptional);
      }
      Optional<PreloadHint> preloadHintOptional = instance.preloadHint();
      if (preloadHintOptional.isPresent()) {
        preloadHint(preloadHintOptional);
      }
      ongoing(instance.ongoing());
      iFramesOnly(instance.iFramesOnly());
      Optional<PlaylistType> playlistTypeOptional = instance.playlistType();
      if (playlistTypeOptional.isPresent()) {
        playlistType(playlistTypeOptional);
      }
      targetDuration(instance.targetDuration());
      discontinuitySequence(instance.discontinuitySequence());
    }
  }

  /**
   * Initializes the value for the {@link MediaPlaylist#targetDuration() targetDuration} attribute.
   * @param targetDuration The value for targetDuration 
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder targetDuration(int targetDuration) {
    this.targetDuration = targetDuration;
    initBits &= ~INIT_BIT_TARGET_DURATION;
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the value for the {@link MediaPlaylist#mediaSequence() mediaSequence} attribute.
   * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link MediaPlaylist#mediaSequence() mediaSequence}.</em>
   * @param mediaSequence The value for mediaSequence 
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder mediaSequence(long mediaSequence) {
    this.mediaSequence = mediaSequence;
    optBits |= OPT_BIT_MEDIA_SEQUENCE;
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the value for the {@link MediaPlaylist#discontinuitySequence() discontinuitySequence} attribute.
   * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link MediaPlaylist#discontinuitySequence() discontinuitySequence}.</em>
   * @param discontinuitySequence The value for discontinuitySequence 
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder discontinuitySequence(long discontinuitySequence) {
    this.discontinuitySequence = discontinuitySequence;
    optBits |= OPT_BIT_DISCONTINUITY_SEQUENCE;
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the value for the {@link MediaPlaylist#ongoing() ongoing} attribute.
   * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link MediaPlaylist#ongoing() ongoing}.</em>
   * @param ongoing The value for ongoing 
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder ongoing(boolean ongoing) {
    this.ongoing = ongoing;
    optBits |= OPT_BIT_ONGOING;
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaPlaylist#allowCache() allowCache} to allowCache.
   * @param allowCache The value for allowCache
   * @return {@code this} builder for chained invocation
   */
  public final MediaPlaylist.Builder allowCache(boolean allowCache) {
    this.allowCache = allowCache;
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaPlaylist#allowCache() allowCache} to allowCache.
   * @param allowCache The value for allowCache
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder allowCache(Optional<Boolean> allowCache) {
    this.allowCache = allowCache.orElse(null);
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaPlaylist#playlistType() playlistType} to playlistType.
   * @param playlistType The value for playlistType
   * @return {@code this} builder for chained invocation
   */
  public final MediaPlaylist.Builder playlistType(PlaylistType playlistType) {
    this.playlistType = Objects.requireNonNull(playlistType, "playlistType");
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaPlaylist#playlistType() playlistType} to playlistType.
   * @param playlistType The value for playlistType
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder playlistType(Optional<? extends PlaylistType> playlistType) {
    this.playlistType = playlistType.orElse(null);
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the value for the {@link MediaPlaylist#iFramesOnly() iFramesOnly} attribute.
   * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link MediaPlaylist#iFramesOnly() iFramesOnly}.</em>
   * @param iFramesOnly The value for iFramesOnly 
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder iFramesOnly(boolean iFramesOnly) {
    this.iFramesOnly = iFramesOnly;
    optBits |= OPT_BIT_I_FRAMES_ONLY;
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Adds one element to {@link MediaPlaylist#mediaSegments() mediaSegments} list.
   * @param element A mediaSegments element
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder addMediaSegments(MediaSegment element) {
    this.mediaSegments.add(Objects.requireNonNull(element, "mediaSegments element"));
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Adds elements to {@link MediaPlaylist#mediaSegments() mediaSegments} list.
   * @param elements An array of mediaSegments elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder addMediaSegments(MediaSegment... elements) {
    for (MediaSegment element : elements) {
      this.mediaSegments.add(Objects.requireNonNull(element, "mediaSegments element"));
    }
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Sets or replaces all elements for {@link MediaPlaylist#mediaSegments() mediaSegments} list.
   * @param elements An iterable of mediaSegments elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder mediaSegments(Iterable<? extends MediaSegment> elements) {
    this.mediaSegments.clear();
    return addAllMediaSegments(elements);
  }

  /**
   * Adds elements to {@link MediaPlaylist#mediaSegments() mediaSegments} list.
   * @param elements An iterable of mediaSegments elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder addAllMediaSegments(Iterable<? extends MediaSegment> elements) {
    for (MediaSegment element : elements) {
      this.mediaSegments.add(Objects.requireNonNull(element, "mediaSegments element"));
    }
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaPlaylist#serverControl() serverControl} to serverControl.
   * @param serverControl The value for serverControl
   * @return {@code this} builder for chained invocation
   */
  public final MediaPlaylist.Builder serverControl(ServerControl serverControl) {
    this.serverControl = Objects.requireNonNull(serverControl, "serverControl");
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaPlaylist#serverControl() serverControl} to serverControl.
   * @param serverControl The value for serverControl
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder serverControl(Optional<? extends ServerControl> serverControl) {
    this.serverControl = serverControl.orElse(null);
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaPlaylist#partialSegmentInformation() partialSegmentInformation} to partialSegmentInformation.
   * @param partialSegmentInformation The value for partialSegmentInformation
   * @return {@code this} builder for chained invocation
   */
  public final MediaPlaylist.Builder partialSegmentInformation(PartialSegmentInformation partialSegmentInformation) {
    this.partialSegmentInformation = Objects.requireNonNull(partialSegmentInformation, "partialSegmentInformation");
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaPlaylist#partialSegmentInformation() partialSegmentInformation} to partialSegmentInformation.
   * @param partialSegmentInformation The value for partialSegmentInformation
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder partialSegmentInformation(Optional<? extends PartialSegmentInformation> partialSegmentInformation) {
    this.partialSegmentInformation = partialSegmentInformation.orElse(null);
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Adds one element to {@link MediaPlaylist#partialSegments() partialSegments} list.
   * @param element A partialSegments element
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder addPartialSegments(PartialSegment element) {
    this.partialSegments.add(Objects.requireNonNull(element, "partialSegments element"));
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Adds elements to {@link MediaPlaylist#partialSegments() partialSegments} list.
   * @param elements An array of partialSegments elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder addPartialSegments(PartialSegment... elements) {
    for (PartialSegment element : elements) {
      this.partialSegments.add(Objects.requireNonNull(element, "partialSegments element"));
    }
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Sets or replaces all elements for {@link MediaPlaylist#partialSegments() partialSegments} list.
   * @param elements An iterable of partialSegments elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder partialSegments(Iterable<? extends PartialSegment> elements) {
    this.partialSegments.clear();
    return addAllPartialSegments(elements);
  }

  /**
   * Adds elements to {@link MediaPlaylist#partialSegments() partialSegments} list.
   * @param elements An iterable of partialSegments elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder addAllPartialSegments(Iterable<? extends PartialSegment> elements) {
    for (PartialSegment element : elements) {
      this.partialSegments.add(Objects.requireNonNull(element, "partialSegments element"));
    }
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaPlaylist#skip() skip} to skip.
   * @param skip The value for skip
   * @return {@code this} builder for chained invocation
   */
  public final MediaPlaylist.Builder skip(Skip skip) {
    this.skip = Objects.requireNonNull(skip, "skip");
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaPlaylist#skip() skip} to skip.
   * @param skip The value for skip
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder skip(Optional<? extends Skip> skip) {
    this.skip = skip.orElse(null);
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaPlaylist#preloadHint() preloadHint} to preloadHint.
   * @param preloadHint The value for preloadHint
   * @return {@code this} builder for chained invocation
   */
  public final MediaPlaylist.Builder preloadHint(PreloadHint preloadHint) {
    this.preloadHint = Objects.requireNonNull(preloadHint, "preloadHint");
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaPlaylist#preloadHint() preloadHint} to preloadHint.
   * @param preloadHint The value for preloadHint
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder preloadHint(Optional<? extends PreloadHint> preloadHint) {
    this.preloadHint = preloadHint.orElse(null);
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Adds one element to {@link MediaPlaylist#renditionReports() renditionReports} list.
   * @param element A renditionReports element
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder addRenditionReports(RenditionReport element) {
    this.renditionReports.add(Objects.requireNonNull(element, "renditionReports element"));
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Adds elements to {@link MediaPlaylist#renditionReports() renditionReports} list.
   * @param elements An array of renditionReports elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder addRenditionReports(RenditionReport... elements) {
    for (RenditionReport element : elements) {
      this.renditionReports.add(Objects.requireNonNull(element, "renditionReports element"));
    }
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Sets or replaces all elements for {@link MediaPlaylist#renditionReports() renditionReports} list.
   * @param elements An iterable of renditionReports elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder renditionReports(Iterable<? extends RenditionReport> elements) {
    this.renditionReports.clear();
    return addAllRenditionReports(elements);
  }

  /**
   * Adds elements to {@link MediaPlaylist#renditionReports() renditionReports} list.
   * @param elements An iterable of renditionReports elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder addAllRenditionReports(Iterable<? extends RenditionReport> elements) {
    for (RenditionReport element : elements) {
      this.renditionReports.add(Objects.requireNonNull(element, "renditionReports element"));
    }
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaPlaylist#version() version} to version.
   * @param version The value for version
   * @return {@code this} builder for chained invocation
   */
  public final MediaPlaylist.Builder version(int version) {
    this.version = version;
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaPlaylist#version() version} to version.
   * @param version The value for version
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder version(Optional<Integer> version) {
    this.version = version.orElse(null);
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the value for the {@link MediaPlaylist#independentSegments() independentSegments} attribute.
   * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link MediaPlaylist#independentSegments() independentSegments}.</em>
   * @param independentSegments The value for independentSegments 
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder independentSegments(boolean independentSegments) {
    this.independentSegments = independentSegments;
    optBits |= OPT_BIT_INDEPENDENT_SEGMENTS;
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaPlaylist#startTimeOffset() startTimeOffset} to startTimeOffset.
   * @param startTimeOffset The value for startTimeOffset
   * @return {@code this} builder for chained invocation
   */
  public final MediaPlaylist.Builder startTimeOffset(StartTimeOffset startTimeOffset) {
    this.startTimeOffset = Objects.requireNonNull(startTimeOffset, "startTimeOffset");
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Initializes the optional value {@link MediaPlaylist#startTimeOffset() startTimeOffset} to startTimeOffset.
   * @param startTimeOffset The value for startTimeOffset
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder startTimeOffset(Optional<? extends StartTimeOffset> startTimeOffset) {
    this.startTimeOffset = startTimeOffset.orElse(null);
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Adds one element to {@link MediaPlaylist#variables() variables} list.
   * @param element A variables element
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder addVariables(PlaylistVariable element) {
    this.variables.add(Objects.requireNonNull(element, "variables element"));
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Adds elements to {@link MediaPlaylist#variables() variables} list.
   * @param elements An array of variables elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder addVariables(PlaylistVariable... elements) {
    for (PlaylistVariable element : elements) {
      this.variables.add(Objects.requireNonNull(element, "variables element"));
    }
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Sets or replaces all elements for {@link MediaPlaylist#variables() variables} list.
   * @param elements An iterable of variables elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder variables(Iterable<? extends PlaylistVariable> elements) {
    this.variables.clear();
    return addAllVariables(elements);
  }

  /**
   * Adds elements to {@link MediaPlaylist#variables() variables} list.
   * @param elements An iterable of variables elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder addAllVariables(Iterable<? extends PlaylistVariable> elements) {
    for (PlaylistVariable element : elements) {
      this.variables.add(Objects.requireNonNull(element, "variables element"));
    }
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Adds one element to {@link MediaPlaylist#comments() comments} list.
   * @param element A comments element
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder addComments(String element) {
    this.comments.add(Objects.requireNonNull(element, "comments element"));
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Adds elements to {@link MediaPlaylist#comments() comments} list.
   * @param elements An array of comments elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder addComments(String... elements) {
    for (String element : elements) {
      this.comments.add(Objects.requireNonNull(element, "comments element"));
    }
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Sets or replaces all elements for {@link MediaPlaylist#comments() comments} list.
   * @param elements An iterable of comments elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder comments(Iterable<String> elements) {
    this.comments.clear();
    return addAllComments(elements);
  }

  /**
   * Adds elements to {@link MediaPlaylist#comments() comments} list.
   * @param elements An iterable of comments elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final MediaPlaylist.Builder addAllComments(Iterable<String> elements) {
    for (String element : elements) {
      this.comments.add(Objects.requireNonNull(element, "comments element"));
    }
    return (MediaPlaylist.Builder) this;
  }

  /**
   * Builds a new {@link MediaPlaylist MediaPlaylist}.
   * @return An immutable instance of MediaPlaylist
   * @throws IllegalStateException if any required attributes are missing
   */
  public MediaPlaylist build() {
    if (initBits != 0) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
    return new ImmutableMediaPlaylist(this);
  }

  private boolean mediaSequenceIsSet() {
    return (optBits & OPT_BIT_MEDIA_SEQUENCE) != 0;
  }

  private boolean discontinuitySequenceIsSet() {
    return (optBits & OPT_BIT_DISCONTINUITY_SEQUENCE) != 0;
  }

  private boolean ongoingIsSet() {
    return (optBits & OPT_BIT_ONGOING) != 0;
  }

  private boolean iFramesOnlyIsSet() {
    return (optBits & OPT_BIT_I_FRAMES_ONLY) != 0;
  }

  private boolean independentSegmentsIsSet() {
    return (optBits & OPT_BIT_INDEPENDENT_SEGMENTS) != 0;
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if ((initBits & INIT_BIT_TARGET_DURATION) != 0) attributes.add("targetDuration");
    return "Cannot build MediaPlaylist, some of required attributes are not set " + attributes;
  }

  /**
   * Immutable implementation of {@link MediaPlaylist}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new MediaPlaylist.Builder()}.
   */
  private static final class ImmutableMediaPlaylist implements MediaPlaylist {
    private final int targetDuration;
    private final long mediaSequence;
    private final long discontinuitySequence;
    private final boolean ongoing;
    private final Boolean allowCache;
    private final PlaylistType playlistType;
    private final boolean iFramesOnly;
    private final List<MediaSegment> mediaSegments;
    private final ServerControl serverControl;
    private final PartialSegmentInformation partialSegmentInformation;
    private final List<PartialSegment> partialSegments;
    private final Skip skip;
    private final PreloadHint preloadHint;
    private final List<RenditionReport> renditionReports;
    private final Integer version;
    private final boolean independentSegments;
    private final StartTimeOffset startTimeOffset;
    private final List<PlaylistVariable> variables;
    private final List<String> comments;

    private ImmutableMediaPlaylist(MediaPlaylistBuilder builder) {
      this.targetDuration = builder.targetDuration;
      this.allowCache = builder.allowCache;
      this.playlistType = builder.playlistType;
      this.mediaSegments = createUnmodifiableList(true, builder.mediaSegments);
      this.serverControl = builder.serverControl;
      this.partialSegmentInformation = builder.partialSegmentInformation;
      this.partialSegments = createUnmodifiableList(true, builder.partialSegments);
      this.skip = builder.skip;
      this.preloadHint = builder.preloadHint;
      this.renditionReports = createUnmodifiableList(true, builder.renditionReports);
      this.version = builder.version;
      this.startTimeOffset = builder.startTimeOffset;
      this.variables = createUnmodifiableList(true, builder.variables);
      this.comments = createUnmodifiableList(true, builder.comments);
      if (builder.mediaSequenceIsSet()) {
        initShim.mediaSequence(builder.mediaSequence);
      }
      if (builder.discontinuitySequenceIsSet()) {
        initShim.discontinuitySequence(builder.discontinuitySequence);
      }
      if (builder.ongoingIsSet()) {
        initShim.ongoing(builder.ongoing);
      }
      if (builder.iFramesOnlyIsSet()) {
        initShim.iFramesOnly(builder.iFramesOnly);
      }
      if (builder.independentSegmentsIsSet()) {
        initShim.independentSegments(builder.independentSegments);
      }
      this.mediaSequence = initShim.mediaSequence();
      this.discontinuitySequence = initShim.discontinuitySequence();
      this.ongoing = initShim.ongoing();
      this.iFramesOnly = initShim.iFramesOnly();
      this.independentSegments = initShim.independentSegments();
      this.initShim = null;
    }

    private static final int STAGE_INITIALIZING = -1;
    private static final int STAGE_UNINITIALIZED = 0;
    private static final int STAGE_INITIALIZED = 1;
    private transient volatile InitShim initShim = new InitShim();

    private final class InitShim {
      private long mediaSequence;
      private int mediaSequenceBuildStage;

      long mediaSequence() {
        if (mediaSequenceBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
        if (mediaSequenceBuildStage == STAGE_UNINITIALIZED) {
          mediaSequenceBuildStage = STAGE_INITIALIZING;
          this.mediaSequence = mediaSequenceInitialize();
          mediaSequenceBuildStage = STAGE_INITIALIZED;
        }
        return this.mediaSequence;
      }

      void mediaSequence(long mediaSequence) {
        this.mediaSequence = mediaSequence;
        mediaSequenceBuildStage = STAGE_INITIALIZED;
      }
      private long discontinuitySequence;
      private int discontinuitySequenceBuildStage;

      long discontinuitySequence() {
        if (discontinuitySequenceBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
        if (discontinuitySequenceBuildStage == STAGE_UNINITIALIZED) {
          discontinuitySequenceBuildStage = STAGE_INITIALIZING;
          this.discontinuitySequence = discontinuitySequenceInitialize();
          discontinuitySequenceBuildStage = STAGE_INITIALIZED;
        }
        return this.discontinuitySequence;
      }

      void discontinuitySequence(long discontinuitySequence) {
        this.discontinuitySequence = discontinuitySequence;
        discontinuitySequenceBuildStage = STAGE_INITIALIZED;
      }
      private boolean ongoing;
      private int ongoingBuildStage;

      boolean ongoing() {
        if (ongoingBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
        if (ongoingBuildStage == STAGE_UNINITIALIZED) {
          ongoingBuildStage = STAGE_INITIALIZING;
          this.ongoing = ongoingInitialize();
          ongoingBuildStage = STAGE_INITIALIZED;
        }
        return this.ongoing;
      }

      void ongoing(boolean ongoing) {
        this.ongoing = ongoing;
        ongoingBuildStage = STAGE_INITIALIZED;
      }
      private boolean iFramesOnly;
      private int iFramesOnlyBuildStage;

      boolean iFramesOnly() {
        if (iFramesOnlyBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
        if (iFramesOnlyBuildStage == STAGE_UNINITIALIZED) {
          iFramesOnlyBuildStage = STAGE_INITIALIZING;
          this.iFramesOnly = iFramesOnlyInitialize();
          iFramesOnlyBuildStage = STAGE_INITIALIZED;
        }
        return this.iFramesOnly;
      }

      void iFramesOnly(boolean iFramesOnly) {
        this.iFramesOnly = iFramesOnly;
        iFramesOnlyBuildStage = STAGE_INITIALIZED;
      }
      private boolean independentSegments;
      private int independentSegmentsBuildStage;

      boolean independentSegments() {
        if (independentSegmentsBuildStage == STAGE_INITIALIZING) throw new IllegalStateException(formatInitCycleMessage());
        if (independentSegmentsBuildStage == STAGE_UNINITIALIZED) {
          independentSegmentsBuildStage = STAGE_INITIALIZING;
          this.independentSegments = independentSegmentsInitialize();
          independentSegmentsBuildStage = STAGE_INITIALIZED;
        }
        return this.independentSegments;
      }

      void independentSegments(boolean independentSegments) {
        this.independentSegments = independentSegments;
        independentSegmentsBuildStage = STAGE_INITIALIZED;
      }

      private String formatInitCycleMessage() {
        ArrayList<String> attributes = new ArrayList<String>();
        if (mediaSequenceBuildStage == STAGE_INITIALIZING) attributes.add("mediaSequence");
        if (discontinuitySequenceBuildStage == STAGE_INITIALIZING) attributes.add("discontinuitySequence");
        if (ongoingBuildStage == STAGE_INITIALIZING) attributes.add("ongoing");
        if (iFramesOnlyBuildStage == STAGE_INITIALIZING) attributes.add("iFramesOnly");
        if (independentSegmentsBuildStage == STAGE_INITIALIZING) attributes.add("independentSegments");
        return "Cannot build MediaPlaylist, attribute initializers form cycle" + attributes;
      }
    }

    private long mediaSequenceInitialize() {
      return MediaPlaylist.super.mediaSequence();
    }

    private long discontinuitySequenceInitialize() {
      return MediaPlaylist.super.discontinuitySequence();
    }

    private boolean ongoingInitialize() {
      return MediaPlaylist.super.ongoing();
    }

    private boolean iFramesOnlyInitialize() {
      return MediaPlaylist.super.iFramesOnly();
    }

    private boolean independentSegmentsInitialize() {
      return MediaPlaylist.super.independentSegments();
    }

    /**
     * @return The value of the {@code targetDuration} attribute
     */
    @Override
    public int targetDuration() {
      return targetDuration;
    }

    /**
     * @return The value of the {@code mediaSequence} attribute
     */
    @Override
    public long mediaSequence() {
      InitShim shim = this.initShim;
      return shim != null
          ? shim.mediaSequence()
          : this.mediaSequence;
    }

    /**
     * @return The value of the {@code discontinuitySequence} attribute
     */
    @Override
    public long discontinuitySequence() {
      InitShim shim = this.initShim;
      return shim != null
          ? shim.discontinuitySequence()
          : this.discontinuitySequence;
    }

    /**
     * @return The value of the {@code ongoing} attribute
     */
    @Override
    public boolean ongoing() {
      InitShim shim = this.initShim;
      return shim != null
          ? shim.ongoing()
          : this.ongoing;
    }

    /**
     * @return The value of the {@code allowCache} attribute
     */
    @Override
    public Optional<Boolean> allowCache() {
      return Optional.ofNullable(allowCache);
    }

    /**
     * @return The value of the {@code playlistType} attribute
     */
    @Override
    public Optional<PlaylistType> playlistType() {
      return Optional.ofNullable(playlistType);
    }

    /**
     * @return The value of the {@code iFramesOnly} attribute
     */
    @Override
    public boolean iFramesOnly() {
      InitShim shim = this.initShim;
      return shim != null
          ? shim.iFramesOnly()
          : this.iFramesOnly;
    }

    /**
     * @return The value of the {@code mediaSegments} attribute
     */
    @Override
    public List<MediaSegment> mediaSegments() {
      return mediaSegments;
    }

    /**
     * @return The value of the {@code serverControl} attribute
     */
    @Override
    public Optional<ServerControl> serverControl() {
      return Optional.ofNullable(serverControl);
    }

    /**
     * @return The value of the {@code partialSegmentInformation} attribute
     */
    @Override
    public Optional<PartialSegmentInformation> partialSegmentInformation() {
      return Optional.ofNullable(partialSegmentInformation);
    }

    /**
     * @return The value of the {@code partialSegments} attribute
     */
    @Override
    public List<PartialSegment> partialSegments() {
      return partialSegments;
    }

    /**
     * @return The value of the {@code skip} attribute
     */
    @Override
    public Optional<Skip> skip() {
      return Optional.ofNullable(skip);
    }

    /**
     * @return The value of the {@code preloadHint} attribute
     */
    @Override
    public Optional<PreloadHint> preloadHint() {
      return Optional.ofNullable(preloadHint);
    }

    /**
     * @return The value of the {@code renditionReports} attribute
     */
    @Override
    public List<RenditionReport> renditionReports() {
      return renditionReports;
    }

    /**
     * @return The value of the {@code version} attribute
     */
    @Override
    public Optional<Integer> version() {
      return Optional.ofNullable(version);
    }

    /**
     * @return The value of the {@code independentSegments} attribute
     */
    @Override
    public boolean independentSegments() {
      InitShim shim = this.initShim;
      return shim != null
          ? shim.independentSegments()
          : this.independentSegments;
    }

    /**
     * @return The value of the {@code startTimeOffset} attribute
     */
    @Override
    public Optional<StartTimeOffset> startTimeOffset() {
      return Optional.ofNullable(startTimeOffset);
    }

    /**
     * @return The value of the {@code variables} attribute
     */
    @Override
    public List<PlaylistVariable> variables() {
      return variables;
    }

    /**
     * @return The value of the {@code comments} attribute
     */
    @Override
    public List<String> comments() {
      return comments;
    }

    /**
     * This instance is equal to all instances of {@code ImmutableMediaPlaylist} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutableMediaPlaylist
          && equalTo((ImmutableMediaPlaylist) another);
    }

    private boolean equalTo(ImmutableMediaPlaylist another) {
      return targetDuration == another.targetDuration
          && mediaSequence == another.mediaSequence
          && discontinuitySequence == another.discontinuitySequence
          && ongoing == another.ongoing
          && Objects.equals(allowCache, another.allowCache)
          && Objects.equals(playlistType, another.playlistType)
          && iFramesOnly == another.iFramesOnly
          && mediaSegments.equals(another.mediaSegments)
          && Objects.equals(serverControl, another.serverControl)
          && Objects.equals(partialSegmentInformation, another.partialSegmentInformation)
          && partialSegments.equals(another.partialSegments)
          && Objects.equals(skip, another.skip)
          && Objects.equals(preloadHint, another.preloadHint)
          && renditionReports.equals(another.renditionReports)
          && Objects.equals(version, another.version)
          && independentSegments == another.independentSegments
          && Objects.equals(startTimeOffset, another.startTimeOffset)
          && variables.equals(another.variables)
          && comments.equals(another.comments);
    }

    /**
     * Computes a hash code from attributes: {@code targetDuration}, {@code mediaSequence}, {@code discontinuitySequence}, {@code ongoing}, {@code allowCache}, {@code playlistType}, {@code iFramesOnly}, {@code mediaSegments}, {@code serverControl}, {@code partialSegmentInformation}, {@code partialSegments}, {@code skip}, {@code preloadHint}, {@code renditionReports}, {@code version}, {@code independentSegments}, {@code startTimeOffset}, {@code variables}, {@code comments}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + targetDuration;
      h += (h << 5) + Long.hashCode(mediaSequence);
      h += (h << 5) + Long.hashCode(discontinuitySequence);
      h += (h << 5) + Boolean.hashCode(ongoing);
      h += (h << 5) + Objects.hashCode(allowCache);
      h += (h << 5) + Objects.hashCode(playlistType);
      h += (h << 5) + Boolean.hashCode(iFramesOnly);
      h += (h << 5) + mediaSegments.hashCode();
      h += (h << 5) + Objects.hashCode(serverControl);
      h += (h << 5) + Objects.hashCode(partialSegmentInformation);
      h += (h << 5) + partialSegments.hashCode();
      h += (h << 5) + Objects.hashCode(skip);
      h += (h << 5) + Objects.hashCode(preloadHint);
      h += (h << 5) + renditionReports.hashCode();
      h += (h << 5) + Objects.hashCode(version);
      h += (h << 5) + Boolean.hashCode(independentSegments);
      h += (h << 5) + Objects.hashCode(startTimeOffset);
      h += (h << 5) + variables.hashCode();
      h += (h << 5) + comments.hashCode();
      return h;
    }

    /**
     * Prints the immutable value {@code MediaPlaylist} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("MediaPlaylist{");
      builder.append("targetDuration=").append(targetDuration);
      builder.append(", ");
      builder.append("mediaSequence=").append(mediaSequence);
      builder.append(", ");
      builder.append("discontinuitySequence=").append(discontinuitySequence);
      builder.append(", ");
      builder.append("ongoing=").append(ongoing);
      if (allowCache != null) {
        builder.append(", ");
        builder.append("allowCache=").append(allowCache);
      }
      if (playlistType != null) {
        builder.append(", ");
        builder.append("playlistType=").append(playlistType);
      }
      builder.append(", ");
      builder.append("iFramesOnly=").append(iFramesOnly);
      builder.append(", ");
      builder.append("mediaSegments=").append(mediaSegments);
      if (serverControl != null) {
        builder.append(", ");
        builder.append("serverControl=").append(serverControl);
      }
      if (partialSegmentInformation != null) {
        builder.append(", ");
        builder.append("partialSegmentInformation=").append(partialSegmentInformation);
      }
      builder.append(", ");
      builder.append("partialSegments=").append(partialSegments);
      if (skip != null) {
        builder.append(", ");
        builder.append("skip=").append(skip);
      }
      if (preloadHint != null) {
        builder.append(", ");
        builder.append("preloadHint=").append(preloadHint);
      }
      builder.append(", ");
      builder.append("renditionReports=").append(renditionReports);
      if (version != null) {
        builder.append(", ");
        builder.append("version=").append(version);
      }
      builder.append(", ");
      builder.append("independentSegments=").append(independentSegments);
      if (startTimeOffset != null) {
        builder.append(", ");
        builder.append("startTimeOffset=").append(startTimeOffset);
      }
      builder.append(", ");
      builder.append("variables=").append(variables);
      builder.append(", ");
      builder.append("comments=").append(comments);
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
