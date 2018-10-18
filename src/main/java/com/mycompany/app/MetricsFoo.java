package com.mycompany.app;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jmx.JmxReporter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** https://metrics.dropwizard.io/4.0.0/getting-started.html */
public class MetricsFoo {
  static final MetricRegistry metrics = new MetricRegistry();
  static final List<String> myList = new ArrayList<>();
  static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
  public static void main(String args[]) {
    final JmxReporter reporter = JmxReporter.forRegistry(metrics).inDomain("com.datastax").build();
    reporter.start();
    startReport();
    Meter requests = metrics.meter("0=x,name=requests");
    metrics.gauge("0=x,name=size", () -> myList::size);
    Counter counter = metrics.counter("0=x,name=ctr");
    Histogram histogram = metrics.histogram("x/histogram");
    executor.scheduleWithFixedDelay(() -> {
      myList.add("jack");
    }, 0, 1, SECONDS);
    executor.scheduleWithFixedDelay(() -> {
      requests.mark();
      counter.inc();
      histogram.update(myList.size());
    }, 0, 4000, TimeUnit.MILLISECONDS);
    wait5Seconds();
    executor.shutdown();
    reporter.stop();
  }

  static void startReport() {
    ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
        .convertRatesTo(SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .build();
    reporter.start(1, SECONDS);
  }

  static void wait5Seconds() {
    try {
      SECONDS.sleep(135);
    }
    catch(InterruptedException e) {
      // swallow
    }
  }
}
