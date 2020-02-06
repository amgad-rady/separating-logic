import java.util.HashSet;
import java.util.Set;

public class LabelledMarkovChain {
	private int[] label; // state labelling
	private double[][] probability; // transition probabilities
	private double[][] distance; // probabilistic bisimilarity distances

	/**
	 * Initializes this labelled Markov chain with the given state labeling, transition probabilities, and the probabilistic bisimilarity distances.
	 * 
	 * @param label state labelling of this labelled Marko chain.
	 * @param probability transition probabilities of this labelled Marko chain.
	 * @param distance probabilistic bisimilarity distances of this labelled Marko chain.
	 */
	public LabelledMarkovChain(int[] label, double[][] probability, double[][] distance) {
		super();
		this.label = label;
		this.probability = probability;
		this.distance = distance;
	}
	
	/**
	 * Returns the label of the given state of this labelled Markov chain.
	 * 
	 * @param state a state.
	 * @return the label of the given state of this labelled Markov chain.
	 */
	public int getLabel(int state) {
		return this.label[state];
	}
	
	/**
	 * Returns the transition probability distribution of this given state of this labelled Markov chain.
	 * 
	 * @param state a state.
	 * @return the transition probability distribution of this given state of this labelled Markov chain.
	 */
	public double[] getDistribution(int state) {
		return this.probability[state];
	}
	
	/**
	 * Returns the set of states u with probability of transitioning from s to u 
	 * 
	 * @param s a state.
	 * @param t a state.
	 * @return
	 */
	public Set<Integer> distinct(int s, int t) {
		Set<Integer> distinct = new HashSet<Integer>();
		for (int u = 0; u < this.label.length; u++) {
			if (this.probability[s][u] != this.probability[t][u]) {
				distinct.add(u);
			}
		}
		return distinct;
	}
	
	/**
	 * Returns a string represenntation of this labelled Markov chain.
	 * 
	 * @return a string representation of this labelled Markov chain.
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
		s.append("\nDistances:\n");
		for (int i = 0; i < this.distance.length; i++) {
			for (int j = 0; j < this.distance[i].length; j++) {
				s.append("\t" + this.distance[i][j]);
			}
			s.append("\n");
		}
		return s.toString();
	}
	
	/**
	 * Returns the value of the given formula in the given state of this labelled Markov chain.
	 * 
	 * @param formula a formula.
	 * @param state a state.
	 * @return the value of the given formula in the given state of this labelled Markov chain.
	 */
	public double valueOf(Formula formula, int state) {
		if (formula instanceof True) {
			return 0.0;
		} else if (formula instanceof False) {
			return 1.0;
		} else if (formula instanceof Label) {
			Label label = (Label) formula;
			if (this.label[state] == label.getLabel()) {
				return 0.0;
			} else {
				return 1.0;
			}
		} else if (formula instanceof Minus) {
			Minus minus = (Minus) formula;
			return Math.max(0.0, this.valueOf(minus.getFormula(), state) - minus.getShift());
		} else if (formula instanceof Plus) {
			Plus plus = (Plus) formula;
			return Math.min(1.0, this.valueOf(plus.getFormula(), state) + plus.getShift());
		} else if (formula instanceof And) {
			And and = (And) formula;
			return Math.max(this.valueOf(and.getLeft(), state), this.valueOf(and.getRight(), state));
		} else if (formula instanceof Or) {
			Or or = (Or) formula;
			return Math.max(this.valueOf(or.getLeft(), state), this.valueOf(or.getRight(), state));
		} else if (formula instanceof Next) {
			Next next = (Next) formula;
			double sum = 0.0;
			for (int target = 0; target < this.probability[state].length; target++) {
				sum += probability[state][target] * this.valueOf(next.getFormula(), target);
			}
			return sum;
		} else {
			throw new RuntimeException();
		}
	}
	
	/**
	 * INCOMPLETE
	 * 
	 * 
	 * @param depth
	 * @param s
	 * @param t
	 * @return
	 */
	public Formula formula(int depth, int s, int t) {
		if (depth == 0) {
			return new True();
		} else {
			if (this.distance[s][t] == 0.0) {
				return new True();
			} else if (this.label[s] != this.label[t]) {
				return new Label(this.label[s]);
			} else {
				return new False();
			}
		}
	}
}
