package com.solvians.showcase;

import com.solvians.showcase.common.*;

/**
 * Stateless ISIN generator. Algorithm: convert letters to 10-35, double every other digit from the
 * right, sum resulting digits, then subtract from the next multiple of ten.
 */
public class IsinGenerator {
  /** Generates a 12-character ISIN. */
  public String generate() {
    StringBuilder b = new StringBuilder(FeedConstants.ISIN_LENGTH);
    for (int i = 0; i < FeedConstants.COUNTRY_CODE_LENGTH; i++)
      b.append(RandomUtils.randomCharacterFrom(FeedConstants.UPPERCASE_LETTERS));
    for (int i = 0; i < FeedConstants.NSIN_LENGTH; i++)
      b.append(RandomUtils.randomCharacterFrom(FeedConstants.ALPHANUMERIC_CHARACTERS));
    return b.append(checkDigit(b.toString())).toString();
  }

  /** Calculates the check digit for an ISIN body. */
  public static int checkDigit(String body) {
    if (body == null || body.length() != FeedConstants.ISIN_BODY_LENGTH)
      throw new IllegalArgumentException(
          "ISIN body must contain exactly " + FeedConstants.ISIN_BODY_LENGTH + " legal characters");
    String d = toDigits(body);
    int sum = 0;
    boolean doubleThisDigit = true;
    for (int i = d.length() - 1; i >= 0; i--) {
      int n = d.charAt(i) - '0';
      if (doubleThisDigit) {
        n *= 2;
        if (n > 9) n -= 9; /* equivalent to splitting 10..18 into digits */
      }
      sum += n;
      doubleThisDigit = !doubleThisDigit;
    }
    return (FeedConstants.CHECK_DIGIT_MODULUS - (sum % FeedConstants.CHECK_DIGIT_MODULUS))
        % FeedConstants.CHECK_DIGIT_MODULUS;
  }

  /** Returns whether an ISIN has a valid check digit. */
  public static boolean isValid(String isin) {
    if (isin == null
        || isin.length() != FeedConstants.ISIN_LENGTH
        || !Character.isDigit(isin.charAt(FeedConstants.ISIN_LENGTH - 1))) return false;
    try {
      return checkDigit(isin.substring(0, FeedConstants.ISIN_BODY_LENGTH)) == isin.charAt(11) - '0';
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  private static String toDigits(String body) {
    StringBuilder r = new StringBuilder();
    for (int i = 0; i < body.length(); i++) {
      char c = body.charAt(i);
      if (c >= '0' && c <= '9') r.append(c);
      else if (c >= 'A' && c <= 'Z') r.append(c - 'A' + FeedConstants.LETTER_A_VALUE);
      else throw new IllegalArgumentException("Illegal ISIN character: " + c);
    }
    return r.toString();
  }
}
