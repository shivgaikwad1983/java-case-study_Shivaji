package com.solvians.showcase.common;

public final class Validations {

  /**
   * @param value the value to check
   * @param name the parameter name, used in the failure message
   * @return {@code value}, unchanged
   * @throws IllegalArgumentException if {@code value} is zero or negative
   */
  public static int requirePositive(int value, String name) {
    if (value <= 0) {
      throw new IllegalArgumentException(name + " must be positive, but was: " + value);
    }
    return value;
  }

  /**
   * @param value the value to check
   * @param name the parameter name, used in the failure message
   * @return {@code value}, unchanged
   * @throws IllegalArgumentException if {@code value} is negative
   */
  public static int requireNonNegative(int value, String name) {
    if (value < 0) {
      throw new IllegalArgumentException(name + " must not be negative, but was: " + value);
    }
    return value;
  }

  /**
   * Multiplies two counts, turning a silent {@code int} overflow into a clear failure.
   *
   * @return {@code first * second}
   * @throws IllegalArgumentException if the product overflows an {@code int}
   */
  public static int multiplyWithoutOverflow(
      int first, String firstName, int second, String secondName) {
    try {
      return Math.multiplyExact(first, second);
    } catch (ArithmeticException e) {
      throw new IllegalArgumentException(
          firstName
              + " ("
              + first
              + ") multiplied by "
              + secondName
              + " ("
              + second
              + ") exceeds the maximum supported number of updates ("
              + Integer.MAX_VALUE
              + ")",
          e);
    }
  }

  private Validations() {
    throw new AssertionError("Validations is not instantiable");
  }
}
