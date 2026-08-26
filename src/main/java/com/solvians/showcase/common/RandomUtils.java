package com.solvians.showcase.common;

import java.util.concurrent.ThreadLocalRandom;

/** Random helpers. */
public final class RandomUtils {
  private RandomUtils() {
    throw new AssertionError("No instances");
  }

  /** Returns an inclusive random integer. */
  public static int nextIntInclusive(int minInclusive, int maxInclusive) {
    if (minInclusive > maxInclusive) throw new IllegalArgumentException("min must not exceed max");
    return ThreadLocalRandom.current().nextInt(minInclusive, maxInclusive + 1);
  }

  /** Selects a random character. */
  public static char randomCharacterFrom(String alphabet) {
    if (alphabet == null || alphabet.isEmpty())
      throw new IllegalArgumentException("alphabet must not be null or empty");
    return alphabet.charAt(ThreadLocalRandom.current().nextInt(alphabet.length()));
  }
}
