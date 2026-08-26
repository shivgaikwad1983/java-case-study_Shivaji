package com.solvians.showcase.format;

import com.solvians.showcase.CertificateUpdate;
import com.solvians.showcase.common.FeedConstants;
import java.util.*;

public class CertificateUpdateFormatter {

  /**
   * @param update the update to render
   * @return the comma separated certificate update line
   * @throws NullPointerException if {@code update} is null
   */
  public String toCsv(CertificateUpdate update) {
    Objects.requireNonNull(update, "update must not be null");
    Objects.requireNonNull(
        update.getIsin(),
        "isin must not be null - a half-populated update would render the literal "
            + "text \"null\" into the feed");

    // Locale.ROOT is required: under a comma-decimal default locale "%.2f" would emit
    // "101,23" and turn a six field line into a seven field one.
    return String.format(
        Locale.ROOT,
        FeedConstants.FEED_LINE_FORMAT,
        update.getTimestamp(),
        update.getIsin(),
        update.getBidPrice(),
        update.getBidSize(),
        update.getAskPrice(),
        update.getAskSize());
  }
}
