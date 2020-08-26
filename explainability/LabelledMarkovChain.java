/*
 * Copyright (C)  2020  Zainab Fatmi, Amgad Rady, and Franck van Breugel
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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A labelled Markov chain.
 *
 * @author Zainab Fatmi
 * @author Amgad Rady
 * @author Franck van Breugel
 */
public class LabelledMarkovChain {
	public int[] label; // state labelling
	public double[][] probability; // transition probabilities

	/**
	 * Initializes this labelled Markov chain from the files &lt;name&gt;.lab that contains
	 * the labelling function and &lt;name&gt;.tra that contains the transitions and their
	 * probabilities.
	 * 
	 * @param name the name of the files that contains the state labelling and the
	 * transition probabilities
	 * @throws IllegalArgumentException if &lt;name&gt;.lab or &lt;name&gt;.tra is not of the right format
	 * @throws FileNotFoundException if &lt;name&gt;.lab or &lt;name&gt;.tra cannot be read
	 */
	public LabelledMarkovChain(String name) throws IllegalArgumentException, FileNotFoundException {
		// read transition probabilities
		Scanner input = new Scanner(new File(name + ".tra"));
		if (!input.hasNextInt()) {
			input.close();
			throw new IllegalArgumentException("Labelled Markov chain is too large: number of states must be representable as an integer.");
		}
		int numberOfStates = input.nextInt(); // number of states
		if (!input.hasNextInt()) {
			input.close();
			throw new IllegalArgumentException("Labelled Markov chain is too large: number of transitions must be representable as an integer");
		}
		int transitions = input.nextInt(); // number of transitions
		this.probability = new double[numberOfStates][numberOfStates]; // transition probabilities
		for (int i = 0; i < transitions; i++) {
			// [source][destination] = probability
			this.probability[input.nextInt()][input.nextInt()] += input.nextDouble(); // add probabilities of transitions with the same source and target
		}
		input.close();

		// read labels
		input = new Scanner(new File(name + ".lab"));
		Map<String, Integer> labelMap = new HashMap<String, Integer>();
		this.label = new int[numberOfStates];
		int index = 1; // states with no label in the .lab file get index 0, BUT WHAT IF THERE ARE NONE?
		int numberOfStatesWithoutLabel = numberOfStates;
		while (input.hasNextLine()) {
			String[] line = input.nextLine().split(":");
			int state = Integer.parseInt(line[0]);
			String labelling = line[1];
			if (labelMap.containsKey(labelling)) {
				label[state] = labelMap.get(labelling);
			} else {
				labelMap.put(labelling, index);
				label[state] = index;
				index++;
			}
			numberOfStatesWithoutLabel--;
		}
		input.close();
		// index 0 is reserved for states without a label
		if (numberOfStatesWithoutLabel == 0) {
			for (int state = 0; state < numberOfStates; state++) {
				label[state]--;
			}
		}
	}

	/**
	 * Initializes this labelled Markov chain with the given state
	 * labeling and transition probabilities.
	 *
	 * @param label the state labelling of this labelled Markov chain
	 * @param probability the transition probabilities of this labelled Markov chain
	 */
	public LabelledMarkovChain(int[] label, double[][] probability) {
		this.label = label;
		this.probability = probability;
	}
	
//	/**
//	 * Returns the number of states of this labelled Markov chain.
//	 * 
//	 * @return the number of states of this labelled Markov chain
//	 */
//	public int getNumberOfStates() {
//		return this.label.length;
//	}

	/**
	 * Returns the labels of this labelled Markov chain.
	 *
	 * @return the labels of this labelled Markov chain
	 */
	public int[] getLabels() {
		return this.label;
	}
	
	/**
	 * Returns the transitions of this labelled Markov chain and their probabilities.
	 *
	 * @return the transitions of this labelled Markov chain and their probabilities
	 */
	public double[][] getProbabilities() {
		return this.probability;
	}

	/**
	 * Returns a string representation of this labelled Markov chain.
	 *
	 * @return a string representation of this labelled Markov chain
	 */
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("Labels:\n");
		for (int i = 0; i < this.label.length; i++) {
			s.append("\t" + this.label[i]);
		}
		s.append("\n\nProbabilities:\n");
		for (int i = 0; i < this.probability.length; i++) {
			for (int j = 0; j < this.probability[i].length; j++) {
				s.append("\t" + this.probability[i][j]);
			}
			s.append("\n");
		}
		return s.toString();
	}
}
