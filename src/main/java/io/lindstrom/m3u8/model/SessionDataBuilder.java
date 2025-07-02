package io.lindstrom.m3u8.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Builds instances of type {@link SessionData SessionData}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code SessionDataBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})

class SessionDataBuilder {
  private static final long INIT_BIT_DATA_ID = 0x1L;
  private long initBits = 0x1L;

  private String dataId;
  private String value;
  private String uri;
  private String language;

  /**
   * Creates a builder for {@link SessionData SessionData} instances.
   */
  SessionDataBuilder() {
    if (!(this instanceof SessionData.Builder)) {
      throw new UnsupportedOperationException("Use: new SessionData.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code SessionData} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final SessionData.Builder from(SessionData instance) {
    Objects.requireNonNull(instance, "instance");
    dataId(instance.dataId());
    Optional<String> valueOptional = instance.value();
    if (valueOptional.isPresent()) {
      value(valueOptional);
    }
    Optional<String> uriOptional = instance.uri();
    if (uriOptional.isPresent()) {
      uri(uriOptional);
    }
    Optional<String> languageOptional = instance.language();
    if (languageOptional.isPresent()) {
      language(languageOptional);
    }
    return (SessionData.Builder) this;
  }

  /**
   * Initializes the value for the {@link SessionData#dataId() dataId} attribute.
   * @param dataId The value for dataId 
   * @return {@code this} builder for use in a chained invocation
   */
  public final SessionData.Builder dataId(String dataId) {
    this.dataId = Objects.requireNonNull(dataId, "dataId");
    initBits &= ~INIT_BIT_DATA_ID;
    return (SessionData.Builder) this;
  }

  /**
   * Initializes the optional value {@link SessionData#value() value} to value.
   * @param value The value for value
   * @return {@code this} builder for chained invocation
   */
  public final SessionData.Builder value(String value) {
    this.value = Objects.requireNonNull(value, "value");
    return (SessionData.Builder) this;
  }

  /**
   * Initializes the optional value {@link SessionData#value() value} to value.
   * @param value The value for value
   * @return {@code this} builder for use in a chained invocation
   */
  public final SessionData.Builder value(Optional<String> value) {
    this.value = value.orElse(null);
    return (SessionData.Builder) this;
  }

  /**
   * Initializes the optional value {@link SessionData#uri() uri} to uri.
   * @param uri The value for uri
   * @return {@code this} builder for chained invocation
   */
  public final SessionData.Builder uri(String uri) {
    this.uri = Objects.requireNonNull(uri, "uri");
    return (SessionData.Builder) this;
  }

  /**
   * Initializes the optional value {@link SessionData#uri() uri} to uri.
   * @param uri The value for uri
   * @return {@code this} builder for use in a chained invocation
   */
  public final SessionData.Builder uri(Optional<String> uri) {
    this.uri = uri.orElse(null);
    return (SessionData.Builder) this;
  }

  /**
   * Initializes the optional value {@link SessionData#language() language} to language.
   * @param language The value for language
   * @return {@code this} builder for chained invocation
   */
  public final SessionData.Builder language(String language) {
    this.language = Objects.requireNonNull(language, "language");
    return (SessionData.Builder) this;
  }

  /**
   * Initializes the optional value {@link SessionData#language() language} to language.
   * @param language The value for language
   * @return {@code this} builder for use in a chained invocation
   */
  public final SessionData.Builder language(Optional<String> language) {
    this.language = language.orElse(null);
    return (SessionData.Builder) this;
  }

  /**
   * Builds a new {@link SessionData SessionData}.
   * @return An immutable instance of SessionData
   * @throws IllegalStateException if any required attributes are missing
   */
  public SessionData build() {
    if (initBits != 0) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
    return new ImmutableSessionData(this);
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if ((initBits & INIT_BIT_DATA_ID) != 0) attributes.add("dataId");
    return "Cannot build SessionData, some of required attributes are not set " + attributes;
  }

  /**
   * Immutable implementation of {@link SessionData}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new SessionData.Builder()}.
   */
  private static final class ImmutableSessionData implements SessionData {
    private final String dataId;
    private final String value;
    private final String uri;
    private final String language;

    private ImmutableSessionData(SessionDataBuilder builder) {
      this.dataId = builder.dataId;
      this.value = builder.value;
      this.uri = builder.uri;
      this.language = builder.language;
    }

    /**
     * @return The value of the {@code dataId} attribute
     */
    @Override
    public String dataId() {
      return dataId;
    }

    /**
     * @return The value of the {@code value} attribute
     */
    @Override
    public Optional<String> value() {
      return Optional.ofNullable(value);
    }

    /**
     * @return The value of the {@code uri} attribute
     */
    @Override
    public Optional<String> uri() {
      return Optional.ofNullable(uri);
    }

    /**
     * @return The value of the {@code language} attribute
     */
    @Override
    public Optional<String> language() {
      return Optional.ofNullable(language);
    }

    /**
     * This instance is equal to all instances of {@code ImmutableSessionData} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutableSessionData
          && equalTo((ImmutableSessionData) another);
    }

    private boolean equalTo(ImmutableSessionData another) {
      return dataId.equals(another.dataId)
          && Objects.equals(value, another.value)
          && Objects.equals(uri, another.uri)
          && Objects.equals(language, another.language);
    }

    /**
     * Computes a hash code from attributes: {@code dataId}, {@code value}, {@code uri}, {@code language}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + dataId.hashCode();
      h += (h << 5) + Objects.hashCode(value);
      h += (h << 5) + Objects.hashCode(uri);
      h += (h << 5) + Objects.hashCode(language);
      return h;
    }

    /**
     * Prints the immutable value {@code SessionData} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("SessionData{");
      builder.append("dataId=").append(dataId);
      if (value != null) {
        builder.append(", ");
        builder.append("value=").append(value);
      }
      if (uri != null) {
        builder.append(", ");
        builder.append("uri=").append(uri);
      }
      if (language != null) {
        builder.append(", ");
        builder.append("language=").append(language);
      }
      return builder.append("}").toString();
    }
  }
}
