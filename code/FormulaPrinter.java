public class FormulaPrinter {
  /**
   * Return the phi_{st}^n formula
   *
   * @param s             first state
   * @param t             second state
   * @param n             iteration
   * @param states        set of states of the LMC
   * @param labels        set of labels of the LMC
   * @param KR_dual       Kantorovich-Rubinstein dual g_{st}^{n-1} NOTE: N-1, NOT N!!
   * @param distances     distance matrix distances_{n-1} NOTE: N-1, NOT N!!
   * @param probabilities transition probability matrix tau
   * @return \phi_{st}^n
   * @throws IllegalArgumentException
   */
  public static Formula formula_generator(int s, int t, int n,
                                          int states, int[] labels, double[] KR_dual,
                                          double[][] distances, double[][] probabilities) throws IllegalArgumentException {

    if (n < 0) {
      throw new IllegalArgumentException("n cannot be negative");
    } else if (n == 0) {
      if (labels[s] == labels[t]) {
        return new True();
      } else {
        return new Label(labels[s]);
      }
    } else {
      Formula conjunction = new True();
      for (int u = 0; u < states; u++) {
        Formula disjunction = new False();
        for (int v = 0; v < states; v++) {
          disjunction = (new Or(
            PsiFormula.generate_formula(u, v, n - 1, labels, KR_dual, distances),
            disjunction)).simplify();
        }
        conjunction = (new And(disjunction, conjunction)).simplify();
      }

      double offset = 0;
      for (int u = 0; u < states; u++) {
        offset += KR_dual[u] * probabilities[s][u];
      }

      return (new Minus(new Next(conjunction), offset)).simplify();
    }
  }
}