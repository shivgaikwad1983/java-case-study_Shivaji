package com.solvians.showcase.common;

public final class FeedConstants {

  // --- ISIN ---------------------------------------------------------------------------

  /** 2 random uppercase alphabets. */
  public static final int COUNTRY_CODE_LENGTH = 2;

  /** 9 random alphanumeric characters. */
  public static final int NSIN_LENGTH = 9;

  /** Length of an ISIN without its trailing check digit. */
  public static final int ISIN_BODY_LENGTH = COUNTRY_CODE_LENGTH + NSIN_LENGTH;

  /** Full length of an ISIN, including the check digit. */
  public static final int ISIN_LENGTH = ISIN_BODY_LENGTH + 1;

  /** The alphabet for the country code. */
  public static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  /** The alphabet for the national security identifier. */
  public static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

  /** The check digit is computed modulo 10. */
  public static final int CHECK_DIGIT_MODULUS = 10;

  /** Numeric value of 'A' in the README's conversion table; 'Z' is therefore 35. */
  public static final int LETTER_A_VALUE = 10;

  // --- prices -------------------------------------------------------------------------

  /** Prices are drawn in whole cents so that "2 decimal places" holds by construction. */
  public static final int CENTS_PER_UNIT = 100;

  /** Lowest bid/ask price, in whole cents: 100.00. Inclusive. */
  public static final int MIN_PRICE_CENTS = 100 * CENTS_PER_UNIT;

  /** Highest bid/ask price, in whole cents: 200.00. Inclusive. */
  public static final int MAX_PRICE_CENTS = 200 * CENTS_PER_UNIT;

  // --- sizes --------------------------------------------------------------------------

  /** Lowest bid/ask size. Inclusive. */
  public static final int MIN_SIZE = 1_000;

  /** Highest bid size. Inclusive. */
  public static final int MAX_BID_SIZE = 5_000;

  /** Highest ask size. Inclusive - note this differs from {@link #MAX_BID_SIZE}. */
  public static final int MAX_ASK_SIZE = 10_000;

  // --- feed line ----------------------------------------------------------------------

  /**
   * {@code %d} rather than {@code %,d} so that sizes never carry a thousand-separator, and {@code
   * %.2f} for the two decimal places. Must be used with {@link java.util.Locale#ROOT}.
   */
  public static final String FEED_LINE_FORMAT = "%d,%s,%.2f,%d,%.2f,%d";

  /** Separator between the six properties of a feed line. */
  public static final String FIELD_SEPARATOR = ",";

  /** Number of properties in one certificate update. */
  public static final int FIELD_COUNT = 6;

  private FeedConstants() {
    throw new AssertionError("FeedConstants is not instantiable");
  }
}
