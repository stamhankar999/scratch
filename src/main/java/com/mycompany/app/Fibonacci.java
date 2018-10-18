package com.mycompany.app;

import java.util.ArrayList;
import java.util.List;

public class Fibonacci {

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Usage: fibonacci <count>");
      System.exit(1);
    }

    int count = Integer.parseInt(args[0]);
//    List<Integer> results = iterative(count);
    for (int i = 0 ; i < count; i++) {
      //System.out.println(iterative(i));
      System.out.println(fib(i));
    }
  }

  private static int fib(int rank) {
    if (rank < 2) {
      return 1;
    }
    return fib(rank - 1) + fib(rank - 2);
  }

  private static int iterative(int rank) {
    if (rank < 2) {
      return 1;
    }
    int secondToLast = 1;
    int last = 1;
    for (int i = 2; i <= rank; i++) {
      int current = secondToLast + last;
      secondToLast = last;
      last = current;
    }
    return last;
  }
}
