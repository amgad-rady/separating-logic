/**
 * Prints n stepwise separating formulas for the given LMC
 *
 * @author Amgad Rady
 */
class MainPrinter {
  /**
   * Print iterations stepwise separating formulas
   *
   * @param lmc Labelled Markov Chain
   * @param n   number of iterations
   * @param s   first state
   * @param t   second state
   * @return
   */
  public static String print(LabelledMarkovChain lmc, int n, int s, int t) {
    double[][] probabilities = lmc.probability;
    int[] labels = lmc.label;
    int states = lmc.label.length;
    double[][] distances = new double[states][states];

    for (int u = 0; u < states; u++) {
      for (int v = 0; v < states; v++) {
        if (labels[u] != labels[v]) {
          distances[u][v] = 1.0;
        }
      }
    }

    double[] KR_dual;

    StringBuffer output = new StringBuffer();
    for (int i = 0; i < n; i++) {
      /*
      Compute the KR dual
       */
      KR_dual = (new KRDualSolver(distances, probabilities, states, s, t)).getKRdual();
      output.append(FormulaPrinter.formula_generator(s, t, n, states, labels,
        KR_dual, distances, probabilities).toLaTeX());

      /*
      Compute the next distance matrix pointwise
       */
      double[][] new_distances = distances.clone();
      for (int u = 0; u < states; u++) {
        for (int v = 0; v < states; v++) {
          new_distances[u][v] = (new OptimalCouplingComputer(u, v, probabilities, distances)).compute_distance();
        }
      }
      distances = new_distances;
    }

    return output.toString();
  }
}
