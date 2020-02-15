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

    public double[][] initialize_flow() {
        double[][] flow = new double[nodes][nodes];
        //Implement North-West corner algorithm.

        return flow;
    }

    public ReducedGraph construct_reduced_graph(double[][] flow) {
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
        return new ReducedGraph(this.nodes, reduced_edges, reduced_costs, reduced_capacities, initialize_flow(), this);
    }
}
