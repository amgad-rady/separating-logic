import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

/**
 * A labelled Markov chain.  The states are represented by nonnegative integers.  If the labelled Markov chain has n states, then they are numbered 0, 1, ..., n-1.
 * The transition probabilities are specified by a two-dimensional array probability, where probability[s][t] captures the probability going from state s to
 * state t.  Each state of the Markov chain has a label.  The labels are integers.  The labelling is specified by an array label, where label[s] captures the
 * label of state s.  The probabilistic bisimilarity distances are specified by a two-dimensional array distance, where distance[s][t] captures the 
 * probabilistic bisimilarity distance of state s and state t. 
 * 
 * @author Franck van Breugel
 */
public class LabelledMarkovChain {
	private double[][] probability; // transition probabilities
	private int[] label; // state labelling
	private double[][] distance; // probabilistic bisimilarity distances of the states

	/**
	 * Initializes this labelled Markov chain with the given probabilities, labelling and distances.
	 *
	 * @param probability the transition probabilities of this labelled Markov chain.
	 * @param label the state labelling of this labelled Markov chain.
	 * @param distance the probabilistic bisimilarity distances of this labelled Markov chain.
	 * @pre. probability != null && label != null && distance != null && probability.length == states && label.length == states && distance.length == states && for all 0 <= s < probability.length : probability[s].length == states && for all 0 <= s < distance.length : distance[s].length == states
	 */
	public LabelledMarkovChain(double[][] probability, int[] label, double[][] distance) {
		this.probability = probability;
		this.label = label;
		this.distance = distance;
	}

	/**
	 * Given states s and t that have the same label and are not probabilistic bisimilar, that is, distance[s][t] > 0, returns a function, 
	 * represented as an array, say f, from the set of states of this labelled Markov chain to the interval [0, 1] that is nonexpansive with 
	 * respect to the distances of this labelled Markov chain and maximizes &sum;<sub>0 &le; u < states</sub> f[s] (probability[s][u] - probability[t][u]).
	 * Note that this sum is equal to distance[s][t].
	 *
	 * @param s a state.
	 * @param t a state.
	 * @return a nonexpansive function f that maximizes &sum;<sub>0 &le; u < states</sub> f[s] (probability[s][u] - probability[t][u]).
	 * @pre. 0 <= s < states && 0 <= t < states && distance[s][t] > 0 && label[s] == label[t]
	 */
	public double[] getOptimalFunction(int s, int t) {
		int states = this.label.length; // number of states of this labelled Markov chain

		// constraints
		List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
		for (int u = 0; u < states; u++) {
			double [] coefficient = new double[states];
			coefficient[u] = 1;
			constraints.add(new LinearConstraint(coefficient, Relationship.GEQ, 0.0)); // g[u] >= 0.0
			constraints.add(new LinearConstraint(coefficient, Relationship.LEQ, 1.0)); // g[u] <= 1.0
			for (int v = 0; v < states; v++) {
				if (v != u) {
					coefficient = new double[states];
					coefficient[u] = 1;
					coefficient[v] = -1;
					constraints.add(new LinearConstraint(coefficient, Relationship.LEQ, this.distance[u][v])); // g[u] - g[v] <= distance[u][v]
				}
			}
		}
		LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
		
		// objective function
		double [] coefficient = new double[states];
		for (int u = 0; u < states; u++) {
			coefficient[u] = probability[s][u] - probability[t][u];
		}
		LinearObjectiveFunction function = new LinearObjectiveFunction(coefficient, 0.0); // &sum;<sub>0 &le; u < states</sub> f[s] (probability[s][u] - probability[t][u])

		// solve linear program
		SimplexSolver solver = new SimplexSolver();
		PointValuePair solution = solver.optimize(function, constraintSet, GoalType.MAXIMIZE);

		// return solution
		return solution.getPoint();
	}

	/**
	 * Prints the given linear constraint.
	 *
	 * @param constraint the linear constraint to be printed.
	 */
	private static void printLinearConstraint(LinearConstraint constraint) {
		double[] coefficient = constraint.getCoefficients().toArray();
		boolean first = true;
		for (int i = 0; i < coefficient.length; i++) {
			if (coefficient[i] != 0.0) {
				if (!first) {
					System.out.print(" + ");
				}
				System.out.printf("%.2f * f[%d]", coefficient[i], i);
				first = false;
			}
		}
		System.out.printf(" %s %.2f%n", constraint.getRelationship(), constraint.getValue());
	}
}
