package com.solvians.showcase;

import com.solvians.showcase.common.Validations;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

/** Concurrent certificate update generator. */
public class CertificateUpdateGenerator {
  private final int threads, quotes;
  private final CertificateUpdateTask task = new CertificateUpdateTask();

  /** The shared task is safe because it and its collaborators are stateless. */
  public CertificateUpdateGenerator(int threads, int quotes) {
    this.threads = Validations.requirePositive(threads, "Number of threads");
    this.quotes = Validations.requireNonNegative(quotes, "Number of quotes");
    Validations.multiplyWithoutOverflow(threads, "threads", quotes, "quotes");
  }

  /** Generates exactly threads times quotes fully materialized updates. */
  public Stream<CertificateUpdate> generateQuotes() {
    int count = threads * quotes;
    ExecutorService pool = Executors.newFixedThreadPool(threads);
    try {
      List<Callable<CertificateUpdate>> tasks = new ArrayList<>(count);
      for (int i = 0; i < count; i++) tasks.add(task::nextUpdate);
      List<Future<CertificateUpdate>> fs = pool.invokeAll(tasks);
      List<CertificateUpdate> out = new ArrayList<>(count);
      for (Future<CertificateUpdate> f : fs) out.add(f.get());
      return out.stream();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Interrupted while generating updates", e);
    } catch (ExecutionException e) {
      throw new IllegalStateException("Generation failed", e.getCause());
    } finally {
      pool.shutdown();
    }
  }
}
