package com.mycompany.app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Graph {
  private final Set<Edge> edges;
  private final Set<Node> nodes;

  public Graph(Set<Edge> edges, Set<Node> nodes) {
    this.edges = edges;
    this.nodes = nodes;
  }

  public Graph() {
    // TODO: Use a ConcurrentHashSet if used in a mt context.
    edges = new HashSet<>();
    nodes = new HashSet<>();
  }

  public Graph addEdge(Edge e) {
    edges.add(e);
    e.getFrom().addEdge(e);
    e.getTo().addEdge(e);
    nodes.add(e.getFrom());
    nodes.add(e.getTo());
    return this;
  }

  public Graph addNode(Node n) {
    nodes.add(n);
    return this;
  }

  public Set<Edge> getEdges() {
    return edges;
  }

  public Set<Node> getNodes() {
    return nodes;
  }

  void dijstra(Graph g, Node start) {
    PriorityQueue<Node> nodesByDistance = new PriorityQueue<>(g.getNodes().size(), (a, b)-> {
      if (a.getDistance() < b.getDistance()) {
        return -1;
      } else if (a.getDistance() == b.getDistance()) {
        return 0;
      } else {
        return 1;
      }
    });

  }

  void relax(Edge e) {
    int newCost = e.getFrom().getDistance() + e.getCost();
    if (e.getTo().getDistance() > newCost) {
      e.getTo().setDistance(newCost);
      e.setShortest();
    }
  }

  public static void main(String[] args) {
    Graph g = new Graph();
    Node[] nodes = {
        new Node(1),
        new Node(2),
        new Node(3),
        new Node(4),
        new Node(5)
    };

    g.addEdge(new Edge(nodes[0], nodes[1], 4))
        .addEdge(new Edge(nodes[0], nodes[2], 1))
        .addEdge(new Edge(nodes[2], nodes[1], 2));

//    List<Node> shortPath = dijstra(g, nodes[0]);
    PriorityQueue<Node> nodesByDistance = new PriorityQueue<>(g.getNodes().size(), (a, b)-> {
      if (a.getDistance() < b.getDistance()) {
        return -1;
      } else if (a.getDistance() == b.getDistance()) {
        return 0;
      } else {
        return 1;
      }
    });
    nodes[0].setDistance(0);
    nodesByDistance.add(nodes[0]);
    nodesByDistance.add(nodes[1]);
    nodesByDistance.add(nodes[2]);
    nodesByDistance.add(nodes[3]);
    nodesByDistance.add(nodes[4]);

    nodes[3].setDistance(12);
    nodes[4].setDistance(15);
    nodesByDistance.remove(nodes[3]);
    nodesByDistance.remove(nodes[4]);
    nodesByDistance.add(nodes[4]);
    nodesByDistance.add(nodes[3]);

    Node n1 = nodesByDistance.poll();
    Node n2 = nodesByDistance.poll();
    System.out.println(n1);
    System.out.println(n2);
    System.out.println(nodesByDistance.size());

  }


  static class Node {
    List<Edge> edges;
    int value;
    int distance;

    public Node(int value) {
      this.value = value;
      distance = Integer.MAX_VALUE;
      edges = new ArrayList<>();
    }

    void addEdge(Edge e) {
      edges.add(e);
    }

    public List<Edge> getEdges() {
      return edges;
    }

    public int getValue() {
      return value;
    }

    public int getDistance() {
      return distance;
    }

    public void setDistance(int distance) {
      this.distance = distance;
    }

    @Override
    public String toString() {
      return "Node{" +
          "value=" + value +
          ", distance=" + distance +
          '}';
    }
  }

  static class Edge {
    Node from;
    Node to;
    int cost;
    boolean shortest;

    public Edge(Node from, Node to, int cost) {
      this.from = from;
      this.to = to;
      this.cost = cost;
      shortest = false;
    }

    public boolean isShortest() {
      return shortest;
    }

    public void setShortest() {
      this.shortest = true;
    }

    public Node getFrom() {
      return from;
    }

    public Node getTo() {
      return to;
    }

    public int getCost() {
      return cost;
    }
  }
}
