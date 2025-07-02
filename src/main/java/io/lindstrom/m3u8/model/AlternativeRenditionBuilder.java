package io.lindstrom.m3u8.model;


import java.util.*;

/**
 * Builds instances of type {@link AlternativeRendition AlternativeRendition}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code AlternativeRenditionBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})

class AlternativeRenditionBuilder {
  private static final long INIT_BIT_TYPE = 0x1L;
  private static final long INIT_BIT_GROUP_ID = 0x2L;
  private static final long INIT_BIT_NAME = 0x4L;
  private long initBits = 0x7L;

  private MediaType type;
  private String uri;
  private String groupId;
  private String language;
  private String assocLanguage;
  private String name;
  private String stableRenditionId;
  private Boolean defaultRendition;
  private Boolean autoSelect;
  private Boolean forced;
  private String inStreamId;
  private List<String> characteristics = new ArrayList<String>();
  private Channels channels;

  /**
   * Creates a builder for {@link AlternativeRendition AlternativeRendition} instances.
   */
  AlternativeRenditionBuilder() {
    if (!(this instanceof AlternativeRendition.Builder)) {
      throw new UnsupportedOperationException("Use: new AlternativeRendition.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code AlternativeRendition} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * Collection elements and entries will be added, not replaced.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final AlternativeRendition.Builder from(AlternativeRendition instance) {
    Objects.requireNonNull(instance, "instance");
    type(instance.type());
    Optional<String> uriOptional = instance.uri();
    if (uriOptional.isPresent()) {
      uri(uriOptional);
    }
    groupId(instance.groupId());
    Optional<String> languageOptional = instance.language();
    if (languageOptional.isPresent()) {
      language(languageOptional);
    }
    Optional<String> assocLanguageOptional = instance.assocLanguage();
    if (assocLanguageOptional.isPresent()) {
      assocLanguage(assocLanguageOptional);
    }
    name(instance.name());
    Optional<String> stableRenditionIdOptional = instance.stableRenditionId();
    if (stableRenditionIdOptional.isPresent()) {
      stableRenditionId(stableRenditionIdOptional);
    }
    Optional<Boolean> defaultRenditionOptional = instance.defaultRendition();
    if (defaultRenditionOptional.isPresent()) {
      defaultRendition(defaultRenditionOptional);
    }
    Optional<Boolean> autoSelectOptional = instance.autoSelect();
    if (autoSelectOptional.isPresent()) {
      autoSelect(autoSelectOptional);
    }
    Optional<Boolean> forcedOptional = instance.forced();
    if (forcedOptional.isPresent()) {
      forced(forcedOptional);
    }
    Optional<String> inStreamIdOptional = instance.inStreamId();
    if (inStreamIdOptional.isPresent()) {
      inStreamId(inStreamIdOptional);
    }
    addAllCharacteristics(instance.characteristics());
    Optional<Channels> channelsOptional = instance.channels();
    if (channelsOptional.isPresent()) {
      channels(channelsOptional);
    }
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the value for the {@link AlternativeRendition#type() type} attribute.
   * @param type The value for type 
   * @return {@code this} builder for use in a chained invocation
   */
  public final AlternativeRendition.Builder type(MediaType type) {
    this.type = Objects.requireNonNull(type, "type");
    initBits &= ~INIT_BIT_TYPE;
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#uri() uri} to uri.
   * @param uri The value for uri
   * @return {@code this} builder for chained invocation
   */
  public final AlternativeRendition.Builder uri(String uri) {
    this.uri = Objects.requireNonNull(uri, "uri");
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#uri() uri} to uri.
   * @param uri The value for uri
   * @return {@code this} builder for use in a chained invocation
   */
  public final AlternativeRendition.Builder uri(Optional<String> uri) {
    this.uri = uri.orElse(null);
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the value for the {@link AlternativeRendition#groupId() groupId} attribute.
   * @param groupId The value for groupId 
   * @return {@code this} builder for use in a chained invocation
   */
  public final AlternativeRendition.Builder groupId(String groupId) {
    this.groupId = Objects.requireNonNull(groupId, "groupId");
    initBits &= ~INIT_BIT_GROUP_ID;
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#language() language} to language.
   * @param language The value for language
   * @return {@code this} builder for chained invocation
   */
  public final AlternativeRendition.Builder language(String language) {
    this.language = Objects.requireNonNull(language, "language");
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#language() language} to language.
   * @param language The value for language
   * @return {@code this} builder for use in a chained invocation
   */
  public final AlternativeRendition.Builder language(Optional<String> language) {
    this.language = language.orElse(null);
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#assocLanguage() assocLanguage} to assocLanguage.
   * @param assocLanguage The value for assocLanguage
   * @return {@code this} builder for chained invocation
   */
  public final AlternativeRendition.Builder assocLanguage(String assocLanguage) {
    this.assocLanguage = Objects.requireNonNull(assocLanguage, "assocLanguage");
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#assocLanguage() assocLanguage} to assocLanguage.
   * @param assocLanguage The value for assocLanguage
   * @return {@code this} builder for use in a chained invocation
   */
  public final AlternativeRendition.Builder assocLanguage(Optional<String> assocLanguage) {
    this.assocLanguage = assocLanguage.orElse(null);
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the value for the {@link AlternativeRendition#name() name} attribute.
   * @param name The value for name 
   * @return {@code this} builder for use in a chained invocation
   */
  public final AlternativeRendition.Builder name(String name) {
    this.name = Objects.requireNonNull(name, "name");
    initBits &= ~INIT_BIT_NAME;
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#stableRenditionId() stableRenditionId} to stableRenditionId.
   * @param stableRenditionId The value for stableRenditionId
   * @return {@code this} builder for chained invocation
   */
  public final AlternativeRendition.Builder stableRenditionId(String stableRenditionId) {
    this.stableRenditionId = Objects.requireNonNull(stableRenditionId, "stableRenditionId");
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#stableRenditionId() stableRenditionId} to stableRenditionId.
   * @param stableRenditionId The value for stableRenditionId
   * @return {@code this} builder for use in a chained invocation
   */
  public final AlternativeRendition.Builder stableRenditionId(Optional<String> stableRenditionId) {
    this.stableRenditionId = stableRenditionId.orElse(null);
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#defaultRendition() defaultRendition} to defaultRendition.
   * @param defaultRendition The value for defaultRendition
   * @return {@code this} builder for chained invocation
   */
  public final AlternativeRendition.Builder defaultRendition(boolean defaultRendition) {
    this.defaultRendition = defaultRendition;
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#defaultRendition() defaultRendition} to defaultRendition.
   * @param defaultRendition The value for defaultRendition
   * @return {@code this} builder for use in a chained invocation
   */
  public final AlternativeRendition.Builder defaultRendition(Optional<Boolean> defaultRendition) {
    this.defaultRendition = defaultRendition.orElse(null);
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#autoSelect() autoSelect} to autoSelect.
   * @param autoSelect The value for autoSelect
   * @return {@code this} builder for chained invocation
   */
  public final AlternativeRendition.Builder autoSelect(boolean autoSelect) {
    this.autoSelect = autoSelect;
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#autoSelect() autoSelect} to autoSelect.
   * @param autoSelect The value for autoSelect
   * @return {@code this} builder for use in a chained invocation
   */
  public final AlternativeRendition.Builder autoSelect(Optional<Boolean> autoSelect) {
    this.autoSelect = autoSelect.orElse(null);
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#forced() forced} to forced.
   * @param forced The value for forced
   * @return {@code this} builder for chained invocation
   */
  public final AlternativeRendition.Builder forced(boolean forced) {
    this.forced = forced;
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#forced() forced} to forced.
   * @param forced The value for forced
   * @return {@code this} builder for use in a chained invocation
   */
  public final AlternativeRendition.Builder forced(Optional<Boolean> forced) {
    this.forced = forced.orElse(null);
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#inStreamId() inStreamId} to inStreamId.
   * @param inStreamId The value for inStreamId
   * @return {@code this} builder for chained invocation
   */
  public final AlternativeRendition.Builder inStreamId(String inStreamId) {
    this.inStreamId = Objects.requireNonNull(inStreamId, "inStreamId");
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#inStreamId() inStreamId} to inStreamId.
   * @param inStreamId The value for inStreamId
   * @return {@code this} builder for use in a chained invocation
   */
  public final AlternativeRendition.Builder inStreamId(Optional<String> inStreamId) {
    this.inStreamId = inStreamId.orElse(null);
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Adds one element to {@link AlternativeRendition#characteristics() characteristics} list.
   * @param element A characteristics element
   * @return {@code this} builder for use in a chained invocation
   */
  public final AlternativeRendition.Builder addCharacteristics(String element) {
    this.characteristics.add(Objects.requireNonNull(element, "characteristics element"));
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Adds elements to {@link AlternativeRendition#characteristics() characteristics} list.
   * @param elements An array of characteristics elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final AlternativeRendition.Builder addCharacteristics(String... elements) {
    for (String element : elements) {
      this.characteristics.add(Objects.requireNonNull(element, "characteristics element"));
    }
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Sets or replaces all elements for {@link AlternativeRendition#characteristics() characteristics} list.
   * @param elements An iterable of characteristics elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final AlternativeRendition.Builder characteristics(Iterable<String> elements) {
    this.characteristics.clear();
    return addAllCharacteristics(elements);
  }

  /**
   * Adds elements to {@link AlternativeRendition#characteristics() characteristics} list.
   * @param elements An iterable of characteristics elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final AlternativeRendition.Builder addAllCharacteristics(Iterable<String> elements) {
    for (String element : elements) {
      this.characteristics.add(Objects.requireNonNull(element, "characteristics element"));
    }
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#channels() channels} to channels.
   * @param channels The value for channels
   * @return {@code this} builder for chained invocation
   */
  public final AlternativeRendition.Builder channels(Channels channels) {
    this.channels = Objects.requireNonNull(channels, "channels");
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Initializes the optional value {@link AlternativeRendition#channels() channels} to channels.
   * @param channels The value for channels
   * @return {@code this} builder for use in a chained invocation
   */
  public final AlternativeRendition.Builder channels(Optional<? extends Channels> channels) {
    this.channels = channels.orElse(null);
    return (AlternativeRendition.Builder) this;
  }

  /**
   * Builds a new {@link AlternativeRendition AlternativeRendition}.
   * @return An immutable instance of AlternativeRendition
   * @throws IllegalStateException if any required attributes are missing
   */
  public AlternativeRendition build() {
    if (initBits != 0) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
    return new ImmutableAlternativeRendition(this);
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if ((initBits & INIT_BIT_TYPE) != 0) attributes.add("type");
    if ((initBits & INIT_BIT_GROUP_ID) != 0) attributes.add("groupId");
    if ((initBits & INIT_BIT_NAME) != 0) attributes.add("name");
    return "Cannot build AlternativeRendition, some of required attributes are not set " + attributes;
  }

  /**
   * Immutable implementation of {@link AlternativeRendition}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new AlternativeRendition.Builder()}.
   */
  private static final class ImmutableAlternativeRendition implements AlternativeRendition {
    private final MediaType type;
    private final String uri;
    private final String groupId;
    private final String language;
    private final String assocLanguage;
    private final String name;
    private final String stableRenditionId;
    private final Boolean defaultRendition;
    private final Boolean autoSelect;
    private final Boolean forced;
    private final String inStreamId;
    private final List<String> characteristics;
    private final Channels channels;

    private ImmutableAlternativeRendition(AlternativeRenditionBuilder builder) {
      this.type = builder.type;
      this.uri = builder.uri;
      this.groupId = builder.groupId;
      this.language = builder.language;
      this.assocLanguage = builder.assocLanguage;
      this.name = builder.name;
      this.stableRenditionId = builder.stableRenditionId;
      this.defaultRendition = builder.defaultRendition;
      this.autoSelect = builder.autoSelect;
      this.forced = builder.forced;
      this.inStreamId = builder.inStreamId;
      this.characteristics = createUnmodifiableList(true, builder.characteristics);
      this.channels = builder.channels;
    }

    /**
     * @return The value of the {@code type} attribute
     */
    @Override
    public MediaType type() {
      return type;
    }

    /**
     * @return The value of the {@code uri} attribute
     */
    @Override
    public Optional<String> uri() {
      return Optional.ofNullable(uri);
    }

    /**
     * @return The value of the {@code groupId} attribute
     */
    @Override
    public String groupId() {
      return groupId;
    }

    /**
     * @return The value of the {@code language} attribute
     */
    @Override
    public Optional<String> language() {
      return Optional.ofNullable(language);
    }

    /**
     * @return The value of the {@code assocLanguage} attribute
     */
    @Override
    public Optional<String> assocLanguage() {
      return Optional.ofNullable(assocLanguage);
    }

    /**
     * @return The value of the {@code name} attribute
     */
    @Override
    public String name() {
      return name;
    }

    /**
     * @return The value of the {@code stableRenditionId} attribute
     */
    @Override
    public Optional<String> stableRenditionId() {
      return Optional.ofNullable(stableRenditionId);
    }

    /**
     * @return The value of the {@code defaultRendition} attribute
     */
    @Override
    public Optional<Boolean> defaultRendition() {
      return Optional.ofNullable(defaultRendition);
    }

    /**
     * @return The value of the {@code autoSelect} attribute
     */
    @Override
    public Optional<Boolean> autoSelect() {
      return Optional.ofNullable(autoSelect);
    }

    /**
     * @return The value of the {@code forced} attribute
     */
    @Override
    public Optional<Boolean> forced() {
      return Optional.ofNullable(forced);
    }

    /**
     * @return The value of the {@code inStreamId} attribute
     */
    @Override
    public Optional<String> inStreamId() {
      return Optional.ofNullable(inStreamId);
    }

    /**
     * @return The value of the {@code characteristics} attribute
     */
    @Override
    public List<String> characteristics() {
      return characteristics;
    }

    /**
     * @return The value of the {@code channels} attribute
     */
    @Override
    public Optional<Channels> channels() {
      return Optional.ofNullable(channels);
    }

    /**
     * This instance is equal to all instances of {@code ImmutableAlternativeRendition} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutableAlternativeRendition
          && equalTo((ImmutableAlternativeRendition) another);
    }

    private boolean equalTo(ImmutableAlternativeRendition another) {
      return type.equals(another.type)
          && Objects.equals(uri, another.uri)
          && groupId.equals(another.groupId)
          && Objects.equals(language, another.language)
          && Objects.equals(assocLanguage, another.assocLanguage)
          && name.equals(another.name)
          && Objects.equals(stableRenditionId, another.stableRenditionId)
          && Objects.equals(defaultRendition, another.defaultRendition)
          && Objects.equals(autoSelect, another.autoSelect)
          && Objects.equals(forced, another.forced)
          && Objects.equals(inStreamId, another.inStreamId)
          && characteristics.equals(another.characteristics)
          && Objects.equals(channels, another.channels);
    }

    /**
     * Computes a hash code from attributes: {@code type}, {@code uri}, {@code groupId}, {@code language}, {@code assocLanguage}, {@code name}, {@code stableRenditionId}, {@code defaultRendition}, {@code autoSelect}, {@code forced}, {@code inStreamId}, {@code characteristics}, {@code channels}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + type.hashCode();
      h += (h << 5) + Objects.hashCode(uri);
      h += (h << 5) + groupId.hashCode();
      h += (h << 5) + Objects.hashCode(language);
      h += (h << 5) + Objects.hashCode(assocLanguage);
      h += (h << 5) + name.hashCode();
      h += (h << 5) + Objects.hashCode(stableRenditionId);
      h += (h << 5) + Objects.hashCode(defaultRendition);
      h += (h << 5) + Objects.hashCode(autoSelect);
      h += (h << 5) + Objects.hashCode(forced);
      h += (h << 5) + Objects.hashCode(inStreamId);
      h += (h << 5) + characteristics.hashCode();
      h += (h << 5) + Objects.hashCode(channels);
      return h;
    }

    /**
     * Prints the immutable value {@code AlternativeRendition} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("AlternativeRendition{");
      builder.append("type=").append(type);
      if (uri != null) {
        builder.append(", ");
        builder.append("uri=").append(uri);
      }
      builder.append(", ");
      builder.append("groupId=").append(groupId);
      if (language != null) {
        builder.append(", ");
        builder.append("language=").append(language);
      }
      if (assocLanguage != null) {
        builder.append(", ");
        builder.append("assocLanguage=").append(assocLanguage);
      }
      builder.append(", ");
      builder.append("name=").append(name);
      if (stableRenditionId != null) {
        builder.append(", ");
        builder.append("stableRenditionId=").append(stableRenditionId);
      }
      if (defaultRendition != null) {
        builder.append(", ");
        builder.append("defaultRendition=").append(defaultRendition);
      }
      if (autoSelect != null) {
        builder.append(", ");
        builder.append("autoSelect=").append(autoSelect);
      }
      if (forced != null) {
        builder.append(", ");
        builder.append("forced=").append(forced);
      }
      if (inStreamId != null) {
        builder.append(", ");
        builder.append("inStreamId=").append(inStreamId);
      }
      builder.append(", ");
      builder.append("characteristics=").append(characteristics);
      if (channels != null) {
        builder.append(", ");
        builder.append("channels=").append(channels);
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
