package com.solvians.showcase;

import static com.solvians.showcase.TestFixtures.ISIN;
import static com.solvians.showcase.TestFixtures.MANY;
import static org.junit.jupiter.api.Assertions.*;

import com.solvians.showcase.common.FeedConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class IsinGeneratorTest {

  private final IsinGenerator isinGenerator = new IsinGenerator();

  // --- check digit: the algorithm, against known bodies ---------------------------------

  /**
   * "DE123456789" is the README's worked example: expand to 1314123456789, sum 54, 60 - 54 = 6.
   * "AA..." and "ZZ..." exercise both ends of the A=10 ... Z=35 conversion table. "AG..." sums to
   * exactly 10, so the check digit must be 0 rather than 10.
   */
  @ParameterizedTest(name = "checkDigit(\"{0}\") == {1}")
  @CsvSource({"DE123456789, 6", "AA000000000, 6", "ZZ000000000, 8", "AG000000000, 0"})
  public void checkDigitFollowsTheReadmeAlgorithm(String body, int expectedCheckDigit) {
    assertEquals(expectedCheckDigit, IsinGenerator.checkDigit(body));
  }

  @ParameterizedTest(name = "isValid(\"{0}\")")
  @ValueSource(strings = {"DE1234567896", "AA0000000006", "ZZ0000000008", "AG0000000000"})
  public void wellFormedIsinsAreAccepted(String isin) {
    assertTrue(IsinGenerator.isValid(isin), isin + " should be valid");
  }

  @ParameterizedTest(name = "DE123456789{0} is rejected")
  @ValueSource(ints = {0, 1, 2, 3, 4, 5, 7, 8, 9})
  public void onlyTheCorrectCheckDigitIsAccepted(int wrongCheckDigit) {
    assertFalse(IsinGenerator.isValid("DE123456789" + wrongCheckDigit));
  }

  @Test
  public void checkDigitIsAlwaysASingleDigit() {
    for (int i = 0; i < MANY; i++) {
      String isin = isinGenerator.generate();
      int checkDigit = IsinGenerator.checkDigit(isin.substring(0, FeedConstants.ISIN_BODY_LENGTH));
      assertTrue(checkDigit >= 0 && checkDigit <= 9, "check digit out of range: " + checkDigit);
    }
  }

  // --- check digit: rejected input ------------------------------------------------------

  @ParameterizedTest(name = "checkDigit(\"{0}\") throws")
  @NullSource
  @EmptySource
  @ValueSource(
      strings = {
        "DE12345678", // 10 chars - too short
        "DE1234567890", // 12 chars - too long
        "DE12345678-", // illegal character
        "de123456789", // lowercase
        "DE 12345678" // space
      })
  public void checkDigitRejectsAnIllegalBody(String body) {
    assertThrows(IllegalArgumentException.class, () -> IsinGenerator.checkDigit(body));
  }

  // --- validation -----------------------------------------------------------------------

  @ParameterizedTest(name = "isValid(\"{0}\") is false")
  @NullSource
  @EmptySource
  @ValueSource(
      strings = {
        "DE123456789", // 11 chars - no check digit
        "DE12345678966", // 13 chars
        "DE123456789X", // check digit is not a digit
        "de1234567896", // lowercase
        "DE1234567895" // wrong check digit
      })
  public void isValidRejectsMalformedInput(String candidate) {
    assertFalse(IsinGenerator.isValid(candidate));
  }

  // --- generation -----------------------------------------------------------------------

  @Test
  public void generatesTwelveCharacterIsins() {
    assertEquals(FeedConstants.ISIN_LENGTH, isinGenerator.generate().length());
  }

  @Test
  public void generatedIsinsMatchTheReadmeFormat() {
    for (int i = 0; i < MANY; i++) {
      String isin = isinGenerator.generate();
      assertTrue(ISIN.matcher(isin).matches(), "does not match ISIN format: " + isin);
    }
  }

  @Test
  public void generatedIsinsCarryACorrectCheckDigit() {
    for (int i = 0; i < MANY; i++) {
      String isin = isinGenerator.generate();
      assertTrue(IsinGenerator.isValid(isin), "invalid check digit: " + isin);
    }
  }

  @Test
  public void generatedIsinsAreRandom() {
    String first = isinGenerator.generate();
    boolean sawADifferentOne = false;
    for (int i = 0; i < MANY && !sawADifferentOne; i++) {
      sawADifferentOne = !first.equals(isinGenerator.generate());
    }
    assertTrue(sawADifferentOne, "generate() kept returning the same ISIN: " + first);
  }
}
