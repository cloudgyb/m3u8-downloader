package io.lindstrom.m3u8.model;


import java.util.Objects;
import java.util.Optional;

/**
 * Builds instances of type {@link ServerControl ServerControl}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code ServerControlBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})

class ServerControlBuilder {
  private static final long OPT_BIT_CAN_BLOCK_RELOAD = 0x1L;
  private long optBits;

  private Double canSkipUntil;
  private Boolean canSkipDateRanges;
  private Double holdBack;
  private Double partHoldBack;
  private boolean canBlockReload;

  /**
   * Creates a builder for {@link ServerControl ServerControl} instances.
   */
  ServerControlBuilder() {
    if (!(this instanceof ServerControl.Builder)) {
      throw new UnsupportedOperationException("Use: new ServerControl.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code ServerControl} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final ServerControl.Builder from(ServerControl instance) {
    Objects.requireNonNull(instance, "instance");
    Optional<Double> canSkipUntilOptional = instance.canSkipUntil();
    if (canSkipUntilOptional.isPresent()) {
      canSkipUntil(canSkipUntilOptional);
    }
    Optional<Boolean> canSkipDateRangesOptional = instance.canSkipDateRanges();
    if (canSkipDateRangesOptional.isPresent()) {
      canSkipDateRanges(canSkipDateRangesOptional);
    }
    Optional<Double> holdBackOptional = instance.holdBack();
    if (holdBackOptional.isPresent()) {
      holdBack(holdBackOptional);
    }
    Optional<Double> partHoldBackOptional = instance.partHoldBack();
    if (partHoldBackOptional.isPresent()) {
      partHoldBack(partHoldBackOptional);
    }
    canBlockReload(instance.canBlockReload());
    return (ServerControl.Builder) this;
  }

  /**
   * Initializes the optional value {@link ServerControl#canSkipUntil() canSkipUntil} to canSkipUntil.
   * @param canSkipUntil The value for canSkipUntil
   * @return {@code this} builder for chained invocation
   */
  public final ServerControl.Builder canSkipUntil(double canSkipUntil) {
    this.canSkipUntil = canSkipUntil;
    return (ServerControl.Builder) this;
  }

  /**
   * Initializes the optional value {@link ServerControl#canSkipUntil() canSkipUntil} to canSkipUntil.
   * @param canSkipUntil The value for canSkipUntil
   * @return {@code this} builder for use in a chained invocation
   */
  public final ServerControl.Builder canSkipUntil(Optional<Double> canSkipUntil) {
    this.canSkipUntil = canSkipUntil.orElse(null);
    return (ServerControl.Builder) this;
  }

  /**
   * Initializes the optional value {@link ServerControl#canSkipDateRanges() canSkipDateRanges} to canSkipDateRanges.
   * @param canSkipDateRanges The value for canSkipDateRanges
   * @return {@code this} builder for chained invocation
   */
  public final ServerControl.Builder canSkipDateRanges(boolean canSkipDateRanges) {
    this.canSkipDateRanges = canSkipDateRanges;
    return (ServerControl.Builder) this;
  }

  /**
   * Initializes the optional value {@link ServerControl#canSkipDateRanges() canSkipDateRanges} to canSkipDateRanges.
   * @param canSkipDateRanges The value for canSkipDateRanges
   * @return {@code this} builder for use in a chained invocation
   */
  public final ServerControl.Builder canSkipDateRanges(Optional<Boolean> canSkipDateRanges) {
    this.canSkipDateRanges = canSkipDateRanges.orElse(null);
    return (ServerControl.Builder) this;
  }

  /**
   * Initializes the optional value {@link ServerControl#holdBack() holdBack} to holdBack.
   * @param holdBack The value for holdBack
   * @return {@code this} builder for chained invocation
   */
  public final ServerControl.Builder holdBack(double holdBack) {
    this.holdBack = holdBack;
    return (ServerControl.Builder) this;
  }

  /**
   * Initializes the optional value {@link ServerControl#holdBack() holdBack} to holdBack.
   * @param holdBack The value for holdBack
   * @return {@code this} builder for use in a chained invocation
   */
  public final ServerControl.Builder holdBack(Optional<Double> holdBack) {
    this.holdBack = holdBack.orElse(null);
    return (ServerControl.Builder) this;
  }

  /**
   * Initializes the optional value {@link ServerControl#partHoldBack() partHoldBack} to partHoldBack.
   * @param partHoldBack The value for partHoldBack
   * @return {@code this} builder for chained invocation
   */
  public final ServerControl.Builder partHoldBack(double partHoldBack) {
    this.partHoldBack = partHoldBack;
    return (ServerControl.Builder) this;
  }

  /**
   * Initializes the optional value {@link ServerControl#partHoldBack() partHoldBack} to partHoldBack.
   * @param partHoldBack The value for partHoldBack
   * @return {@code this} builder for use in a chained invocation
   */
  public final ServerControl.Builder partHoldBack(Optional<Double> partHoldBack) {
    this.partHoldBack = partHoldBack.orElse(null);
    return (ServerControl.Builder) this;
  }

  /**
   * Initializes the value for the {@link ServerControl#canBlockReload() canBlockReload} attribute.
   * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link ServerControl#canBlockReload() canBlockReload}.</em>
   * @param canBlockReload The value for canBlockReload 
   * @return {@code this} builder for use in a chained invocation
   */
  public final ServerControl.Builder canBlockReload(boolean canBlockReload) {
    this.canBlockReload = canBlockReload;
    optBits |= OPT_BIT_CAN_BLOCK_RELOAD;
    return (ServerControl.Builder) this;
  }

  /**
   * Builds a new {@link ServerControl ServerControl}.
   * @return An immutable instance of ServerControl
   * @throws IllegalStateException if any required attributes are missing
   */
  public ServerControl build() {
    return new ImmutableServerControl(this);
  }

  private boolean canBlockReloadIsSet() {
    return (optBits & OPT_BIT_CAN_BLOCK_RELOAD) != 0;
  }

  /**
   * Immutable implementation of {@link ServerControl}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new ServerControl.Builder()}.
   */
  private static final class ImmutableServerControl implements ServerControl {
    private final Double canSkipUntil;
    private final Boolean canSkipDateRanges;
    private final Double holdBack;
    private final Double partHoldBack;
    private final boolean canBlockReload;

    private ImmutableServerControl(ServerControlBuilder builder) {
      this.canSkipUntil = builder.canSkipUntil;
      this.canSkipDateRanges = builder.canSkipDateRanges;
      this.holdBack = builder.holdBack;
      this.partHoldBack = builder.partHoldBack;
      this.canBlockReload = builder.canBlockReloadIsSet()
          ? builder.canBlockReload
          : ServerControl.super.canBlockReload();
    }

    /**
     * @return The value of the {@code canSkipUntil} attribute
     */
    @Override
    public Optional<Double> canSkipUntil() {
      return Optional.ofNullable(canSkipUntil);
    }

    /**
     * @return The value of the {@code canSkipDateRanges} attribute
     */
    @Override
    public Optional<Boolean> canSkipDateRanges() {
      return Optional.ofNullable(canSkipDateRanges);
    }

    /**
     * @return The value of the {@code holdBack} attribute
     */
    @Override
    public Optional<Double> holdBack() {
      return Optional.ofNullable(holdBack);
    }

    /**
     * @return The value of the {@code partHoldBack} attribute
     */
    @Override
    public Optional<Double> partHoldBack() {
      return Optional.ofNullable(partHoldBack);
    }

    /**
     * @return The value of the {@code canBlockReload} attribute
     */
    @Override
    public boolean canBlockReload() {
      return canBlockReload;
    }

    /**
     * This instance is equal to all instances of {@code ImmutableServerControl} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutableServerControl
          && equalTo((ImmutableServerControl) another);
    }

    private boolean equalTo(ImmutableServerControl another) {
      return Objects.equals(canSkipUntil, another.canSkipUntil)
          && Objects.equals(canSkipDateRanges, another.canSkipDateRanges)
          && Objects.equals(holdBack, another.holdBack)
          && Objects.equals(partHoldBack, another.partHoldBack)
          && canBlockReload == another.canBlockReload;
    }

    /**
     * Computes a hash code from attributes: {@code canSkipUntil}, {@code canSkipDateRanges}, {@code holdBack}, {@code partHoldBack}, {@code canBlockReload}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + Objects.hashCode(canSkipUntil);
      h += (h << 5) + Objects.hashCode(canSkipDateRanges);
      h += (h << 5) + Objects.hashCode(holdBack);
      h += (h << 5) + Objects.hashCode(partHoldBack);
      h += (h << 5) + Boolean.hashCode(canBlockReload);
      return h;
    }

    /**
     * Prints the immutable value {@code ServerControl} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("ServerControl{");
      if (canSkipUntil != null) {
        builder.append("canSkipUntil=").append(canSkipUntil);
      }
      if (canSkipDateRanges != null) {
        if (builder.length() > 14) builder.append(", ");
        builder.append("canSkipDateRanges=").append(canSkipDateRanges);
      }
      if (holdBack != null) {
        if (builder.length() > 14) builder.append(", ");
        builder.append("holdBack=").append(holdBack);
      }
      if (partHoldBack != null) {
        if (builder.length() > 14) builder.append(", ");
        builder.append("partHoldBack=").append(partHoldBack);
      }
      if (builder.length() > 14) builder.append(", ");
      builder.append("canBlockReload=").append(canBlockReload);
      return builder.append("}").toString();
    }
  }
}
