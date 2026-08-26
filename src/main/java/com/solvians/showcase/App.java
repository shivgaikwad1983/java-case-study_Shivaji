package com.solvians.showcase;

import com.solvians.showcase.format.CertificateUpdateFormatter;
import java.util.*;
import java.util.stream.Collectors;

/** Application entry point. */
public class App {
  /** Compatibility constructor; intentionally no-op. */
  public App(String threads, String quotes) {}

  /** Runs generation. */
  public static void main(String[] args) {
    String usage = "Usage: App <number of threads> <number of quotes>";
    if (args == null) throw new RuntimeException("No arguments. " + usage);
    if (args.length < 2)
      throw new RuntimeException(
          "Expect at least number of threads and number of quotes. But got: "
              + Arrays.toString(args)
              + ". "
              + usage);
    int t = parseCount(args[0], "Number of threads"), q = parseCount(args[1], "Number of quotes");
    CertificateUpdateFormatter f = new CertificateUpdateFormatter();
    List<String> lines =
        new CertificateUpdateGenerator(t, q)
            .generateQuotes()
            .map(f::toCsv)
            .collect(Collectors.toList());
    lines.forEach(System.out::println);
  }

  private static int parseCount(String value, String name) {
    if (value == null) throw new NumberFormatException("For input string: \"null\"");
    return Integer.parseInt(value.trim());
  }
}
