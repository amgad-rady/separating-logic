public class FormulaPrinter {

  public static Formula formula_constructor(String param, Formula left, Formula right) {
    switch (param) {
      case "And":
        if ((left instanceof False) || (right instanceof False)) {
          return new False();
        } else if ((left instanceof True) && (right instanceof True)) {
          return new True();
        } else if (left instanceof True) {
          return right;
        } else if (right instanceof True) {
          return left;
        } else {
          return new And(left, right);
        }
      case "Or":
        if ((left instanceof True) || (right instanceof True)) {
          return new True();
        } else if ((left instanceof False) && (right instanceof False)) {
          return new False();
        } else if ((left instanceof False)) {
          return right;
        } else if ((right instanceof False)) {
          return left;
        } else {
          return new Or(left, right);
        }
      default:
        throw new IllegalArgumentException();
    }
  }

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
      return new True();
    } else {
      Formula conjunction = new True();
      for (int u = 0; u < states; u++) {
        Formula disjunction = new False();
        for (int v = 0; v < states; v++) {
          disjunction = formula_constructor("Or", PsiFormula.generate_formula(u, v, n - 1, labels, KR_dual, distances), disjunction);
        }
        conjunction = formula_constructor("And", disjunction, conjunction);
      }

      double offset = 0;
      for (int u = 0; u < states; u++) {
        offset += KR_dual[u] * probabilities[s][u];
      }

      if (offset != 0) {
        return new Minus(new Next(conjunction), offset);
      } else {
        return new Next(conjunction);
      }
    }
  }
}