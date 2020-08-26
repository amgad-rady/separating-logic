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

/**
 * Explains the probabilistic bisimilarity distances of a labelled Markov chain.
 * 
 * @author Amgad Rady
 * @author Franck van Breugel
 */
public class Distances {
	/**
	 * Returns for each state pair of the given labelled Markov chain, a sequence of formulas of the given length
	 * that explains the probabilistic bisimilarity distances. 
	 * 
	 * @param probability the transition probabilities of a labelled Markov chain
	 * @param label the labelling function of a labelled Markov chain
	 * @param number the number of formulas for each state pair
	 * @return 
	 */
	public static Formula[][][] explain(double[][] probability, int[] label, int number) {
		final Formula TRUE = new True();

		int numberOfStates = probability.length;

		Formula[][][] formula = new Formula[numberOfStates][numberOfStates][number + 1];
		double[][] distance = new double[numberOfStates][numberOfStates];
		double[][][] function = new double[numberOfStates][numberOfStates][numberOfStates];

		boolean[][] bisimilar = ProbabilisticBisimilarity.decide(probability, label);
		for (int s = 0; s < numberOfStates; s++) {
			for (int t = 0; t < numberOfStates; t++) {
				formula[s][t][0] = TRUE;
				if (bisimilar[s][t]) {
					formula[s][t][1] = TRUE;
				} else if (label[s] != label[t]) {
					formula[s][t][1] = new Label(label[t]);
				} else {
					formula[s][t][1] = TRUE;
					for (int u = 0; u < numberOfStates; u++) {
						function[s][t][u] = 0;
					}
				}
			}
		}

		for (int n = 1; n < number; n++) {
			for (int s = 0; s < numberOfStates; s++) {
				for (int t = 0; t < numberOfStates; t++) {
					if (bisimilar[s][t]) {
						distance[s][t] = 0;
					} else if (label[s] != label[t]) {
						distance[s][t] = 1;
					} else {
						distance[s][t] = 0;
						for (int u = 0; u < numberOfStates; u++) {
							distance[s][t] += function[s][t][u] * (probability[s][u] - probability[t][u]);	
						}
					}
				}
			}

			Formula[][][][] subformula = new Formula[numberOfStates][numberOfStates][numberOfStates][numberOfStates];
			for (int s = 0; s < numberOfStates; s++) {
				for (int t = 0; t < numberOfStates; t++) {
					if (bisimilar[s][t]) {
						formula[s][t][n + 1] = TRUE;
					} else if (label[s] != label[t]) {
						formula[s][t][n + 1] = new Label(label[t]);
					} else {
						function[s][t] = Vertex.find(distance, probability[s], probability[t]);
						Or disjunction = new Or();
						for (int u = 0; u < numberOfStates; u++) {
							And conjunction = new And();
							for (int v = 0; v < numberOfStates; v++) {
								if (subformula[s][t][v][u] != null) {
									subformula[s][t][u][v] = subformula[s][t][v][u];
								} else {
									if (function[s][t][u] == function[s][t][v]) { 
										subformula[s][t][u][v] = new Plus(TRUE, function[s][t][u]);
									} else {
										double minusShift = Math.max(distance[u][v] - Math.abs(function[s][t][u] - function[s][t][v]), 0); // mitigate rounding errors
										double plusShift = Math.min(function[s][t][u], function[s][t][v]);
										if (function[s][t][u] > function[s][t][v]) {
											subformula[s][t][u][v] = new Plus(new Minus(formula[u][v][n], minusShift), plusShift); 
										} else {
											subformula[s][t][u][v] = new Plus(new Minus(formula[v][u][n], minusShift), plusShift);
										}
									}
								}
								conjunction.add(subformula[s][t][u][v]);
							}
							disjunction.add(conjunction);
						}
						double shift = 0;
						for (int u = 0; u < numberOfStates; u++) {
							shift += function[s][t][u] * probability[t][u];
						}
						formula[s][t][n + 1] = new Minus(new Next(disjunction), shift);
					}
				}
			}
		}
		return formula;
	}
}
