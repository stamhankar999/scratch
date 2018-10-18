package com.mycompany.app;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Threads {

  private static void rawThreads() {
    Worker worker = new Worker();
    Thread t = new Thread(worker);
    t.start();
    try {
      Thread.sleep(2500);
      worker.stop();
      t.interrupt();
      t.join();
    } catch (InterruptedException e) {
      // no-op
    }
  }

  private static void executors() {
    ExecutorService executor = Executors.newFixedThreadPool(2);
    Worker worker = new Worker();
    executor.execute(worker);

    try {
      Thread.sleep(2500);
      worker.stop();
      executor.shutdownNow();
    } catch (InterruptedException e) {
      // no-op
    }
  }

  private static void caffeine() throws InterruptedException {
    Cache<String, Integer> cache = Caffeine.newBuilder().build();
    Thread t1 = new Thread(()-> {
      cache.get("foo", k-> {
        try {
          Thread.sleep(1300);
        } catch (InterruptedException e) {
          // no-op
        }
        int result = (int) (Math.random() * 123);
        System.out.println(String.format("sleeper %d", result));
        return result;
      });
    });

    Thread t2 = new Thread(()-> {
      cache.get("foo", k-> {
        int result = (int) (Math.random() * 123);
        System.out.println(result);
        return result;
      });
    });

    t1.start();
    Thread.sleep(100);
    t2.start();
    t1.join();
    t2.join();

    System.out.println(String.format("Final value: %d", cache.getIfPresent("foo")));
  }

  private static void computeIfAbsent() throws InterruptedException {
    Map<String, Integer> cache = new ConcurrentHashMap<>();
    Thread t1 = new Thread(()-> {
      cache.computeIfAbsent("foo", k-> {
        try {
          Thread.sleep(1300);
        } catch (InterruptedException e) {
          // no-op
        }
        int result = (int) (Math.random() * 123);
        System.out.println(String.format("sleeper %d", result));
        return result;
      });
    });

    Thread t2 = new Thread(()-> {
      Instant start = Instant.now();
      cache.computeIfAbsent("foo", k-> {
        int result = (int) (Math.random() * 123);
        System.out.println(result);
        return result;
      });
      System.out.printf("t2 time: %d\n", Duration.between(start, Instant.now()).toMillis());
    });

    t1.start();
    Thread.sleep(100);
    t2.start();
    t1.join();
    t2.join();

    System.out.println(String.format("Final value: %d", cache.get("foo")));
  }

  private static void stream() {
    List<Integer> data = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    Instant start = Instant.now();
    data.stream().map(x -> {
      try {
        MILLISECONDS.sleep(500);
        System.out.println("thread: " + Thread.currentThread().getName() + " inside");
      } catch (InterruptedException e) {
        //swallow
      }
      return x + 1;
    })
        .forEach(x -> {
          System.out.println("thread: " + Thread.currentThread().getName() + " terminator");
        });
    System.out.printf("Duration: %d\n", Duration.between(start, Instant.now()).toMillis());
  }
  public static void main(String[] args) throws InterruptedException {
    stream();
    System.out.println("got here");
  }

  private static class Worker implements Runnable {
    volatile boolean stopped = false;
    private int counter = 0;

    @Override
    public void run() {
      while (!stopped) {
        System.out.println(String.format("%d: %d", System.currentTimeMillis(), counter++));
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          System.out.println(String.format("%d: Cleaning up.", System.currentTimeMillis()));
        }
      }
    }

    public void stop(){
      stopped = true;
    }
  }
}
