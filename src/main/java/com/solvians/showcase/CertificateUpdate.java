package com.solvians.showcase;

import com.solvians.showcase.common.*;
import java.util.Objects;

/** Mutable holder for one certificate update. */
public class CertificateUpdate {

  private long timestamp;
  private String isin;
  private double bidPrice;
  private int bidSize;
  private double askPrice;
  private int askSize;

  public CertificateUpdate() {}

  public CertificateUpdate(
      long timestamp, String isin, double bidPrice, int bidSize, double askPrice, int askSize) {
    this.timestamp = timestamp;
    this.isin = isin;
    this.bidPrice = bidPrice;
    this.bidSize = bidSize;
    this.askPrice = askPrice;
    this.askSize = askSize;
  }

  /**
   * Builds one certificate update with the current timestamp, a freshly generated ISIN, and random
   * prices and sizes inside the ranges given in the README.
   *
   * @param isinGenerator supplies property 2
   * @return a fully populated certificate update
   */
  public static CertificateUpdate random(IsinGenerator isinGenerator) {
    Objects.requireNonNull(isinGenerator, "isinGenerator must not be null");
    return new CertificateUpdate(
        System.currentTimeMillis(),
        isinGenerator.generate(),
        randomPrice(),
        RandomUtils.nextIntInclusive(FeedConstants.MIN_SIZE, FeedConstants.MAX_BID_SIZE),
        randomPrice(),
        RandomUtils.nextIntInclusive(FeedConstants.MIN_SIZE, FeedConstants.MAX_ASK_SIZE));
  }

  /**
   * Random price in [100.00, 200.00] with exactly 2 decimal places. Drawn as a whole number of
   * cents so that "2 decimal places" holds by construction rather than by rounding.
   */
  private static double randomPrice() {
    return RandomUtils.nextIntInclusive(
            FeedConstants.MIN_PRICE_CENTS, FeedConstants.MAX_PRICE_CENTS)
        / (double) FeedConstants.CENTS_PER_UNIT;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public String getIsin() {
    return isin;
  }

  public void setIsin(String isin) {
    this.isin = isin;
  }

  public double getBidPrice() {
    return bidPrice;
  }

  public void setBidPrice(double bidPrice) {
    this.bidPrice = bidPrice;
  }

  public int getBidSize() {
    return bidSize;
  }

  public void setBidSize(int bidSize) {
    this.bidSize = bidSize;
  }

  public double getAskPrice() {
    return askPrice;
  }

  public void setAskPrice(double askPrice) {
    this.askPrice = askPrice;
  }

  public int getAskSize() {
    return askSize;
  }

  public void setAskSize(int askSize) {
    this.askSize = askSize;
  }

  /**
   * Human readable form, for debugging and test failure messages. The feed format is {@link
   * #toCsv()}.
   */
  @Override
  public String toString() {
    return "CertificateUpdate{timestamp="
        + timestamp
        + ", isin='"
        + isin
        + '\''
        + ", bidPrice="
        + bidPrice
        + ", bidSize="
        + bidSize
        + ", askPrice="
        + askPrice
        + ", askSize="
        + askSize
        + '}';
  }
}
