/*
 * Copyright (C)  2020  Amgad Rady and Franck van Breugel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package explainability;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

/**
 * Finds a vertex of the Lipschitz polytope.
 * 
 * @author Amgad Rady
 * @author Franck van Breugel
 */
public class Vertex {
	/**
	 * Returns a vertex of the Lipschitz polytope.
	 * 
	 * @param distance the distances of the states of the labelled Markov chain
	 * @param first the transition probabilities of a state of the labelled Markov chain
	 * @param second the transition probabilities of a state of the labelled Markov chain
	 * @return a vertex of the Lipschitz polytope
	 */
	public static double[] find(double[][] distance, double[] first, double[] second) {
		int numberOfStates = distance.length;

		// objective function
		double[] coefficient = new double[numberOfStates];
		for (int u = 0; u < numberOfStates; u++) {
			coefficient[u] = first[u] - second[u];
		}
		LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(coefficient, 0.0);

		// constraints
		Set<LinearConstraint> constraintSet = new HashSet<LinearConstraint>();
		for (int u = 0; u < numberOfStates; u++) {
			for (int v = 0; v < numberOfStates; v++) {
				coefficient = new double[numberOfStates];
				if (u != v) {
					coefficient[u] = 1;
				} 
				coefficient[v] = -1;
				constraintSet.add(new LinearConstraint(coefficient, Relationship.LEQ, distance[u][v]));
			}
		}
		for (int u = 0; u < numberOfStates; u++) {
			coefficient = new double[numberOfStates];
			coefficient[u] = 1;
			constraintSet.add(new LinearConstraint(coefficient, Relationship.LEQ, 1));
		}
		LinearConstraintSet constraints = new LinearConstraintSet(constraintSet);

		// solve
		SimplexSolver solver = new SimplexSolver();
		return solver.optimize(objectiveFunction, constraints, GoalType.MAXIMIZE).getPoint();
	}
}
