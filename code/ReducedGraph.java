import java.util.HashMap;
import java.util.LinkedList;

public class ReducedGraph {
    public int nodes;
    public int[][] reduced_edges; //edges represented by a square matrix of size nodes.
    public double[][] reduced_costs; //edge capacities. In this case the nth element of the distance sequence.
    public double[][] reduced_capacities; //edge capacities. Usually, 1. Reduced in reduced graphs.

    public Graph parent;

    public double[][] flow;

    public ReducedGraph(int nodes, int[][] reduced_edges, double[][] reduced_costs, double[][] reduced_capacities, double[][] flow, Graph parent) {
        this.parent = parent;
        this.nodes = nodes;
        this.reduced_edges = reduced_edges;
        this.reduced_costs = reduced_costs;
        this.reduced_capacities = reduced_capacities;
        this.flow = flow;
    }

    //Having found the cycle, augment the flow in the reduced graph along the cycle to establish a new
    public void augment_flow(Cycle negative_cycle) {
        double delta = Double.MAX_VALUE;
        int j = negative_cycle.first;
        int i = negative_cycle.predecessor_tree.get(j);
        do {
            delta = Math.min(delta, reduced_capacities[i][j]);
            j = i;
            i = negative_cycle.predecessor_tree.get(j);
        } while (j != negative_cycle.first);

        do {
            //Important: we locally modify the reduced graph here instead of generating a new reduced graph with the new flow
            //This is where most logical errors are likely to be.
            //These calculations with double precision floating point operators is likely to have precision problems

            //if (i, j) is a negative edge do...
            if (parent.edges[j][i] == 1) {
                this.flow[i][j] -= delta;
                if (this.flow[i][j] == 0.0) {
                    this.reduced_edges[i][j] = 0;
                }
                this.reduced_capacities[i][j] -= delta;
            }   //if (i, j) is a positive edge do...
            else if (parent.edges[i][j] == 1) {
                if (this.reduced_capacities[i][j] - this.flow[i][j] - delta == 0.0) {
                    this.reduced_edges[i][j] = 0;
                }
                this.reduced_capacities[i][j] -= delta;
            }
            j = i;
            i = negative_cycle.predecessor_tree.get(j);
        } while (j != negative_cycle.first);
    }

    //This is a variant of the distance relabling function used to compute shortest paths adapted to detecing negative cycles.
    public Cycle find_negative_cycle() throws NullPointerException {
        double[] d = new double[this.nodes];
        int[] visits = new int[this.nodes];

        HashMap<Integer, Integer> predecessors = new HashMap<>();

        for (int i = 1; i < this.nodes; i++) {
            d[i] = Double.MAX_VALUE;
        }
        LinkedList<Integer> queue = new LinkedList<>();
        queue.push(0);
        while (!queue.isEmpty()) {
            int i = queue.pop();
            visits[i]++;

            //An exit condition is if we visit the same node at least |nodes| times.
            if (visits[i] >= this.nodes) {
                return new Cycle(predecessors, i);
            }

            for (int j = 0; j < this.nodes; j++) {
                if (this.reduced_edges[i][j] == 1 && d[j] > d[i] + this.reduced_costs[i][j]) {
                    d[j] = d[i] + this.reduced_costs[i][j];
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