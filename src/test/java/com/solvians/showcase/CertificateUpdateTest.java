package com.solvians.showcase;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CertificateUpdateTest {
  @Test
  void randomIsPopulated() {
    CertificateUpdate c = CertificateUpdate.random(new IsinGenerator());
    assertTrue(IsinGenerator.isValid(c.getIsin()));
    assertTrue(c.getTimestamp() > 0);
  }
}
