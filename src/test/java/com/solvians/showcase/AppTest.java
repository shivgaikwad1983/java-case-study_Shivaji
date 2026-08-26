package com.solvians.showcase;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AppTest {
  @Test
  public void expectTwoIntArgs() {
    RuntimeException thrown =
        Assertions.assertThrows(
            RuntimeException.class,
            () -> {
              App.main(new String[] {"xxx"});
            });
    NumberFormatException numbers =
        Assertions.assertThrows(
            NumberFormatException.class,
            () -> {
              App.main(new String[] {"xxx", "zzz"});
            });
    numbers =
        Assertions.assertThrows(
            NumberFormatException.class,
            () -> {
              App.main(new String[] {"10", "zzz"});
            });
    assertEquals("For input string: \"zzz\"", numbers.getMessage());
  }

  @ParameterizedTest(name = "{0} threads x {1} quotes prints {2} feed lines")
  @CsvSource({"1, 1,  1", "3, 2,  6", "4, 3,  12", "2, 10, 20"})
  public void printsThreadsTimesQuotesFeedLines(String threads, String quotes, int expectedLines) {
    String[] lines = runApp(threads, quotes).split(System.lineSeparator());

    assertEquals(expectedLines, lines.length, "unexpected line count");
    for (String line : lines) {
      assertTrue(FEED_LINE.matcher(line).matches(), "not a feed line: " + line);
    }
  }

  @Test
  public void everyPrintedLineCarriesAValidIsin() {
    for (String line : runApp("2", "5").split(System.lineSeparator())) {
      assertTrue(IsinGenerator.isValid(line.split(",")[1]), "invalid ISIN in: " + line);
    }
  }

  @Test
  public void printsNothingWhenNoQuotesAreRequested() {
    assertEquals("", runApp("4", "0"));
  }

  // --- input validation -----------------------------------------------------------------

  @Test
  public void rejectsNullArguments() {
    RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> App.main(null));

    assertTrue(thrown.getMessage().contains("no arguments"), thrown.getMessage());
  }

  @Test
  public void rejectsAnEmptyArgumentArray() {
    RuntimeException thrown =
        Assertions.assertThrows(RuntimeException.class, () -> App.main(new String[0]));

    assertTrue(thrown.getMessage().contains("Usage"), thrown.getMessage());
  }

  @Test
  public void reportsANullArgumentAsANumberFormatProblem() {
    Assertions.assertThrows(NumberFormatException.class, () -> App.main(new String[] {null, "5"}));
    Assertions.assertThrows(NumberFormatException.class, () -> App.main(new String[] {"5", null}));
  }

  @ParameterizedTest(name = "args [{0}, {1}] are rejected")
  @CsvSource({"0,   5", "-1,  5", "5,   -1", "-3,  -3"})
  public void rejectsOutOfRangeCounts(String threads, String quotes) {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> App.main(new String[] {threads, quotes}));
  }

  @ParameterizedTest(name = "whitespace-padded args [{0}, {1}] are accepted")
  @CsvSource({"'  2', '3  '", "' 1 ', ' 4 '"})
  public void toleratesWhitespaceAroundArguments(String threads, String quotes) {
    String output = runApp(threads, quotes);

    int expected = Integer.parseInt(threads.trim()) * Integer.parseInt(quotes.trim());
    assertEquals(expected, output.split(System.lineSeparator()).length);
  }

  @Test
  public void rejectsARequestThatWouldOverflow() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> App.main(new String[] {"100000", "100000"}));
  }

  @Test
  public void ignoresArgumentsBeyondTheFirstTwo() {
    String[] lines = runApp("2", "2", "ignored", "also-ignored").split(System.lineSeparator());

    assertEquals(2 * 2, lines.length);
  }

  /** Runs {@link App#main} with stdout captured, and returns what it printed. */
  private static String runApp(String... args) {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream captured = new ByteArrayOutputStream();
    try {
      System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8.name()));
      App.main(args);
    } catch (Exception e) {
      throw new AssertionError("App.main failed for args " + String.join(" ", args), e);
    } finally {
      System.setOut(originalOut);
    }
    return captured.toString(StandardCharsets.UTF_8).trim();
  }
}
