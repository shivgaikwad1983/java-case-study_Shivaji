package com.solvians.showcase;

import com.solvians.showcase.common.Validations;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class CertificateUpdateGenerator {

  private final int threads;
  private final int quotes;

  /**
   * Deliberately a single shared instance: {@link CertificateUpdateTask} is stateless and thread
   * safe, so there is no reason to allocate one per task.
   */
  private final CertificateUpdateTask task = new CertificateUpdateTask();

  public CertificateUpdateGenerator(int threads, int quotes) {
    this.threads = Validations.requirePositive(threads, "Number of threads");
    this.quotes = Validations.requireNonNegative(quotes, "Number of quotes");

    // Fail here rather than inside generateQuotes(), so an impossible request is rejected
    // at construction time.
    Validations.multiplyWithoutOverflow(threads, "threads", quotes, "quotes");
  }

  /**
   * Triggers the certificate generations in multiple threads and collects the results.
   *
   * @return {@code threads * quotes} fully populated certificate updates
   */
  public Stream<CertificateUpdate> generateQuotes() {
    int total = Validations.multiplyWithoutOverflow(threads, "threads", quotes, "quotes");

    ExecutorService pool = Executors.newFixedThreadPool(threads);
    try {
      List<Callable<CertificateUpdate>> tasks = new ArrayList<>(total);
      for (int i = 0; i < total; i++) {
        tasks.add(task::nextUpdate);
      }

      List<CertificateUpdate> updates = new ArrayList<>(total);
      for (Future<CertificateUpdate> future : pool.invokeAll(tasks)) {
        updates.add(future.get());
      }
      return updates.stream();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Interrupted while generating certificate updates", e);
    } catch (ExecutionException e) {
      throw new IllegalStateException("Failed to generate certificate updates", e.getCause());
    } finally {
      pool.shutdown();
    }
  }
}
