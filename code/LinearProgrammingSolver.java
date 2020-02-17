import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.HashSet;

public class LinearProgrammingSolver {
    final private double[][] distances;
    final private double[] tau_s;
    final private double[] tau_t;
    final private int n;
    final private int m;


    public LinearProgrammingSolver(int s, int t, double[][] probabilities, double[][] distance) {

        //First compute the size of support tau(s) (n) and support tau(t) (m)
        int n = 0;
        int m = 0;
        for (int j = 0; j < probabilities.length; j++) {
            if (probabilities[s][j] > 0) {
                n++;
            }
            if (probabilities[t][j] > 0) {
                m++;
            }
        }

        this.n = n;
        this.m = m;

        double[][] nontrivial_distances = new double[n][m];
        double[] tau_s = new double[n];
        double[] tau_t = new double[m];

        //Fill in the new matrices
        int k = 0;
        int l = 0;
        for (int i = 0; i < probabilities.length; i++) {
            if (probabilities[s][i] > 0) {
                for (int j = 0; j < probabilities.length; j++) {
                    if (probabilities[t][j] > 0) {
                        nontrivial_distances[k][l] = distance[i][j];
                        l++;
                    }
                }
            }
            k++;
            l = 0;
        }

        this.distances = nontrivial_distances;

        int count_s = 0;
        int count_t = 0;
        for (int i = 0; i < probabilities.length; i++) {
            if (probabilities[s][i] > 0) {
                tau_s[count_s] = probabilities[s][i];
                count_s++;
            }
            if (probabilities[t][i] > 0) {
                tau_t[count_t] = probabilities[t][i];
                count_t++;
            }
        }

        this.tau_s = tau_s;
        this.tau_t = tau_t;
    }

    //Flatten the distance matrix
    public double[] flatten() {
        double[] output = new double[n * m];
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                output[count] = distances[i][j];
                count++;
            }
        }
        return output;
    }
    //Note that we can recover i and j from count through count / m and count % m, respectively.


    //Generate the set of linear constraints.
    LinearConstraintSet generate_constraints() {
        HashSet<LinearConstraint> set = new HashSet<>();
        //The tau(s) constraints
        for (int i = 0; i < n * m; i += m) {
            double[] coefficients = new double[n * m];
            for (int j = 0; j < m; j++) {
                coefficients[i + j] = 1.0;
            }
            set.add(new LinearConstraint(coefficients, Relationship.EQ, tau_s[i / m]));
        }

        //The tau(t) constraints
        for (int i = 0; i < m; i++) {
            double[] coefficients = new double[n * m];
            for (int j = 0; j < n; j++) {
                coefficients[m * j + i] = 1.0;
            }
            set.add(new LinearConstraint(coefficients, Relationship.EQ, tau_t[i]));
        }
        return new LinearConstraintSet(set);
    }

    public double compute_distance() {
        SimplexSolver solver = new SimplexSolver();
        NonNegativeConstraint noneg = new NonNegativeConstraint(true);

        LinearObjectiveFunction objective_function = new LinearObjectiveFunction(flatten(), 0.0);
        LinearConstraintSet constraints = generate_constraints();

        PointValuePair optimal = solver.optimize(objective_function, noneg, constraints, GoalType.MINIMIZE);
        return optimal.getValue();
    }
}
