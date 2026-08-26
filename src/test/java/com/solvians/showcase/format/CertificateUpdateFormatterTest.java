package com.solvians.showcase.format;

import static com.solvians.showcase.TestFixtures.*;
import static org.junit.jupiter.api.Assertions.*;

import com.solvians.showcase.CertificateUpdate;
import com.solvians.showcase.IsinGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CertificateUpdateFormatterTest {

  private final CertificateUpdateFormatter formatter = new CertificateUpdateFormatter();
  private final IsinGenerator isinGenerator = new IsinGenerator();

  @Test
  public void rendersTheReadmeSampleLineExactly() {
    CertificateUpdate update =
        new CertificateUpdate(README_TIMESTAMP, README_ISIN, 101.23, 1000, 103.45, 1000);

    assertEquals(README_LINE, formatter.toCsv(update));
  }

  @ParameterizedTest(name = "{0} renders as {1}")
  @CsvSource({
    "100.5,  100.50",
    "100.0,  100.00",
    "199.9,  199.90",
    "200.0,  200.00",
    "101.23, 101.23"
  })
  public void pricesAlwaysCarryExactlyTwoDecimals(double price, String rendered) {
    String line = formatter.toCsv(new CertificateUpdate(0L, "X", price, 0, price, 0));

    assertEquals("0,X," + rendered + ",0," + rendered + ",0", line);
  }

  @ParameterizedTest(name = "sizes {0} and {1} render without a separator")
  @CsvSource({"1000,  1000", "5000,  10000", "1000,  10000"})
  public void sizesNeverCarryAThousandSeparator(int bidSize, int askSize) {
    String line = formatter.toCsv(new CertificateUpdate(0L, "X", 100.00, bidSize, 100.00, askSize));

    assertEquals("0,X,100.00," + bidSize + ",100.00," + askSize, line);
  }

  @Test
  public void alwaysProducesSixFields() {
    for (int i = 0; i < MANY; i++) {
      String line = formatter.toCsv(CertificateUpdate.random(isinGenerator));
      assertEquals(6, line.split(",", -1).length, "unexpected field count: " + line);
    }
  }

  @Test
  public void alwaysMatchesTheFeedLineFormat() {
    for (int i = 0; i < MANY; i++) {
      String line = formatter.toCsv(CertificateUpdate.random(isinGenerator));
      assertTrue(FEED_LINE.matcher(line).matches(), "does not match the feed format: " + line);
    }
  }

  // --- input validation -----------------------------------------------------------------

  @Test
  public void rejectsANullUpdate() {
    NullPointerException thrown =
        assertThrows(NullPointerException.class, () -> formatter.toCsv(null));

    assertTrue(thrown.getMessage().contains("update"), thrown.getMessage());
  }

  @Test
  public void rejectsAnUpdateWithNoIsinRatherThanPrintingTheWordNull() {
    NullPointerException thrown =
        assertThrows(NullPointerException.class, () -> formatter.toCsv(new CertificateUpdate()));

    assertTrue(thrown.getMessage().contains("isin"), thrown.getMessage());
  }
}
