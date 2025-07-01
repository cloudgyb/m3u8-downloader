package io.lindstrom.m3u8.model;

import java.util.*;

/**
 * Builds instances of type {@link Channels Channels}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code ChannelsBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})
class ChannelsBuilder {
  private static final long INIT_BIT_COUNT = 0x1L;
  private long initBits = 0x1L;

  private int count;
  private List<String> objectCodingIdentifiers = new ArrayList<String>();

  /**
   * Creates a builder for {@link Channels Channels} instances.
   */
  ChannelsBuilder() {
    if (!(this instanceof Channels.Builder)) {
      throw new UnsupportedOperationException("Use: new Channels.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code Channels} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * Collection elements and entries will be added, not replaced.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final Channels.Builder from(Channels instance) {
    Objects.requireNonNull(instance, "instance");
    count(instance.count());
    addAllObjectCodingIdentifiers(instance.objectCodingIdentifiers());
    return (Channels.Builder) this;
  }

  /**
   * Initializes the value for the {@link Channels#count() count} attribute.
   * @param count The value for count 
   * @return {@code this} builder for use in a chained invocation
   */
  public final Channels.Builder count(int count) {
    this.count = count;
    initBits &= ~INIT_BIT_COUNT;
    return (Channels.Builder) this;
  }

  /**
   * Adds one element to {@link Channels#objectCodingIdentifiers() objectCodingIdentifiers} list.
   * @param element A objectCodingIdentifiers element
   * @return {@code this} builder for use in a chained invocation
   */
  public final Channels.Builder addObjectCodingIdentifiers(String element) {
    this.objectCodingIdentifiers.add(Objects.requireNonNull(element, "objectCodingIdentifiers element"));
    return (Channels.Builder) this;
  }

  /**
   * Adds elements to {@link Channels#objectCodingIdentifiers() objectCodingIdentifiers} list.
   * @param elements An array of objectCodingIdentifiers elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final Channels.Builder addObjectCodingIdentifiers(String... elements) {
    for (String element : elements) {
      this.objectCodingIdentifiers.add(Objects.requireNonNull(element, "objectCodingIdentifiers element"));
    }
    return (Channels.Builder) this;
  }

  /**
   * Sets or replaces all elements for {@link Channels#objectCodingIdentifiers() objectCodingIdentifiers} list.
   * @param elements An iterable of objectCodingIdentifiers elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final Channels.Builder objectCodingIdentifiers(Iterable<String> elements) {
    this.objectCodingIdentifiers.clear();
    return addAllObjectCodingIdentifiers(elements);
  }

  /**
   * Adds elements to {@link Channels#objectCodingIdentifiers() objectCodingIdentifiers} list.
   * @param elements An iterable of objectCodingIdentifiers elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final Channels.Builder addAllObjectCodingIdentifiers(Iterable<String> elements) {
    for (String element : elements) {
      this.objectCodingIdentifiers.add(Objects.requireNonNull(element, "objectCodingIdentifiers element"));
    }
    return (Channels.Builder) this;
  }

  /**
   * Builds a new {@link Channels Channels}.
   * @return An immutable instance of Channels
   * @throws IllegalStateException if any required attributes are missing
   */
  public Channels build() {
    if (initBits != 0) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
    return new ImmutableChannels(this);
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if ((initBits & INIT_BIT_COUNT) != 0) attributes.add("count");
    return "Cannot build Channels, some of required attributes are not set " + attributes;
  }

  /**
   * Immutable implementation of {@link Channels}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new Channels.Builder()}.
   */
  private static final class ImmutableChannels implements Channels {
    private final int count;
    private final List<String> objectCodingIdentifiers;

    private ImmutableChannels(ChannelsBuilder builder) {
      this.count = builder.count;
      this.objectCodingIdentifiers = createUnmodifiableList(true, builder.objectCodingIdentifiers);
    }

    /**
     * @return The value of the {@code count} attribute
     */
    @Override
    public int count() {
      return count;
    }

    /**
     * @return The value of the {@code objectCodingIdentifiers} attribute
     */
    @Override
    public List<String> objectCodingIdentifiers() {
      return objectCodingIdentifiers;
    }

    /**
     * This instance is equal to all instances of {@code ImmutableChannels} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutableChannels
          && equalTo((ImmutableChannels) another);
    }

    private boolean equalTo(ImmutableChannels another) {
      return count == another.count
          && objectCodingIdentifiers.equals(another.objectCodingIdentifiers);
    }

    /**
     * Computes a hash code from attributes: {@code count}, {@code objectCodingIdentifiers}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + count;
      h += (h << 5) + objectCodingIdentifiers.hashCode();
      return h;
    }

    /**
     * Prints the immutable value {@code Channels} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      return "Channels{"
          + "count=" + count
          + ", objectCodingIdentifiers=" + objectCodingIdentifiers
          + "}";
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
