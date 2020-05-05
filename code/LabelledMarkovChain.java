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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A labelled Markov chain.
 *
 * @author Amgad Rady
 * @author Franck van Breugel
 */
public class LabelledMarkovChain {
  public int[] label; // state labelling
  public double[][] probability; // transition probabilities
  public double[][] distance; // probabilistic bisimilarity distances

  /**
   * Initializes this labelled Markov chain with the given state
   * labeling, transition probabilities, and the probabilistic
   * bisimilarity distances.
   *
   * @param label       state labelling of this labelled Markov chain
   * @param probability transition probabilities of this labelled
   *                    Markov chain
   * @param distance    probabilistic bisimilarity distances of this
   *                    labelled Marko chain
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
   * @param state a state
   * @return the label of the given state of this labelled Markov chain
   */
  public int getLabel(int state) {
    return this.label[state];
  }

  /**
   * Returns the transition probability distribution of this given state of this labelled Markov chain.
   *
   * @param state a state
   * @return the transition probability distribution of this given state of this labelled Markov chain
   */
  public double[] getDistribution(int state) {
    return this.probability[state];
  }

  /**
   * Returns the set of states u with the probability of transitioning from
   * s to u being different from the probability of transitioning from
   * t to u.
   *
   * @param s a state
   * @param t a state
   * @return the set of states u with the probability of transitioning from
   * s to u being different from the probability of transitioning from
   * t to u
   */
  private Set<Integer> distinct(int s, int t) {
    Set<Integer> distinct = new HashSet<Integer>();
    for (int u = 0; u < this.label.length; u++) {
      if (this.probability[s][u] != this.probability[t][u]) {
        distinct.add(u);
      }
    }
    return distinct;
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
    s.append("\nDistances:\n");
    for (int i = 0; i < this.distance.length; i++) {
      for (int j = 0; j < this.distance[i].length; j++) {
        s.append("\t" + this.distance[i][j]);
      }
      s.append("\n");
    }
    return s.toString();
  }

  public double valueOf(Formula formula, int state, double[][][] KR) {
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
      return Math.max(0.0, this.valueOf(minus.getFormula(), state, KR) - minus.getShift());
    } else if (formula instanceof Plus) {
      Plus plus = (Plus) formula;
      return Math.min(1.0, this.valueOf(plus.getFormula(), state, KR) + plus.getShift());
    } else if (formula instanceof And) {
      And and = (And) formula;
      return Math.max(this.valueOf(and.getLeft(), state, KR), this.valueOf(and.getRight(), state, KR));
    } else if (formula instanceof Or) {
      Or or = (Or) formula;
      return Math.min(this.valueOf(or.getLeft(), state, KR), this.valueOf(or.getRight(), state, KR));
    } else if (formula instanceof Next) {
      Next next = (Next) formula;
      double sum = 0.0;
      for (int target = 0; target < this.probability[state].length; target++) {
        sum += probability[state][target] * this.valueOf(next.getFormula(), target, KR);
      }
      return sum;
    } else if (formula instanceof Phi) {
      Phi phi = (Phi) formula;

      int nStates = this.label.length;
      int s = phi.getPhi()[0];
      int t = phi.getPhi()[1];
      double[] tau_s = this.probability[s];
      double[] tau_u = this.probability[state];

      double sum = 0.0;
      for (int target = 0; target < nStates; target++) {
        sum += KR[s][t][target] * (tau_u[target] - tau_s[target]);
      }
      return sum;
    } else {
      throw new RuntimeException("Trying to compute the value of formula of unknown type");
    }
  }


  /**
   * Print iterations stepwise separating formulas
   *
   * @param n number of iterations
   * @param s first state
   * @param t second state
   * @return string containing stepwise separating formulas
   */
  public String printEvaluations(int n, int s, int t) {
    /*
    We need matrices of distances and KR duals for at least two levels.
     */
    double[][] probabilities = this.probability;
    int[] labels = this.label;
    int states = this.label.length;
    double[][] distances = this.distance;

    double[][] diminus2 = new double[states][states];
    double[][] diminus1 = new double[states][states];
    double[][] di = new double[states][states];

    for (int u = 0; u < states; u++) {
      for (int v = 0; v < states; v++) {
        if (labels[u] != labels[v]) {
          di[u][v] = 1.0;
        }
      }
    }

    double[][][] KRminus2 = new double[states][states][states];
    double[][][] KRminus1 = new double[states][states][states];
    double[][][] KR = new double[states][states][states];
    for (int u = 0; u < states; u++) {
      for (int v = 0; v < states; v++) {
        KRminus2[u][v] = (new KRDualSolver(diminus2, probabilities, states, u, v)).getKRdual();
        KRminus1[u][v] = (new KRDualSolver(diminus1, probabilities, states, u, v)).getKRdual();
        KR[u][v] = (new KRDualSolver(di, probabilities, states, u, v)).getKRdual();
      }
    }

    StringBuffer output = new StringBuffer();
    for (int i = 0; i < n; i++) {

      Formula phi = FormulaPrinter.formula_generator(s, t, i, states, labels, KRminus1[s][t],
        diminus1, probabilities);
      double val = this.valueOf(phi, t, KRminus2);

      output.append("$");
      output.append(phi.toLaTeX() + "$");
      output.append("\n\n" + val + "\n\n");
      /*
      Compute the next distance matrix point-wise
       */
      for (int u = 0; u < states; u++) {
        diminus2[u] = Arrays.copyOf(diminus1[u], states);
        diminus1[u] = Arrays.copyOf(di[u], states);
      }

      for (int u = 0; u < states; u++) {
        for (int v = 0; v < states; v++) {
          if (distances[u][v] != 0 && labels[u] == labels[v]) {
            OptimalCouplingComputer o = new OptimalCouplingComputer(u, v, probabilities, diminus1);
            di[u][v] = o.compute_distance();
          }
        }
      }
      for (int u = 0; u < states; u++) {
        for (int v = 0; v < states; v++) {
          KRDualSolver k = new KRDualSolver(diminus2, probabilities, states, u, v);
          KRminus2[u][v] = k.getKRdual();
        }
      }

      for (int u = 0; u < states; u++) {
        for (int v = 0; v < states; v++) {
          KRDualSolver k = new KRDualSolver(diminus1, probabilities, states, s, t);
          KRminus1[u][v] = k.getKRdual();
        }
      }

      for (int u = 0; u < states; u++) {
        for (int v = 0; v < states; v++) {
          KRDualSolver k = new KRDualSolver(di, probabilities, states, s, t);
          KR[u][v] = k.getKRdual();
        }
      }

    }
    return output.toString();
  }

  /**
   * Returns a formula that approximately seperates the given two states.
   * The formula is limited to the given depth (the number of nested next
   * operators).  As the depth increases, the formula separates the states
   * more accurately.
   *
   * @param depth the maximum number of nested next operators
   * @param s     a state
   * @param t     a state
   * @return a formula that seperates the given two states
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
