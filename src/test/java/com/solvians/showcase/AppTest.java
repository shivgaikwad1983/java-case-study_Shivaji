package com.solvians.showcase;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AppTest {
  @Test
  void oneArgumentFails() {
    assertThrows(RuntimeException.class, () -> App.main(new String[] {"1"}));
  }

  @Test
  void nonNumericSurfacesRawNumberFormat() {
    NumberFormatException e =
        assertThrows(NumberFormatException.class, () -> App.main(new String[] {"10", "zzz"}));
    assertEquals("For input string: \"zzz\"", e.getMessage());
  }
}
