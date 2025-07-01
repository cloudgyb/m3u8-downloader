package io.lindstrom.m3u8.model;


import java.util.Objects;
import java.util.Optional;

/**
 * Builds instances of type {@link PlaylistVariable PlaylistVariable}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code PlaylistVariableBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})

class PlaylistVariableBuilder {
  private String name;
  private String value;
  private String importAttribute;

  /**
   * Creates a builder for {@link PlaylistVariable PlaylistVariable} instances.
   */
  PlaylistVariableBuilder() {
    if (!(this instanceof PlaylistVariable.Builder)) {
      throw new UnsupportedOperationException("Use: new PlaylistVariable.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code PlaylistVariable} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final PlaylistVariable.Builder from(PlaylistVariable instance) {
    Objects.requireNonNull(instance, "instance");
    Optional<String> nameOptional = instance.name();
    if (nameOptional.isPresent()) {
      name(nameOptional);
    }
    Optional<String> valueOptional = instance.value();
    if (valueOptional.isPresent()) {
      value(valueOptional);
    }
    Optional<String> importAttributeOptional = instance.importAttribute();
    if (importAttributeOptional.isPresent()) {
      importAttribute(importAttributeOptional);
    }
    return (PlaylistVariable.Builder) this;
  }

  /**
   * Initializes the optional value {@link PlaylistVariable#name() name} to name.
   * @param name The value for name
   * @return {@code this} builder for chained invocation
   */
  public final PlaylistVariable.Builder name(String name) {
    this.name = Objects.requireNonNull(name, "name");
    return (PlaylistVariable.Builder) this;
  }

  /**
   * Initializes the optional value {@link PlaylistVariable#name() name} to name.
   * @param name The value for name
   * @return {@code this} builder for use in a chained invocation
   */
  public final PlaylistVariable.Builder name(Optional<String> name) {
    this.name = name.orElse(null);
    return (PlaylistVariable.Builder) this;
  }

  /**
   * Initializes the optional value {@link PlaylistVariable#value() value} to value.
   * @param value The value for value
   * @return {@code this} builder for chained invocation
   */
  public final PlaylistVariable.Builder value(String value) {
    this.value = Objects.requireNonNull(value, "value");
    return (PlaylistVariable.Builder) this;
  }

  /**
   * Initializes the optional value {@link PlaylistVariable#value() value} to value.
   * @param value The value for value
   * @return {@code this} builder for use in a chained invocation
   */
  public final PlaylistVariable.Builder value(Optional<String> value) {
    this.value = value.orElse(null);
    return (PlaylistVariable.Builder) this;
  }

  /**
   * Initializes the optional value {@link PlaylistVariable#importAttribute() importAttribute} to importAttribute.
   * @param importAttribute The value for importAttribute
   * @return {@code this} builder for chained invocation
   */
  public final PlaylistVariable.Builder importAttribute(String importAttribute) {
    this.importAttribute = Objects.requireNonNull(importAttribute, "importAttribute");
    return (PlaylistVariable.Builder) this;
  }

  /**
   * Initializes the optional value {@link PlaylistVariable#importAttribute() importAttribute} to importAttribute.
   * @param importAttribute The value for importAttribute
   * @return {@code this} builder for use in a chained invocation
   */
  public final PlaylistVariable.Builder importAttribute(Optional<String> importAttribute) {
    this.importAttribute = importAttribute.orElse(null);
    return (PlaylistVariable.Builder) this;
  }

  /**
   * Builds a new {@link PlaylistVariable PlaylistVariable}.
   * @return An immutable instance of PlaylistVariable
   * @throws IllegalStateException if any required attributes are missing
   */
  public PlaylistVariable build() {
    return new ImmutablePlaylistVariable(this);
  }

  /**
   * Immutable implementation of {@link PlaylistVariable}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new PlaylistVariable.Builder()}.
   */
  private static final class ImmutablePlaylistVariable implements PlaylistVariable {
    private final String name;
    private final String value;
    private final String importAttribute;

    private ImmutablePlaylistVariable(PlaylistVariableBuilder builder) {
      this.name = builder.name;
      this.value = builder.value;
      this.importAttribute = builder.importAttribute;
    }

    /**
     * @return The value of the {@code name} attribute
     */
    @Override
    public Optional<String> name() {
      return Optional.ofNullable(name);
    }

    /**
     * @return The value of the {@code value} attribute
     */
    @Override
    public Optional<String> value() {
      return Optional.ofNullable(value);
    }

    /**
     * @return The value of the {@code importAttribute} attribute
     */
    @Override
    public Optional<String> importAttribute() {
      return Optional.ofNullable(importAttribute);
    }

    /**
     * This instance is equal to all instances of {@code ImmutablePlaylistVariable} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutablePlaylistVariable
          && equalTo((ImmutablePlaylistVariable) another);
    }

    private boolean equalTo(ImmutablePlaylistVariable another) {
      return Objects.equals(name, another.name)
          && Objects.equals(value, another.value)
          && Objects.equals(importAttribute, another.importAttribute);
    }

    /**
     * Computes a hash code from attributes: {@code name}, {@code value}, {@code importAttribute}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + Objects.hashCode(name);
      h += (h << 5) + Objects.hashCode(value);
      h += (h << 5) + Objects.hashCode(importAttribute);
      return h;
    }

    /**
     * Prints the immutable value {@code PlaylistVariable} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("PlaylistVariable{");
      if (name != null) {
        builder.append("name=").append(name);
      }
      if (value != null) {
        if (builder.length() > 17) builder.append(", ");
        builder.append("value=").append(value);
      }
      if (importAttribute != null) {
        if (builder.length() > 17) builder.append(", ");
        builder.append("importAttribute=").append(importAttribute);
      }
      return builder.append("}").toString();
    }
  }
}
