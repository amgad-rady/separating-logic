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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * 
 * 
 * 
 * 
 * @author Amgad Rady
 * @author Franck van Breugel
 */
public class Explanation {
	/**
	 * Prints the 
	 * 
	 * @param args[0] base name of the transition probabilties and labels files as well as the formulas file
	 * @param args[1] number of formulas
	 */
	public static void main(String[] args) {
		try {
			LabelledMarkovChain chain = new LabelledMarkovChain(args[0]);
			int number = Integer.parseInt(args[1]);
			PrintStream output = new PrintStream(new File(args[0] + ".txt"));
			//int s = Integer.parseInt(args[2]);
			//int t = Integer.parseInt(args[3]);

			double[][] probability = chain.getProbabilities();
			int[] label = chain.getLabels();

			Formula[][][] formula = Distances.explain(probability, label, number);

			int numberOfStates = probability.length;
			for (int n = 0; n <= number; n++) {
				output.printf("Iteration %d%n", n);
				for (int s = 0; s < numberOfStates; s++) {
					for (int t = 0; t < numberOfStates; t++) {
						output.printf("  Formula for (%d, %d)%n", s, t);
						output.printf("    %s%n", formula[s][t][n]);
						output.printf("    %s%n", formula[s][t][n].simplify());
					}
				}
			}
		} catch (IllegalArgumentException e) {
			System.out.printf("File %1$s.lab or %1$s.tra is not in the right format%n", args[0]);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.printf("File %1$s.lab or %1$s.tra cannot be read%n", args[0]);
			e.printStackTrace();
		}
	}
}
