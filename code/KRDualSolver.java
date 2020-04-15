import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.HashSet;

public class KRDualSolver {
  double[][] distances;
  double[][] probabilities;
  int states;
  int s;
  int t;

  PointValuePair solution;

  public KRDualSolver(LabelledMarkovChain LMC, int s, int t) {
    this.distances = LMC.distance;
    this.probabilities = LMC.probability;
    this.states = LMC.distance.length;
    this.s = s;
    this.t = t;
    this.solution = generate_solution();
  }

  public KRDualSolver(double[][] distances, double[][] probabilities, int states, int s, int t) {
    this.distances = distances;
    this.probabilities = probabilities;
    this.states = states;
    this.s = s;
    this.t = t;
    this.solution = generate_solution();
  }

  private PointValuePair generate_solution() {
    double[] coefficients = new double[states];
    for (int u = 0; u < states; u++) {
      coefficients[u] = probabilities[t][u] - probabilities[s][u];
    }
    LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(coefficients, 0.0);

    HashSet<LinearConstraint> set = new HashSet<>();
    //express the non-expansiveness constraint
    for (int i = 0; i < states; i++) {
      for (int j = i + 1; j < states; j++) {
        double[] f = new double[states];
        f[i] = 1;
        f[j] = -1;
        set.add(new LinearConstraint(f, Relationship.LEQ, distances[i][j]));
        set.add(new LinearConstraint(f, Relationship.GEQ, -distances[i][j]));
      }
    }

    //express f : S -> [0, 1]
    for (int i = 0; i < states; i++) {
      double[] f = new double[states];
      f[i] = 1;
      set.add(new LinearConstraint(f, Relationship.LEQ, 1));
      set.add(new LinearConstraint(f, Relationship.GEQ, 0));
    }

    LinearConstraintSet constraints = new LinearConstraintSet(set);
    SimplexSolver solver = new SimplexSolver();

    return solver.optimize(objectiveFunction,
      constraints,
      GoalType.MAXIMIZE);
  }

  public double getValue() {
    return solution.getValue();
  }

  public double[] getKRdual() {
    return solution.getPoint();
  }
}