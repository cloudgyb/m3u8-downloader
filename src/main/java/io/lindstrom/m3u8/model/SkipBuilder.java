package io.lindstrom.m3u8.model;


import java.util.*;

/**
 * Builds instances of type {@link Skip Skip}.
 * Initialize attributes and then invoke the {@link #build()} method to create an
 * immutable instance.
 * <p><em>{@code SkipBuilder} is not thread-safe and generally should not be stored in a field or collection,
 * but instead used immediately to create instances.</em>
 */
@SuppressWarnings({"all"})

class SkipBuilder {
  private static final long INIT_BIT_SKIPPED_SEGMENTS = 0x1L;
  private long initBits = 0x1L;

  private long skippedSegments;
  private List<String> recentlyRemovedDateRanges = new ArrayList<String>();

  /**
   * Creates a builder for {@link Skip Skip} instances.
   */
  SkipBuilder() {
    if (!(this instanceof Skip.Builder)) {
      throw new UnsupportedOperationException("Use: new Skip.Builder()");
    }
  }

  /**
   * Fill a builder with attribute values from the provided {@code Skip} instance.
   * Regular attribute values will be replaced with those from the given instance.
   * Absent optional values will not replace present values.
   * Collection elements and entries will be added, not replaced.
   * @param instance The instance from which to copy values
   * @return {@code this} builder for use in a chained invocation
   */
  public final Skip.Builder from(Skip instance) {
    Objects.requireNonNull(instance, "instance");
    skippedSegments(instance.skippedSegments());
    addAllRecentlyRemovedDateRanges(instance.recentlyRemovedDateRanges());
    return (Skip.Builder) this;
  }

  /**
   * Initializes the value for the {@link Skip#skippedSegments() skippedSegments} attribute.
   * @param skippedSegments The value for skippedSegments 
   * @return {@code this} builder for use in a chained invocation
   */
  public final Skip.Builder skippedSegments(long skippedSegments) {
    this.skippedSegments = skippedSegments;
    initBits &= ~INIT_BIT_SKIPPED_SEGMENTS;
    return (Skip.Builder) this;
  }

  /**
   * Adds one element to {@link Skip#recentlyRemovedDateRanges() recentlyRemovedDateRanges} list.
   * @param element A recentlyRemovedDateRanges element
   * @return {@code this} builder for use in a chained invocation
   */
  public final Skip.Builder addRecentlyRemovedDateRanges(String element) {
    this.recentlyRemovedDateRanges.add(Objects.requireNonNull(element, "recentlyRemovedDateRanges element"));
    return (Skip.Builder) this;
  }

  /**
   * Adds elements to {@link Skip#recentlyRemovedDateRanges() recentlyRemovedDateRanges} list.
   * @param elements An array of recentlyRemovedDateRanges elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final Skip.Builder addRecentlyRemovedDateRanges(String... elements) {
    for (String element : elements) {
      this.recentlyRemovedDateRanges.add(Objects.requireNonNull(element, "recentlyRemovedDateRanges element"));
    }
    return (Skip.Builder) this;
  }

  /**
   * Sets or replaces all elements for {@link Skip#recentlyRemovedDateRanges() recentlyRemovedDateRanges} list.
   * @param elements An iterable of recentlyRemovedDateRanges elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final Skip.Builder recentlyRemovedDateRanges(Iterable<String> elements) {
    this.recentlyRemovedDateRanges.clear();
    return addAllRecentlyRemovedDateRanges(elements);
  }

  /**
   * Adds elements to {@link Skip#recentlyRemovedDateRanges() recentlyRemovedDateRanges} list.
   * @param elements An iterable of recentlyRemovedDateRanges elements
   * @return {@code this} builder for use in a chained invocation
   */
  public final Skip.Builder addAllRecentlyRemovedDateRanges(Iterable<String> elements) {
    for (String element : elements) {
      this.recentlyRemovedDateRanges.add(Objects.requireNonNull(element, "recentlyRemovedDateRanges element"));
    }
    return (Skip.Builder) this;
  }

  /**
   * Builds a new {@link Skip Skip}.
   * @return An immutable instance of Skip
   * @throws IllegalStateException if any required attributes are missing
   */
  public Skip build() {
    if (initBits != 0) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
    return new ImmutableSkip(this);
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<String>();
    if ((initBits & INIT_BIT_SKIPPED_SEGMENTS) != 0) attributes.add("skippedSegments");
    return "Cannot build Skip, some of required attributes are not set " + attributes;
  }

  /**
   * Immutable implementation of {@link Skip}.
   * <p>
   * Use the builder to create immutable instances:
   * {@code new Skip.Builder()}.
   */
  private static final class ImmutableSkip implements Skip {
    private final long skippedSegments;
    private final List<String> recentlyRemovedDateRanges;

    private ImmutableSkip(SkipBuilder builder) {
      this.skippedSegments = builder.skippedSegments;
      this.recentlyRemovedDateRanges = createUnmodifiableList(true, builder.recentlyRemovedDateRanges);
    }

    /**
     * @return The value of the {@code skippedSegments} attribute
     */
    @Override
    public long skippedSegments() {
      return skippedSegments;
    }

    /**
     * @return The value of the {@code recentlyRemovedDateRanges} attribute
     */
    @Override
    public List<String> recentlyRemovedDateRanges() {
      return recentlyRemovedDateRanges;
    }

    /**
     * This instance is equal to all instances of {@code ImmutableSkip} that have equal attribute values.
     * @return {@code true} if {@code this} is equal to {@code another} instance
     */
    @Override
    public boolean equals(Object another) {
      if (this == another) return true;
      return another instanceof ImmutableSkip
          && equalTo((ImmutableSkip) another);
    }

    private boolean equalTo(ImmutableSkip another) {
      return skippedSegments == another.skippedSegments
          && recentlyRemovedDateRanges.equals(another.recentlyRemovedDateRanges);
    }

    /**
     * Computes a hash code from attributes: {@code skippedSegments}, {@code recentlyRemovedDateRanges}.
     * @return hashCode value
     */
    @Override
    public int hashCode() {
      int h = 5381;
      h += (h << 5) + Long.hashCode(skippedSegments);
      h += (h << 5) + recentlyRemovedDateRanges.hashCode();
      return h;
    }

    /**
     * Prints the immutable value {@code Skip} with attribute values.
     * @return A string representation of the value
     */
    @Override
    public String toString() {
      return "Skip{"
          + "skippedSegments=" + skippedSegments
          + ", recentlyRemovedDateRanges=" + recentlyRemovedDateRanges
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
