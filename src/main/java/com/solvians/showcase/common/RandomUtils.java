package com.solvians.showcase.common;

import java.util.concurrent.ThreadLocalRandom;

public final class RandomUtils {

  /**
   * The README's ranges are inclusive at both ends, but {@link ThreadLocalRandom#nextInt(int, int)}
   * excludes its upper bound. This wraps the {@code + 1} once, rather than repeating it at every
   * call site.
   *
   * @param minInclusive lowest value that may be returned
   * @param maxInclusive highest value that may be returned
   * @return a random value in {@code [minInclusive, maxInclusive]}
   */
  public static int nextIntInclusive(int minInclusive, int maxInclusive) {
    if (minInclusive > maxInclusive) {
      throw new IllegalArgumentException(
          "minInclusive " + minInclusive + " must not exceed maxInclusive " + maxInclusive);
    }
    return ThreadLocalRandom.current().nextInt(minInclusive, maxInclusive + 1);
  }

  /**
   * @param alphabet the characters to choose from
   * @return one randomly chosen character
   */
  public static char randomCharacterFrom(String alphabet) {
    if (alphabet == null || alphabet.isEmpty()) {
      throw new IllegalArgumentException("alphabet must not be null or empty");
    }
    return alphabet.charAt(ThreadLocalRandom.current().nextInt(alphabet.length()));
  }

  private RandomUtils() {
    throw new AssertionError("RandomUtils is not instantiable");
  }
}
