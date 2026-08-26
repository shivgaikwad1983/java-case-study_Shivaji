package com.solvians.showcase;

import com.solvians.showcase.format.CertificateUpdateFormatter;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Callable line generator; safe to share because it has no mutable state and randomness is
 * per-thread.
 */
public class CertificateUpdateTask implements Callable<String> {
  private final IsinGenerator isinGenerator;
  private final CertificateUpdateFormatter formatter;

  /** Uses default collaborators. */
  public CertificateUpdateTask() {
    this(new IsinGenerator(), new CertificateUpdateFormatter());
  }

  /** Uses supplied collaborators. */
  public CertificateUpdateTask(IsinGenerator i, CertificateUpdateFormatter f) {
    isinGenerator = Objects.requireNonNull(i, "isinGenerator");
    formatter = Objects.requireNonNull(f, "formatter");
  }

  /** Generates one CSV line. */
  public String call() {
    return formatter.toCsv(nextUpdate());
  }

  /** Generates one typed update. */
  public CertificateUpdate nextUpdate() {
    return CertificateUpdate.random(isinGenerator);
  }
}
