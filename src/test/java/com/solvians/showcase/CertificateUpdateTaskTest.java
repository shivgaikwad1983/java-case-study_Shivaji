package com.solvians.showcase;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CertificateUpdateTaskTest {

  private static final double MIN_PRICE = FeedConstants.MIN_PRICE_CENTS / 100.0;
  private static final double MAX_PRICE = FeedConstants.MAX_PRICE_CENTS / 100.0;

  private final CertificateUpdateTask task = new CertificateUpdateTask();

  @Test
  public void callReturnsSixCommaSeparatedFields() {
    String line = task.call();
    assertEquals(6, line.split(",", -1).length, "unexpected field count: " + line);
  }

  @Test
  public void callMatchesTheReadmeLineFormat() {
    for (int i = 0; i < MANY; i++) {
      String line = task.call();
      assertTrue(FEED_LINE.matcher(line).matches(), "does not match the feed format: " + line);
    }
  }

  @Test
  public void secondFieldIsAValidIsin() {
    for (int i = 0; i < MANY; i++) {
      String isin = task.call().split(",")[1];
      assertTrue(IsinGenerator.isValid(isin), "invalid ISIN in line: " + isin);
    }
  }

  @Test
  public void numericFieldsRespectTheReadmeRanges() {
    for (int i = 0; i < MANY; i++) {
      String[] fields = task.call().split(",");

      double bidPrice = Double.parseDouble(fields[2]);
      int bidSize = Integer.parseInt(fields[3]);
      double askPrice = Double.parseDouble(fields[4]);
      int askSize = Integer.parseInt(fields[5]);

      assertTrue(bidPrice >= MIN_PRICE && bidPrice <= MAX_PRICE, "bid price: " + bidPrice);
      assertTrue(askPrice >= MIN_PRICE && askPrice <= MAX_PRICE, "ask price: " + askPrice);
      assertTrue(
          bidSize >= FeedConstants.MIN_SIZE && bidSize <= FeedConstants.MAX_BID_SIZE,
          "bid size: " + bidSize);
      assertTrue(
          askSize >= FeedConstants.MIN_SIZE && askSize <= FeedConstants.MAX_ASK_SIZE,
          "ask size: " + askSize);
    }
  }

  @Test
  public void firstFieldIsCurrentEpochMillis() {
    long before = System.currentTimeMillis();
    long timestamp = Long.parseLong(task.call().split(",")[0]);
    long after = System.currentTimeMillis();

    assertTrue(
        timestamp >= before && timestamp <= after,
        "timestamp " + timestamp + " not within [" + before + ", " + after + "]");
  }

  @Test
  public void producesDifferentLinesOnRepeatedCalls() {
    String first = task.call();
    boolean sawADifferentOne = false;
    for (int i = 0; i < MANY && !sawADifferentOne; i++) {
      sawADifferentOne = !first.equals(task.call());
    }
    assertTrue(sawADifferentOne, "call() kept returning the same line: " + first);
  }

  @Test
  public void nextUpdateReturnsTheSameDataAsAnObject() {
    CertificateUpdate update = task.nextUpdate();

    assertNotNull(update.getIsin());
    assertTrue(IsinGenerator.isValid(update.getIsin()));
    assertTrue(update.getTimestamp() > 0);
  }

  @Test
  public void oneSharedInstanceIsSafeAcrossManyThreads() {
    // CertificateUpdateGenerator shares a single task between all pool threads, so that
    // invariant is asserted here rather than left implicit.
    ExecutorService pool = Executors.newFixedThreadPool(8);
    try {
      List<Callable<String>> tasks = new ArrayList<>();
      for (int i = 0; i < 800; i++) {
        tasks.add(task);
      }

      List<Future<String>> futures = pool.invokeAll(tasks);
      assertEquals(800, futures.size());

      for (Future<String> future : futures) {
        String line = future.get();
        assertTrue(FEED_LINE.matcher(line).matches(), "bad line from pool: " + line);
      }
    } catch (Exception e) {
      throw new AssertionError("concurrent use of a shared task failed", e);
    } finally {
      pool.shutdown();
    }
  }

  // --- input validation -----------------------------------------------------------------

  @Test
  public void constructorRejectsNullCollaborators() {
    NullPointerException noIsin =
        assertThrows(
            NullPointerException.class,
            () -> new CertificateUpdateTask(null, new CertificateUpdateFormatter()));
    assertTrue(noIsin.getMessage().contains("isinGenerator"), noIsin.getMessage());

    NullPointerException noFormatter =
        assertThrows(
            NullPointerException.class, () -> new CertificateUpdateTask(new IsinGenerator(), null));
    assertTrue(noFormatter.getMessage().contains("formatter"), noFormatter.getMessage());
  }
}
