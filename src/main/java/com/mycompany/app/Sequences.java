package com.mycompany.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Sequences {
  private static int binSearch(int[] data, int term)
  {
    int low = 0;
    int high = data.length;
    while (low < high) {
      int mid = (low + high) / 2;
      if (data[mid] < term) {
        low = mid + 1;
      } else if (data[mid] > term) {
        high = mid;
      } else {
        // Found it!
        return mid;
      }
    }
    return -1;
  }

  private static int binSearch(String[] data, String term)
  {
    int low = 0;
    int high = data.length;
    while (low < high) {
      int mid = (low + high) / 2;
      int comparison = data[mid].compareTo(term);
      if (comparison < 0) {
        // term is higher.
        low = mid + 1;
      } else if (comparison > 0) {
        // term is lower
        high = mid;
      } else {
        // Equal!
        return mid;
      }
    }
    return -1;
  }

  static int max(int[] data)
  {
    return Arrays.stream(data).max().orElse(-1);
  }

  static int evenMax(int[] data)
  {
    return Arrays.stream(data).filter(i -> i%2==0).max().orElse(-1);
  }

  static int oddMax(List<Integer> data)
  {
    return data.stream().filter(Objects::nonNull).filter(i -> i%2==1).max(Comparator.naturalOrder()).orElse(-1);
  }

  public static void main(String[] args)
  {
    {
      int[] data = {1, 3, 5};
      System.out.println(binSearch(data, 1));
      System.out.println(binSearch(data, 3));
      System.out.println(binSearch(data, 5));
      System.out.println(binSearch(data, 0));
      System.out.println(binSearch(data, 2));
      System.out.println(binSearch(data, 6));
      System.out.printf("max: %d\n", max(data));
      System.out.printf("evenMax: %d\n", evenMax(data));

      List<Integer> list = new ArrayList<>();
      list.add(1);
      list.add(3);
      list.add(5);
      list.add(null);
      System.out.printf("oddMax: %d\n", oddMax(list));
    }

    {
      String[] data = {};//{"abc", "lmn", "opq", "rst"};
      System.out.println(binSearch(data, "abc"));
      System.out.println(binSearch(data, "lmn"));
      System.out.println(binSearch(data, "opq"));
      System.out.println(binSearch(data, "aa"));
      System.out.println(binSearch(data, "bb"));
      System.out.println(binSearch(data, "z"));
    }

    {
      Set<Integer> data = new TreeSet<>((left, right)->right.compareTo(left));
      data.add(1);
      data.add(3);
      data.add(2);
      data.forEach(System.out::println);
    }
  }
}
