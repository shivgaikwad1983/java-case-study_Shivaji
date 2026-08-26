package com.solvians.showcase;

import com.solvians.showcase.format.CertificateUpdateFormatter;
import java.util.*;
import java.util.concurrent.Callable;

public class CertificateUpdateTask implements Callable<String> {

  private final IsinGenerator isinGenerator;
  private final CertificateUpdateFormatter formatter;

  /** Uses a fresh {@link IsinGenerator} and {@link CertificateUpdateFormatter}. */
  public CertificateUpdateTask() {
    this(new IsinGenerator(), new CertificateUpdateFormatter());
  }

  /**
   * @param isinGenerator the ISIN source to use, allowing a stub to be injected in tests
   * @param formatter renders the update as a feed line
   */
  public CertificateUpdateTask(IsinGenerator isinGenerator, CertificateUpdateFormatter formatter) {
    this.isinGenerator = Objects.requireNonNull(isinGenerator, "isinGenerator must not be null");
    this.formatter = Objects.requireNonNull(formatter, "formatter must not be null");
  }

  /**
   * @return one comma separated certificate update line
   */
  @Override
  public String call() {
    return formatter.toCsv(nextUpdate());
  }

  /**
   * The same update {@link #call()} renders, but as an object rather than a line. Used by {@link
   * CertificateUpdateGenerator}, which must return typed updates.
   *
   * @return a fully populated certificate update
   */
  public CertificateUpdate nextUpdate() {
    return CertificateUpdate.random(isinGenerator);
  }
}
