package com.solvians.showcase;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CertificateUpdateGeneratorTest {
  @Test
  void generatesRequestedCount() {
    assertEquals(1000, new CertificateUpdateGenerator(10, 100).generateQuotes().count());
  }

  @Test
  void zeroQuotesWorks() {
    assertEquals(0, new CertificateUpdateGenerator(4, 0).generateQuotes().count());
  }
}
