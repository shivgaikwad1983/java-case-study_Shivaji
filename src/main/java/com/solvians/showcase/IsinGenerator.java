package com.solvians.showcase;

import com.solvians.showcase.common.*;

public class IsinGenerator {

  /**
   * Generates a random ISIN: 2 uppercase alphabets, 9 alphanumeric characters and the matching
   * check digit.
   *
   * @return a random, valid 12 character ISIN
   */
  public String generate() {
    StringBuilder isin = new StringBuilder(FeedConstants.ISIN_LENGTH);
    for (int i = 0; i < FeedConstants.COUNTRY_CODE_LENGTH; i++) {
      isin.append(RandomUtils.randomCharacterFrom(FeedConstants.UPPERCASE_LETTERS));
    }
    for (int i = 0; i < FeedConstants.NSIN_LENGTH; i++) {
      isin.append(RandomUtils.randomCharacterFrom(FeedConstants.ALPHANUMERIC_CHARACTERS));
    }

    return isin.append(checkDigit(isin.toString())).toString();
  }

  /**
   * Calculates the check digit for an ISIN body.
   *
   * @param body 2 uppercase alphabets + 9 alphanumeric characters, without the check digit
   * @return the check digit, 0 to 9
   * @throws IllegalArgumentException if the body is not exactly 11 legal ISIN characters
   */
  public static int checkDigit(String body) {
    if (body == null || body.length() != FeedConstants.ISIN_BODY_LENGTH) {
      throw new IllegalArgumentException(
          "ISIN body must be exactly "
              + FeedConstants.ISIN_BODY_LENGTH
              + " characters, but was: "
              + body);
    }

    String digits = toDigits(body);

    // Starting from the rightmost digit, every other digit is multiplied by two.
    int sum = 0;
    boolean doubleThisDigit = true;
    for (int i = digits.length() - 1; i >= 0; i--) {
      int digit = digits.charAt(i) - '0';
      if (doubleThisDigit) {
        digit *= 2;
        if (digit > 9) {
          // Numbers greater than 9 become two separate digits, e.g. 18 -> 1 + 8 = 9.
          // Doubling a single digit never exceeds 18, so this is the same as -9.
          digit -= 9;
        }
      }
      sum += digit;
      doubleThisDigit = !doubleThisDigit;
    }

    // Subtract the sum from the smallest multiple of 10 which is greater than or equal to it.
    return (FeedConstants.CHECK_DIGIT_MODULUS - (sum % FeedConstants.CHECK_DIGIT_MODULUS))
        % FeedConstants.CHECK_DIGIT_MODULUS;
  }

  /**
   * Checks whether a string is a well-formed ISIN carrying the correct check digit.
   *
   * @param isin the candidate ISIN
   * @return true if it is 12 legal characters and the check digit matches
   */
  public static boolean isValid(String isin) {
    if (isin == null || isin.length() != FeedConstants.ISIN_LENGTH) {
      return false;
    }

    char lastCharacter = isin.charAt(FeedConstants.ISIN_BODY_LENGTH);
    if (lastCharacter < '0' || lastCharacter > '9') {
      return false;
    }

    try {
      return checkDigit(isin.substring(0, FeedConstants.ISIN_BODY_LENGTH)) == lastCharacter - '0';
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  /**
   * Converts any letters to numbers by the README's conversion table (A=10 ... Z=35) and keeps
   * digits as they are.
   *
   * @param body the ISIN body
   * @return the expanded string of digits, e.g. "DE123456789" -> "1314123456789"
   */
  private static String toDigits(String body) {
    StringBuilder digits = new StringBuilder(body.length() * 2);
    for (int i = 0; i < body.length(); i++) {
      char character = body.charAt(i);
      if (character >= '0' && character <= '9') {
        digits.append(character);
      } else if (character >= 'A' && character <= 'Z') {
        digits.append(character - 'A' + FeedConstants.LETTER_A_VALUE);
      } else {
        throw new IllegalArgumentException(
            "Illegal ISIN character '" + character + "' in: " + body);
      }
    }
    return digits.toString();
  }
}
