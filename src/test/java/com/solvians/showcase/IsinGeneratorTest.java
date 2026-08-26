package com.solvians.showcase;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IsinGeneratorTest {
  @Test
  void checkDigitMatchesWorkedExample() {
    assertEquals(6, IsinGenerator.checkDigit("DE123456789"));
  }

  @Test
  void zeroCheckDigitWorks() {
    assertEquals(0, IsinGenerator.checkDigit("AG000000000"));
  }

  @Test
  void generatedIsinIsValid() {
    for (int i = 0; i < 1000; i++)
      assertTrue(IsinGenerator.isValid(new IsinGenerator().generate()));
  }
}
