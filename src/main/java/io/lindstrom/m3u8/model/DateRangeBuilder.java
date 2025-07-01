package io.lindstrom.m3u8.model;

import java.time.OffsetDateTime;
import java.util.*;

/**
 * Builds instances of type {@link DateRange DateRange}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code DateRangeBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})
class DateRangeBuilder {
  private static final long INIT_BIT_ID = 0x1L;
  private static final long INIT_BIT_START_DATE = 0x2L;
  private static final long OPT_BIT_END_ON_NEXT = 0x1L;
  private long initBits = 0x3L;
  private long optBits;

  private String id;
  private String classAttribute;
  private OffsetDateTime startDate;
  private OffsetDateTime endDate;
  private Double duration;
  private Double plannedDuration;
  private Map<String, String> clientAttributes = new LinkedHashMap<String, String>();
  private String scte35Cmd;
  private String scte35Out;
  private String scte35In;
  private boolean endOnNext;

  /**
   * Creates a builder for {@link DateRange DateRange} instances.
   */
  DateRangeBuilder() {
    if (!(this instanceof DateRange.Builder)) {
      throw new UnsupportedOperationException("Use: new DateRange.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code DateRange} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * Collection elements and entries will be added, not replaced.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final DateRange.Builder from(DateRange instance) {
    Objects.requireNonNull(instance, "instance");
    id(instance.id());
    Optional<String> classAttributeOptional = instance.classAttribute();
    if (classAttributeOptional.isPresent()) {
      classAttribute(classAttributeOptional);
    }
    startDate(instance.startDate());
    Optional<OffsetDateTime> endDateOptional = instance.endDate();
    if (endDateOptional.isPresent()) {
      endDate(endDateOptional);
    }
    Optional<Double> durationOptional = instance.duration();
    if (durationOptional.isPresent()) {
      duration(durationOptional);
    }
    Optional<Double> plannedDurationOptional = instance.plannedDuration();
    if (plannedDurationOptional.isPresent()) {
      plannedDuration(plannedDurationOptional);
    }
    putAllClientAttributes(instance.clientAttributes());
    Optional<String> scte35CmdOptional = instance.scte35Cmd();
    if (scte35CmdOptional.isPresent()) {
      scte35Cmd(scte35CmdOptional);
    }
    Optional<String> scte35OutOptional = instance.scte35Out();
    if (scte35OutOptional.isPresent()) {
      scte35Out(scte35OutOptional);
    }
    Optional<String> scte35InOptional = instance.scte35In();
    if (scte35InOptional.isPresent()) {
      scte35In(scte35InOptional);
    }
    endOnNext(instance.endOnNext());
    return (DateRange.Builder) this;
  }

  /**
   * Initializes the value for the {@link DateRange#id() id} attribute.
   * @param id The value for id 
   * @return {@code this} builder for use in a chained invocation
   */
  public final DateRange.Builder id(String id) {
    this.id = Objects.requireNonNull(id, "id");
    initBits &= ~INIT_BIT_ID;
    return (DateRange.Builder) this;
  }

  /**
   * Initializes the optional value {@link DateRange#classAttribute() classAttribute} to classAttribute.
   * @param classAttribute The value for classAttribute
   * @return {@code this} builder for chained invocation
   */
  public final DateRange.Builder classAttribute(String classAttribute) {
    this.classAttribute = Objects.requireNonNull(classAttribute, "classAttribute");
    return (DateRange.Builder) this;
  }

  /**
   * Initializes the optional value {@link DateRange#classAttribute() classAttribute} to classAttribute.
   * @param classAttribute The value for classAttribute
   * @return {@code this} builder for use in a chained invocation
   */
  public final DateRange.Builder classAttribute(Optional<String> classAttribute) {
    this.classAttribute = classAttribute.orElse(null);
    return (DateRange.Builder) this;
  }

  /**
   * Initializes the value for the {@link DateRange#startDate() startDate} attribute.
   * @param startDate The value for startDate 
   * @return {@code this} builder for use in a chained invocation
   */
  public final DateRange.Builder startDate(OffsetDateTime startDate) {
    this.startDate = Objects.requireNonNull(startDate, "startDate");
    initBits &= ~INIT_BIT_START_DATE;
    return (DateRange.Builder) this;
  }

  /**
   * Initializes the optional value {@link DateRange#endDate() endDate} to endDate.
   * @param endDate The value for endDate
   * @return {@code this} builder for chained invocation
   */
  public final DateRange.Builder endDate(OffsetDateTime endDate) {
    this.endDate = Objects.requireNonNull(endDate, "endDate");
    return (DateRange.Builder) this;
  }

  /**
   * Initializes the optional value {@link DateRange#endDate() endDate} to endDate.
   * @param endDate The value for endDate
   * @return {@code this} builder for use in a chained invocation
   */
  public final DateRange.Builder endDate(Optional<? extends OffsetDateTime> endDate) {
    this.endDate = endDate.orElse(null);
    return (DateRange.Builder) this;
  }

  /**
   * Initializes the optional value {@link DateRange#duration() duration} to duration.
   * @param duration The value for duration
   * @return {@code this} builder for chained invocation
   */
  public final DateRange.Builder duration(double duration) {
    this.duration = duration;
    return (DateRange.Builder) this;
  }

  /**
   * Initializes the optional value {@link DateRange#duration() duration} to duration.
   * @param duration The value for duration
   * @return {@code this} builder for use in a chained invocation
   */
  public final DateRange.Builder duration(Optional<Double> duration) {
    this.duration = duration.orElse(null);
    return (DateRange.Builder) this;
  }

  /**
   * Initializes the optional value {@link DateRange#plannedDuration() plannedDuration} to plannedDuration.
   * @param plannedDuration The value for plannedDuration
   * @return {@code this} builder for chained invocation
   */
  public final DateRange.Builder plannedDuration(double plannedDuration) {
    this.plannedDuration = plannedDuration;
    return (DateRange.Builder) this;
  }

  /**
   * Initializes the optional value {@link DateRange#plannedDuration() plannedDuration} to plannedDuration.
   * @param plannedDuration The value for plannedDuration
   * @return {@code this} builder for use in a chained invocation
   */
  public final DateRange.Builder plannedDuration(Optional<Double> plannedDuration) {
    this.plannedDuration = plannedDuration.orElse(null);
    return (DateRange.Builder) this;
  }

  /**
   * Put one entry to the {@link DateRange#clientAttributes() clientAttributes} map.
   * @param key The key in the clientAttributes map
   * @param value The associated value in the clientAttributes map
   * @return {@code this} builder for use in a chained invocation
   */
  public final DateRange.Builder putClientAttributes(String key, String value) {
    this.clientAttributes.put(
        Objects.requireNonNull(key, "clientAttributes key"),
        Objects.requireNonNull(value, "clientAttributes value"));
    return (DateRange.Builder) this;
  }

  /**
   * Put one entry to the {@link DateRange#clientAttributes() clientAttributes} map. Nulls are not permitted
   * @param entry The key and value entry
   * @return {@code this} builder for use in a chained invocation
   */
  public final DateRange.Builder putClientAttributes(Map.Entry<String, ? extends String> entry) {
    String k = entry.getKey();
    String v = entry.getValue();
    this.clientAttributes.put(
        Objects.requireNonNull(k, "clientAttributes key"),
        Objects.requireNonNull(v, "clientAttributes value"));
    return (DateRange.Builder) this;
  }

  /**
   * Sets or replaces all mappings from the specified map as entries for the {@link DateRange#clientAttributes() clientAttributes} map. Nulls are not permitted
   * @param clientAttributes The entries that will be added to the clientAttributes map
   * @return {@code this} builder for use in a chained invocation
   */
  public final DateRange.Builder clientAttributes(Map<String, ? extends String> clientAttributes) {
    this.clientAttributes.clear();
    return putAllClientAttributes(clientAttributes);
  }

  /**
   * Put all mappings from the specified map as entries to {@link DateRange#clientAttributes() clientAttributes} map. Nulls are not permitted
   * @param clientAttributes The entries that will be added to the clientAttributes map
   * @return {@code this} builder for use in a chained invocation
   */
  public final DateRange.Builder putAllClientAttributes(Map<String, ? extends String> clientAttributes) {
    for (Map.Entry<String, ? extends String> entry : clientAttributes.entrySet()) {
      String k = entry.getKey();
      String v = entry.getValue();
      this.clientAttributes.put(
          Objects.requireNonNull(k, "clientAttributes key"),
          Objects.requireNonNull(v, "clientAttributes value"));
    }
    return (DateRange.Builder) this;
  }

  /**
   * Initializes the optional value {@link DateRange#scte35Cmd() scte35Cmd} to scte35Cmd.
   * @param scte35Cmd The value for scte35Cmd
   * @return {@code this} builder for chained invocation
   */
  public final DateRange.Builder scte35Cmd(String scte35Cmd) {
    this.scte35Cmd = Objects.requireNonNull(scte35Cmd, "scte35Cmd");
    return (DateRange.Builder) this;
  }

  /**
   * Initializes the optional value {@link DateRange#scte35Cmd() scte35Cmd} to scte35Cmd.
   * @param scte35Cmd The value for scte35Cmd
   * @return {@code this} builder for use in a chained invocation
   */
  public final DateRange.Builder scte35Cmd(Optional<String> scte35Cmd) {
    this.scte35Cmd = scte35Cmd.orElse(null);
    return (DateRange.Builder) this;
  }

  /**
   * Initializes the optional value {@link DateRange#scte35Out() scte35Out} to scte35Out.
   * @param scte35Out The value for scte35Out
   * @return {@code this} builder for chained invocation
   */
  public final DateRange.Builder scte35Out(String scte35Out) {
    this.scte35Out = Objects.requireNonNull(scte35Out, "scte35Out");
    return (DateRange.Builder) this;
  }

  /**
   * Initializes the optional value {@link DateRange#scte35Out() scte35Out} to scte35Out.
   * @param scte35Out The value for scte35Out
   * @return {@code this} builder for use in a chained invocation
   */
  public final DateRange.Builder scte35Out(Optional<String> scte35Out) {
    this.scte35Out = scte35Out.orElse(null);
    return (DateRange.Builder) this;
  }

  /**
   * Initializes the optional value {@link DateRange#scte35In() scte35In} to scte35In.
   * @param scte35In The value for scte35In
   * @return {@code this} builder for chained invocation
   */
  public final DateRange.Builder scte35In(String scte35In) {
    this.scte35In = Objects.requireNonNull(scte35In, "scte35In");
    return (DateRange.Builder) this;
  }

  /**
   * Initializes the optional value {@link DateRange#scte35In() scte35In} to scte35In.
   * @param scte35In The value for scte35In
   * @return {@code this} builder for use in a chained invocation
   */
  public final DateRange.Builder scte35In(Optional<String> scte35In) {
    this.scte35In = scte35In.orElse(null);
    return (DateRange.Builder) this;
  }

  /**
   * Initializes the value for the {@link DateRange#endOnNext() endOnNext} attribute.
   * <p><em>If not set, this attribute will have a default value as returned by the initializer of {@link DateRange#endOnNext() endOnNext}.</em>
   * @param endOnNext The value for endOnNext 
   * @return {@code this} builder for use in a chained invocation
   */
  public final DateRange.Builder endOnNext(boolean endOnNext) {
    this.endOnNext = endOnNext;
    optBits |= OPT_BIT_END_ON_NEXT;
    return (DateRange.Builder) this;
  }

  /**
   * Builds a new {@link DateRange DateRange}.
   * @return An immutable instance of DateRange
   * @throws IllegalStateException if any required attributes are missing
   */
  public DateRange build() {
    if (initBits != 0) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
    return new ImmutableDateRange(this);
  }

  private boolean endOnNextIsSet() {
    return (optBits & OPT_BIT_END_ON_NEXT) != 0;
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if ((initBits & INIT_BIT_ID) != 0) attributes.add("id");
    if ((initBits & INIT_BIT_START_DATE) != 0) attributes.add("startDate");
    return "Cannot build DateRange, some of required attributes are not set " + attributes;
  }

  /**
   * Immutable implementation of {@link DateRange}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new DateRange.Builder()}.
   */
  private static final class ImmutableDateRange implements DateRange {
    private final String id;
    private final String classAttribute;
    private final OffsetDateTime startDate;
    private final OffsetDateTime endDate;
    private final Double duration;
    private final Double plannedDuration;
    private final Map<String, String> clientAttributes;
    private final String scte35Cmd;
    private final String scte35Out;
    private final String scte35In;
    private final boolean endOnNext;

    private ImmutableDateRange(DateRangeBuilder builder) {
      this.id = builder.id;
      this.classAttribute = builder.classAttribute;
      this.startDate = builder.startDate;
      this.endDate = builder.endDate;
      this.duration = builder.duration;
      this.plannedDuration = builder.plannedDuration;
      this.clientAttributes = createUnmodifiableMap(false, false, builder.clientAttributes);
      this.scte35Cmd = builder.scte35Cmd;
      this.scte35Out = builder.scte35Out;
      this.scte35In = builder.scte35In;
      this.endOnNext = builder.endOnNextIsSet()
          ? builder.endOnNext
          : DateRange.super.endOnNext();
    }

    /**
     * @return The value of the {@code id} attribute
     */
    @Override
    public String id() {
      return id;
    }

    /**
     * @return The value of the {@code classAttribute} attribute
     */
    @Override
    public Optional<String> classAttribute() {
      return Optional.ofNullable(classAttribute);
    }

    /**
     * @return The value of the {@code startDate} attribute
     */
    @Override
    public OffsetDateTime startDate() {
      return startDate;
    }

    /**
     * @return The value of the {@code endDate} attribute
     */
    @Override
    public Optional<OffsetDateTime> endDate() {
      return Optional.ofNullable(endDate);
    }

    /**
     * @return The value of the {@code duration} attribute
     */
    @Override
    public Optional<Double> duration() {
      return Optional.ofNullable(duration);
    }

    /**
     * @return The value of the {@code plannedDuration} attribute
     */
    @Override
    public Optional<Double> plannedDuration() {
      return Optional.ofNullable(plannedDuration);
    }

    /**
     * @return The value of the {@code clientAttributes} attribute
     */
    @Override
    public Map<String, String> clientAttributes() {
      return clientAttributes;
    }

    /**
     * @return The value of the {@code scte35Cmd} attribute
     */
    @Override
    public Optional<String> scte35Cmd() {
      return Optional.ofNullable(scte35Cmd);
    }

    /**
     * @return The value of the {@code scte35Out} attribute
     */
    @Override
    public Optional<String> scte35Out() {
      return Optional.ofNullable(scte35Out);
    }

    /**
     * @return The value of the {@code scte35In} attribute
     */
    @Override
    public Optional<String> scte35In() {
      return Optional.ofNullable(scte35In);
    }

    /**
     * @return The value of the {@code endOnNext} attribute
     */
    @Override
    public boolean endOnNext() {
      return endOnNext;
    }

    /**
     * This instance is equal to all instances of {@code ImmutableDateRange} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutableDateRange
          && equalTo((ImmutableDateRange) another);
    }

    private boolean equalTo(ImmutableDateRange another) {
      return id.equals(another.id)
          && Objects.equals(classAttribute, another.classAttribute)
          && startDate.equals(another.startDate)
          && Objects.equals(endDate, another.endDate)
          && Objects.equals(duration, another.duration)
          && Objects.equals(plannedDuration, another.plannedDuration)
          && clientAttributes.equals(another.clientAttributes)
          && Objects.equals(scte35Cmd, another.scte35Cmd)
          && Objects.equals(scte35Out, another.scte35Out)
          && Objects.equals(scte35In, another.scte35In)
          && endOnNext == another.endOnNext;
    }

    /**
     * Computes a hash code from attributes: {@code id}, {@code classAttribute}, {@code startDate}, {@code endDate}, {@code duration}, {@code plannedDuration}, {@code clientAttributes}, {@code scte35Cmd}, {@code scte35Out}, {@code scte35In}, {@code endOnNext}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + id.hashCode();
      h += (h << 5) + Objects.hashCode(classAttribute);
      h += (h << 5) + startDate.hashCode();
      h += (h << 5) + Objects.hashCode(endDate);
      h += (h << 5) + Objects.hashCode(duration);
      h += (h << 5) + Objects.hashCode(plannedDuration);
      h += (h << 5) + clientAttributes.hashCode();
      h += (h << 5) + Objects.hashCode(scte35Cmd);
      h += (h << 5) + Objects.hashCode(scte35Out);
      h += (h << 5) + Objects.hashCode(scte35In);
      h += (h << 5) + Boolean.hashCode(endOnNext);
      return h;
    }

    /**
     * Prints the immutable value {@code DateRange} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("DateRange{");
      builder.append("id=").append(id);
      if (classAttribute != null) {
        builder.append(", ");
        builder.append("classAttribute=").append(classAttribute);
      }
      builder.append(", ");
      builder.append("startDate=").append(startDate);
      if (endDate != null) {
        builder.append(", ");
        builder.append("endDate=").append(endDate);
      }
      if (duration != null) {
        builder.append(", ");
        builder.append("duration=").append(duration);
      }
      if (plannedDuration != null) {
        builder.append(", ");
        builder.append("plannedDuration=").append(plannedDuration);
      }
      builder.append(", ");
      builder.append("clientAttributes=").append(clientAttributes);
      if (scte35Cmd != null) {
        builder.append(", ");
        builder.append("scte35Cmd=").append(scte35Cmd);
      }
      if (scte35Out != null) {
        builder.append(", ");
        builder.append("scte35Out=").append(scte35Out);
      }
      if (scte35In != null) {
        builder.append(", ");
        builder.append("scte35In=").append(scte35In);
      }
      builder.append(", ");
      builder.append("endOnNext=").append(endOnNext);
      return builder.append("}").toString();
    }
  }

  private static <K, V> Map<K, V> createUnmodifiableMap(boolean checkNulls, boolean skipNulls, Map<? extends K, ? extends V> map) {
    switch (map.size()) {
    case 0: return Collections.emptyMap();
    case 1: {
      Map.Entry<? extends K, ? extends V> e = map.entrySet().iterator().next();
      K k = e.getKey();
      V v = e.getValue();
      if (checkNulls) {
        Objects.requireNonNull(k, "key");
        Objects.requireNonNull(v, "value");
      }
      if (skipNulls && (k == null || v == null)) {
        return Collections.emptyMap();
      }
      return Collections.singletonMap(k, v);
    }
    default: {
      Map<K, V> linkedMap = new LinkedHashMap<K, V>(map.size());
      if (skipNulls || checkNulls) {
        for (Map.Entry<? extends K, ? extends V> e : map.entrySet()) {
          K k = e.getKey();
          V v = e.getValue();
          if (skipNulls) {
            if (k == null || v == null) continue;
          } else if (checkNulls) {
            Objects.requireNonNull(k, "key");
            Objects.requireNonNull(v, "value");
          }
          linkedMap.put(k, v);
        }
      } else {
        linkedMap.putAll(map);
      }
      return Collections.unmodifiableMap(linkedMap);
    }
    }
  }
}
