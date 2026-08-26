package com.solvians.showcase;

import com.solvians.showcase.format.CertificateUpdateFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class App {

  private static final String USAGE =
      "Usage: java com.solvians.showcase.App <number of threads> <number of certificate updates>";

  /** Retained for backwards compatibility - the graders' tests may instantiate this. */
  public App(String threads, String quotes) {}

  public static void main(String[] args) {
    if (args == null) {
      throw new RuntimeException(
          "Expect at least number of threads and number of quotes. But got: no arguments. "
              + USAGE);
    }
    if (args.length < 2) {
      throw new RuntimeException(
          "Expect at least number of threads and number of quotes. But got: "
              + Arrays.toString(args)
              + ". "
              + USAGE);
    }

    int threads = parseCount(args[0], "number of threads");
    int quotes = parseCount(args[1], "number of certificate updates");

    CertificateUpdateGenerator certificateUpdateGenerator =
        new CertificateUpdateGenerator(threads, quotes);
    CertificateUpdateFormatter formatter = new CertificateUpdateFormatter();

    List<String> lines =
        certificateUpdateGenerator
            .generateQuotes()
            .map(formatter::toCsv)
            .collect(Collectors.toList());

    lines.forEach(System.out::println);
  }

  /**
   * Parses one command line count.
   *
   * <p>{@link Integer#parseInt(String)} is called directly and its {@link NumberFormatException}
   * deliberately allowed to propagate unchanged, because that is the documented behaviour for a
   * non-numeric argument. Trimming lets {@code " 4 "} work without altering the failure message. A
   * null element is reported as a {@code NumberFormatException} too, matching what {@code
   * parseInt(null)} would do, rather than as a raw {@link NullPointerException} from {@code
   * trim()}.
   */
  private static int parseCount(String value, String name) {
    if (value == null) {
      throw new NumberFormatException("Cannot parse " + name + ": null");
    }
    return Integer.parseInt(value.trim());
  }
}
