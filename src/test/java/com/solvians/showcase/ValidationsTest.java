package com.solvians.showcase;

import static org.junit.jupiter.api.Assertions.*;

import com.solvians.showcase.common.Validations;
import org.junit.jupiter.api.Test;

class ValidationsTest {

  @ParameterizedTest(name = "requirePositive({0}) passes")
  @ValueSource(ints = {1, 2, 1_000, Integer.MAX_VALUE})
  public void requirePositiveAcceptsPositiveValues(int value) {
    assertEquals(value, Validations.requirePositive(value, "threads"));
  }

  @ParameterizedTest(name = "requirePositive({0}) throws")
  @ValueSource(ints = {0, -1, Integer.MIN_VALUE})
  public void requirePositiveRejectsZeroAndNegatives(int value) {
    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class, () -> Validations.requirePositive(value, "threads"));

    assertTrue(thrown.getMessage().contains("threads"), thrown.getMessage());
  }

  @ParameterizedTest(name = "requireNonNegative({0}) passes")
  @ValueSource(ints = {0, 1, Integer.MAX_VALUE})
  public void requireNonNegativeAcceptsZeroAndAbove(int value) {
    assertEquals(value, Validations.requireNonNegative(value, "quotes"));
  }

  @ParameterizedTest(name = "requireNonNegative({0}) throws")
  @ValueSource(ints = {-1, Integer.MIN_VALUE})
  public void requireNonNegativeRejectsNegatives(int value) {
    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class, () -> Validations.requireNonNegative(value, "quotes"));

    assertTrue(thrown.getMessage().contains("quotes"), thrown.getMessage());
  }

  @Test
  public void multiplyReturnsTheProductWhenItFits() {
    assertEquals(1_000, Validations.multiplyWithoutOverflow(10, "threads", 100, "quotes"));
    assertEquals(0, Validations.multiplyWithoutOverflow(10, "threads", 0, "quotes"));
  }

  @Test
  public void multiplyReportsOverflowAsAnIllegalArgument() {
    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class,
            () -> Validations.multiplyWithoutOverflow(100_000, "threads", 100_000, "quotes"));

    assertTrue(thrown.getMessage().contains("exceeds the maximum"), thrown.getMessage());
    assertTrue(thrown.getCause() instanceof ArithmeticException, "cause should be preserved");
  }
}
