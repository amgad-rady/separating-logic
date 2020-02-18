import java.util.HashMap;
import java.util.LinkedList;

public class OptimalCouplingComputer {
    final BipartitieGraph graph;
    final int n;
    final int m;
    final double[][] initial_flow;
    final private double[][] distances;
    final double[][] costs;

    double[][] flow;
    double[][] capacity;

    public OptimalCouplingComputer(int s, int t, double[][] probabilities, double[][] distances) {
        this.distances = distances;

        BipartitieGraph graph = new BipartitieGraph(new LinkedList<>(), new LinkedList<>());

        for (int j = 0; j < probabilities.length; j++) {
            if (probabilities[s][j] > 0) {
                graph.left.add(new Pair(j, probabilities[s][j]));
            }
            if (probabilities[t][j] > 0) {
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

    //Generate an initial flow
    public double[][] initial_feasible_flow() {
        double[][] flow = new double[n + m][n + m];

        //The northwest corner algorithm
        double[] column = new double[m];
        double[] row = new double[n];

        for (int i = 0; i < n; i++) {
            assert this.graph != null;
            row[i] = this.graph.left.get(i).probability;
        }
        for (int j = 0; j < m; j++) {
            assert this.graph != null;
            column[j] = this.graph.right.get(j).probability;
        }

        int i = 0;
        int j = n;
        while (i < n && j < n + m) {
            if (row[i] < column[j - n]) {
                flow[i][j] = row[i];
                row[i] -= row[i];
                column[j - n] -= row[i];
                i++;
            } else {
                flow[i][j] = column[j - n];
                row[i] -= column[j - n];
                column[j - n] -= column[j - n];
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
                assert distances != null;
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
                return new Cycle(tail, predecessor_tree);
            }

            if (tail < n) {
                for (int head = n; head < n + m; head++) {
                    if (d[head] > d[tail] + costs[tail][head]) {
                        q.add(head);
                        predecessor_tree.put(head, tail);
                    }
                }
            } else {
                for (int head = 0; head < n; head++) {
                    if (d[head] > d[tail] + costs[tail][head]) {
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
            while (true) {
                Cycle cycle = this.find_cycle();
                double delta = Double.MAX_VALUE;

                int j = cycle.head;
                int i = cycle.predecessor_tree.get(j);
                do {
                    delta = Math.min(delta, capacity[i][j]);
                    j = i;
                    i = cycle.predecessor_tree.get(j);
                } while (j != cycle.head);

                //Note that upon exit from this loop j and i have been reset.
                do {
                    if (i < n) {
                        flow[i][j] += delta;
                    } else {
                        flow[i][j] -= delta;
                    }
                    capacity[i][j] -= delta;

                    j = i;
                    i = cycle.predecessor_tree.get(j);
                } while (j != cycle.head);

            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public double compute_distance() {
        compute_optimal_flow();
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