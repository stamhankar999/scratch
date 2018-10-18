package com.mycompany.app;
/*
 Instructions:

 Given an empty chessboard (8x8 grid), a knight is placed
 on one of the squares. The knight 'K' at position (3, 3)
 and it's possible movements 'X' are shown in the example
 below:

 * * * * * * * *
 * * X * X * * *
 * X * * * X * *
 * * * K * * * *
 * X * * * X * *
 * * X * X * * *
 * * * * * * * *
 * * * * * * * *

 Depending on the knight's position on the board, 0-6 of
 the 8 possible movements may cause the knight to leave
 the chess board.

 If the knight moves n times, each time choosing one of
 the 8 possible moves uniformly at random, determine the
 probability that the knight is still on the board after
 making n random moves. After the knight has left the
 board, it may not reenter.

 Please implement the method probability which given a
 start position x, y, and a number of moves n,
 returns the probability a knight remains on the board
 as described above.
 */
import java.util.*;

public class Solution {
  /**
   * TODO: Implement Me!
   */
  public static double probability(int x, int y, int n) {
    int boardSize = 8;
    // Define an 8x8 grid
    double[][] board = new double[boardSize][boardSize];
    // Set the starting position
    board[x][y] = 1.0;
    // Possible moves
    int[][] moves = new int[][] {
        { -2, -1 }, { -2, 1 }, { 2, -1 }, { 2, 1 },
        { -1, -2 }, { -1, 2 }, { 1, -2 }, { 1, 2 },
    };
    int curX = x;
    int curY = y;
    Queue<Point> found = new ArrayDeque<Point>();
    Point root = new Point(x, y);
    found.add(root);
    double runningProbability = 1.0;
    for (int i = 0; i < n; i++) {
      List<Point> children = new ArrayList<Point>();
      while (!found.isEmpty()) {
        Point cur = found.remove();
        int successMoveCount = 0;
        for (int moveInd = 0; moveInd < moves.length; moveInd++) {
          int newX = cur.x + moves[moveInd][0];
          int newY = cur.y + moves[moveInd][1];
          if (newX >= 0 && newX <= 7 && newY >=0 && newY <=7) {
            successMoveCount++;
            Point child = new Point(newX, newY);
            children.add(child);
            cur.children.add(child);
          }
        }
        cur.probability = successMoveCount / 8.0;
      }
      found.addAll(children);
    };

    return 1.0 * getPathProbability(root);
  }

  static double getPathProbability(Point cur) {
    if (cur.children.isEmpty()) {
      return cur.probability;
    }

    double childProbability =
        cur.children.stream().mapToDouble(p -> getPathProbability(p)).sum();

    return cur.probability * childProbability;
  }

  public static boolean doTestsPass() {
    // TODO: please feel free to make testing more elegant
    boolean result = true;
    // Start in a corner, no moves
    result = result && probability(0, 0, 0) == 1.0;
    // Start in the middle, one move
    result = result && probability(3, 3, 1) == 1.0;
    // Start in a corner, one move
    result = result && probability(0, 0, 1) == 0.25;
    result = result && probability(0,0,2) == 0.1875;
    return result;
  }

  /**
   * Execution entry point
   */
  public static void main(String[] args) {
    if (doTestsPass()) {
      System.out.println("All tests pass");
    } else {
      System.out.println("There are test failures");
    }
  }

  static class Point
  {
    int x;
    int y;
    double probability;
    List<Point> children = new ArrayList<>();
    public Point(int x, int y)
    {
      this.x = x;
      this.y = y;
    }
  }

}
