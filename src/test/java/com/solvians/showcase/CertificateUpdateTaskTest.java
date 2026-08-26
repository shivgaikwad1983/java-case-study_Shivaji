package com.solvians.showcase;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CertificateUpdateTaskTest {
  @Test
  void callProducesSixFields() throws Exception {
    assertEquals(6, new CertificateUpdateTask().call().split(",").length);
  }
}
