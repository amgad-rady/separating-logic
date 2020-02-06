public class Phi extends Formula {
    private int s;
    private int t;
    private int n;

    public Phi(int first, int second, int iteration) {
        super();
        this.s = first;
        this.t = second;
        this.n = iteration;
    }

    public int[] getPhi() {
        int[] x = new int[3];
        x[0] = this.s;
        x[1] = this.t;
        x[2] = this.n;
        return x;
    }

    @Override
    public String toString() {
        return String.format("Phi{%d, %d, %d}", this.s, this.t, this.n);
    }

    @Override
    public String toLaTeX() {
        return String.format("\\phi_{%d, %d}^{%d}", this.s, this.t, this.n);
    }
}
