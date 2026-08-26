package com.solvians.showcase;

import java.util.regex.Pattern;

/**
 * Shared test constants, so the feed-line format and the sample data are declared once rather than
 * copied into every test class.
 */
public final class TestFixtures {

  /** timestamp , 12 char ISIN , price.2dp , size , price.2dp , size */
  public static final Pattern FEED_LINE =
      Pattern.compile("^\\d+,[A-Z]{2}[A-Z0-9]{9}\\d,\\d+\\.\\d{2},\\d+,\\d+\\.\\d{2},\\d+$");

  /** 2 uppercase alphabets + 9 alphanumeric characters + 1 check digit. */
  static final Pattern ISIN = Pattern.compile("^[A-Z]{2}[A-Z0-9]{9}[0-9]$");

  /** The example line from the README. */
  public static final String README_LINE = "1352122280502,DE1234567896,101.23,1000,103.45,1000";

  public static final long README_TIMESTAMP = 1352122280502L;
  public static final String README_ISIN = "DE1234567896";

  /** Draw count for statistical assertions over random output. */
  public static final int MANY = 10_000;

  private TestFixtures() {}
}
