import java.util.HashMap;
import java.util.LinkedList;

public class Graph {
    public int nodes;
    public int[][] edges; //edges represented by a square matrix of size nodes.
    public double[][] costs; //edge capacities. In this case the nth element of the distance sequence.
    public double[][] capacities; //edge capacities. Usually, 1. Reduced in reduced graphs.

    public Graph(int nodes, int[][] edges, double[][] costs, double[][] capacities) {
        this.nodes = nodes;
        this.edges = edges;
        this.costs = costs;
        this.capacities = capacities;
    }

    public Graph construct_reduced_graph(double[][] flow) {
        double[][] reduced_costs = new double[this.nodes][this.nodes];
        double[][] reduced_capacities = new double[this.nodes][this.nodes];
        int[][] reduced_edges = new int[this.nodes][this.nodes];

        for (int i = 0; i < this.nodes; i++) {
            for (int j = 0; j < this.nodes; j++) {
                if (this.edges[i][j] == 1 && flow[i][j] < this.capacities[i][j]) {
                    reduced_edges[i][j] = 1;
                    reduced_capacities[i][j] = this.capacities[i][j] - flow[i][j];
                    reduced_costs = this.costs;
                }
                if (this.edges[i][j] == 1 && flow[i][j] > 0) {
                    reduced_edges[j][i] = 1;
                    reduced_capacities[j][i] = flow[i][j];
                    reduced_costs[j][i] = -this.costs[i][j];
                }
            }
        }
        return new Graph(this.nodes, reduced_edges, reduced_costs, reduced_capacities);
    }

    //This is a variant of the distance relabling function used to compute shortest paths adapted to detecing negative cycles.
    public Cycle find_negative_cycle(Graph reduced_graph, int initial_node) throws NullPointerException {
        double[] d = new double[reduced_graph.nodes];
        int[] visits = new int[reduced_graph.nodes];

        HashMap<Integer, Integer> predecessors = new HashMap<>();

        for (int i = 0; i < reduced_graph.nodes && i != initial_node; i++) {
            d[i] = Double.MAX_VALUE;
        }
        LinkedList<Integer> queue = new LinkedList<>();
        queue.push(initial_node);
        while (!queue.isEmpty()) {
            int i = queue.pop();
            visits[i]++;

            //An exit condition is if we visit the same node at least |nodes| times.
            if (visits[i] >= reduced_graph.nodes) {
                return new Cycle(predecessors, i);
            }

            for (int j = 0; j < reduced_graph.nodes; j++) {
                if (reduced_graph.edges[i][j] == 1 && d[j] > d[i] + reduced_graph.costs[i][j]) {
                    d[j] = d[i] + reduced_graph.costs[i][j];
                    predecessors.put(j, i);
                    queue.push(j);
                }
            }
        }
        throw new NullPointerException("No negative cycle");
    }

    private class Cycle {
        HashMap<Integer, Integer> predecessor_tree;
        int first;

        public Cycle(HashMap<Integer, Integer> pred, int element) {
            this.predecessor_tree = pred;
            this.first = element;
        }
    }
}