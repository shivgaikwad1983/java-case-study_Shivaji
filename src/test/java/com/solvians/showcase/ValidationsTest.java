package com.solvians.showcase;

import static org.junit.jupiter.api.Assertions.*;

import com.solvians.showcase.common.Validations;
import org.junit.jupiter.api.Test;

class ValidationsTest {
  @Test
  void validatesCounts() {
    assertThrows(IllegalArgumentException.class, () -> Validations.requirePositive(0, "x"));
    assertThrows(IllegalArgumentException.class, () -> Validations.requireNonNegative(-1, "x"));
  }
}
