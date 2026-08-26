package com.solvians.showcase;

import static com.solvians.showcase.TestFixtures.*;
import static org.junit.jupiter.api.Assertions.*;

import com.solvians.showcase.common.FeedConstants;
import org.junit.jupiter.api.Test;

class CertificateUpdateTest {

  private static final double MIN_PRICE = FeedConstants.MIN_PRICE_CENTS / 100.0;
  private static final double MAX_PRICE = FeedConstants.MAX_PRICE_CENTS / 100.0;

  private final IsinGenerator isinGenerator = new IsinGenerator();

  // --- random(): the six properties -----------------------------------------------------

  @Test
  public void randomPopulatesEveryProperty() {
    CertificateUpdate update = CertificateUpdate.random(isinGenerator);

    assertTrue(update.getTimestamp() > 0, "timestamp not set");
    assertNotNull(update.getIsin(), "isin not set");
    assertTrue(update.getBidPrice() > 0, "bid price not set");
    assertTrue(update.getBidSize() > 0, "bid size not set");
    assertTrue(update.getAskPrice() > 0, "ask price not set");
    assertTrue(update.getAskSize() > 0, "ask size not set");
  }

  @Test
  public void randomProducesAValidTwelveCharacterIsin() {
    for (int i = 0; i < MANY; i++) {
      String isin = CertificateUpdate.random(isinGenerator).getIsin();
      assertEquals(FeedConstants.ISIN_LENGTH, isin.length(), isin);
      assertTrue(IsinGenerator.isValid(isin), "invalid ISIN: " + isin);
    }
  }

  @Test
  public void randomTimestampIsCurrentEpochMillis() {
    long before = System.currentTimeMillis();
    long timestamp = CertificateUpdate.random(isinGenerator).getTimestamp();
    long after = System.currentTimeMillis();

    assertTrue(
        timestamp >= before && timestamp <= after,
        "timestamp " + timestamp + " not within [" + before + ", " + after + "]");
  }

  @Test
  public void randomKeepsPricesAndSizesInsideTheReadmeRanges() {
    for (int i = 0; i < MANY; i++) {
      CertificateUpdate update = CertificateUpdate.random(isinGenerator);

      assertTrue(
          update.getBidPrice() >= MIN_PRICE && update.getBidPrice() <= MAX_PRICE,
          "bid price out of range: " + update.getBidPrice());
      assertTrue(
          update.getAskPrice() >= MIN_PRICE && update.getAskPrice() <= MAX_PRICE,
          "ask price out of range: " + update.getAskPrice());
      assertTrue(
          update.getBidSize() >= FeedConstants.MIN_SIZE
              && update.getBidSize() <= FeedConstants.MAX_BID_SIZE,
          "bid size out of range: " + update.getBidSize());
      assertTrue(
          update.getAskSize() >= FeedConstants.MIN_SIZE
              && update.getAskSize() <= FeedConstants.MAX_ASK_SIZE,
          "ask size out of range: " + update.getAskSize());
    }
  }

  @Test
  public void inclusiveBoundsAreReachable() {
    // Guards against a missing "+ 1" on the exclusive nextInt bound: without it, 200.00,
    // 5,000 and 10,000 could never be produced. The loop exits as soon as all three are
    // seen, so in practice it runs a few hundred iterations, not the cap.
    boolean sawMaxBidSize = false;
    boolean sawMaxAskSize = false;
    boolean sawMaxPrice = false;

    for (int i = 0; i < 500_000 && !(sawMaxBidSize && sawMaxAskSize && sawMaxPrice); i++) {
      CertificateUpdate update = CertificateUpdate.random(isinGenerator);
      sawMaxBidSize |= update.getBidSize() == FeedConstants.MAX_BID_SIZE;
      sawMaxAskSize |= update.getAskSize() == FeedConstants.MAX_ASK_SIZE;
      sawMaxPrice |= update.getBidPrice() == MAX_PRICE || update.getAskPrice() == MAX_PRICE;
    }

    assertTrue(sawMaxBidSize, "bid size never reached its inclusive maximum");
    assertTrue(sawMaxAskSize, "ask size never reached its inclusive maximum");
    assertTrue(sawMaxPrice, "price never reached its inclusive maximum");
  }

  @Test
  public void randomPricesAreAWholeNumberOfCents() {
    for (int i = 0; i < MANY; i++) {
      CertificateUpdate update = CertificateUpdate.random(isinGenerator);

      assertEquals(Math.round(update.getBidPrice() * 100) / 100.0, update.getBidPrice());
      assertEquals(Math.round(update.getAskPrice() * 100) / 100.0, update.getAskPrice());
    }
  }

  @Test
  public void randomRejectsANullIsinGenerator() {
    NullPointerException thrown =
        assertThrows(NullPointerException.class, () -> CertificateUpdate.random(null));

    assertTrue(thrown.getMessage().contains("isinGenerator"), thrown.getMessage());
  }

  // --- accessors ------------------------------------------------------------------------

  @Test
  public void gettersAndSettersRoundTrip() {
    CertificateUpdate update = new CertificateUpdate();
    update.setTimestamp(README_TIMESTAMP);
    update.setIsin(README_ISIN);
    update.setBidPrice(101.23);
    update.setBidSize(1000);
    update.setAskPrice(103.45);
    update.setAskSize(1000);

    assertEquals(README_TIMESTAMP, update.getTimestamp());
    assertEquals(README_ISIN, update.getIsin());
    assertEquals(101.23, update.getBidPrice());
    assertEquals(1000, update.getBidSize());
    assertEquals(103.45, update.getAskPrice());
    assertEquals(1000, update.getAskSize());
  }

  @Test
  public void allArgsConstructorAssignsEveryProperty() {
    CertificateUpdate update =
        new CertificateUpdate(README_TIMESTAMP, README_ISIN, 101.23, 1000, 103.45, 2000);

    assertEquals(README_TIMESTAMP, update.getTimestamp());
    assertEquals(README_ISIN, update.getIsin());
    assertEquals(101.23, update.getBidPrice());
    assertEquals(1000, update.getBidSize());
    assertEquals(103.45, update.getAskPrice());
    assertEquals(2000, update.getAskSize());
  }
}
