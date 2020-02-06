public class IntermediateFormulaWriter {
    private int x; // first state
    private int y; // second state

    private int n; // iteration

    private int[] labels;
    private double[] KR_dual; // Kantorovich-Rubinstein dual function g_st
    private double[][] distances; // distance function

    private Formula base = generate_base();
    private Formula output = new Plus(
            new Minus(base, distances[x][y] - Math.abs(KR_dual[x] - KR_dual[y])),
            Math.min(KR_dual[x], KR_dual[y]));

    public IntermediateFormulaWriter(int first, int second, int iteration, int[] labels, double[] KR_dual, double[][] distances) {
        this.x = first;
        this.y = second;
        this.n = iteration;

        this.labels = labels;
        this.KR_dual = KR_dual;
        this.distances = distances;
    }

    private Formula generate_base() {
        if (labels[x] != labels[y]) {
            return new Label(x);
        } else if (n == 0 && labels[x] == labels[y]) {
            return new True();
        } else {
            return new Phi(this.x, this.y, this.n);
        }
    }
}
