import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class OptimalCouplingComputer {
  final BipartitieGraph graph;
  final int n;
  final int m;
  final double[][] initial_flow;
  private double[][] distances;
  final double[][] costs;

  final double delta = 1e-10; //a precision factor to test approximate equality

  double[][] flow;
  double[][] capacity;

  public OptimalCouplingComputer(int s, int t, double[][] probabilities, double[][] distances) {
    this.distances = new double[distances[0].length][];
    for (int u = 0; u < distances[0].length; u++) {
      this.distances[u] = Arrays.copyOf(distances[u], distances[0].length);
    }

    BipartitieGraph graph = new BipartitieGraph(new LinkedList<>(), new LinkedList<>());

    for (int j = 0; j < probabilities.length; j++) {
      if (Math.abs(probabilities[s][j]) > delta) {
        graph.left.add(new Pair(j, probabilities[s][j]));
      }
      if (Math.abs(probabilities[t][j]) > delta) {
        graph.right.add(new Pair(j, probabilities[t][j]));
      }
    }

    this.graph = graph;
    this.n = this.graph.left.size();
    this.m = this.graph.right.size();

    this.initial_flow = initial_feasible_flow();

    this.costs = generate_costs();

    this.flow = this.initial_flow;
    this.capacity = this.generate_capacity(this.initial_flow);
  }

  /**
   * Returns a feasible flow.  The feasible flow is computed by means
   * of the North West corner method due to Hitchcock.
   *
   * <p>
   * <p>
   * Frank Hitchcock.  The Distribution of a Product from Several Sources to Numerous Localities.
   * <i>Studies in Applied Mathematics</i>, 20(1/4): 224-230, April 1941.
   *
   * @return a feasible flow.
   */
  public double[][] initial_feasible_flow() {
    double[][] flow = new double[n + m][n + m];

    //The northwest corner algorithm
    double[] column = new double[m];
    double[] row = new double[n];

    for (int i = 0; i < n; i++) {
      row[i] = this.graph.left.get(i).probability;
    }
    for (int j = 0; j < m; j++) {
      column[j] = this.graph.right.get(j).probability;
    }

    int i = 0;
    int j = n;
    while (i < n && j < n + m) {
      if (row[i] < column[j - n]) {
        flow[i][j] = row[i];
        row[i] -= flow[i][j];
        column[j - n] -= flow[i][j];
        if (Math.abs(column[j - n]) < delta) {
          j++;
        }
        i++;
      } else {
        flow[i][j] = column[j - n];
        row[i] -= flow[i][j];
        column[j - n] -= flow[i][j];
        if (Math.abs(row[i]) < delta) {
          i++;
        }
        j++;
      }
    }
    return flow;
  }

  //Generate the (n + m) x (n + m) cost matrix from the distance matrix.
  double[][] generate_costs() {
    double[][] costs = new double[n + m][n + m];
    for (int i = 0; i < n; i++) {
      for (int j = n; j < n + m; j++) {
        assert this.graph != null;
        int k = this.graph.left.get(i).state;
        int l = this.graph.right.get(j - n).state;
        costs[i][j] = distances[k][l];
        costs[j][i] = -distances[k][l];
      }
    }
    return costs;
  }

  //Generate the initial capacity matrix.
  double[][] generate_capacity(double[][] flow) {
    double[][] initial_capacity = new double[n + m][n + m];
    for (int i = 0; i < n; i++) {
      for (int j = n; j < n + m; j++) {
        initial_capacity[i][j] = 1 - flow[i][j]; //this capacity may be erroneous.
        initial_capacity[j][i] = flow[i][j];
      }
    }
    return initial_capacity;
  }

  //Find a cycle in a graph
  public Cycle find_cycle() throws NullPointerException {
    HashMap<Integer, Integer> predecessor_tree = new HashMap<>();
    int[] visited = new int[n + m];
    LinkedList<Integer> q = new LinkedList<>();
    double[] d = new double[n + m];

    for (int i = 1; i < n + m; i++) {
      d[i] = Double.MAX_VALUE;
    }

    q.add(0);
    while (!q.isEmpty()) {
      int tail = q.remove();
      visited[tail]++;
      if (visited[tail] > n + m) {
        //find a proper node in the cycle
        int[] tail_corrector = new int[n + m];
        while (tail_corrector[tail] < 2) {
          tail_corrector[tail]++;
          tail = predecessor_tree.get(tail);
        }
        return new Cycle(tail, predecessor_tree);
      }

      if (tail < n) {
        for (int head = n; head < n + m; head++) {
          if (d[head] > d[tail] + costs[tail][head] && Math.abs(this.capacity[tail][head]) > delta) {
            d[head] = d[tail] + costs[tail][head];
            q.add(head);
            predecessor_tree.put(head, tail);
          }
        }
      } else {
        for (int head = 0; head < n; head++) {
          if (d[head] > d[tail] + costs[tail][head] && Math.abs(this.capacity[tail][head]) > delta) {
            d[head] = d[tail] + costs[tail][head];
            q.add(head);
            predecessor_tree.put(head, tail);
          }
        }
      }
    }
    throw new NullPointerException("No negative cycle");
  }

  //Compute the optimal flow
  public void compute_optimal_flow() {
    try {
      //int count = 1;
      while (true) {
        Cycle cycle = this.find_cycle();
        //System.out.print("Negative cycle found: " + count + ".\n");
        //count++;
        double delta = Double.MAX_VALUE;

        int j = cycle.head;
        int i = cycle.predecessor_tree.get(j);
        do {
          delta = Math.min(delta, capacity[i][j]);
          j = i;
          i = cycle.predecessor_tree.get(j);
        } while (j != cycle.head);

        //Note that upon exit from this loop j and i have been reset.
        //Augment the flow by delta and remove delta from capacities along the cycle.
        do {
          this.flow[i][j] += delta;
          this.capacity[i][j] -= delta;

          j = i;
          i = cycle.predecessor_tree.get(j);
        } while (j != cycle.head);

      }
    } catch (NullPointerException e) {
      //System.out.println(e.getMessage());
    }
  }

  //Transform the flow from one on the reduced graph to one on the original network.
  public void rectify_flow() {
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        this.flow[i][n + j] -= this.flow[n + j][i];
        this.flow[n + j][i] = 0.0;
      }
    }
  }

  public double compute_distance() {
    compute_optimal_flow();
    rectify_flow();
    double distance = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        distance += flow[i][n + j] * distances[this.graph.left.get(i).state][this.graph.right.get(j).state];
      }
    }
    return distance;
  }

  private static class Pair {
    public int state;
    public double probability;

    public Pair(int state, double probability) {
      this.state = state;
      this.probability = probability;
    }
  }

  private static class Cycle {
    int head;
    HashMap<Integer, Integer> predecessor_tree;

    public Cycle(int head, HashMap<Integer, Integer> predecessor_tree) {
      this.head = head;
      this.predecessor_tree = predecessor_tree;
    }
  }

  private class BipartitieGraph {
    private LinkedList<Pair> left;
    private LinkedList<Pair> right;

    public BipartitieGraph(LinkedList<Pair> left, LinkedList<Pair> right) {
      this.left = left;
      this.right = right;
    }
  }
}
