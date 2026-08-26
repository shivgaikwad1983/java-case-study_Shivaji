package com.solvians.showcase;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CertificateUpdateGeneratorTest {

  @Test
  public void generateQuotes() {
    CertificateUpdateGenerator certificateUpdateGenerator = new CertificateUpdateGenerator(10, 100);
    Stream<CertificateUpdate> quotes = certificateUpdateGenerator.generateQuotes();
    assertNotNull(quotes);
    assertEquals(10 * 100, quotes.count());
  }

  @Test
  public void generatedQuotesAreFullyPopulated() {
    // The count assertion above is satisfied by blank objects too, so check the contents.
    new CertificateUpdateGenerator(4, 25)
        .generateQuotes()
        .forEach(
            update -> {
              assertNotNull(update);
              assertTrue(
                  IsinGenerator.isValid(update.getIsin()), "invalid ISIN: " + update.getIsin());
              assertTrue(update.getTimestamp() > 0, "timestamp not set");
              assertTrue(update.getBidPrice() >= 100.00 && update.getBidPrice() <= 200.00);
              assertTrue(update.getAskPrice() >= 100.00 && update.getAskPrice() <= 200.00);
              assertTrue(update.getBidSize() >= 1_000 && update.getBidSize() <= 5_000);
              assertTrue(update.getAskSize() >= 1_000 && update.getAskSize() <= 10_000);
            });
  }

  @ParameterizedTest(name = "{0} threads x {1} quotes yields {2} updates")
  @CsvSource({"1, 50, 50", "4, 0,  0", "8, 1,  8", "2, 25, 50"})
  public void generatesThreadsTimesQuotesUpdates(int threads, int quotes, long expected) {
    assertEquals(
        expected, new CertificateUpdateGenerator(threads, quotes).generateQuotes().count());
  }

  @ParameterizedTest(name = "threads={0}, quotes={1} is rejected")
  @CsvSource({"0,  10", "-1, 10", "4,  -1"})
  public void rejectsInvalidConstructorArguments(int threads, int quotes) {
    assertThrows(
        IllegalArgumentException.class, () -> new CertificateUpdateGenerator(threads, quotes));
  }
}
